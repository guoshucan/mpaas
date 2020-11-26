/*
 * Copyright 2002-2019 the original author or authors.
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
package ghost.framework.data.transaction.reactive.support;

import ghost.framework.data.transaction.reactive.ReactiveTransactionContext;
import ghost.framework.data.transaction.reactive.ReactiveTransactionSynchronization;
import ghost.framework.data.transaction.reactive.ReactiveTransactionSynchronizationManager;
import ghost.framework.transaction.NoTransactionException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.ArrayDeque;
import java.util.function.Function;

/**
 * Delegate to register and obtain transactional contexts.
 *
 * <p>Typically used by components that intercept or orchestrate transactional flows
 * such as AOP interceptors or transactional operators.
 *
 * @author Mark Paluch
 * @since 5.2
 * @see ReactiveTransactionSynchronization
 */
public abstract class ReactiveTransactionContextManager {
	private ReactiveTransactionContextManager() {
	}
	/**
	 * Obtain the current {@link ReactiveTransactionContext} from the subscriber context or the
	 * transactional context holder. Context retrieval fails with NoTransactionException
	 * if no context or context holder is registered.
	 * @return the current {@link ReactiveTransactionContext}
	 * @throws NoTransactionException if no ReactiveTransactionContext was found in the subscriber context
	 * or no context found in a holder
	 */
	public static Mono<ReactiveTransactionContext> currentContext() throws NoTransactionException {
		return Mono.subscriberContext().handle((ctx, sink) -> {
			if (ctx.hasKey(ReactiveTransactionContext.class)) {
				sink.next(ctx.get(ReactiveTransactionContext.class));
				return;
			}
			if (ctx.hasKey(ReactiveTransactionContextHolder.class)) {
				ReactiveTransactionContextHolder holder = ctx.get(ReactiveTransactionContextHolder.class);
				if (holder.hasContext()) {
					sink.next(holder.currentContext());
					return;
				}
			}
			sink.error(new NoTransactionInContextException());
		});
	}

	/**
	 * Create a {@link ReactiveTransactionContext} and register it in the subscriber {@link Context}.
	 * @return functional context registration.
	 * @throws IllegalStateException if a transaction context is already associated.
	 * @see Mono#subscriberContext(Function)
	 * @see Flux#subscriberContext(Function)
	 */
	public static Function<Context, Context> createTransactionContext() {
		return context -> context.put(ReactiveTransactionContext.class, new ReactiveTransactionContext());
	}

	/**
	 * Return a {@link Function} to create or associate a new {@link ReactiveTransactionContext}.
	 * Interaction with transactional resources through
	 * {@link ReactiveTransactionSynchronizationManager} requires a ReactiveTransactionContext
	 * to be registered in the subscriber context.
	 * @return functional context registration.
	 */
	public static Function<Context, Context> getOrCreateContext() {
		return context -> {
			ReactiveTransactionContextHolder holder = context.get(ReactiveTransactionContextHolder.class);
			if (holder.hasContext()) {
				return context.put(ReactiveTransactionContext.class, holder.currentContext());
			}
			return context.put(ReactiveTransactionContext.class, holder.createContext());
		};
	}

	/**
	 * Return a {@link Function} to create or associate a new
	 * {@link ReactiveTransactionContextHolder}. Creation and release of transactions
	 * within a reactive flow requires a mutable holder that follows a top to
	 * down execution scheme. Reactor's subscriber context follows a down to top
	 * approach regarding mutation visibility.
	 * @return functional context registration.
	 */
	public static Function<Context, Context> getOrCreateContextHolder() {
		return context -> {
			if (!context.hasKey(ReactiveTransactionContextHolder.class)) {
				return context.put(ReactiveTransactionContextHolder.class, new ReactiveTransactionContextHolder(new ArrayDeque<>()));
			}
			return context;
		};
	}


	/**
	 * Stackless variant of {@link NoTransactionException} for reactive flows.
	 */
	@SuppressWarnings("serial")
	private static class NoTransactionInContextException extends NoTransactionException {

		public NoTransactionInContextException() {
			super("No transaction in context");
		}

		@Override
		public synchronized Throwable fillInStackTrace() {
			// stackless exception
			return this;
		}
	}

}
