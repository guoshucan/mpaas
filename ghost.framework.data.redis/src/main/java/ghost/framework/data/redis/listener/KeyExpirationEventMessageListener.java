/*
 * Copyright 2015-2020 the original author or authors.
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
package ghost.framework.data.redis.listener;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.application.event.AbstractApplicationEvent;
import ghost.framework.context.application.event.ApplicationEventPublisher;
import ghost.framework.context.message.Topic;
import ghost.framework.data.redis.connection.Message;
import ghost.framework.data.redis.connection.MessageListener;
import ghost.framework.data.redis.core.RedisKeyExpiredEvent;

/**
 * {@link MessageListener} publishing {@link RedisKeyExpiredEvent}s via {@link ApplicationEventPublisher} by listening
 * to Redis keyspace notifications for key expirations.
 *
 * @author Christoph Strobl
 * @since 1.7
 */
public class KeyExpirationEventMessageListener extends KeyspaceEventMessageListener implements ApplicationEventPublisher {

	private static final Topic KEYEVENT_EXPIRED_TOPIC = new PatternTopic("__keyevent@*__:expired");

	private @Nullable
	ApplicationEventPublisher publisher;

	/**
	 * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
	 *
	 * @param listenerContainer must not be {@literal null}.
	 */
	public KeyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.listener.KeyspaceEventMessageListener#doRegister(ghost.framework.data.redis.listener.RedisMessageListenerContainer)
	 */
	@Override
	protected void doRegister(RedisMessageListenerContainer listenerContainer) {
		listenerContainer.addMessageListener(this, KEYEVENT_EXPIRED_TOPIC);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.listener.KeyspaceEventMessageListener#doHandleMessage(ghost.framework.data.redis.connection.Message)
	 */
	@Override
	protected void doHandleMessage(Message message) {
		publishEvent(new RedisKeyExpiredEvent(message.getBody()));
	}
	/**
	 * Publish the event in case an {@link ApplicationEventPublisher} is set.
	 *
	 * @param event can be {@literal null}.
	 */
	protected void publishEvent(RedisKeyExpiredEvent event) {

		if (publisher != null) {
			this.publisher.publishEvent(event);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(ghost.framework.context.ApplicationEventPublisher)
	 */
//	@Override
//	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//		this.publisher = applicationEventPublisher;
//	}

	@Override
	public void publishEvent(AbstractApplicationEvent event) {

	}
}
