/*
 * Copyright 2002-2018 the original author or authors.
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

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.util.Assert;
import ghost.framework.util.MultiValueMap;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.http.HttpInputMessage;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.server.ServletServerHttpRequest;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.module.http.converter.FormHttpMessageConverter;
import ghost.framework.web.module.http.converter.support.AllEncompassingFormHttpMessageConverter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * {@code Filter} that parses form data for HTTP PUT, PATCH, and DELETE requests
 * and exposes it as Servlet request parameters. By default the Servlet spec
 * only requires this for HTTP POST.
 *
 * @author Rossen Stoyanchev
 * @since 5.1
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(7)//执行顺序，小为先
@WebFilter(filterName = "FormContentFilter", urlPatterns = "/*", displayName = "FormContentFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class FormContentFilter extends GenericFilter {
	/**
	 * 处理http模式列表
	 */
	private static final List<String> HTTP_METHODS = Arrays.asList("PUT", "PATCH", "DELETE");
	/**
	 *
	 * form转换器
	 */
	private FormHttpMessageConverter formConverter = new AllEncompassingFormHttpMessageConverter();


	/**
	 * Set the converter to use for parsing form content.
	 * <p>By default this is an instance of {@link AllEncompassingFormHttpMessageConverter}.
	 */
	public void setFormConverter(FormHttpMessageConverter converter) {
		Assert.notNull(converter, "FormHttpMessageConverter is required");
		this.formConverter = converter;
	}

	/**
	 * The default character set to use for reading form data.
	 * This is a shortcut for:<br>
	 * {@code getFormConverter.setCharset(charset)}.
	 */
	public void setCharset(Charset charset) {
		this.formConverter.setCharset(charset);
	}

	/**
	 * 重写过滤器内部方法
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//获取是否有需要处理的数据
//		MultiValueMap<String, String> params = parseIfNecessary(request);
//		//判断是否有数据处理
//		if (!CollectionUtils.isEmpty(params)) {
//			//设置QueryString参数
//			request.setAttribute(WebUtils.QUERY_STRING_ATTRIBUTE, params);
//			//重新包装form内容请求对象
//			FormContentRequestWrapper requestWrapper = new FormContentRequestWrapper(request, params);
//			//往下一个过滤器
//			filterChain.doFilter(requestWrapper, response);
//		}
//		else {
			//往下一个过滤器
			filterChain.doFilter(request, response);
//		}
	}

	/**
	 * 获取是否需要解析的内容
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Nullable
	private MultiValueMap<String, String> parseIfNecessary(HttpServletRequest request) throws IOException {
		if (!shouldParse(request)) {
			return null;
		}
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request) {
			@Override
			public InputStream getBody() throws IOException {
				return request.getInputStream();
			}
		};
		return this.formConverter.read(null, inputMessage);
	}

	/**
	 * 判断是否为 {@link MediaType#APPLICATION_FORM_URLENCODED} 与 {@link FormContentFilter#HTTP_METHODS} 类型解析
	 * @param request 请求对象
	 * @return
	 */
	private boolean shouldParse(HttpServletRequest request) {
		String contentType = request.getContentType();
//		String method = request.getMethod();
		if (StringUtils.hasLength(contentType) && HTTP_METHODS.contains(request.getMethod())) {
			try {
//				MediaType mediaType = MediaType.parseMediaType(contentType);
				return MediaType.APPLICATION_FORM_URLENCODED.includes(MediaType.parseMediaType(contentType));
			}
			catch (IllegalArgumentException ex) {
			}
		}
		return false;
	}

	/**
	 * 重新包装请求对象
	 */
	private static class FormContentRequestWrapper extends HttpServletRequestWrapper {
		private MultiValueMap<String, String> formParams;
		public FormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> params) {
			super(request);
			this.formParams = params;
		}
		@Override
		@Nullable
		public String getParameter(String name) {
			String queryStringValue = super.getParameter(name);
			String formValue = this.formParams.getFirst(name);
			return (queryStringValue != null ? queryStringValue : formValue);
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> result = new LinkedHashMap<>();
			Enumeration<String> names = getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				result.put(name, getParameterValues(name));
			}
			return result;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			Set<String> names = new LinkedHashSet<>();
			names.addAll(Collections.list(super.getParameterNames()));
			names.addAll(this.formParams.keySet());
			return Collections.enumeration(names);
		}

		@Override
		@Nullable
		public String[] getParameterValues(String name) {
			String[] parameterValues = super.getParameterValues(name);
			List<String> formParam = this.formParams.get(name);
			if (formParam == null) {
				return parameterValues;
			}
			if (parameterValues == null || getQueryString() == null) {
				return StringUtils.toStringArray(formParam);
			}
			else {
				List<String> result = new ArrayList<>(parameterValues.length + formParam.size());
				result.addAll(Arrays.asList(parameterValues));
				result.addAll(formParam);
				return StringUtils.toStringArray(result);
			}
		}
	}
}