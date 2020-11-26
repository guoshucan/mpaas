/*
 * Copyright 2002-2015 the original author or authors.
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

package ghost.framework.web.module.servlet.filter;


import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.web.context.http.request.RequestContextHolder;
import ghost.framework.web.context.http.request.ServletRequestAttributes;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.module.servlet.locale.LocaleContextHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter that exposes the request to the current thread,
 * through both {@link ghost.framework.context.i18n.LocaleContextHolder} and
 * {@link RequestContextHolder}. To be registered as filter in {@code web.xml}.
 *
 * <p>Alternatively, Spring's {@link ghost.framework.web.context.request.RequestContextListener}
 * and Spring's {@link ghost.framework.web.servlet.DispatcherServlet} also expose
 * the same request context to the current thread.
 *
 * <p>This filter is mainly for use with third-party servlets, e.g. the JSF FacesServlet.
 * Within Spring's own web support, DispatcherServlet's processing is perfectly sufficient.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Rossen Stoyanchev
 * @since 2.0
 * @see ghost.framework.context.i18n.LocaleContextHolder
 * @see ghost.framework.web.context.request.RequestContextHolder
 * @see ghost.framework.web.context.request.RequestContextListener
 * @see ghost.framework.web.servlet.DispatcherServlet
 */
@Component
@Order(2)//执行顺序，小为先
@WebFilter(filterName = "RequestContextFilter", urlPatterns = "/*", displayName = "RequestContextFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
//@ConditionalOnMissingClass({ServletContextFilterContainer.class, CharacterEncodingFilter.class, CrossFilter.class, ServletEnvironment.class})
@BeanMapContainer(IFilterContainer.class)
public class RequestContextFilter extends GenericFilter {
	private boolean threadContextInheritable = false;
	/**
	 * Set whether to expose the LocaleContext and RequestAttributes as inheritable
	 * for child threads (using an {@link InheritableThreadLocal}).
	 * <p>Default is "false", to avoid side effects on spawned background threads.
	 * Switch this to "true" to enable inheritance for custom child threads which
	 * are spawned during request processing and only used for this request
	 * (that is, ending after their initial task, without reuse of the thread).
	 * <p><b>WARNING:</b> Do not use inheritance for child threads if you are
	 * accessing a thread pool which is configured to potentially add new threads
	 * on demand (e.g. a JDK {@link java.util.concurrent.ThreadPoolExecutor}),
	 * since this will expose the inherited context to such a pooled thread.
	 */
	public void setThreadContextInheritable(boolean threadContextInheritable) {
		this.threadContextInheritable = threadContextInheritable;
	}

	private void initContextHolders(HttpServletRequest request, ServletRequestAttributes requestAttributes) {
		LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);
		RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}
	}

	private void resetContextHolders() {
		LocaleContextHolder.resetLocaleContext();
		RequestContextHolder.resetRequestAttributes();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
		initContextHolders(request, attributes);
		try {
			this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
			chain.doFilter(request, response);
			this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
		} finally {
			resetContextHolders();
			if (logger.isTraceEnabled()) {
				logger.trace("Cleared thread-bound request context: " + request);
			}
			attributes.requestCompleted();
		}
	}
}