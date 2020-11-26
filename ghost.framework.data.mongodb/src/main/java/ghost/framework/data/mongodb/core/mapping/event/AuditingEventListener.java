/*
 * Copyright 2012-2020 the original author or authors.
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
package ghost.framework.data.mongodb.core.mapping.event;

import ghost.framework.context.application.event.ApplicationEventListener;
import ghost.framework.context.core.Ordered;
import ghost.framework.context.factory.ObjectFactory;
import ghost.framework.data.commons.auditing.AuditingHandler;
import ghost.framework.data.commons.auditing.IsNewAwareAuditingHandler;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.util.Assert;

/**
 * Event listener to populate auditing related fields on an entity about to be saved.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @deprecated since 2.2, use {@link AuditingEntityCallback}.
 */
@Deprecated
public class AuditingEventListener implements ApplicationEventListener<BeforeConvertEvent<Object>>, Ordered {

	private final ObjectFactory<IsNewAwareAuditingHandler> auditingHandlerFactory;

	/**
	 * Creates a new {@link AuditingEventListener} using the given {@link MappingContext} and {@link AuditingHandler}
	 * provided by the given {@link ObjectFactory}.
	 *
	 * @param auditingHandlerFactory must not be {@literal null}.
	 */
	public AuditingEventListener(ObjectFactory<IsNewAwareAuditingHandler> auditingHandlerFactory) {

		Assert.notNull(auditingHandlerFactory, "IsNewAwareAuditingHandler must not be null!");
		this.auditingHandlerFactory = auditingHandlerFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationListener#onApplicationEvent(ghost.framework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(BeforeConvertEvent<Object> event) {
		event.mapSource(it -> auditingHandlerFactory.getObject().markAudited(it));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return 100;
	}
}
