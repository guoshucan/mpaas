package ghost.framework.web.module.servlet;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.log.LogFormatUtils;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.http.HttpMethod;
import ghost.framework.web.context.http.multipart.MultipartException;
import ghost.framework.web.context.http.multipart.MultipartHttpServletRequest;
import ghost.framework.web.context.http.multipart.support.MultipartResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;
import ghost.framework.web.context.http.server.ServletServerHttpRequest;
import ghost.framework.web.context.io.IWebResource;
import ghost.framework.web.context.io.WebIResourceLoader;
import ghost.framework.web.context.resource.IWebResourceResolverContainer;
import ghost.framework.web.context.server.MimeMappings;
import ghost.framework.web.context.servlet.context.IServletContainer;
import ghost.framework.web.context.utils.WebUtils;
import ghost.framework.web.module.exception.NoSuchBeanDefinitionException;
import ghost.framework.web.module.multipart.support.StandardServletMultipartResolver;
import ghost.framework.web.module.servlet.resolver.AcceptHeaderLocaleResolver;
import ghost.framework.web.module.servlet.locale.LocaleContext;
import ghost.framework.web.context.servlet.locale.LocaleResolver;
import ghost.framework.web.module.servlet.support.SessionFlashMapManager;
import org.apache.log4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
@Component
@ConditionalOnMissingClass(
		{
				HandlerExceptionResolverContainer.class,
				HandlerMappingContainer.class,
//				HandlerAdapterContainer.class,
//				ViewResolverContainer.class,
				StandardServletMultipartResolver.class,
				SessionFlashMapManager.class,
				AcceptHeaderLocaleResolver.class
		}
		)
@BeanMapContainer(IServletContainer.class)
@WebServlet(loadOnStartup = 3, urlPatterns = {"/*"})
public class DispatcherServlet extends FrameworkServlet {
	@Autowired
	private IModule module;
	@Autowired(required = false)
	private IHttpRequestMethodContainer requestMethodContainer;
	/**
	 * 注入mime地图
	 */
	@Autowired
	private MimeMappings mimeMappings;
	public static final String DIRECTORY_LISTING = "directory-listing";
	public static final String DEFAULT_ALLOWED = "default-allowed";
	public static final String ALLOWED_EXTENSIONS = "allowed-extensions";
	public static final String DISALLOWED_EXTENSIONS = "disallowed-extensions";
	public static final String ALLOW_POST = "allow-post";
	private static final Set<String> DEFAULT_ALLOWED_EXTENSIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("js", "css", "png", "jpg", "gif", "html", "htm", "txt", "pdf", "jpeg", "xml")));
	/**
	 * 默认主页
	 */
	private static final List<String> DEFAULT_INDEX = Arrays.asList("/index.html");
	/**
	 * 是否启用目录列表
	 */
	private boolean directoryListingEnabled = false;
	private boolean defaultAllowed = true;
	private Set<String> allowed = DEFAULT_ALLOWED_EXTENSIONS;
	private Set<String> disallowed = Collections.emptySet();
	private boolean allowPost = false;
	/**
	 * web全局资源加载器
	 */
	private WebIResourceLoader resourceLoader;
	/**
	 * web全局资源解析器容器接口
	 */
	private IWebResourceResolverContainer resourceResolverContainer;
	/**
	 * Request attribute to hold the current web application context.
	 * Otherwise only the global web app context is obtainable by tags etc.
	 * @see ghost.framework.web.servlet.support.RequestContextUtils#findWebApplicationContext
	 */
	public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

	/**
	 * Request attribute to hold the current LocaleResolver, retrievable by views.
	 * @see ghost.framework.web.servlet.support.RequestContextUtils#getLocaleResolver
	 */
	public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";

	/**
	 * Name of request attribute that holds a read-only {@code Map<String,?>}
	 * with "input" flash attributes saved by a previous request, if any.
	 * @see ghost.framework.web.servlet.support.RequestContextUtils#getInputFlashMap(HttpServletRequest)
	 */
	public static final String INPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".INPUT_FLASH_MAP";

	/**
	 * Name of request attribute that holds the "output" {@link FlashMap} with
	 * attributes to save for a subsequent request.
	 * @see ghost.framework.web.servlet.support.RequestContextUtils#getOutputFlashMap(HttpServletRequest)
	 */
	public static final String OUTPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".OUTPUT_FLASH_MAP";

	/**
	 * Name of request attribute that holds the {@link FlashMapManager}.
	 * @see ghost.framework.web.servlet.support.RequestContextUtils#getFlashMapManager(HttpServletRequest)
	 */
	public static final String FLASH_MAP_MANAGER_ATTRIBUTE = DispatcherServlet.class.getName() + ".FLASH_MAP_MANAGER";
	/**
	 * 作为在本Servlet内没有找到处理对象时流转至下一个Servlet处理
	 * 此参数作为流转回来的参数凭证，有次参数表示流转了
	 */
	public static final String REQUEST_DISPATCHER_STATUS = DispatcherServlet.class.getName() + ".REQUEST_DISPATCHER_STATUS";
	/**
	 * Name of request attribute that exposes an Exception resolved with a
	 * {@link HandlerExceptionResolver} but where no view was rendered
	 * (e.g. setting the status code).
	 */
	public static final String EXCEPTION_ATTRIBUTE = DispatcherServlet.class.getName() + ".EXCEPTION";

	/** Log category to use when no mapped handler is found for a request. */
	public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "ghost.framework.web.servlet.PageNotFound";

	/**
	 * Name of the class path resource (relative to the DispatcherServlet class)
	 * that defines DispatcherServlet's default strategy names.
	 */
	private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

	/**
	 * Common prefix that DispatcherServlet's default strategy attributes start with.
	 */
	private static final String DEFAULT_STRATEGIES_PREFIX = "ghost.framework.web.servlet";

	/** Additional logger to use when no mapped handler is found for a request. */
	protected static final Logger pageNotFoundLogger = Logger.getLogger(PAGE_NOT_FOUND_LOG_CATEGORY);

	/** Detect all HandlerAdapters or just expect "handlerAdapter" bean?. */
	private boolean detectAllHandlerAdapters = true;

	/** Detect all HandlerExceptionResolvers or just expect "handlerExceptionResolver" bean?. */
	private boolean detectAllHandlerExceptionResolvers = true;

	/** Detect all ViewResolvers or just expect "viewResolver" bean?. */
	private boolean detectAllViewResolvers = true;

	/** Throw a NoHandlerFoundException if no Handler was found to process this request? *.*/
	private boolean throwExceptionIfNoHandlerFound = false;

	/** Perform cleanup of request attributes after include request?. */
	private boolean cleanupAfterInclude = true;

	/** MultipartResolver used by this servlet. */
	@Nullable
	private MultipartResolver multipartResolver;

	/** LocaleResolver used by this servlet. */
	@Nullable
	private LocaleResolver localeResolver;
	/**
	 * 请求地图列表
	 */
	@Nullable
	private List<HandlerMapping> handlerMappings;
	/**
	 * 请求适配器列表
	 */
//	@Nullable
//	private List<HandlerAdapter> handlerAdapters;
	/**
	 * 错误解析器列表
	 */
//	@Nullable
//	private List<HandlerExceptionResolver> handlerExceptionResolvers;

	/** FlashMapManager used by this servlet. */
//	@Nullable
	private FlashMapManager flashMapManager;

	/** List of ViewResolvers used by this servlet. */
//	@Nullable
//	private List<ViewResolver> viewResolvers;

	public DispatcherServlet() {
		super();
		setDispatchOptionsRequest(true);
	}
	/**
	 * Set whether to detect all HandlerAdapter beans in this servlet's context. Otherwise,
	 * just a single bean with name "handlerAdapter" will be expected.
	 * <p>Default is "true". Turn this off if you want this servlet to use a single
	 * HandlerAdapter, despite multiple HandlerAdapter beans being defined in the context.
	 */
	public void setDetectAllHandlerAdapters(boolean detectAllHandlerAdapters) {
		this.detectAllHandlerAdapters = detectAllHandlerAdapters;
	}

	/**
	 * Set whether to detect all HandlerExceptionResolver beans in this servlet's context. Otherwise,
	 * just a single bean with name "handlerExceptionResolver" will be expected.
	 * <p>Default is "true". Turn this off if you want this servlet to use a single
	 * HandlerExceptionResolver, despite multiple HandlerExceptionResolver beans being defined in the context.
	 */
	public void setDetectAllHandlerExceptionResolvers(boolean detectAllHandlerExceptionResolvers) {
		this.detectAllHandlerExceptionResolvers = detectAllHandlerExceptionResolvers;
	}

	/**
	 * Set whether to detect all ViewResolver beans in this servlet's context. Otherwise,
	 * just a single bean with name "viewResolver" will be expected.
	 * <p>Default is "true". Turn this off if you want this servlet to use a single
	 * ViewResolver, despite multiple ViewResolver beans being defined in the context.
	 */
	public void setDetectAllViewResolvers(boolean detectAllViewResolvers) {
		this.detectAllViewResolvers = detectAllViewResolvers;
	}

	/**
	 * Set whether to throw a NoHandlerFoundException when no Handler was found for this request.
	 * This exception can then be caught with a HandlerExceptionResolver or an
	 * {@code @ExceptionHandler} controller method.
	 * <p>Note that if {@link ghost.framework.web.servlet.resource.DefaultServletHttpRequestHandler}
	 * is used, then requests will always be forwarded to the default servlet and a
	 * NoHandlerFoundException would never be thrown in that case.
	 * <p>Default is "false", meaning the DispatcherServlet sends a NOT_FOUND error through the
	 * Servlet response.
	 * @since 4.0
	 */
	public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound) {
		this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
	}

	/**
	 * Set whether to perform cleanup of request attributes after an include request, that is,
	 * whether to reset the original state of all request attributes after the DispatcherServlet
	 * has processed within an include request. Otherwise, just the DispatcherServlet's own
	 * request attributes will be reset, but not entity attributes for JSPs or special attributes
	 * set by views (for example, JSTL's).
	 * <p>Default is "true", which is strongly recommended. Views should not rely on request attributes
	 * having been set by (dynamic) includes. This allows JSP views rendered by an included controller
	 * to use any entity attributes, even with the same names as in the main JSP, without causing side
	 * effects. Only turn this off for special needs, for example to deliberately allow main JSPs to
	 * access attributes from JSP views rendered by an included controller.
	 */
	public void setCleanupAfterInclude(boolean cleanupAfterInclude) {
		this.cleanupAfterInclude = cleanupAfterInclude;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config.getInitParameter("isDefaultAllowed") != null) {
			defaultAllowed = Boolean.valueOf(config.getInitParameter("isDefaultAllowed"));
			allowed = new HashSet<>();
			if (config.getInitParameter("Allowed") != null) {
				allowed.addAll(Arrays.asList(config.getInitParameter("Allowed").split(",")));
			}
			disallowed = new HashSet<>();
			if (config.getInitParameter("Disallowed") != null) {
				disallowed.addAll(Arrays.asList(config.getInitParameter("Disallowed").split(",")));
			}
		}
		if (config.getInitParameter(DEFAULT_ALLOWED) != null) {
			defaultAllowed = Boolean.parseBoolean(config.getInitParameter(DEFAULT_ALLOWED));
		}
		if (config.getInitParameter(ALLOWED_EXTENSIONS) != null) {
			String extensions = config.getInitParameter(ALLOWED_EXTENSIONS);
			allowed = new HashSet<>(Arrays.asList(extensions.split(",")));
		}
		if (config.getInitParameter(DISALLOWED_EXTENSIONS) != null) {
			String extensions = config.getInitParameter(DISALLOWED_EXTENSIONS);
			disallowed = new HashSet<>(Arrays.asList(extensions.split(",")));
		}
		if (config.getInitParameter(ALLOW_POST) != null) {
			allowPost = Boolean.parseBoolean(config.getInitParameter(ALLOW_POST));
		}
		String listings = config.getInitParameter(DIRECTORY_LISTING);
		if (Boolean.valueOf(listings)) {
			this.directoryListingEnabled = true;
		}
		//初始化文件类型
//		MimeMappings.DEFAULT.getAll().forEach(mime ->{this.mimeMappings.add(mime.getExtension(), mime.getMimeType());});
		//获取资源加载接口
		this.resourceLoader = this.module.getBean(WebIResourceLoader.class);
		//获取资源解析器容器接口
		this.resourceResolverContainer = this.module.getBean(IWebResourceResolverContainer.class);
		logger.debug("init(config)");
	}

	@Override
	public void init() throws ServletException {
		super.init();
		logger.debug("init");
		initMultipartResolver();
		initLocaleResolver();
		initHandlerMappings();
		initHandlerAdapters();
		initHandlerExceptionResolvers();
		initViewResolvers();
		initFlashMapManager();
	}

	/**
	 * Initialize the MultipartResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
	 * no multipart handling is provided.
	 */
	private void initMultipartResolver() {
		try {
			this.multipartResolver =  this.module.getBean(MultipartResolver.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Detected " + this.multipartResolver);
			}
			else if (logger.isDebugEnabled()) {
				logger.debug("Detected " + this.multipartResolver.getClass().getSimpleName());
			}
		}
		catch (NoSuchBeanDefinitionException ex) {
			// Default is no multipart resolver.
			this.multipartResolver = null;
			if (logger.isTraceEnabled()) {
				logger.trace("No MultipartResolver declared");
			}
		}
	}
	/**
	 * Initialize the LocaleResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
	 * we default to AcceptHeaderLocaleResolver.
	 */
	private void initLocaleResolver() {
		try {
			this.localeResolver = this.module.getBean(LocaleResolver.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Detected " + this.localeResolver);
			}
			else if (logger.isDebugEnabled()) {
				logger.debug("Detected " + this.localeResolver.getClass().getSimpleName());
			}
		}
		catch (NoSuchBeanDefinitionException ex) {
			if (logger.isTraceEnabled()) {
				logger.trace("No LocaleResolver : using default [" + this.localeResolver.getClass().getSimpleName() + "]");
			}
		}
	}
	/**
	 * Initialize the HandlerMappings used by this class.
	 * <p>If no HandlerMapping beans are defined in the BeanFactory for this namespace,
	 * we default to BeanNameUrlHandlerMapping.
	 */
	private void initHandlerMappings() {
		// Find all HandlerMappings in the ApplicationContext, including ancestor contexts.
		Map<String, HandlerMapping> matchingBeans = this.module.getBean(IHandlerMappingContainer.class);
		if (!matchingBeans.isEmpty()) {
			this.handlerMappings = new ArrayList<>(matchingBeans.values());
		}
		// Ensure we have at least one HandlerMapping, by registering
		// a default HandlerMapping if no other mappings are found.
		if (this.handlerMappings == null) {
			if (logger.isTraceEnabled()) {
				logger.trace("No HandlerMappings declared for servlet '" + getServletName() +
						"': using default strategies from DispatcherServlet.properties");
			}
		}
	}

	/**
	 * Initialize the HandlerAdapters used by this class.
	 * <p>If no HandlerAdapter beans are defined in the BeanFactory for this namespace,
	 * we default to SimpleControllerHandlerAdapter.
	 */
	private void initHandlerAdapters() {
		// Find all HandlerAdapters in the ApplicationContext, including ancestor contexts.
//		Map<String, HandlerAdapter> matchingBeans = this.module.getBean(IHandlerAdapterContainer.class);
//		if (!matchingBeans.isEmpty()) {
//			this.handlerAdapters = new ArrayList<>(matchingBeans.values());
//			// We keep HandlerAdapters in sorted order.
//		}
		// Ensure we have at least some HandlerAdapters, by registering
		// default HandlerAdapters if no other adapters are found.
//		if (this.handlerAdapters == null) {
//			if (logger.isTraceEnabled()) {
//				logger.trace("No HandlerAdapters declared for servlet '" + getServletName() +
//						"': using default strategies from DispatcherServlet.properties");
//			}
//		}
	}

	/**
	 * Initialize the HandlerExceptionResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
	 * we default to no exception resolver.
	 */
	private void initHandlerExceptionResolvers() {
		// Find all HandlerExceptionResolvers in the ApplicationContext, including ancestor contexts.
//		Map<String, HandlerExceptionResolver> matchingBeans = this.module.getBean(IHandlerExceptionResolverContainer.class);
//		if (!matchingBeans.isEmpty()) {
//			this.handlerExceptionResolvers = new ArrayList<>(matchingBeans.values());
//			// We keep HandlerExceptionResolvers in sorted order.
//		}
		// Ensure we have at least some HandlerExceptionResolvers, by registering
		// default HandlerExceptionResolvers if no other resolvers are found.
//		if (this.handlerExceptionResolvers == null) {
//			if (logger.isTraceEnabled()) {
//				logger.trace("No HandlerExceptionResolvers declared in servlet '" + getServletName() +
//						"': using default strategies from DispatcherServlet.properties");
//			}
//		}
	}

	/**
	 * Initialize the ViewResolvers used by this class.
	 * <p>If no ViewResolver beans are defined in the BeanFactory for this
	 * namespace, we default to InternalResourceViewResolver.
	 */
	private void initViewResolvers() {
		// Find all ViewResolvers in the ApplicationContext, including ancestor contexts.
//		Map<String, ViewResolver> matchingBeans = this.module.getBean(IViewResolverContainer.class);
//		if (!matchingBeans.isEmpty()) {
//			this.viewResolvers = new ArrayList<>(matchingBeans.values());
//			// We keep ViewResolvers in sorted order.
//		}
		// Ensure we have at least one ViewResolver, by registering
		// a default ViewResolver if no other resolvers are found.
//		if (this.viewResolvers == null) {
//			if (logger.isTraceEnabled()) {
//				logger.trace("No ViewResolvers declared for servlet '" + getServletName() +
//						"': using default strategies from DispatcherServlet.properties");
//			}
//		}
	}

	/**
	 * Initialize the {@link FlashMapManager} used by this servlet instance.
	 * <p>If no implementation is configured then we default to
	 * {@code ghost.framework.web.servlet.support.DefaultFlashMapManager}.
	 */
	private void initFlashMapManager() {
		try {
			this.flashMapManager = this.module.getBean(FlashMapManager.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Detected " + this.flashMapManager.getClass().getSimpleName());
			}
			else if (logger.isDebugEnabled()) {
				logger.debug("Detected " + this.flashMapManager);
			}
		}
		catch (NoSuchBeanDefinitionException ex) {
			// We need to use the default.
			if (logger.isTraceEnabled()) {
				logger.trace("No FlashMapManager : using default [" + this.flashMapManager.getClass().getSimpleName() + "]");
			}
		}
	}

//	/**
//	 * Return this servlet's ThemeSource, if any; else return {@code null}.
//	 * <p>Default is to return the WebApplicationContext as ThemeSource,
//	 * provided that it implements the ThemeSource interface.
//	 * @return the ThemeSource, if any
//	 * @see #getWebApplicationContext()
//	 */
//	@Nullable
//	public final ThemeSource getThemeSource() {
//		return (getWebApplicationContext() instanceof ThemeSource ? (ThemeSource) getWebApplicationContext() : null);
//	}

	/**
	 * Obtain this servlet's MultipartResolver, if any.
	 * @return the MultipartResolver used by this servlet, or {@code null} if none
	 * (indicating that no multipart support is available)
	 */
	@Nullable
	public final MultipartResolver getMultipartResolver() {
		return this.multipartResolver;
	}

	/**
	 * Return the configured {@link HandlerMapping} beans that were detected by
	 * type in the {@link WebApplicationContext} or initialized based on the
	 * default set of strategies from {@literal DispatcherServlet.properties}.
	 * <p><strong>Note:</strong> This method may return {@code null} if invoked
	 * prior to {@link #onRefresh(ApplicationContext)}.
	 * @return an immutable list with the configured mappings, or {@code null}
	 * if not initialized yet
	 * @since 5.0
	 */
	@Nullable
	public final List<HandlerMapping> getHandlerMappings() {
		return (this.handlerMappings != null ? Collections.unmodifiableList(this.handlerMappings) : null);
	}

	/**
	 * 请求入口，在基础处理后执行 {@link #doDispatch} 调度
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//判断如果有指向地址直接退出
		if(request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI) != null) {
			return;
		}
		logRequest(request);
		// Keep a snapshot of the request attributes in case of an include,
		// to be able to restore the original attributes after the include.
		Map<String, Object> attributesSnapshot = null;
		if (WebUtils.isIncludeRequest(request)) {
			attributesSnapshot = new HashMap<>();
			Enumeration<?> attrNames = request.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String attrName = (String) attrNames.nextElement();
				if (this.cleanupAfterInclude || attrName.startsWith(DEFAULT_STRATEGIES_PREFIX)) {
					attributesSnapshot.put(attrName, request.getAttribute(attrName));
				}
			}
		}
		//设置请求对象的扩展属性
		request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
		request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
		//
		if (this.flashMapManager != null) {
			FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
			if (inputFlashMap != null) {
				request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
			}
			request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
			request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
		}
		//基础处理完成，调度执行
		try {
			doDispatch(request, response);
		} finally {
//			if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
			// Restore the original attribute snapshot, in case of an include.
			if (attributesSnapshot != null) {
				restoreAttributesAfterInclude(request, attributesSnapshot);
			}
//			}
		}
	}

	private void logRequest(HttpServletRequest request) {
		LogFormatUtils.traceDebug(logger, traceOn -> {
			String params;
			if (isEnableLoggingRequestDetails()) {
				params = request.getParameterMap().entrySet().stream()
						.map(entry -> entry.getKey() + ":" + Arrays.toString(entry.getValue()))
						.collect(Collectors.joining(", "));
			}
			else {
				params = (request.getParameterMap().isEmpty() ? "" : "masked");
			}

			String queryString = request.getQueryString();
			String queryClause = (StringUtils.hasLength(queryString) ? "?" + queryString : "");
			String dispatchType = (!request.getDispatcherType().equals(DispatcherType.REQUEST) ?
					"\"" + request.getDispatcherType().name() + "\" dispatch for " : "");
			String message = (dispatchType + request.getMethod() + " \"" + getRequestUri(request) +
					queryClause + "\", parameters={" + params + "}");

			if (traceOn) {
				List<String> values = Collections.list(request.getHeaderNames());
				String headers = values.size() > 0 ? "masked" : "";
				if (isEnableLoggingRequestDetails()) {
					headers = values.stream().map(name -> name + ":" + Collections.list(request.getHeaders(name)))
							.collect(Collectors.joining(", "));
				}
				return message + ", headers={" + headers + "} in DispatcherServlet '" + getServletName() + "'";
			}
			else {
				return message;
			}
		});
	}

	/**
	 * 执行调度
	 * 在 {@sse #doService} 基础处理完成后调用此函数
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		//声明处理执行链
		HandlerExecutionChain mappedHandler = null;
		//声明是否为 multipart 上传文件模式发起的请求
		boolean multipartRequestParsed = false;
//		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
		try {
//			ModelAndView mv = null;
			//声明调度错误
			Exception dispatchException = null;
			try {
				//检查是否为 multipart 上传文件请求模式
				processedRequest = checkMultipart(request);
				//判断 multipart 上传文件的请求模式，如果请求与检查返回的请求对象不等同侧为 multipart 上传文件请求模式
				multipartRequestParsed = (processedRequest != request);
				//获取请求地图对象
				mappedHandler = getHandler(processedRequest);
				//判断是否有找到请求处理对象
				if (mappedHandler == null) {
					//处理其它路径页面内容，转到下一个Servlet
					// 获取session对象
					HttpSession session = request.getSession();
					// 保存数据
					session.setAttribute("username", "username");
					// 打印SessionID
					System.out.println(session.getId());
//					if (request.getAttribute(REQUEST_DISPATCHER_STATUS) == null) {
//						request.setAttribute(REQUEST_DISPATCHER_STATUS, "INCLUDE");
//						request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
						//返回核对处理结果
//						return;
//					}
					//没有找到处理请求对象执行 HttpServletResponse.SC_NOT_FOUND 返回
//					noHandlerFound(processedRequest, response);
					//处理资源请求
					this.doResourceDispatch(processedRequest, response);
					//不再做任何处理
					return;
				}
				// Determine handler adapter for the current request.
//				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
//				// Process last-modified header, if supported by the handler.
//				String method = request.getMethod();
//				boolean isGet = "GET".equals(method);
//				if (isGet || "HEAD".equals(method)) {
//					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
//					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
//						return;
//					}
//				}
//
//				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
//					return;
//				}
//
//				// Actually invoke the handler.
//				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

//				if (asyncManager.isConcurrentHandlingStarted()) {
//					return;
//				}

//				applyDefaultViewName(processedRequest, mv);
//				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
			catch (Exception ex) {
				dispatchException = ex;
			}
			catch (Throwable err) {
				// As of 4.3, we're processing Errors thrown from handler methods as well,
				// making them available for @ExceptionHandler methods and other scenarios.
				dispatchException = new NestedServletException("Handler dispatch failed", err);
			}
//			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		}
		catch (Exception ex) {
			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
		}
		catch (Throwable err) {
			triggerAfterCompletion(processedRequest, response, mappedHandler,
					new NestedServletException("Handler processing failed", err));
		}
		finally {
//			if (asyncManager.isConcurrentHandlingStarted()) {
//				// Instead of postHandle and afterCompletion
//				if (mappedHandler != null) {
//					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
//				}
//			}
//			else {
				// Clean up any resources used by a multipart request.
				if (multipartRequestParsed) {
					cleanupMultipart(processedRequest);
				}
//			}
		}
	}

	/**
	 * 解析默认资源文件调度处理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void doResourceDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!request.getMethod().equals(HttpMethod.GET.name())) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
//		//获取请求路径
		String path = getPath(request);
		//判断开始路径
		if (path.endsWith("/")) {
			String defaultIndex = (String) request.getAttribute(WebUtils.DEFAULT_INDEX_ATTRIBUTE);
			if (defaultIndex == null) {
				defaultIndex = DEFAULT_INDEX.get(0);
				request.setAttribute(WebUtils.DEFAULT_INDEX_ATTRIBUTE, defaultIndex);
				request.getRequestDispatcher(defaultIndex).forward(request, response);
				return;
			}
		}
		//判断不允许的请求直接返回 SC_NOT_FOUND==404
		if (!this.resourceResolverContainer.isAllowed(path, request.getDispatcherType())) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		//获取请求资源路径
		final IWebResource resource = resourceLoader.getResource(path);
		if (resource == null) {
			//
			if (request.getDispatcherType() == DispatcherType.INCLUDE) {
				//DispatcherType.INCLUDE 如果为本生处理引发错误
				throw new FileNotFoundException(path);
			} else {
				String defaultIndex = (String) request.getAttribute(WebUtils.DEFAULT_INDEX_ATTRIBUTE);
				if (defaultIndex != null) {
					//判断默认主页位置是否遍历，如果到了末位还为找到资源直接返回错误
					if (DEFAULT_INDEX.indexOf(defaultIndex) == (DEFAULT_INDEX.size() - 1)) {
						//UNDERTOW-432
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
					defaultIndex = DEFAULT_INDEX.get(DEFAULT_INDEX.indexOf(defaultIndex) + 1);
					request.setAttribute(WebUtils.DEFAULT_INDEX_ATTRIBUTE, defaultIndex);
					request.getRequestDispatcher(defaultIndex).forward(request, response);
					return;
				}
				//SC_NOT_FOUND==404
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			return;
		}
		//设置解析类型地图
		request.setAttribute(WebUtils.MIME_MAPPINGS_ATTRIBUTE, this.mimeMappings);
		//响应解析器处理
		this.resourceResolverContainer.resolver(path, request, response, resource);
	}
	/**
	 * 获取请求路径
	 *
	 * @param request
	 * @return
	 */
	private String getPath(final HttpServletRequest request) {
		String servletPath;
		String pathInfo;
		//判断如果为转过来的获取转向路径信息
		if (/*request.getDispatcherType() == DispatcherType.INCLUDE && */request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI) != null) {
			pathInfo = (String) request.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
			servletPath = (String) request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
		} else {
			pathInfo = request.getPathInfo();
			servletPath = request.getServletPath();
		}
		String result = pathInfo;
		if (result == null) {
			//将本地路径转换为url路径
			result = org.apache.commons.lang3.StringUtils.replaceAll(servletPath, "\\", "/");
		}
		if (org.apache.commons.lang3.StringUtils.isEmpty(result)) {
			result = "/";
		}
		return result;
	}

//	/**
//	 * Do we need view name translation?
//	 */
//	private void applyDefaultViewName(HttpServletRequest request, @Nullable ModelAndView mv) throws Exception {
//		if (mv != null && !mv.hasView()) {
//			String defaultViewName = getDefaultViewName(request);
//			if (defaultViewName != null) {
//				mv.setViewName(defaultViewName);
//			}
//		}
//	}
//
//	/**
//	 * Handle the result of handler selection and handler invocation, which is
//	 * either a ModelAndView or an Exception to be resolved to a ModelAndView.
//	 */
//	private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
//									   @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
//									   @Nullable Exception exception) throws Exception {
//
//		boolean errorView = false;
//
//		if (exception != null) {
//			if (exception instanceof ModelAndViewDefiningException) {
//				logger.debug("ModelAndViewDefiningException encountered", exception);
//				mv = ((ModelAndViewDefiningException) exception).getModelAndView();
//			}
//			else {
//				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
//				mv = processHandlerException(request, response, handler, exception);
//				errorView = (mv != null);
//			}
//		}
//
//		// Did the handler return a view to render?
//		if (mv != null && !mv.wasCleared()) {
//			render(mv, request, response);
//			if (errorView) {
//				WebUtils.clearErrorRequestAttributes(request);
//			}
//		}
//		else {
//			if (logger.isTraceEnabled()) {
//				logger.trace("No view rendering, null ModelAndView returned.");
//			}
//		}
//
//		if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
//			// Concurrent handling started during a forward
//			return;
//		}
//
//		if (mappedHandler != null) {
//			// Exception (if any) is already handled..
//			mappedHandler.triggerAfterCompletion(request, response, null);
//		}
//	}

	/**
	 * Build a LocaleContext for the given request, exposing the request's primary locale as current locale.
	 * <p>The default implementation uses the dispatcher's LocaleResolver to obtain the current locale,
	 * which might change during a request.
	 * @param request current HTTP request
	 * @return the corresponding LocaleContext
	 */
	@Override
	protected LocaleContext buildLocaleContext(final HttpServletRequest request) {
		LocaleResolver lr = this.localeResolver;
		if (lr instanceof LocaleContextResolver) {
			return ((LocaleContextResolver) lr).resolveLocaleContext(request);
		}
		else {
			return () -> (lr != null ? lr.resolveLocale(request) : request.getLocale());
		}
	}

	/**
	 * Convert the request into a multipart request, and make multipart resolver available.
	 * <p>If no multipart resolver is set, simply use the existing request.
	 * @param request current HTTP request
	 * @return the processed request (multipart wrapper if necessary)
	 * @see MultipartResolver#resolveMultipart
	 */
	protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
		if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
			if (WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class) != null) {
				if (request.getDispatcherType().equals(DispatcherType.REQUEST)) {
					logger.trace("Request already resolved to MultipartHttpServletRequest, e.g. by MultipartFilter");
				}
			}
			else if (hasMultipartException(request)) {
				logger.debug("Multipart resolution previously failed for current request - " +
						"skipping re-resolution for undisturbed error rendering");
			}
			else {
				try {
					return this.multipartResolver.resolveMultipart(request);
				}
				catch (MultipartException ex) {
					if (request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) != null) {
						logger.debug("Multipart resolution failed for error dispatch", ex);
						// Keep processing error dispatch with regular request handle below
					}
					else {
						throw ex;
					}
				}
			}
		}
		// If not returned before: return original request.
		return request;
	}

	/**
	 * Check "javax.servlet.error.exception" attribute for a multipart exception.
	 */
	private boolean hasMultipartException(HttpServletRequest request) {
		Throwable error = (Throwable) request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
		while (error != null) {
			if (error instanceof MultipartException) {
				return true;
			}
			error = error.getCause();
		}
		return false;
	}

	/**
	 * Clean up any resources used by the given multipart request (if any).
	 * @param request current HTTP request
	 * @see MultipartResolver#cleanupMultipart
	 */
	protected void cleanupMultipart(HttpServletRequest request) {
		if (this.multipartResolver != null) {
			MultipartHttpServletRequest multipartRequest =
					WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
			if (multipartRequest != null) {
				this.multipartResolver.cleanupMultipart(multipartRequest);
			}
		}
	}

	/**
	 * Return the HandlerExecutionChain for this request.
	 * <p>Tries all handler mappings in order.
	 * @param request current HTTP request
	 * @return the HandlerExecutionChain, or {@code null} if no handler could be found
	 */
	@Nullable
	protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		if (this.handlerMappings != null) {
			for (HandlerMapping mapping : this.handlerMappings) {
				HandlerExecutionChain handler = mapping.getHandler(request);
				if (handler != null) {
					return handler;
				}
			}
		}
		return null;
	}

	/**
	 * No handler found -> set appropriate HTTP response status.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception if preparing the response failed
	 */
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//if (pageNotFoundLogger.isWarnEnabled()) {
			pageNotFoundLogger.warn("No mapping for " + request.getMethod() + " " + getRequestUri(request));
		//}
		if (this.throwExceptionIfNoHandlerFound) {
			throw new NoHandlerFoundException(request.getMethod(), getRequestUri(request),
					new ServletServerHttpRequest(request).getHeaders());
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

//	/**
//	 * 获取处理适配器
//	 * @param handler
//	 * @return
//	 * @throws ServletException
//	 */
//	protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
//		//如果没有设配器列表不处理
//		if (this.handlerAdapters != null) {
//			//遍历设配器列表
//			for (HandlerAdapter adapter : this.handlerAdapters) {
//				//匹配设配器
//				if (adapter.supports(handler)) {
//					//找到适配器返回
//					return adapter;
//				}
//			}
//		}
//		//没有找到有效处理适配器错误
//		throw new ServletException("No adapter for handler [" + handler.toString() +
//				"]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
//	}
//
//	/**
//	 * Determine an error ModelAndView via the registered HandlerExceptionResolvers.
//	 * @param request current HTTP request
//	 * @param response current HTTP response
//	 * @param handler the executed handler, or {@code null} if none chosen at the time of the exception
//	 * (for example, if multipart resolution failed)
//	 * @param ex the exception that got thrown during handler execution
//	 * @return a corresponding ModelAndView to forward to
//	 * @throws Exception if no error ModelAndView found
//	 */
//	@Nullable
//	protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response,
//                                                   @Nullable Object handler, Exception ex) throws Exception {
//
//		// Success and error responses may use different content types
//		request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
//
//		// Check registered HandlerExceptionResolvers...
//		ModelAndView exMv = null;
//		if (this.handlerExceptionResolvers != null) {
//			for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
//				exMv = resolver.resolveException(request, response, handler, ex);
//				if (exMv != null) {
//					break;
//				}
//			}
//		}
//		if (exMv != null) {
//			if (exMv.isEmpty()) {
//				request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
//				return null;
//			}
//			// We might still need view name translation for a plain error entity...
//			if (!exMv.hasView()) {
//				String defaultViewName = getDefaultViewName(request);
//				if (defaultViewName != null) {
//					exMv.setViewName(defaultViewName);
//				}
//			}
//			if (logger.isTraceEnabled()) {
//				logger.trace("Using resolved error view: " + exMv, ex);
//			}
//			else if (logger.isDebugEnabled()) {
//				logger.debug("Using resolved error view: " + exMv);
//			}
//			WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
//			return exMv;
//		}
//
//		throw ex;
//	}

//	/**
//	 * Render the given ModelAndView.
//	 * <p>This is the last stage in handling a request. It may involve resolving the view by name.
//	 * @param mv the ModelAndView to render
//	 * @param request current HTTP servlet request
//	 * @param response current HTTP servlet response
//	 * @throws ServletException if view is missing or cannot be resolved
//	 * @throws Exception if there's a problem rendering the view
//	 */
//	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// Determine locale for request and apply it to the response.
//		Locale locale =
//				(this.localeResolver != null ? this.localeResolver.resolveLocale(request) : request.getLocale());
//		response.setLocale(locale);
//
//		View view;
//		String viewName = mv.getViewName();
//		if (viewName != null) {
//			// We need to resolve the view name.
//			view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
//			if (view == null) {
//				throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
//						"' in servlet with name '" + getServletName() + "'");
//			}
//		}
//		else {
//			// No need to lookup: the ModelAndView object contains the actual View object.
//			view = mv.getView();
//			if (view == null) {
//				throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
//						"View object in servlet with name '" + getServletName() + "'");
//			}
//		}
//
//		// Delegate to the View object for rendering.
//		if (logger.isTraceEnabled()) {
//			logger.trace("Rendering view [" + view + "] ");
//		}
//		try {
//			if (mv.getStatus() != null) {
//				response.setStatus(mv.getStatus().value());
//			}
//			view.render(mv.getModelInternal(), request, response);
//		}
//		catch (Exception ex) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Error rendering view [" + view + "]", ex);
//			}
//			throw ex;
//		}
//	}

	/**
	 * Translate the supplied request into a default view name.
	 * @param request current HTTP servlet request
	 * @return the view name (or {@code null} if no default found)
	 * @throws Exception if view name translation failed
	 */
	@Nullable
	protected String getDefaultViewName(HttpServletRequest request) throws Exception {
		return (this.viewNameTranslator != null ? this.viewNameTranslator.getViewName(request) : null);
	}
	/** RequestToViewNameTranslator used by this servlet. */
	@Nullable
	private RequestToViewNameTranslator viewNameTranslator;
//	/**
//	 * Resolve the given view name into a View object (to be rendered).
//	 * <p>The default implementations asks all ViewResolvers of this dispatcher.
//	 * Can be overridden for custom resolution strategies, potentially based on
//	 * specific entity attributes or request parameters.
//	 * @param viewName the name of the view to resolve
//	 * @param entity the entity to be passed to the view
//	 * @param locale the current locale
//	 * @param request current HTTP servlet request
//	 * @return the View object, or {@code null} if none found
//	 * @throws Exception if the view cannot be resolved
//	 * (typically in case of problems creating an actual View object)
//	 * @see ViewResolver#resolveViewName
//	 */
//	@Nullable
//	protected View resolveViewName(String viewName, @Nullable Map<String, Object> entity,
//			Locale locale, HttpServletRequest request) throws Exception {
//
//		if (this.viewResolvers != null) {
//			for (ViewResolver viewResolver : this.viewResolvers) {
//				View view = viewResolver.resolveViewName(viewName, locale);
//				if (view != null) {
//					return view;
//				}
//			}
//		}
//		return null;
//	}

	private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response,
										@Nullable HandlerExecutionChain mappedHandler, Exception ex) throws Exception {

		if (mappedHandler != null) {
			mappedHandler.triggerAfterCompletion(request, response, ex);
		}
		throw ex;
	}

	/**
	 * Restore the request attributes after an include.
	 * @param request current HTTP request
	 * @param attributesSnapshot the snapshot of the request attributes before the include
	 */
	@SuppressWarnings("unchecked")
	private void restoreAttributesAfterInclude(HttpServletRequest request, Map<?, ?> attributesSnapshot) {
		// Need to copy into separate Collection here, to avoid side effects
		// on the Enumeration when removing attributes.
		Set<String> attrsToCheck = new HashSet<>();
		Enumeration<?> attrNames = request.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String attrName = (String) attrNames.nextElement();
			if (this.cleanupAfterInclude || attrName.startsWith(DEFAULT_STRATEGIES_PREFIX)) {
				attrsToCheck.add(attrName);
			}
		}

		// Add attributes that may have been removed
		attrsToCheck.addAll((Set<String>) attributesSnapshot.keySet());

		// Iterate over the attributes to check, restoring the original value
		// or removing the attribute, respectively, if appropriate.
		for (String attrName : attrsToCheck) {
			Object attrValue = attributesSnapshot.get(attrName);
			if (attrValue == null) {
				request.removeAttribute(attrName);
			}
			else if (attrValue != request.getAttribute(attrName)) {
				request.setAttribute(attrName, attrValue);
			}
		}
	}

	private static String getRequestUri(HttpServletRequest request) {
		String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return uri;
	}
}