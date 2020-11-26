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
import ghost.framework.transaction.NoTransactionException;

import java.util.Deque;

/**
 * Mutable holder for reactive transaction {@link ReactiveTransactionContext contexts}.
 * This holder keeps references to individual {@link ReactiveTransactionContext}s.
 *
 * @author Mark Paluch
 * @author Juergen Hoeller
 * @since 5.2
 * @see ReactiveTransactionContext
 */
public final class ReactiveTransactionContextHolder {

	private final Deque<ReactiveTransactionContext> transactionStack;


	ReactiveTransactionContextHolder(Deque<ReactiveTransactionContext> transactionStack) {
		this.transactionStack = transactionStack;
	}


	/**
	 * Return the current {@link ReactiveTransactionContext}.
	 * @throws NoTransactionException if no transaction is ongoing
	 */
	ReactiveTransactionContext currentContext() {
		ReactiveTransactionContext context = this.transactionStack.peek();
		if (context == null) {
			throw new NoTransactionException("No transaction in context");
		}
		return context;
	}
	/**
	 * Create a new {@link ReactiveTransactionContext}.
	 */
	ReactiveTransactionContext createContext() {
		ReactiveTransactionContext context = this.transactionStack.peek();
		if (context != null) {
			context = new ReactiveTransactionContext(context);
		}
		else {
			context = new ReactiveTransactionContext();
		}
		this.transactionStack.push(context);
		return context;
	}

	/**
	 * Check whether the holder has a {@link ReactiveTransactionContext}.
	 * @return {@literal true} if a {@link ReactiveTransactionContext} is associated
	 */
	boolean hasContext() {
		return !this.transactionStack.isEmpty();
	}

}
