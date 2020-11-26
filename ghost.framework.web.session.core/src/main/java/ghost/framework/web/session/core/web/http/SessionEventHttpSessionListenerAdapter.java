/*
 * Copyright 2014-2019 the original author or authors.
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

package ghost.framework.web.session.core.web.http;

import ghost.framework.context.application.event.ApplicationEventListener;
import ghost.framework.web.session.core.Session;
import ghost.framework.web.session.core.events.AbstractSessionEvent;
import ghost.framework.web.session.core.events.SessionCreatedEvent;
import ghost.framework.web.session.core.events.SessionDestroyedEvent;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

/**
 * Receives {@link SessionDestroyedEvent} and {@link SessionCreatedEvent} and translates
 * them into {@link HttpSessionEvent} and submits the {@link HttpSessionEvent} to every
 * registered {@link HttpSessionListener}.
 *
 * @author Rob Winch
 * @since 1.1
 */
public class SessionEventHttpSessionListenerAdapter
		implements ApplicationEventListener<AbstractSessionEvent> {

	private final List<HttpSessionListener> listeners;

	private ServletContext context;

	public SessionEventHttpSessionListenerAdapter(List<HttpSessionListener> listeners) {
		super();
		this.listeners = listeners;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ghost.framework.context.ApplicationListener#onApplicationEvent(org.
	 * springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(AbstractSessionEvent event) {
		if (this.listeners.isEmpty()) {
			return;
		}

		HttpSessionEvent httpSessionEvent = createHttpSessionEvent(event);

		for (HttpSessionListener listener : this.listeners) {
			if (event instanceof SessionDestroyedEvent) {
				listener.sessionDestroyed(httpSessionEvent);
			}
			else if (event instanceof SessionCreatedEvent) {
				listener.sessionCreated(httpSessionEvent);
			}
		}
	}

	private HttpSessionEvent createHttpSessionEvent(AbstractSessionEvent event) {
		Session session = event.getSession();
		HttpSession httpSession = new HttpSessionAdapter<>(session, this.context);
		return new HttpSessionEvent(httpSession);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ghost.framework.web.context.ServletContextAware#setServletContext(javax.servlet
	 * .ServletContext)
	 */
//	@Override
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}
}
