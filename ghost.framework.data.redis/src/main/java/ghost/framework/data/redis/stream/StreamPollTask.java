/*
 * Copyright 2018-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghost.framework.data.redis.stream;

import ghost.framework.dao.DataAccessResourceFailureException;
import ghost.framework.data.redis.connection.stream.Consumer;
import ghost.framework.data.redis.connection.stream.ReadOffset;
import ghost.framework.data.redis.connection.stream.Record;
import ghost.framework.data.redis.connection.stream.StreamOffset;
import ghost.framework.util.ErrorHandler;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * {@link Task} that invokes a {@link BiFunction read function} to poll on a Redis Stream.
 *
 * @author Mark Paluch
 * @see 2.2
 */
class StreamPollTask<K, V extends Record<K, ?>> implements Task {

	private final StreamMessageListenerContainer.StreamReadRequest<K> request;
	private final StreamListener<K, V> listener;
	private final ErrorHandler errorHandler;
	private final Predicate<Throwable> cancelSubscriptionOnError;
	private final BiFunction<K, ReadOffset, List<V>> readFunction;

	private final PollState pollState;
	private volatile boolean isInEventLoop = false;

	StreamPollTask(StreamMessageListenerContainer.StreamReadRequest<K> streamRequest, StreamListener<K, V> listener, ErrorHandler errorHandler,
				   BiFunction<K, ReadOffset, List<V>> readFunction) {

		this.request = streamRequest;
		this.listener = listener;
		this.errorHandler = Optional.ofNullable(streamRequest.getErrorHandler()).orElse(errorHandler);
		this.cancelSubscriptionOnError = streamRequest.getCancelSubscriptionOnError();
		this.readFunction = readFunction;
		this.pollState = createPollState(streamRequest);
	}

	private static PollState createPollState(StreamMessageListenerContainer.StreamReadRequest<?> streamRequest) {

		StreamOffset<?> streamOffset = streamRequest.getStreamOffset();

		if (streamRequest instanceof StreamMessageListenerContainer.ConsumerStreamReadRequest) {
			return PollState.consumer(((StreamMessageListenerContainer.ConsumerStreamReadRequest<?>) streamRequest).getConsumer(), streamOffset.getOffset());
		}

		return PollState.standalone(streamOffset.getOffset());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.stream.Cancelable#cancel()
	 */
	@Override
	public void cancel() throws DataAccessResourceFailureException {
		this.pollState.cancel();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.stream.Task#getState()
	 */
	@Override
	public State getState() {
		return pollState.getState();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.stream.Task#awaitStart(java.time.Duration)
	 */
	@Override
	public boolean awaitStart(Duration timeout) throws InterruptedException {
		return pollState.awaitStart(timeout.toNanos(), TimeUnit.NANOSECONDS);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.scheduling.SchedulingAwareRunnable#isLongLived()
	 */
	@Override
	public boolean isLongLived() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		pollState.starting();

		try {

			isInEventLoop = true;
			pollState.running();
			doLoop(request.getStreamOffset().getKey());
		} finally {
			isInEventLoop = false;
		}
	}

	private void doLoop(K key) {

		do {

			try {

				// allow interruption
				Thread.sleep(0);

				List<V> read = readFunction.apply(key, pollState.getCurrentReadOffset());

				for (V message : read) {

					listener.onMessage(message);
					pollState.updateReadOffset(message.getId().getValue());
				}
			} catch (InterruptedException e) {

				cancel();
				Thread.currentThread().interrupt();
			} catch (RuntimeException e) {

				if (cancelSubscriptionOnError.test(e)) {
					cancel();
				}

				errorHandler.handleError(e);
			}
		} while (pollState.isSubscriptionActive());
	}

	@Override
	public boolean isActive() {
		return State.RUNNING.equals(getState()) || isInEventLoop;
	}

	/**
	 * Object representing the current polling state for a particular stream subscription.
	 */
	static class PollState {

		private final ReadOffsetStrategy readOffsetStrategy;
		private final Optional<Consumer> consumer;
		private volatile ReadOffset currentOffset;
		private volatile State state = State.CREATED;
		private volatile CountDownLatch awaitStart = new CountDownLatch(1);

		private PollState(Optional<Consumer> consumer, ReadOffsetStrategy readOffsetStrategy, ReadOffset currentOffset) {

			this.readOffsetStrategy = readOffsetStrategy;
			this.currentOffset = currentOffset;
			this.consumer = consumer;
		}

		/**
		 * Create a new state object for standalone-read.
		 *
		 * @param offset the {@link ReadOffset} to use.
		 * @return new instance of {@link PollState}.
		 */
		static PollState standalone(ReadOffset offset) {

			ReadOffsetStrategy strategy = ReadOffsetStrategy.getStrategy(offset);
			return new PollState(Optional.empty(), strategy, strategy.getFirst(offset, Optional.empty()));
		}

		/**
		 * Create a new state object for consumergroup-read.
		 *
		 * @param consumer the {@link Consumer} to use.
		 * @param offset the {@link ReadOffset} to apply.
		 * @return new instance of {@link PollState}.
		 */
		static PollState consumer(Consumer consumer, ReadOffset offset) {

			ReadOffsetStrategy strategy = ReadOffsetStrategy.getStrategy(offset);
			Optional<Consumer> optionalConsumer = Optional.of(consumer);
			return new PollState(optionalConsumer, strategy, strategy.getFirst(offset, optionalConsumer));
		}

		boolean awaitStart(long timeout, TimeUnit unit) throws InterruptedException {
			return awaitStart.await(timeout, unit);
		}

		public State getState() {
			return state;
		}

		/**
		 * @return {@literal true} if the subscription is active.
		 */
		boolean isSubscriptionActive() {
			return state == State.STARTING || state == State.RUNNING;
		}

		/**
		 * Set the state to {@link ghost.framework.data.redis.stream.Task.State#STARTING}.
		 */
		void starting() {
			state = State.STARTING;
		}

		/**
		 * Switch the state to {@link ghost.framework.data.redis.stream.Task.State#RUNNING}.
		 */
		void running() {

			state = State.RUNNING;

			CountDownLatch awaitStart = this.awaitStart;

			if (awaitStart.getCount() == 1) {
				awaitStart.countDown();
			}
		}

		/**
		 * Set the state to {@link ghost.framework.data.redis.stream.Task.State#CANCELLED} and re-arm the
		 * {@link #awaitStart(long, TimeUnit) await synchronizer}.
		 */
		void cancel() {

			awaitStart = new CountDownLatch(1);
			state = State.CANCELLED;
		}

		/**
		 * Advance the {@link ReadOffset}.
		 */
		void updateReadOffset(String messageId) {
			currentOffset = readOffsetStrategy.getNext(getCurrentReadOffset(), consumer, messageId);
		}

		ReadOffset getCurrentReadOffset() {
			return currentOffset;
		}
	}
}
