package ghost.framework.web.context.utils;

import ghost.framework.web.context.bind.annotation.PathVariable;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.HttpHeaders;
import ghost.framework.web.context.http.HttpRequest;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.server.ServletServerHttpRequest;
import ghost.framework.util.*;
import ghost.framework.web.context.server.MimeMappings;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.*;

/**
 * package: ghost.framework.web.module.util
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:0:57
 */
public final class WebUtils {
    /**
     * 默认主页键
     */
    public static final String DEFAULT_INDEX_ATTRIBUTE = "javax.servlet.index.url";
    /**
     * 存放 {@link MimeMappings} 对象的键
     */
    public static final String MIME_MAPPINGS_ATTRIBUTE = "javax.servlet.mime.mappings";
    /**
     * 为 {@link PathVariable} 注释存放的地址解析参数
     * 存储于 {@link HttpServletRequest#setAttribute(String, Object)} 中
     */
    public static final String URI_TEMPLATE_VARIABLES_ATTRIBUTE = "javax.servlet.uri.template.variables";
    /**
     * Standard Servlet 2.3+ spec request attribute for include request& URI.
     * <p>If included via a {@code RequestDispatcher}, the current resource will see the
     * originating request. Its own request URI is exposed as a request attribute.
     */
    public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";

    /**
     * Standard Servlet 2.3+ spec request attribute for include context path.
     * <p>If included via a {@code RequestDispatcher}, the current resource will see the
     * originating context path. Its own context path is exposed as a request attribute.
     */
    public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";

    /**
     * Standard Servlet 2.3+ spec request attribute for include servlet path.
     * <p>If included via a {@code RequestDispatcher}, the current resource will see the
     * originating servlet path. Its own servlet path is exposed as a request attribute.
     */
    public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";

    /**
     * Standard Servlet 2.3+ spec request attribute for include path info.
     * <p>If included via a {@code RequestDispatcher}, the current resource will see the
     * originating path info. Its own path info is exposed as a request attribute.
     */
    public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";

    /**
     * Standard Servlet 2.3+ spec request attribute for include query string.
     * <p>If included via a {@code RequestDispatcher}, the current resource will see the
     * originating query string. Its own query string is exposed as a request attribute.
     */
    public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

    /**
     * Standard Servlet 2.4+ spec request attribute for forward request URI.
     * <p>If forwarded to via a RequestDispatcher, the current resource will see its
     * own request URI. The originating request URI is exposed as a request attribute.
     */
    public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";

    /**
     * Standard Servlet 2.4+ spec request attribute for forward context path.
     * <p>If forwarded to via a RequestDispatcher, the current resource will see its
     * own context path. The originating context path is exposed as a request attribute.
     */
    public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";

    /**
     * Standard Servlet 2.4+ spec request attribute for forward servlet path.
     * <p>If forwarded to via a RequestDispatcher, the current resource will see its
     * own servlet path. The originating servlet path is exposed as a request attribute.
     */
    public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";

    /**
     * Standard Servlet 2.4+ spec request attribute for forward path info.
     * <p>If forwarded to via a RequestDispatcher, the current resource will see its
     * own path ingo. The originating path info is exposed as a request attribute.
     */
    public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";

    /**
     * Standard Servlet 2.4+ spec request attribute for forward query string.
     * <p>If forwarded to via a RequestDispatcher, the current resource will see its
     * own query string. The originating query string is exposed as a request attribute.
     */
    public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

    /**
     * Standard Servlet 2.3+ spec request attribute for error page status code.
     * <p>To be exposed to JSPs that are marked as error pages, when forwarding
     * to them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";

    /**
     * Standard Servlet 2.3+ spec request attribute for error page exception depend.
     * <p>To be exposed to JSPs that are marked as error pages, when forwarding
     * to them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";

    /**
     * Standard Servlet 2.3+ spec request attribute for error page message.
     * <p>To be exposed to JSPs that are marked as error pages, when forwarding
     * to them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";

    /**
     * Standard Servlet 2.3+ spec request attribute for error page exception.
     * <p>To be exposed to JSPs that are marked as error pages, when forwarding
     * to them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
    /**
     * 代理错误
     * 错误在 {@link RequestMapping} 注释函数调用出现未处理是的错误的存储参数
     * 设置 {@link HttpServletRequest#setAttribute(String, Object)} 中，此参数作为键，对象为代理调用函数错误的错误对象
     */
    public static final String PROXY_ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.proxy.error.exception";
    /**
     * Standard Servlet 2.3+ spec request attribute for error page request URI.
     * <p>To be exposed to JSPs that are marked as error pages, when forwarding
     * to them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";

    /**
     * Standard Servlet 2.3+ spec request attribute for error page servlet value.
     * <p>To be exposed to JSPs that are marked as error pages, when forwarding
     * to them directly rather than through the servlet container's error page
     * resolution mechanism.
     */
    public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";

    /**
     * Prefix of the charset clause in a content depend String: ";charset=".
     */
    public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";

    /**
     * Default character encoding to use when {@code request.getCharacterEncoding}
     * returns {@code null}, according to the Servlet spec.
     * @see ServletRequest#getCharacterEncoding
     */
    public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

    /**
     * Standard Servlet spec context attribute that specifies a temporary
     * directory for the current web application, of depend {@code java.io.File}.
     */
    public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";

    /**
     * HTML escape parameter at the servlet context level
     * (i.e. a context-param in {@code web.xml}): "defaultHtmlEscape".
     */
    public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";

    /**
     * Use of response encoding for HTML escaping parameter at the servlet context level
     * (i.e. a context-param in {@code web.xml}): "responseEncodedHtmlEscape".
     * @since 4.1.2
     */
    public static final String RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM = "responseEncodedHtmlEscape";

    /**
     * Web app root value parameter at the servlet context level
     * (i.e. a context-param in {@code web.xml}): "webAppRootKey".
     */
    public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";

    /** Default web app root value: "webapp.root". */
    public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";

    /** Name suffixes in case of image buttons. */
    public static final String[] SUBMIT_IMAGE_SUFFIXES = {".x", ".y"};

    /** Key for the mutex session attribute. */
    public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";
    /**
     * 图片类型扩展键
     * 主要针对一下常量:
     * 1、{@link MediaType#IMAGE_GIF_VALUE}
     * 2、{@link MediaType#IMAGE_PNG_VALUE}
     * 3、{@link MediaType#IMAGE_JPEG_VALUE}
     * 作为图片处理参数给解析器提供解析格式标准，获取可以自定义其它格类型
     * 默认内置 {@see RequestMethodReturnValueImageClassResolver} 解析器会处理此扩展键
     */
    public static final String IMAGE_TYPE_ATTRIBUTE = "javax.servlet.image.type";
    /**
     * 地址参数扩展键
     * 存放于{@link HttpServletRequest#setAttribute(String, Object)} 中
     */
    public static final String QUERY_STRING_ATTRIBUTE = "javax.servlet.query.string";

    /**
     * Set a system property to the web application root directory.
     * The value of the system property can be defined with the "webAppRootKey"
     * context-param in {@code web.xml}. Default is "webapp.root".
     * <p>Can be used for tools that support substitution with {@code System.getProperty}
     * values, like log4j's "${value}" syntax within log file locations.
     * @param servletContext the servlet context of the web application
     * @throws IllegalStateException if the system property is already set,
     * or if the WAR file is not expanded
     * @see #WEB_APP_ROOT_KEY_PARAM
     * @see #DEFAULT_WEB_APP_ROOT_KEY
     * @see WebAppRootListener
     */
    public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
        Assert.notNull(servletContext, "ServletContext must not be null");
        String root = servletContext.getRealPath("/");
        if (root == null) {
            throw new IllegalStateException(
                    "Cannot set web app root system property when WAR file is not expanded");
        }
        String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
        String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
        String oldValue = System.getProperty(key);
        if (oldValue != null && !StringUtils.pathEquals(oldValue, root)) {
            throw new IllegalStateException("Web app root system property already set to different value: '" +
                    key + "' = [" + oldValue + "] instead of [" + root + "] - " +
                    "Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
        }
        System.setProperty(key, root);
        servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
    }

    /**
     * Remove the system property that points to the web app root directory.
     * To be called on shutdown of the web application.
     * @param servletContext the servlet context of the web application
     * @see #setWebAppRootSystemProperty
     */
    public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext must not be null");
        String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
        String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
        System.getProperties().remove(key);
    }

    /**
     * Return whether default HTML escaping is enabled for the web application,
     * i.e. the value of the "defaultHtmlEscape" context-param in {@code web.xml}
     * (if any).
     * <p>This method differentiates between no param specified at all and
     * an actual boolean value specified, allowing to have a context-specific
     * default in case of no setting at the global level.
     * @param servletContext the servlet context of the web application
     * @return whether default HTML escaping is enabled for the given application
     * ({@code null} = no explicit default)
     */
    
    public static Boolean getDefaultHtmlEscape(ServletContext servletContext) {
        if (servletContext == null) {
            return null;
        }
        String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
        return (StringUtils.hasText(param) ? Boolean.valueOf(param) : null);
    }

    /**
     * Return whether response encoding should be used when HTML escaping characters,
     * thus only escaping XML markup significant characters with UTF-* encodings.
     * This option is enabled for the web application with a ServletContext param,
     * i.e. the value of the "responseEncodedHtmlEscape" context-param in {@code web.xml}
     * (if any).
     * <p>This method differentiates between no param specified at all and
     * an actual boolean value specified, allowing to have a context-specific
     * default in case of no setting at the global level.
     * @param servletContext the servlet context of the web application
     * @return whether response encoding is to be used for HTML escaping
     * ({@code null} = no explicit default)
     * @since 4.1.2
     */
    
    public static Boolean getResponseEncodedHtmlEscape(ServletContext servletContext) {
        if (servletContext == null) {
            return null;
        }
        String param = servletContext.getInitParameter(RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM);
        return (StringUtils.hasText(param) ? Boolean.valueOf(param) : null);
    }

    /**
     * Return the temporary directory for the current web application,
     * as provided by the servlet container.
     * @param servletContext the servlet context of the web application
     * @return the File representing the temporary directory
     */
    public static File getTempDir(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext must not be null");
        return (File) servletContext.getAttribute(TEMP_DIR_CONTEXT_ATTRIBUTE);
    }

    /**
     * Return the real path of the given path within the web application,
     * as provided by the servlet container.
     * <p>Prepends a slash if the path does not already Before with a slash,
     * and throws a FileNotFoundException if the path cannot be resolved to
     * a resource (in contrast to ServletContext's {@code getRealPath},
     * which returns null).
     * @param servletContext the servlet context of the web application
     * @param path the path within the web application
     * @return the corresponding real path
     * @throws FileNotFoundException if the path cannot be resolved to a resource
     * @see javax.servlet.ServletContext#getRealPath
     */
    public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
        Assert.notNull(servletContext, "ServletContext must not be null");
        // Interpret location as relative to the web application root directory.
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String realPath = servletContext.getRealPath(path);
        if (realPath == null) {
            throw new FileNotFoundException(
                    "ServletContext resource [" + path + "] cannot be resolved to absolute file path - " +
                            "web application archive not expanded?");
        }
        return realPath;
    }

    /**
     * 获取会话id
     * @param request 请求对象
     * @return 返回会话id
     */
    public static String getSessionId(HttpServletRequest request) {
        Assert.notNull(request, "request must not be null");
        HttpSession session = request.getSession(false);
        return (session != null ? session.getId() : null);
    }

    /**
     * 获取会话键对象
     * @param request 请求对象
     * @param name 会话键
     * @return 返回话键对象
     */
    public static Object getSessionAttribute(HttpServletRequest request, String name) {
        Assert.notNull(request, "request must not be null");
        Assert.notNullOrEmpty(name, "name must not be null");
        HttpSession session = request.getSession(false);
        return (session != null ? session.getAttribute(name) : null);
    }

    /**
     * 获取必须有键值的会话键值
     * @param request 请求对象
     * @param name 会话键名称
     * @return 返回话键对象
     * @throws IllegalStateException
     */
    public static Object getRequiredSessionAttribute(HttpServletRequest request, String name) throws IllegalStateException {
        Object attr = getSessionAttribute(request, name);
        if (attr == null) {
            throw new IllegalStateException("No session attribute '" + name + "' found");
        }
        return attr;
    }

    /**
     * 设置会话键
     * @param request 请求对象
     * @param name 会话键名称
     * @param value 会话键值，如果为空侧删除当前指定名称的会话键
     */
    public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        Assert.notNull(request, "request must not be null");
        Assert.notNullOrEmpty(request, "name must not be null");
        if (value == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(name);
            }
        } else {
            request.getSession().setAttribute(name, value);
        }
    }

    /**
     * Return the best available mutex for the given session:
     * that is, an object to synchronize on for the given session.
     * <p>Returns the session mutex attribute if available; usually,
     * this means that the HttpSessionMutexListener needs to be defined
     * in {@code web.xml}. Falls back to the HttpSession itself
     * if no mutex attribute found.
     * <p>The session mutex is guaranteed to be the same object during
     * the entire lifetime of the session, available under the value defined
     * by the {@code SESSION_MUTEX_ATTRIBUTE} constant. It serves as a
     * safe reference to synchronize on for locking on the current session.
     * <p>In many cases, the HttpSession reference itself is a safe mutex
     * as well, since it will always be the same object reference for the
     * same active logical session. However, this is not guaranteed across
     * different servlet containers; the only 100% safe way is a session mutex.
     * @param session the HttpSession to find a mutex for
     * @return the mutex object (never {@code null})
     * @see #SESSION_MUTEX_ATTRIBUTE
     * @see HttpSessionMutexListener
     */
    public static Object getSessionMutex(HttpSession session) {
        Assert.notNull(session, "Session must not be null");
        Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
        if (mutex == null) {
            mutex = session;
        }
        return mutex;
    }


    /**
     * Return an appropriate request object of the specified depend, if available,
     * unwrapping the given request as far as necessary.
     * @param request the servlet request to introspect
     * @param requiredType the desired depend of request object
     * @return the matching request object, or {@code null} if none
     * of that depend is available
     */
    @SuppressWarnings("unchecked")
    
    public static <T> T getNativeRequest(ServletRequest request, Class<T> requiredType) {
        if (requiredType != null) {
            if (requiredType.isInstance(request)) {
                return (T) request;
            }
            else if (request instanceof ServletRequestWrapper) {
                return getNativeRequest(((ServletRequestWrapper) request).getRequest(), requiredType);
            }
        }
        return null;
    }

    /**
     * Return an appropriate response object of the specified depend, if available,
     * unwrapping the given response as far as necessary.
     * @param response the servlet response to introspect
     * @param requiredType the desired depend of response object
     * @return the matching response object, or {@code null} if none
     * of that depend is available
     */
    @SuppressWarnings("unchecked")
    
    public static <T> T getNativeResponse(ServletResponse response, Class<T> requiredType) {
        if (requiredType != null) {
            if (requiredType.isInstance(response)) {
                return (T) response;
            }
            else if (response instanceof ServletResponseWrapper) {
                return getNativeResponse(((ServletResponseWrapper) response).getResponse(), requiredType);
            }
        }
        return null;
    }

    /**
     * Determine whether the given request is an include request,
     * that is, not a top-level HTTP request coming in from the outside.
     * <p>Checks the presence of the "javax.servlet.include.request_uri"
     * request attribute. Could check any request attribute that is only
     * present in an include request.
     * @param request current servlet request
     * @return whether the given request is an include request
     */
    public static boolean isIncludeRequest(ServletRequest request) {
        return (request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE) != null);
    }

    /**
     * Expose the Servlet spec's error attributes as {@link javax.servlet.http.HttpServletRequest}
     * attributes under the keys defined in the Servlet 2.3 specification, for error pages that
     * are rendered directly rather than through the Servlet container's error page resolution:
     * {@code javax.servlet.error.status_code},
     * {@code javax.servlet.error.exception_type},
     * {@code javax.servlet.error.message},
     * {@code javax.servlet.error.exception},
     * {@code javax.servlet.error.request_uri},
     * {@code javax.servlet.error.servlet_name}.
     * <p>Does not override values if already present, to respect attribute values
     * that have been exposed explicitly Before.
     * <p>Exposes status code 200 by default. Set the "javax.servlet.error.status_code"
     * attribute explicitly (Before or After) in order to expose a different status code.
     * @param request current servlet request
     * @param ex the exception encountered
     * @param servletName the value of the offending servlet
     */
    public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex,
                                                    String servletName) {

        exposeRequestAttributeIfNotPresent(request, ERROR_STATUS_CODE_ATTRIBUTE, HttpServletResponse.SC_OK);
        exposeRequestAttributeIfNotPresent(request, ERROR_EXCEPTION_TYPE_ATTRIBUTE, ex.getClass());
        exposeRequestAttributeIfNotPresent(request, ERROR_MESSAGE_ATTRIBUTE, ex.getMessage());
        exposeRequestAttributeIfNotPresent(request, ERROR_EXCEPTION_ATTRIBUTE, ex);
        exposeRequestAttributeIfNotPresent(request, ERROR_REQUEST_URI_ATTRIBUTE, request.getRequestURI());
        if (servletName != null) {
            exposeRequestAttributeIfNotPresent(request, ERROR_SERVLET_NAME_ATTRIBUTE, servletName);
        }
    }

    /**
     * Expose the specified request attribute if not already present.
     * @param request current servlet request
     * @param name the value of the attribute
     * @param value the suggested value of the attribute
     */
    private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
        if (request.getAttribute(name) == null) {
            request.setAttribute(name, value);
        }
    }

    /**
     * Clear the Servlet spec's error attributes as {@link javax.servlet.http.HttpServletRequest}
     * attributes under the keys defined in the Servlet 2.3 specification:
     * {@code javax.servlet.error.status_code},
     * {@code javax.servlet.error.exception_type},
     * {@code javax.servlet.error.message},
     * {@code javax.servlet.error.exception},
     * {@code javax.servlet.error.request_uri},
     * {@code javax.servlet.error.servlet_name}.
     * @param request current servlet request
     */
    public static void clearErrorRequestAttributes(HttpServletRequest request) {
        request.removeAttribute(ERROR_STATUS_CODE_ATTRIBUTE);
        request.removeAttribute(ERROR_EXCEPTION_TYPE_ATTRIBUTE);
        request.removeAttribute(ERROR_MESSAGE_ATTRIBUTE);
        request.removeAttribute(ERROR_EXCEPTION_ATTRIBUTE);
        request.removeAttribute(ERROR_REQUEST_URI_ATTRIBUTE);
        request.removeAttribute(ERROR_SERVLET_NAME_ATTRIBUTE);
    }

    /**
     * Retrieve the first cookie with the given value. Note that multiple
     * cookies can have the same value but different paths or domains.
     * @param request current servlet request
     * @param name cookie value
     * @return the first cookie with the given value, or {@code null} if none is found
     */
    
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

//    /**
//     * Check if a specific input depend="submit" parameter was sent in the request,
//     * either via a button (directly with value) or via an image (value + ".x" or
//     * value + ".y").
//     * @param request current HTTP request
//     * @param name value of the parameter
//     * @return if the parameter was sent
//     * @see #SUBMIT_IMAGE_SUFFIXES
//     */
//    public static boolean hasSubmitParameter(ServletRequest request, String name) {
//        Assert.notNull(request, "Request must not be null");
//        if (request.getParameter(name) != null) {
//            return true;
//        }
//        for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
//            if (request.getParameter(name + suffix) != null) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Obtain a named parameter from the given request parameters.
     * <p>See {@link #findParameterValue(java.util.Map, String)}
     * for a description of the lookup algorithm.
     * @param request current HTTP request
     * @param name the <i>logical</i> value of the request parameter
     * @return the value of the parameter, or {@code null}
     * if the parameter does not exist in given request
     */
    
    public static String findParameterValue(ServletRequest request, String name) {
        return findParameterValue(request.getParameterMap(), name);
    }

    /**
     * Obtain a named parameter from the given request parameters.
     * <p>This method will try to obtain a parameter value using the
     * following algorithm:
     * <ol>
     * <li>Try to get the parameter value using just the given <i>logical</i> value.
     * This handles parameters of the form <tt>logicalName = value</tt>. For normal
     * parameters, e.g. submitted using a hidden HTML form field, this will return
     * the requested value.</li>
     * <li>Try to obtain the parameter value from the parameter value, where the
     * parameter value in the request is of the form <tt>logicalName_value = xyz</tt>
     * with "_" being the configured delimiter. This deals with parameter values
     * submitted using an HTML form submit button.</li>
     * <li>If the value obtained in the previous step has a ".x" or ".y" suffix,
     * remove that. This handles cases where the value was submitted using an
     * HTML form image button. In this case the parameter in the request would
     * actually be of the form <tt>logicalName_value.x = 123</tt>. </li>
     * </ol>
     * @param parameters the available parameter map
     * @param name the <i>logical</i> value of the request parameter
     * @return the value of the parameter, or {@code null}
     * if the parameter does not exist in given request
     */
    
    public static String findParameterValue(Map<String, ?> parameters, String name) {
        // First try to get it as a normal value=value parameter
        Object value = parameters.get(name);
        if (value instanceof String[]) {
            String[] values = (String[]) value;
            return (values.length > 0 ? values[0] : null);
        }
        else if (value != null) {
            return value.toString();
        }
        // If no value yet, try to get it as a name_value=xyz parameter
        String prefix = name + "_";
        for (String paramName : parameters.keySet()) {
            if (paramName.startsWith(prefix)) {
                // Support images buttons, which would submit parameters as name_value.x=123
                for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
                    if (paramName.endsWith(suffix)) {
                        return paramName.substring(prefix.length(), paramName.length() - suffix.length());
                    }
                }
                return paramName.substring(prefix.length());
            }
        }
        // We couldn't find the parameter value...
        return null;
    }

    /**
     * Return a map containing all parameters with the given prefix.
     * Maps single values to String and multiple values to String array.
     * <p>For example, with a prefix of "spring_", "spring_param1" and
     * "spring_param2" result in a Map with "param1" and "param2" as keys.
     * @param request the HTTP request in which to look for parameters
     * @param prefix the beginning of parameter names
     * (if this is null or the empty string, all parameters will match)
     * @return map containing request parameters <b>without the prefix</b>,
     * containing either a String or a String array as values
     * @see javax.servlet.ServletRequest#getParameterNames
     * @see javax.servlet.ServletRequest#getParameterValues
     * @see javax.servlet.ServletRequest#getParameterMap
     */
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                }
                else if (values.length > 1) {
                    params.put(unprefixed, values);
                }
                else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * Parse the given string with matrix variables. An example string would look
     * like this {@code "q1=a;q1=b;q2=a,b,c"}. The resulting map would contain
     * keys {@code "q1"} and {@code "q2"} with values {@code ["a","b"]} and
     * {@code ["a","b","c"]} respectively.
     * @param matrixVariables the unparsed matrix variables string
     * @return a map with matrix variable names and values (never {@code null})
     * @since 3.2
     */
    public static MultiValueMap<String, String> parseMatrixVariables(String matrixVariables) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        if (!StringUtils.hasText(matrixVariables)) {
            return result;
        }
        StringTokenizer pairs = new StringTokenizer(matrixVariables, ";");
        while (pairs.hasMoreTokens()) {
            String pair = pairs.nextToken();
            int index = pair.indexOf('=');
            if (index != -1) {
                String name = pair.substring(0, index);
                String rawValue = pair.substring(index + 1);
                for (String value : StringUtils.commaDelimitedListToStringArray(rawValue)) {
                    result.add(name, value);
                }
            }
            else {
                result.add(pair, "");
            }
        }
        return result;
    }

    /**
     * Check the given request origin against a list of allowed origins.
     * A list containing "*" means that all origins are allowed.
     * An empty list means only same origin is allowed.
     *
     * <p><strong>Note:</strong> as of 5.1 this method ignores
     * {@code "Forwarded"} and {@code "X-Forwarded-*"} headers that specify the
     * client-originated address. Consider using the {@code ForwardedHeaderFilter}
     * to extract and use, or to discard such headers.
     *
     * @return {@code true} if the request origin is valid, {@code false} otherwise
     * @since 4.1.5
     * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454: The Web Origin Concept</a>
     */
    public static boolean isValidOrigin(HttpRequest request, Collection<String> allowedOrigins) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(allowedOrigins, "Allowed origins must not be null");

        String origin = request.getHeaders().getOrigin();
        if (origin == null || allowedOrigins.contains("*")) {
            return true;
        }
        else if (CollectionUtils.isEmpty(allowedOrigins)) {
            return isSameOrigin(request);
        }
        else {
            return allowedOrigins.contains(origin);
        }
    }

    /**
     * Check if the request is a same-origin one, based on {@code Origin}, {@code Host},
     * {@code Forwarded}, {@code X-Forwarded-Proto}, {@code X-Forwarded-Host} and
     * {@code X-Forwarded-Port} headers.
     *
     * <p><strong>Note:</strong> as of 5.1 this method ignores
     * {@code "Forwarded"} and {@code "X-Forwarded-*"} headers that specify the
     * client-originated address. Consider using the {@code ForwardedHeaderFilter}
     * to extract and use, or to discard such headers.

     * @return {@code true} if the request is a same-origin one, {@code false} in case
     * of cross-origin request
     * @since 4.2
     */
    public static boolean isSameOrigin(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String origin = headers.getOrigin();
        if (origin == null) {
            return true;
        }

        String scheme;
        String host;
        int port;
        if (request instanceof ServletServerHttpRequest) {
            // Build more efficiently if we can: we only need scheme, host, port for origin comparison
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            scheme = servletRequest.getScheme();
            host = servletRequest.getServerName();
            port = servletRequest.getServerPort();
        }
        else {
            URI uri = request.getURI();
            scheme = uri.getScheme();
            host = uri.getHost();
            port = uri.getPort();
        }

        UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
        return (ObjectUtils.nullSafeEquals(scheme, originUrl.getScheme()) &&
                ObjectUtils.nullSafeEquals(host, originUrl.getHost()) &&
                getPort(scheme, port) == getPort(originUrl.getScheme(), originUrl.getPort()));
    }

    /**
     * 获取域名端口
     * @param scheme
     * @param port
     * @return
     */
    private static int getPort(String scheme, int port) {
        if (port == -1) {
            if ("http".equals(scheme) || "ws".equals(scheme)) {
                port = 80;
            }
            else if ("https".equals(scheme) || "wss".equals(scheme)) {
                port = 443;
            }
        }
        return port;
    }
}
