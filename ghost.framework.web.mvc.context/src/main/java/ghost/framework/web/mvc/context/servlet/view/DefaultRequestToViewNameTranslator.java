///*
// * Copyright 2002-2019 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package ghost.framework.web.mvc.context.servlet.view;
//
//
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.util.Assert;
//import ghost.framework.util.StringUtils;
//import ghost.framework.web.module.servlet.HandlerMapping;
//import ghost.framework.web.module.servlet.RequestToViewNameTranslator;
//import ghost.framework.web.context.utils.UrlPathHelper;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * {@link RequestToViewNameTranslator} that simply transforms the URI of
// * the incoming request into a view name.
// *
// * <p>Can be explicitly defined as the {@code viewNameTranslator} bean in a
// * {@link ghost.framework.web.servlet.DispatcherServlet} context.
// * Otherwise, a plain default instance will be used.
// *
// * <p>The default transformation simply strips leading and trailing slashes
// * as well as the file extension of the URI, and returns the result as the
// * view name with the configured {@link #setPrefix prefix} and a
// * {@link #setSuffix suffix} added as appropriate.
// *
// * <p>The stripping of the leading slash and file extension can be disabled
// * using the {@link #setStripLeadingSlash stripLeadingSlash} and
// * {@link #setStripExtension stripExtension} properties, respectively.
// *
// * <p>Find below some examples of request to view name translation.
// * <ul>
// * <li>{@code http://localhost:8080/gamecast/display.html} &raquo; {@code display}</li>
// * <li>{@code http://localhost:8080/gamecast/displayShoppingCart.html} &raquo; {@code displayShoppingCart}</li>
// * <li>{@code http://localhost:8080/gamecast/admin/index.html} &raquo; {@code admin/index}</li>
// * </ul>
// *
// * @author Rob Harrop
// * @author Juergen Hoeller
// * @since 2.0
// * @see ghost.framework.web.servlet.RequestToViewNameTranslator
// * @see ghost.framework.web.servlet.ViewResolver
// */
//@Component
//public class DefaultRequestToViewNameTranslator implements RequestToViewNameTranslator {
//
//	private static final String SLASH = "/";
//
//
//	private String prefix = "";
//
//	private String suffix = "";
//
//	private String separator = SLASH;
//
//	private boolean stripLeadingSlash = true;
//
//	private boolean stripTrailingSlash = true;
//
//	private boolean stripExtension = true;
//
//	private UrlPathHelper urlPathHelper = new UrlPathHelper();
//
//
//	/**
//	 * Set the prefix to prepend to generated view names.
//	 * @param prefix the prefix to prepend to generated view names
//	 */
//	public void setPrefix(@Nullable String prefix) {
//		this.prefix = (prefix != null ? prefix : "");
//	}
//
//	/**
//	 * Set the suffix to append to generated view names.
//	 * @param suffix the suffix to append to generated view names
//	 */
//	public void setSuffix(@Nullable String suffix) {
//		this.suffix = (suffix != null ? suffix : "");
//	}
//
//	/**
//	 * Set the value that will replace '{@code /}' as the separator
//	 * in the view name. The default behavior simply leaves '{@code /}'
//	 * as the separator.
//	 */
//	public void setSeparator(String separator) {
//		this.separator = separator;
//	}
//
//	/**
//	 * Set whether or not leading slashes should be stripped from the URI when
//	 * generating the view name. Default is "true".
//	 */
//	public void setStripLeadingSlash(boolean stripLeadingSlash) {
//		this.stripLeadingSlash = stripLeadingSlash;
//	}
//
//	/**
//	 * Set whether or not trailing slashes should be stripped from the URI when
//	 * generating the view name. Default is "true".
//	 */
//	public void setStripTrailingSlash(boolean stripTrailingSlash) {
//		this.stripTrailingSlash = stripTrailingSlash;
//	}
//
//	/**
//	 * Set whether or not file extensions should be stripped from the URI when
//	 * generating the view name. Default is "true".
//	 */
//	public void setStripExtension(boolean stripExtension) {
//		this.stripExtension = stripExtension;
//	}
//
//	/**
//	 * Shortcut to same property on underlying {@link #setUrlPathHelper UrlPathHelper}.
//	 * @see ghost.framework.web.util.UrlPathHelper#setAlwaysUseFullPath
//	 */
//	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
//		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
//	}
//
//	/**
//	 * Shortcut to same property on underlying {@link #setUrlPathHelper UrlPathHelper}.
//	 * @see ghost.framework.web.util.UrlPathHelper#setUrlDecode
//	 */
//	public void setUrlDecode(boolean urlDecode) {
//		this.urlPathHelper.setUrlDecode(urlDecode);
//	}
//
//	/**
//	 * Set if ";" (semicolon) content should be stripped from the request URI.
//	 * @see ghost.framework.web.util.UrlPathHelper#setRemoveSemicolonContent(boolean)
//	 */
//	public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
//		this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
//	}
//
//	/**
//	 * Set the {@link ghost.framework.web.util.UrlPathHelper} to use for
//	 * the resolution of lookup paths.
//	 * <p>Use this to override the default UrlPathHelper with a custom subclass,
//	 * or to share common UrlPathHelper settings across multiple web components.
//	 */
//	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
//		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
//		this.urlPathHelper = urlPathHelper;
//	}
//
//
//	/**
//	 * Translates the request URI of the incoming {@link HttpServletRequest}
//	 * into the view name based on the configured parameters.
//	 * @see ghost.framework.web.util.UrlPathHelper#getLookupPathForRequest
//	 * @see #transformPath
//	 */
//	@Override
//	public String getViewName(HttpServletRequest request) {
//		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request, HandlerMapping.LOOKUP_PATH);
//		return (this.prefix + transformPath(lookupPath) + this.suffix);
//	}
//
//	/**
//	 * Transform the request URI (in the context of the webapp) stripping
//	 * slashes and extensions, and replacing the separator as required.
//	 * @param lookupPath the lookup path for the current request,
//	 * as determined by the UrlPathHelper
//	 * @return the transformed path, with slashes and extensions stripped
//	 * if desired
//	 */
//	@Nullable
//	protected String transformPath(String lookupPath) {
//		String path = lookupPath;
//		if (this.stripLeadingSlash && path.startsWith(SLASH)) {
//			path = path.substring(1);
//		}
//		if (this.stripTrailingSlash && path.endsWith(SLASH)) {
//			path = path.substring(0, path.length() - 1);
//		}
//		if (this.stripExtension) {
//			path = StringUtils.stripFilenameExtension(path);
//		}
//		if (!SLASH.equals(this.separator)) {
//			path = StringUtils.replace(path, SLASH, this.separator);
//		}
//		return path;
//	}
//
//}
