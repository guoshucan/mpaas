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

package ghost.framework.data.transaction.reactive;

import org.reactivestreams.Publisher;

/**
 * Callback interface for reactive transactional code. Used with {@link ReactiveTransactionalOperator}'s
 * {@code execute} method, often as anonymous class within a method implementation.
 *
 * <p>Typically used to assemble various calls to transaction-unaware data access
 * services into a higher-level service method with transaction demarcation. As an
 * alternative, consider the use of declarative transaction demarcation (e.g. through
 * Spring's {@link ghost.framework.transaction.annotation.Transactional} annotation).
 *
 * @author Mark Paluch
 * @author Juergen Hoeller
 * @since 5.2
 * @see ReactiveTransactionalOperator
 * @param <T> the result type
 */
@FunctionalInterface
public interface ReactiveTransactionCallback<T> {

	/**
	 * Gets called by {@link ReactiveTransactionalOperator} within a transactional context.
	 * Does not need to care about transactions itself, although it can retrieve and
	 * influence the status of the current transaction via the given status object,
	 * e.g. setting rollback-only.
	 * @param status associated transaction status
	 * @return a result publisher
	 * @see ReactiveTransactionalOperator#transactional
	 */
	Publisher<T> doInTransaction(ReactiveTransaction status);

}
