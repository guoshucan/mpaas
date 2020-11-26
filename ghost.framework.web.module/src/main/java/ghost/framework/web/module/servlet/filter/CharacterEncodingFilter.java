package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.util.Assert;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.context.servlet.filter.ICharacterEncodingFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 处理所有请求的唯一编码规范过滤器
 * @see javax.servlet.http.HttpServletRequest#setCharacterEncoding
 * @see javax.servlet.http.HttpServletResponse#setCharacterEncoding
 * 过滤器执行顺序
 * 1、CharacterEncodingFilter
 * 2、CookiesFilter
 * 3、SessionFilter
 * 4、CrossFilter
 * 5、AuthorizationFilter
 * 6、HttpRequestMethodFilter
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(1)//执行顺序，小为先
@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = "/*", displayName = "CharacterEncodingFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class CharacterEncodingFilter extends GenericFilter implements ICharacterEncodingFilter {
	/**
	 * 指定默认请求与响应编码
	 */
	@Nullable
	private String encoding = "UTF-8";
	/**
	 * 是否强制请求编码
	 */
	private boolean forceRequestEncoding = true;
	/**
	 * 是否强制响应编码
	 */
	private boolean forceResponseEncoding = true;

	/**
	 * 默认自动化构建
	 */
	@Constructor
	public CharacterEncodingFilter() {
	}

	/**
	 * Create a {@code CharacterEncodingFilter} for the given encoding.
	 *
	 * @param encoding the encoding to apply
	 * @see #setEncoding
	 * @since 4.2.3
	 */
	public CharacterEncodingFilter(String encoding) {
		this(encoding, false);
	}

	/**
	 * Create a {@code CharacterEncodingFilter} for the given encoding.
	 *
	 * @param encoding      the encoding to apply
	 * @param forceEncoding whether the specified encoding is supposed to
	 *                      override existing request and response encodings
	 * @see #setEncoding
	 * @see #setForceEncoding
	 * @since 4.2.3
	 */
	public CharacterEncodingFilter(String encoding, boolean forceEncoding) {
		this(encoding, forceEncoding, forceEncoding);
	}

	/**
	 * Create a {@code CharacterEncodingFilter} for the given encoding.
	 *
	 * @param encoding              the encoding to apply
	 * @param forceRequestEncoding  whether the specified encoding is supposed to
	 *                              override existing request encodings
	 * @param forceResponseEncoding whether the specified encoding is supposed to
	 *                              override existing response encodings
	 * @see #setEncoding
	 * @see #setForceRequestEncoding(boolean)
	 * @see #setForceResponseEncoding(boolean)
	 * @since 4.3
	 */
	public CharacterEncodingFilter(String encoding, boolean forceRequestEncoding, boolean forceResponseEncoding) {
		Assert.hasLength(encoding, "Encoding must not be empty");
		this.encoding = encoding;
		this.forceRequestEncoding = forceRequestEncoding;
		this.forceResponseEncoding = forceResponseEncoding;
	}

	/**
	 * 设置编码字符
	 * {@link javax.servlet.http.HttpServletRequest#setCharacterEncoding}
	 * {@link javax.servlet.http.HttpServletResponse#setCharacterEncoding()}
	 * {@see #setForceEncoding}
	 * {@see #setForceEncoding}
	 */
	@Override
	public void setEncoding(@Nullable String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 获取编码字符
	 */
	@Override
	@Nullable
	public String getEncoding() {
		return this.encoding;
	}

	/**
	 * 设置是否强制执行唯一编码
	 * {@link javax.servlet.http.HttpServletRequest#getCharacterEncoding()}
	 * {@link javax.servlet.http.HttpServletResponse#getCharacterEncoding()}
	 */
	@Override
	public void setForceEncoding(boolean forceEncoding) {
		this.forceRequestEncoding = forceEncoding;
		this.forceResponseEncoding = forceEncoding;
	}

	/**
	 * 设置是否强制执行请求唯一编码
	 */
	@Override
	public void setForceRequestEncoding(boolean forceRequestEncoding) {
		this.forceRequestEncoding = forceRequestEncoding;
	}

	/**
	 * 获取是否请求执行请求唯一编码
	 */
	@Override
	public boolean isForceRequestEncoding() {
		return this.forceRequestEncoding;
	}

	/**
	 * 设置是否响应执行唯一编码
	 */
	@Override
	public void setForceResponseEncoding(boolean forceResponseEncoding) {
		this.forceResponseEncoding = forceResponseEncoding;
	}

	/**
	 * 获取是否响应执行唯一编码
	 */
	@Override
	public boolean isForceResponseEncoding() {
		return this.forceResponseEncoding;
	}

	/**
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String encoding = getEncoding();
		if (encoding != null) {
			if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
				request.setCharacterEncoding(encoding);
				request.setAttribute("isForceRequestEncoding", true);
			}
			if (isForceResponseEncoding()) {
				response.setCharacterEncoding(encoding);
				request.setAttribute("isForceResponseEncoding", true);
			}
		}
		this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
		chain.doFilter(request, response);
		this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
	}
}