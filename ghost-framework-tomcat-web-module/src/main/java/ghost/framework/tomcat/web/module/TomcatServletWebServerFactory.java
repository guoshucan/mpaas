/*
 * Copyright 2012-2019 the original author or authors.
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

package ghost.framework.tomcat.web.module;
import ghost.framework.beans.annotation.*;
import ghost.framework.beans.annotation.TempDirectory;
import ghost.framework.beans.annotation.module.annotation.Module;
import ghost.framework.core.module.IModule;
import ghost.framework.web.module.server.ErrorPage;
import ghost.framework.web.module.server.MimeMappings;
import ghost.framework.web.module.server.WebServer;
import ghost.framework.web.module.servlet.context.ServletContextInitializer;
import ghost.framework.web.module.servlet.server.AbstractServletWebServerFactory;
import ghost.framework.web.module.servlet.test.HttpServletTest;
import ghost.framework.util.Assert;
import ghost.framework.util.ReflectionUtils;
import ghost.framework.util.StringUtils;
import org.apache.catalina.*;
import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.Tomcat.FixContextListener;
import org.apache.catalina.util.LifecycleBase;
import org.apache.catalina.webresources.AbstractResourceSet;
import org.apache.catalina.webresources.EmptyResource;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link AbstractServletWebServerFactory} that can be used to create
 * {@link TomcatWebServer}s. Can be initialized using Spring's
 * {@link ServletContextInitializer}s or Tomcat {@link LifecycleListener}s.
 * <p>
 * Unless explicitly configured otherwise this factory will create containers that listen
 * for HTTP requests on port 8080.
 *
 * @author Phillip Webb
 * @author Dave Syer
 * @author Brock Mills
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Eddú Meléndez
 * @author Christoffer Sawicki
 * @author Dawid Antecki
 * @since 2.0.0
 * @see #setPort(int)
 * @see #setContextLifecycleListeners(Collection)
 * @see TomcatWebServer
 */
@Component
public class TomcatServletWebServerFactory extends AbstractServletWebServerFactory implements ConfigurableTomcatWebServerFactory {
	/**
	 * 注入模块
	 */
	@Autowired
	private IModule module;

	/**
	 * @param className 类型名称
	 * @return
	 */
	@Override
	protected boolean isPresent(String className) {
		return module.isPresent(className);
	}

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();

	/**
	 * The class name of default protocol used.
	 */
	public static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
	/**
	 * 注入模块临时模块
	 */
	@Module
	@TempDirectory(
			increasePath = "servlet",//注释在目录增加的路径
			suffixMethodName = "getPort",//注释在目录增加的后缀路径函数名称
			deleteAfterCreation = false,
			deleteOnExit = false
	)
	@Order
	private File baseDirectory;

	/**
	 * Servlet容器处理列表
	 */
	private List<Valve> engineValves = new ArrayList<>();
	/**
	 *
	 */
	private List<Valve> contextValves = new ArrayList<>();
	/**
	 * 生命周期事件监听列表
	 */
	private List<LifecycleListener> contextLifecycleListeners = getDefaultLifecycleListeners();
	/**
	 * 内容定义列表
	 */
	private Set<TomcatContextCustomizer> tomcatContextCustomizers = new LinkedHashSet<>();
	/**
	 * 连接器定义列表
	 */
	private Set<TomcatConnectorCustomizer> tomcatConnectorCustomizers = new LinkedHashSet<>();
	/**
	 * 链接处理定义列表
	 */
	private Set<TomcatProtocolHandlerCustomizer<?>> tomcatProtocolHandlerCustomizers = new LinkedHashSet<>();
	/**
	 * 自定义连接器列表
	 */
	private final List<Connector> additionalTomcatConnectors = new ArrayList<>();
	/**
	 * 连接协议
	 */
	private String protocol = DEFAULT_PROTOCOL;
	/**
	 * TOMCAT依赖包
	 */
	private Set<String> tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.TOMCAT);
	/**
	 * 连接编码
	 */
	private Charset uriEncoding = DEFAULT_CHARSET;
	/**
	 * Servlet容器Engine的线程读取时间定义
	 */
	private int backgroundProcessorDelay = 10;

//	private boolean disableMBeanRegistry = true;

	/**
	 * Create a new {@link TomcatServletWebServerFactory} instance.
	 */
	@Constructor
	public TomcatServletWebServerFactory() {
	}

	/**
	 * Create a new {@link TomcatServletWebServerFactory} that listens for requests using
	 * the specified port.
	 *
	 * @param port the port to listen on
	 */
	public TomcatServletWebServerFactory(int port) {
		super(port);
	}

	/**
	 * Create a new {@link TomcatServletWebServerFactory} with the specified context path
	 * and port.
	 *
	 * @param contextPath the root context path
	 * @param port        the port to listen on
	 */
	public TomcatServletWebServerFactory(String contextPath, int port) {
		super(contextPath, port);
	}

	/**
	 * 获取默认生命周期事件监听列表
	 * @return
	 */
	private static List<LifecycleListener> getDefaultLifecycleListeners() {
		AprLifecycleListener aprLifecycleListener = new AprLifecycleListener();
		return AprLifecycleListener.isAprAvailable() ? new ArrayList<>(Arrays.asList(aprLifecycleListener))
				: new ArrayList<>();
	}

	/**
	 * 获取tomcat web服务接口
	 *
	 * @param initializers {@link ServletContextInitializer}s that should be applied as
	 *                     the server starts
	 * @return
	 */
	@Bean
	@Override
	public WebServer getWebServer(
			@Module(name = TomcatWebModuleConstant.ModuleWebName)//注释从指定模块容器获取注入参数
			@Autowired("ghost.framework.web.module.servlet.context.ServletContextInitializerContainer")//指定注入参数类型
					Collection<ServletContextInitializer> initializers) {
//		if (this.disableMBeanRegistry) {
			Registry.disableRegistry();//jmx功能注释
//		}
		//创建压缩
//		this.setCompression(new Compression());
//		this.getCompression().setEnabled(true);
		//创建服务
		Tomcat tomcat = new Tomcat();
		//设置基础目录
		tomcat.setBaseDir(this.baseDirectory.getAbsolutePath());
		//设置文档根目录
		configureDocBaseDirectory();
		//创建连接协议
		Connector connector = new Connector(this.protocol);
		//未处理错误退出连接器
		connector.setThrowOnFailure(false);
		//添加连接器
		tomcat.getService().addConnector(connector);
		//配置自定义连接器
		customizeConnector(connector);
		//定义连接器后重新设置连接器
		tomcat.setConnector(connector);
		//设置禁止自动web.xml修改重载
		tomcat.getHost().setAutoDeploy(false);
		//设置Servlet容器
		configureEngine(tomcat.getEngine());
		//设置添加定义连接器
		for (Connector additionalConnector : this.additionalTomcatConnectors) {
			tomcat.getService().addConnector(additionalConnector);
		}
		//设置Host容器
		prepareContext(tomcat.getHost(), initializers);
		//创建返回web服务
		return getTomcatWebServer(tomcat);
	}

	/**
	 * 设置文档根目录
	 */
	private void configureDocBaseDirectory() {
		File file = new File(this.baseDirectory.getPath() + File.separator + "docbase");
		if (file.exists()) {
			file.delete();
		}
		file.mkdir();
		file.deleteOnExit();
		this.setDocumentRoot(file);
	}
	/**
	 * 设置Servlet容器
	 * @param engine
	 */
	private void configureEngine(Engine engine) {
		//设置容器的背景进程时间值
		engine.setBackgroundProcessorDelay(this.backgroundProcessorDelay);
		//设置Servlet容器值
		for (Valve valve : this.engineValves) {
			//添加Servlet容器通道值
			engine.getPipeline().addValve(valve);
		}
	}

	/**
	 * 设置Host容器
	 * @param host 容器
	 * @param initializers 初始化servlet filter listener内容
	 */
	protected void prepareContext(Host host, Collection<ServletContextInitializer> initializers) {
		//获取web根目录
		File documentRoot = getValidDocumentRoot();
		//初始化嵌入内容
		TomcatEmbeddedContext context = new TomcatEmbeddedContext();
		//设置嵌入内容
		if (documentRoot != null) {
			context.setResources(new LoaderHidingResourceRoot(context));
		}
		context.setName(getContextPath());
		context.setDisplayName(getDisplayName());
		context.setPath(getContextPath());
		//设置文档基本目录
		context.setDocBase(documentRoot.getAbsolutePath());
		//设置生命周期事件监听器
		context.addLifecycleListener(new FixContextListener());
		//设置父级类加载器
		context.setParentClassLoader(new URLClassLoader(new URL[] {}));
		//重置默认区域信息
		resetDefaultLocaleMapping(context);
		//加载区域信息
		addLocaleMappings(context);
		//
		context.setUseRelativeRedirects(false);
		try {
			context.setCreateUploadTargets(true);
		} catch (NoSuchMethodError ex) {
			// Tomcat is < 8.5.39. Continue.
		}
		//配置基础依赖包
		configureTldSkipPatterns(context);
		//初始化加载器
		WebappLoader loader = new WebappLoader(context.getParentClassLoader());
		//注册嵌入器类加载器
		loader.setLoaderClass(TomcatEmbeddedWebappClassLoader.class.getName());
		//开启委托配置
		loader.setDelegate(true);
		//设置类加载器
		context.setLoader(loader);
		if (isRegisterDefaultServlet()) {
			addDefaultServlet(context);
		}
		//设置jsp
		if (shouldRegisterJspServlet()) {
			addJspServlet(context);
			addJasperInitializer(context);
		}
		addTestServlet(context);
		//添加静态资源配置
		context.addLifecycleListener(new StaticResourceConfigurer(context));
		//合并ServletContext与参数配置
		ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
		//添加子StandardContext
		host.addChild(context);
		//配置内容
		configureContext(context, initializersToUse);
		//设置内容进程
		postProcessContext(context);
	}

	/**
	 * Override Tomcat's default locale mappings to align with other servers. See
	 * {@code org.apache.catalina.util.CharsetMapperDefault.properties}.
	 *
	 * @param context the context to reset
	 */
	private void resetDefaultLocaleMapping(TomcatEmbeddedContext context) {
		context.addLocaleEncodingMappingParameter(Locale.ENGLISH.toString(), DEFAULT_CHARSET.displayName());
		context.addLocaleEncodingMappingParameter(Locale.FRENCH.toString(), DEFAULT_CHARSET.displayName());
	}

	/**
	 * 添加区域
	 * @param context
	 */
	private void addLocaleMappings(TomcatEmbeddedContext context) {
		getLocaleCharsetMappings().forEach(
				(locale, charset) -> context.addLocaleEncodingMappingParameter(locale.toString(), charset.toString()));
	}

	/**
	 * 配置基础依赖包
	 * @param context
	 */
	private void configureTldSkipPatterns(TomcatEmbeddedContext context) {
		StandardJarScanFilter filter = new StandardJarScanFilter();
		filter.setTldSkip(StringUtils.collectionToCommaDelimitedString(this.tldSkipPatterns));
//		context.getJarScanner().setJarScanFilter(filter);
	}

	/**
	 * 添加默认Servlet
	 * @param context
	 */
	private void addDefaultServlet(Context context) {
		Wrapper defaultServlet = context.createWrapper();
		defaultServlet.setName("default");
		defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
		defaultServlet.addInitParameter("debug", "0");
		defaultServlet.addInitParameter("listings", "false");
		defaultServlet.setLoadOnStartup(1);
		// Otherwise the default location of a Spring DispatcherServlet cannot be set
		defaultServlet.setOverridable(false);
		context.addChild(defaultServlet);
		context.addServletMappingDecoded("/", "default");
	}

	/**
	 * 添加jsp Servlet支持
	 * @param context
	 */
	private void addJspServlet(Context context) {
		//创建连接器包装
		Wrapper jspServlet = context.createWrapper();
		//设置连接器名称
		jspServlet.setName("jsp");
		//设置jsp Servlet类型
		jspServlet.setServletClass(getJsp().getClassName());
		//设置初始化参数
		jspServlet.addInitParameter("fork", "false");
		//将jsp初始化参数添加入jspServlet中的初始化参数
		getJsp().getInitParameters().forEach(jspServlet::addInitParameter);
		//设置加载安装位置
		jspServlet.setLoadOnStartup(3);
		//将包装的连接器添加入内容中
		context.addChild(jspServlet);
		//添加解析后缀
		context.addServletMappingDecoded("*.jsp", "jsp");
		context.addServletMappingDecoded("*.jspx", "jsp");
	}
	private void addTestServlet(Context context) {
		//创建连接器包装
		Wrapper jspServlet = context.createWrapper();
		//设置连接器名称
		jspServlet.setName("test");
		//设置jsp Servlet类型
		jspServlet.setServletClass(HttpServletTest.class.getName());
		//设置初始化参数
		jspServlet.addInitParameter("fork", "false");
		//将jsp初始化参数添加入jspServlet中的初始化参数
//		getJsp().getInitParameters().forEach(jspServlet::addInitParameter);
		//设置加载安装位置
		jspServlet.setLoadOnStartup(3);
		//将包装的连接器添加入内容中
		context.addChild(jspServlet);
		//添加解析后缀
		context.addServletMappingDecoded("/", "test");
	}
	/**
	 * 添加jsp支持
	 * @param context
	 */
	private void addJasperInitializer(TomcatEmbeddedContext context) {
		try {
			org.apache.jasper.servlet.JasperInitializer jasperInitializer = new org.apache.jasper.servlet.JasperInitializer();
//			ServletContainerInitializer initializer = (ServletContainerInitializer) this.module.forName("org.apache.jasper.servlet.JasperInitializer").newInstance();
			context.addServletContainerInitializer(jasperInitializer, null);
//			org.apache.jasper.servlet.JspServlet jspServlet = new org.apache.jasper.servlet.JspServlet();
//			context.addServletContainerInitializer(new org.apache.jasper.servlet.JasperInitializer(), null);
		} catch (Exception ex) {
			// Probably not Tomcat 8
		}
	}

	/**
	 * 自定义连接器
	 * Needs to be protected so it can be used by subclasses
	 * @param connector 连接器
	 */
	protected void customizeConnector(Connector connector) {
		//判断最大端口值
		int port = Math.max(getPort(), 0);
		//设置端口
		connector.setPort(port);
		//设置连接器头
		if (StringUtils.hasText(this.getServerHeader())) {
			connector.setAttribute("server", this.getServerHeader());
		}
		//设置自定义连接器协议
		if (connector.getProtocolHandler() instanceof AbstractProtocol) {
			customizeProtocol((AbstractProtocol<?>) connector.getProtocolHandler());
		}
		//调用连接器协议头定义
		invokeProtocolHandlerCustomizers(connector.getProtocolHandler());
		//设置连接器地址编码
		if (getUriEncoding() != null) {
			connector.setURIEncoding(getUriEncoding().name());
		}
		// Don't bind to the socket prematurely if ApplicationContext is slow to start
		connector.setProperty("bindOnInit", "false");
//		connector.setProperty("bindOnInit", "true");
		//设置ssl
		if (getSsl() != null && getSsl().isEnabled()) {
			customizeSsl(connector);
		}
		//设置压缩自定
		new CompressionConnectorCustomizer(getCompression()).customize(connector);
		//设置连接器定义
		for (TomcatConnectorCustomizer customizer : this.tomcatConnectorCustomizers) {
			customizer.customize(connector);
		}
	}

	/**
	 * 自定义连接器协议
	 * @param protocol 连接协议
	 */
	private void customizeProtocol(AbstractProtocol<?> protocol) {
		if (getAddress() != null) {
			protocol.setAddress(getAddress());
		}
	}

	/**
	 * 连接器协议头定义
	 * @param protocolHandler
	 */
//	@SuppressWarnings("unchecked")
	private void invokeProtocolHandlerCustomizers(ProtocolHandler protocolHandler) {
		for (TomcatProtocolHandlerCustomizer customizer : this.tomcatProtocolHandlerCustomizers) {
			customizer.customize(protocolHandler);
		}
	}

	/**
	 * 自定义连接器的ssl设置
	 * @param connector
	 */
	private void customizeSsl(Connector connector) {
		//创建定义ssl并定义
		new SslConnectorCustomizer((ClassLoader) this.module.getClassLoader(), getSsl(), getSslStoreProvider()).customize(connector);
		//设置http2的协议更新
		if (getHttp2() != null && getHttp2().isEnabled()) {
			connector.addUpgradeProtocol(new Http2Protocol());
		}
	}

	/**
	 * Configure the Tomcat {@link Context}.
	 *
	 * @param context      the Tomcat context
	 * @param initializers initializers to apply
	 */
	protected void configureContext(Context context, ServletContextInitializer[] initializers) {
		//初始化启动类
		TomcatStarter starter = new TomcatStarter(initializers);
		//判断内容是否为tomcat嵌入器内容
		if (context instanceof TomcatEmbeddedContext) {
			//设置启动器
			TomcatEmbeddedContext embeddedContext = (TomcatEmbeddedContext) context;
			embeddedContext.setStarter(starter);
			embeddedContext.setFailCtxIfServletStartFails(true);
		}
		//初始化Servlet容器启动器与Servlet类列表
		context.addServletContainerInitializer(starter, NO_CLASSES);
		//添加生命周期事件监听列表
		for (LifecycleListener lifecycleListener : this.contextLifecycleListeners) {
			context.addLifecycleListener(lifecycleListener);
		}
		//添加内容管道列表
		for (Valve valve : this.contextValves) {
			context.getPipeline().addValve(valve);
		}
		//添加错误处理列表
		for (ErrorPage errorPage : getErrorPages()) {
			org.apache.tomcat.util.descriptor.web.ErrorPage tomcatErrorPage = new org.apache.tomcat.util.descriptor.web.ErrorPage();
			tomcatErrorPage.setLocation(errorPage.getPath());
			tomcatErrorPage.setErrorCode(errorPage.getStatusCode());
			tomcatErrorPage.setExceptionType(errorPage.getExceptionName());
			context.addErrorPage(tomcatErrorPage);
		}
		//添加miniType类型列表
		for (MimeMappings.Mapping mapping : getMimeMappings()) {
			context.addMimeMapping(mapping.getExtension(), mapping.getMimeType());
		}
		//设置会话
		configureSession(context);
		//禁用引用清楚定义内容
		new DisableReferenceClearingContextCustomizer().customize(context);
		//遍历内容定义
		for (TomcatContextCustomizer customizer : this.tomcatContextCustomizers) {
			customizer.customize(context);
		}
	}

	/**
	 * 配置内容会话
	 * @param context
	 */
	private void configureSession(Context context) {
		//获取会话超时值
		long sessionTimeout = getSessionTimeoutInMinutes();
		//设置内容会话超时值
		context.setSessionTimeout((int) sessionTimeout);
		//获取Cookie是否存在
		Boolean httpOnly = getSession().getCookie().getHttpOnly();
		//存在Cookie
		if (httpOnly != null) {
			//设置用户使用Cookie
			context.setUseHttpOnly(httpOnly);
		}
		//判断是否需要会话
		if (getSession().isPersistent()) {
			Manager manager = context.getManager();
			if (manager == null) {
				manager = new StandardManager();
				context.setManager(manager);
			}
			//配置会话
			configurePersistSession(manager);
		} else {
			//无需会话，设置禁用会话监听处理
			context.addLifecycleListener(new DisablePersistSessionListener());
		}
	}

	/**
	 * 配置会话
	 * @param manager
	 */
	private void configurePersistSession(Manager manager) {
		Assert.state(manager instanceof StandardManager,
				() -> "Unable to persist HTTP session state using manager type " + manager.getClass().getName());
		//获取验证会话存储目录，无效时创建它
		File dir = getValidSessionStoreDir();
		//创建会话文件
		File file = new File(dir, "SESSIONS.ser");
		//设置会话目录
		((StandardManager) manager).setPathname(file.getAbsolutePath());
	}

	private long getSessionTimeoutInMinutes() {
		Duration sessionTimeout = getSession().getTimeout();
		if (isZeroOrLess(sessionTimeout)) {
			return 0;
		}
		return Math.max(sessionTimeout.toMinutes(), 1);
	}

	private boolean isZeroOrLess(Duration sessionTimeout) {
		return sessionTimeout == null || sessionTimeout.isNegative() || sessionTimeout.isZero();
	}

	/**
	 * Post process the Tomcat {@link Context} before it's used with the Tomcat Server.
	 * Subclasses can override this method to apply additional processing to the
	 * {@link Context}.
	 *
	 * @param context the Tomcat {@link Context}
	 */
	protected void postProcessContext(Context context) {
	}

	/**
	 * Factory method called to create the {@link TomcatWebServer}. Subclasses can
	 * override this method to return a different {@link TomcatWebServer} or apply
	 * additional processing to the Tomcat server.
	 *
	 * @param tomcat the Tomcat server.
	 * @return a new {@link TomcatWebServer} instance
	 */
	protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
		return new TomcatWebServer((ClassLoader) this.module.getClassLoader(), tomcat, getPort() >= 0);
	}

	@Override
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	/**
	 * Returns a mutable set of the patterns that match jars to ignore for TLD scanning.
	 *
	 * @return the list of jars to ignore for TLD scanning
	 */
	public Set<String> getTldSkipPatterns() {
		return this.tldSkipPatterns;
	}

	/**
	 * Set the patterns that match jars to ignore for TLD scanning. See Tomcat's
	 * catalina.properties for typical values. Defaults to a list drawn from that source.
	 *
	 * @param patterns the jar patterns to skip when scanning for TLDs etc
	 */
	public void setTldSkipPatterns(Collection<String> patterns) {
		Assert.notNull(patterns, "Patterns must not be null");
		this.tldSkipPatterns = new LinkedHashSet<>(patterns);
	}

	/**
	 * Add patterns that match jars to ignore for TLD scanning. See Tomcat's
	 * catalina.properties for typical values.
	 *
	 * @param patterns the additional jar patterns to skip when scanning for TLDs etc
	 */
	public void addTldSkipPatterns(String... patterns) {
		Assert.notNull(patterns, "Patterns must not be null");
		this.tldSkipPatterns.addAll(Arrays.asList(patterns));
	}

	/**
	 * The Tomcat protocol to use when create the {@link Connector}.
	 *
	 * @param protocol the protocol
	 * @see Connector#Connector(String)
	 */
	public void setProtocol(String protocol) {
		Assert.hasLength(protocol, "Protocol must not be empty");
		this.protocol = protocol;
	}

	/**
	 * Set {@link Valve}s that should be applied to the Tomcat {@link Engine}. Calling
	 * this method will replace any existing valves.
	 *
	 * @param engineValves the valves to set
	 */
	public void setEngineValves(Collection<? extends Valve> engineValves) {
		Assert.notNull(engineValves, "Valves must not be null");
		this.engineValves = new ArrayList<>(engineValves);
	}

	/**
	 * Returns a mutable collection of the {@link Valve}s that will be applied to the
	 * Tomcat {@link Engine}.
	 *
	 * @return the engine valves that will be applied
	 */
	public Collection<Valve> getEngineValves() {
		return this.engineValves;
	}

	@Override
	public void addEngineValves(Valve... engineValves) {
		Assert.notNull(engineValves, "Valves must not be null");
		this.engineValves.addAll(Arrays.asList(engineValves));
	}

	/**
	 * Set {@link Valve}s that should be applied to the Tomcat {@link Context}. Calling
	 * this method will replace any existing valves.
	 *
	 * @param contextValves the valves to set
	 */
	public void setContextValves(Collection<? extends Valve> contextValves) {
		Assert.notNull(contextValves, "Valves must not be null");
		this.contextValves = new ArrayList<>(contextValves);
	}

	/**
	 * Returns a mutable collection of the {@link Valve}s that will be applied to the
	 * Tomcat {@link Context}.
	 *
	 * @return the context valves that will be applied
	 * @see #getEngineValves()
	 */
	public Collection<Valve> getContextValves() {
		return this.contextValves;
	}

	/**
	 * Add {@link Valve}s that should be applied to the Tomcat {@link Context}.
	 *
	 * @param contextValves the valves to add
	 */
	public void addContextValves(Valve... contextValves) {
		Assert.notNull(contextValves, "Valves must not be null");
		this.contextValves.addAll(Arrays.asList(contextValves));
	}

	/**
	 * Set {@link LifecycleListener}s that should be applied to the Tomcat
	 * {@link Context}. Calling this method will replace any existing listeners.
	 *
	 * @param contextLifecycleListeners the listeners to set
	 */
	public void setContextLifecycleListeners(Collection<? extends LifecycleListener> contextLifecycleListeners) {
		Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
		this.contextLifecycleListeners = new ArrayList<>(contextLifecycleListeners);
	}

	/**
	 * Returns a mutable collection of the {@link LifecycleListener}s that will be applied
	 * to the Tomcat {@link Context}.
	 *
	 * @return the context lifecycle listeners that will be applied
	 */
	public Collection<LifecycleListener> getContextLifecycleListeners() {
		return this.contextLifecycleListeners;
	}

	/**
	 * Add {@link LifecycleListener}s that should be added to the Tomcat {@link Context}.
	 *
	 * @param contextLifecycleListeners the listeners to add
	 */
	public void addContextLifecycleListeners(LifecycleListener... contextLifecycleListeners) {
		Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
		this.contextLifecycleListeners.addAll(Arrays.asList(contextLifecycleListeners));
	}

	/**
	 * Set {@link TomcatContextCustomizer}s that should be applied to the Tomcat
	 * {@link Context}. Calling this method will replace any existing customizers.
	 *
	 * @param tomcatContextCustomizers the customizers to set
	 */
	public void setTomcatContextCustomizers(Collection<? extends TomcatContextCustomizer> tomcatContextCustomizers) {
		Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
		this.tomcatContextCustomizers = new LinkedHashSet<>(tomcatContextCustomizers);
	}

	/**
	 * Returns a mutable collection of the {@link TomcatContextCustomizer}s that will be
	 * applied to the Tomcat {@link Context}.
	 *
	 * @return the listeners that will be applied
	 */
	public Collection<TomcatContextCustomizer> getTomcatContextCustomizers() {
		return this.tomcatContextCustomizers;
	}

	@Override
	public void addContextCustomizers(TomcatContextCustomizer... tomcatContextCustomizers) {
		Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
		this.tomcatContextCustomizers.addAll(Arrays.asList(tomcatContextCustomizers));
	}

	/**
	 * Set {@link TomcatConnectorCustomizer}s that should be applied to the Tomcat
	 * {@link Connector}. Calling this method will replace any existing customizers.
	 *
	 * @param tomcatConnectorCustomizers the customizers to set
	 */
	public void setTomcatConnectorCustomizers(
			Collection<? extends TomcatConnectorCustomizer> tomcatConnectorCustomizers) {
		Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
		this.tomcatConnectorCustomizers = new LinkedHashSet<>(tomcatConnectorCustomizers);
	}

	@Override
	public void addConnectorCustomizers(TomcatConnectorCustomizer... tomcatConnectorCustomizers) {
		Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
		this.tomcatConnectorCustomizers.addAll(Arrays.asList(tomcatConnectorCustomizers));
	}

	/**
	 * Returns a mutable collection of the {@link TomcatConnectorCustomizer}s that will be
	 * applied to the Tomcat {@link Connector}.
	 *
	 * @return the customizers that will be applied
	 */
	public Collection<TomcatConnectorCustomizer> getTomcatConnectorCustomizers() {
		return this.tomcatConnectorCustomizers;
	}

	/**
	 * Set {@link TomcatProtocolHandlerCustomizer}s that should be applied to the Tomcat
	 * {@link Connector}. Calling this method will replace any existing customizers.
	 *
	 * @param tomcatProtocolHandlerCustomizer the customizers to set
	 * @since 2.2.0
	 */
	public void setTomcatProtocolHandlerCustomizers(
			Collection<? extends TomcatProtocolHandlerCustomizer<?>> tomcatProtocolHandlerCustomizer) {
		Assert.notNull(tomcatProtocolHandlerCustomizer, "TomcatProtocolHandlerCustomizers must not be null");
		this.tomcatProtocolHandlerCustomizers = new LinkedHashSet<>(tomcatProtocolHandlerCustomizer);
	}

	/**
	 * Add {@link TomcatProtocolHandlerCustomizer}s that should be added to the Tomcat
	 * {@link Connector}.
	 *
	 * @param tomcatProtocolHandlerCustomizers the customizers to add
	 * @since 2.2.0
	 */
	@Override
	public void addProtocolHandlerCustomizers(TomcatProtocolHandlerCustomizer<?>... tomcatProtocolHandlerCustomizers) {
		Assert.notNull(tomcatProtocolHandlerCustomizers, "TomcatProtocolHandlerCustomizers must not be null");
		this.tomcatProtocolHandlerCustomizers.addAll(Arrays.asList(tomcatProtocolHandlerCustomizers));
	}

	/**
	 * Returns a mutable collection of the {@link TomcatProtocolHandlerCustomizer}s that
	 * will be applied to the Tomcat {@link Connector}.
	 *
	 * @return the customizers that will be applied
	 * @since 2.2.0
	 */
	public Collection<TomcatProtocolHandlerCustomizer<?>> getTomcatProtocolHandlerCustomizers() {
		return this.tomcatProtocolHandlerCustomizers;
	}

	/**
	 * Add {@link Connector}s in addition to the default connector, e.g. for SSL or AJP
	 *
	 * @param connectors the connectors to add
	 */
	public void addAdditionalTomcatConnectors(Connector... connectors) {
		Assert.notNull(connectors, "Connectors must not be null");
		this.additionalTomcatConnectors.addAll(Arrays.asList(connectors));
	}

	/**
	 * Returns a mutable collection of the {@link Connector}s that will be added to the
	 * Tomcat.
	 *
	 * @return the additionalTomcatConnectors
	 */
	public List<Connector> getAdditionalTomcatConnectors() {
		return this.additionalTomcatConnectors;
	}

	/**
	 * 设置连接器uri编码
	 * @param uriEncoding the uri encoding to set
	 */
	@Override
	public void setUriEncoding(Charset uriEncoding) {
		this.uriEncoding = uriEncoding;
	}

	/**
	 * Returns the character encoding to use for URL decoding.
	 *
	 * @return the URI encoding
	 */
	public Charset getUriEncoding() {
		return this.uriEncoding;
	}

	@Override
	public void setBackgroundProcessorDelay(int delay) {
		this.backgroundProcessorDelay = delay;
	}

//	/**
//	 * Set whether the factory should disable Tomcat's MBean registry prior to creating
//	 * the server.
//	 *
//	 * @param disableMBeanRegistry whether to disable the MBean registry
//	 * @since 2.2.0
//	 */
//	public void setDisableMBeanRegistry(boolean disableMBeanRegistry) {
//		this.disableMBeanRegistry = disableMBeanRegistry;
//	}

	/**
	 * {@link LifecycleListener} to disable persistence in the {@link StandardManager}. A
	 * {@link LifecycleListener} is used so not to interfere with Tomcat's default manager
	 * creation logic.
	 */
	private static class DisablePersistSessionListener implements LifecycleListener {
		/**
		 * 重写禁用会话处理
		 * @param event
		 */
		@Override
		public void lifecycleEvent(LifecycleEvent event) {
			if (event.getType().equals(Lifecycle.START_EVENT)) {
				Context context = (Context) event.getLifecycle();
				Manager manager = context.getManager();
				if (manager instanceof StandardManager) {
					((StandardManager) manager).setPathname(null);
				}
			}
		}

	}

	/**
	 * 静态资源配置
	 */
	private final class StaticResourceConfigurer implements LifecycleListener {

		private final Context context;

		private StaticResourceConfigurer(Context context) {
			this.context = context;
		}

		@Override
		public void lifecycleEvent(LifecycleEvent event) {
			if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
				addResourceJars(getUrlsOfJarsWithMetaInfResources());
			}
		}

		private void addResourceJars(List<URL> resourceJarUrls) {
			for (URL url : resourceJarUrls) {
				String path = url.getPath();
				if (path.endsWith(".jar") || path.endsWith(".jar!/")) {
					String jar = url.toString();
					if (!jar.startsWith("jar:")) {
						// A jar file in the file system. Convert to Jar URL.
						jar = "jar:" + jar + "!/";
					}
					addResourceSet(jar);
				} else {
					addResourceSet(url.toString());
				}
			}
		}

		private void addResourceSet(String resource) {
			try {
				if (isInsideNestedJar(resource)) {
					// It's a nested jar but we now don't want the suffix because Tomcat
					// is going to try and locate it as a root URL (not the resource
					// inside it)
					resource = resource.substring(0, resource.length() - 2);
				}
				URL url = new URL(resource);
				String path = "/META-INF/resources";
				this.context.getResources().createWebResourceSet(ResourceSetType.RESOURCE_JAR, "/", url, path);
			} catch (Exception ex) {
				// Ignore (probably not a directory)
			}
		}

		private boolean isInsideNestedJar(String dir) {
			return dir.indexOf("!/") < dir.lastIndexOf("!/");
		}

	}

	/**
	 * 加载隐式根资源
	 */
	private static final class LoaderHidingResourceRoot extends StandardRoot {
		/**
		 * 初始化加载隐式根资源
		 * @param context 嵌入式内容
		 */
		private LoaderHidingResourceRoot(TomcatEmbeddedContext context) {
			super(context);
		}

		@Override
		protected WebResourceSet createMainResourceSet() {
			return new LoaderHidingWebResourceSet(super.createMainResourceSet());
		}

	}

	private static final class LoaderHidingWebResourceSet extends AbstractResourceSet {

		private final WebResourceSet delegate;

		private final Method initInternal;

		private LoaderHidingWebResourceSet(WebResourceSet delegate) {
			this.delegate = delegate;
			try {
				this.initInternal = LifecycleBase.class.getDeclaredMethod("initInternal");
				this.initInternal.setAccessible(true);
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		}

		@Override
		public WebResource getResource(String path) {
			if (path.startsWith("/ghost/framework/tomcat/web/module")) {
				return new EmptyResource(getRoot(), path);
			}
			return this.delegate.getResource(path);
		}

		@Override
		public String[] list(String path) {
			return this.delegate.list(path);
		}

		@Override
		public Set<String> listWebAppPaths(String path) {
			return this.delegate.listWebAppPaths(path).stream()
					.filter((webAppPath) -> !webAppPath.startsWith("/ghost/framework/tomcat/web/module"))
					.collect(Collectors.toSet());
		}

		@Override
		public boolean mkdir(String path) {
			return this.delegate.mkdir(path);
		}

		@Override
		public boolean write(String path, InputStream is, boolean overwrite) {
			return this.delegate.write(path, is, overwrite);
		}

		@Override
		public URL getBaseUrl() {
			return this.delegate.getBaseUrl();
		}

		@Override
		public void setReadOnly(boolean readOnly) {
			this.delegate.setReadOnly(readOnly);
		}

		@Override
		public boolean isReadOnly() {
			return this.delegate.isReadOnly();
		}

		@Override
		public void gc() {
			this.delegate.gc();
		}

		@Override
		protected void initInternal() throws LifecycleException {
			if (this.delegate instanceof LifecycleBase) {
				try {
					ReflectionUtils.invokeMethod(this.initInternal, this.delegate);
				} catch (Exception ex) {
					throw new LifecycleException(ex);
				}
			}
		}
	}
}