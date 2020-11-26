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
package ghost.framework.undertow.web.module;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.TempDirectory;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.undertow.web.context.ConfigurableUndertowWebServerFactory;
import ghost.framework.undertow.web.context.SslBuilderCustomizer;
import ghost.framework.undertow.web.context.UndertowBuilderCustomizer;
import ghost.framework.util.Assert;
import ghost.framework.web.context.server.IConfigurableServletWebServerFactoryContainer;
import ghost.framework.web.context.server.MimeMappings;
import ghost.framework.web.context.server.WebServer;
import ghost.framework.web.context.servlet.context.IServletContextContainer;
import ghost.framework.web.context.servlet.context.ServletContextInitializer;
import ghost.framework.web.context.servlet.server.AbstractServletWebServerFactory;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.UndertowOptions;
import io.undertow.jsp.HackInstanceManager;
import io.undertow.jsp.JspServletBuilder;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.server.handlers.accesslog.DefaultAccessLogReceiver;
import io.undertow.server.handlers.resource.*;
import io.undertow.server.session.SessionManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.servlet.core.DeploymentImpl;
import io.undertow.servlet.handlers.DefaultServlet;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.apache.jasper.deploy.JspPropertyGroup;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import javax.servlet.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
/**
 * {@link ServletWebServerFactory} that can be used to create
 * {@link UndertowServletWebServer}s.
 * <p>
 * Unless explicitly configured otherwise, the factory will create servers that listen for
 * HTTP requests on port 8080.
 *
 * @author Ivan Sopov
 * @author Andy Wilkinson
 * @author Marcos Barbero
 * @author Eddú Meléndez
 * @since 2.0.0
 * @see UndertowServletWebServer
 */
@Component
public class UndertowServletWebServerFactory extends AbstractServletWebServerFactory
		implements ConfigurableUndertowWebServerFactory {
	@Override
	public String toString() {
		return "UndertowServletWebServerFactory{" +
				"name='" + this.name + '\'' +
				"port='" + this.getPort() + '\'' +
				"contextPath='" + this.getContextPath() + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UndertowServletWebServerFactory that = (UndertowServletWebServerFactory) o;
		return accessLogEnabled == that.accessLogEnabled &&
				accessLogRotate == that.accessLogRotate &&
				useForwardHeaders == that.useForwardHeaders &&
				eagerInitFilters == that.eagerInitFilters &&
				name.equals(that.name) &&
				module.equals(that.module) &&
				builderCustomizers.equals(that.builderCustomizers) &&
				deploymentInfoCustomizers.equals(that.deploymentInfoCustomizers) &&
				bufferSize.equals(that.bufferSize) &&
				ioThreads.equals(that.ioThreads) &&
				workerThreads.equals(that.workerThreads) &&
				directBuffers.equals(that.directBuffers) &&
				baseDirectory.equals(that.baseDirectory) &&
				accessLogDirectory.equals(that.accessLogDirectory) &&
				accessLogPattern.equals(that.accessLogPattern) &&
				accessLogPrefix.equals(that.accessLogPrefix) &&
				accessLogSuffix.equals(that.accessLogSuffix);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, module, builderCustomizers, deploymentInfoCustomizers, bufferSize, ioThreads, workerThreads, directBuffers, baseDirectory, accessLogDirectory, accessLogPattern, accessLogPrefix, accessLogSuffix, accessLogEnabled, accessLogRotate, useForwardHeaders, eagerInitFilters);
	}

	/**
	 * 重写配置名称
	 *
	 * @return
	 */
	@Override
	public String getName() {
		return name;
	}

	private String name = "Undertow";

	/**
	 * 获取此模块接口
	 *
	 * @return
	 */
	@Override
	public IModule getModule() {
		return module;
	}

	/**
	 * 注入模块接口
	 */
	@Autowired
	private IModule module;
	/**
	 * web模块接口
	 */
	private IModule moduleWeb;
	private static final Pattern ENCODED_SLASH = Pattern.compile("%2F", Pattern.LITERAL);

	private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();

	private Set<UndertowBuilderCustomizer> builderCustomizers = new LinkedHashSet<>();

	private Set<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers = new LinkedHashSet<>();

	private Integer bufferSize;

	private Integer ioThreads;

	private Integer workerThreads;

	private Boolean directBuffers;
	/**
	 * 注入模块临时模块
	 */
	@Module
	@TempDirectory(
			increasePath = "Undertow",//注释在目录增加的路径
			suffixMethodName = "getPort",//注释在目录增加的后缀路径函数名称
			deleteAfterCreation = false,
			deleteOnExit = false
	)
	@Order
	private File baseDirectory;

	private File accessLogDirectory;

	private String accessLogPattern;

	private String accessLogPrefix;

	private String accessLogSuffix;
	/**
	 * 是否启用日志设置
	 */
	private boolean accessLogEnabled = false;

	private boolean accessLogRotate = true;

	private boolean useForwardHeaders;
	/**
	 * 是否初始化过滤器
	 */
	private boolean eagerInitFilters = true;

	/**
	 * Create a new {@link UndertowServletWebServerFactory} instance.
	 *
	 * @param moduleWeb web模块
	 */
	@Constructor
	public UndertowServletWebServerFactory(@Module(value = UndertowWebModuleConstant.ModuleWebName) @Autowired IModule moduleWeb) {
		this.moduleWeb = moduleWeb;
		getJsp().setRegistered(true);
		//将本身服务工厂添加入web模块配置服务工厂容器中
		this.moduleWeb.getBean(IConfigurableServletWebServerFactoryContainer.class).add(this);
	}

	/**
	 * Create a new {@link UndertowServletWebServerFactory} that listens for requests
	 * using the specified port.
	 *
	 * @param port the port to listen on
	 */
	public UndertowServletWebServerFactory(int port) {
		super(port);
		getJsp().setRegistered(false);
	}

	/**
	 * @param className 类型名称
	 * @return
	 */
	@Override
	protected boolean isPresent(String className) {
		return this.module.isPresent(className);
	}

	/**
	 * Create a new {@link UndertowServletWebServerFactory} with the specified context
	 * path and port.
	 *
	 * @param contextPath the root context path
	 * @param port        the port to listen on
	 */
	public UndertowServletWebServerFactory(String contextPath, int port) {
		super(contextPath, port);
		getJsp().setRegistered(false);
	}

	/**
	 * Set {@link UndertowBuilderCustomizer}s that should be applied to the Undertow
	 * {@link Builder}. Calling this method will replace any existing customizers.
	 *
	 * @param customizers the customizers to set
	 */
	public void setBuilderCustomizers(Collection<? extends UndertowBuilderCustomizer> customizers) {
		Assert.notNull(customizers, "Customizers must not be null");
		this.builderCustomizers = new LinkedHashSet<>(customizers);
	}

	/**
	 * Returns a mutable collection of the {@link UndertowBuilderCustomizer}s that will be
	 * applied to the Undertow {@link Builder}.
	 *
	 * @return the customizers that will be applied
	 */
	public Collection<UndertowBuilderCustomizer> getBuilderCustomizers() {
		return this.builderCustomizers;
	}

	@Override
	public void addBuilderCustomizers(UndertowBuilderCustomizer... customizers) {
		Assert.notNull(customizers, "Customizers must not be null");
		this.builderCustomizers.addAll(Arrays.asList(customizers));
	}

	/**
	 * Set {@link UndertowDeploymentInfoCustomizer}s that should be applied to the
	 * Undertow {@link DeploymentInfo}. Calling this method will replace any existing
	 * customizers.
	 *
	 * @param customizers the customizers to set
	 */
	public void setDeploymentInfoCustomizers(Collection<? extends UndertowDeploymentInfoCustomizer> customizers) {
		Assert.notNull(customizers, "Customizers must not be null");
		this.deploymentInfoCustomizers = new LinkedHashSet<>(customizers);
	}

	/**
	 * Returns a mutable collection of the {@link UndertowDeploymentInfoCustomizer}s that
	 * will be applied to the Undertow {@link DeploymentInfo}.
	 *
	 * @return the customizers that will be applied
	 */
	public Collection<UndertowDeploymentInfoCustomizer> getDeploymentInfoCustomizers() {
		return this.deploymentInfoCustomizers;
	}

	/**
	 * Add {@link UndertowDeploymentInfoCustomizer}s that should be used to customize the
	 * Undertow {@link DeploymentInfo}.
	 *
	 * @param customizers the customizers to add
	 */
	public void addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer... customizers) {
		Assert.notNull(customizers, "UndertowDeploymentInfoCustomizers must not be null");
		this.deploymentInfoCustomizers.addAll(Arrays.asList(customizers));
	}

	/**
	 * 初始化 {@link ServletContextInitializer} 内容
	 *
	 * @return
	 */
	@Override
	public WebServer getWebServer() {
		return this.getWebServer(Arrays.asList(new ServletContextInitializer[]{this.moduleWeb.getBean(IServletContextContainer.class)}));
	}

	/**
	 * 绑定执行函数
	 *
	 * @param initializers {@link ServletContextInitializer}s that should be applied as
	 *                     the server starts
	 * @return
	 */
	@Override
	public WebServer getWebServer(Collection<ServletContextInitializer> initializers) {
		//创建部署管理
		DeploymentManager manager = this.createDeploymentManager(initializers);
		//获取端口
		int port = this.getPort();
		//创建生成器
		Builder builder = this.createBuilder(port);
		//获取web服务
		return this.getUndertowWebServer(builder, manager, port);
	}

	/**
	 * 创建生成器
	 *
	 * @param port 指定端口
	 * @return
	 */
	private Builder createBuilder(int port) {
		Builder builder = Undertow.builder();
		if (this.bufferSize != null) {
			builder.setBufferSize(this.bufferSize);
		}
		if (this.ioThreads != null) {
			builder.setIoThreads(this.ioThreads);
		}
		if (this.workerThreads != null) {
			builder.setWorkerThreads(this.workerThreads);
		}
		if (this.directBuffers != null) {
			builder.setDirectBuffers(this.directBuffers);
		}
		if (getSsl() != null && getSsl().isEnabled()) {
			customizeSsl(builder);
		} else {
			builder.addHttpListener(port, getListenAddress());
		}
		for (UndertowBuilderCustomizer customizer : this.builderCustomizers) {
			customizer.customize(builder);
		}
		return builder;
	}

	/**
	 * 定制ssl生成器
	 *
	 * @param builder
	 */
	private void customizeSsl(Builder builder) {
		new SslBuilderCustomizer(this.getServletClassLoader(), getPort(), getAddress(), getSsl(), getSslStoreProvider()).customize(builder);
		if (getHttp2() != null) {
			builder.setServerOption(UndertowOptions.ENABLE_HTTP2, getHttp2().isEnabled());
		}
	}

	/**
	 * 获取ip地址
	 *
	 * @return
	 */
	private String getListenAddress() {
		if (getAddress() == null) {
			return "0.0.0.0";
		}
		return getAddress().getHostAddress();
	}

	/**
	 * 创建部署管理
	 *
	 * @param initializers ServletContext初始化列表
	 * @return 返回部署管理对象
	 */
	private DeploymentManager createDeploymentManager(Collection<ServletContextInitializer> initializers) {
		//创建部署信息
		DeploymentInfo deployment = Servlets.deployment();
		//初始化ServletContext容器
		registerServletContainerInitializerToDriveServletContextInitializers(deployment, initializers);
		//设置Servlet类加载器
		deployment.setClassLoader(getServletClassLoader());
		//设置内容路径
		deployment.setContextPath(getContextPath());
		//设置显示名称
		deployment.setDisplayName(getDisplayName());
		//设置部署名称
		deployment.setDeploymentName(this.module.getName() + "[undertow]");
		//判断注册默认Servlet
		if (isRegisterDefaultServlet()) {
			ServletInfo info = Servlets.servlet("default", DefaultServlet.class);
			Arrays.asList("*.js", "*.css", "*.png", "*.jpg", "*.gif", "*.html", "*.htm", "*.txt", "*.pdf", "*.jpeg", "*.xml").forEach(info::addMapping);
			deployment.addServlet(info);

		}
		//jsp
		if (this.getJsp().getRegistered()) {
			deployment.addServlet(JspServletBuilder.createServlet("Default Jsp Servlet", "*.jsp"));
			HashMap<String, TagLibraryInfo> tagLibraryInfo = new HashMap<String, TagLibraryInfo>();
			JspServletBuilder.setupDeployment(deployment, new HashMap<String, JspPropertyGroup>(), tagLibraryInfo, new HackInstanceManager());
		}
		//配置错误
		configureErrorPages(deployment);
		//
		deployment.setServletStackTraces(ServletStackTraces.NONE);
		//设置资源管理
		deployment.setResourceManager(getDocumentRootResourceManager());
		//设置临时目录
		deployment.setTempDir(this.baseDirectory);
		//设置是否初始化过滤器
		deployment.setEagerFilterInit(this.eagerInitFilters);
		//配置miniType
		configureMimeMappings(deployment);
		//遍历自定义部署信息
		for (UndertowDeploymentInfoCustomizer customizer : this.deploymentInfoCustomizers) {
			customizer.customize(deployment);
		}
		//判断是否启用日志设置
		if (isAccessLogEnabled()) {
			//设置日志
			configureAccessLog(deployment);
		}
		//判断是否使用会话
		if (getSession().isPersistent()) {
			//获取会话目录
			File dir = getValidSessionStoreDir();
			//设置会话文件与权限
			deployment.setSessionPersistenceManager(new FileSessionPersistence(dir));
		}
		//添加区域信息
		addLocaleMappings(deployment);
		//创建部署管理
		DeploymentManager manager = Servlets.newContainer().addDeployment(deployment);
		//部署
		manager.deploy();
		//判断是否移除多余的映射
		if (manager.getDeployment() instanceof DeploymentImpl) {
			removeSuperfluousMimeMappings((DeploymentImpl) manager.getDeployment(), deployment);
		}
		//获取部署会话管理
		SessionManager sessionManager = manager.getDeployment().getSessionManager();
		//获取会话超时值
		Duration timeoutDuration = getSession().getTimeout();
		//计算会话超时单位值
		int sessionTimeout = (isZeroOrLess(timeoutDuration) ? -1 : (int) timeoutDuration.getSeconds());
		//设置会话超时
		sessionManager.setDefaultSessionTimeout(sessionTimeout);
		//返回部署管理
		return manager;
	}

	/**
	 * 判断小于零值
	 *
	 * @param timeoutDuration 超时单位
	 * @return
	 */
	private boolean isZeroOrLess(Duration timeoutDuration) {
		return timeoutDuration == null || timeoutDuration.isZero() || timeoutDuration.isNegative();
	}

	/**
	 * 设置日志
	 *
	 * @param deploymentInfo 部署信息
	 */
	private void configureAccessLog(DeploymentInfo deploymentInfo) {
		try {
			createAccessLogDirectoryIfNecessary();
			XnioWorker worker = createWorker();
			String prefix = (this.accessLogPrefix != null) ? this.accessLogPrefix : "access_log.";
			DefaultAccessLogReceiver accessLogReceiver = new DefaultAccessLogReceiver(worker, this.accessLogDirectory,
					prefix, this.accessLogSuffix, this.accessLogRotate);
			EventListener listener = new AccessLogShutdownListener(worker, accessLogReceiver);
			deploymentInfo.addListener(
					new ListenerInfo(AccessLogShutdownListener.class, new ImmediateInstanceFactory<>(listener)));
			deploymentInfo
					.addInitialHandlerChainWrapper((handler) -> createAccessLogHandler(handler, accessLogReceiver));
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to create AccessLogHandler", ex);
		}
	}

	/**
	 * @param handler
	 * @param accessLogReceiver
	 * @return
	 */
	private AccessLogHandler createAccessLogHandler(HttpHandler handler, AccessLogReceiver accessLogReceiver) {
		createAccessLogDirectoryIfNecessary();
		String formatString = (this.accessLogPattern != null) ? this.accessLogPattern : "common";
		return new AccessLogHandler(handler, accessLogReceiver, formatString, Undertow.class.getClassLoader());
	}

	/**
	 *
	 */
	private void createAccessLogDirectoryIfNecessary() {
		Assert.state(this.accessLogDirectory != null, "Access log directory is not set");
		if (!this.accessLogDirectory.isDirectory() && !this.accessLogDirectory.mkdirs()) {
			throw new IllegalStateException("Failed to create access log directory '" + this.accessLogDirectory + "'");
		}
	}

	/**
	 * 创建工作目录
	 *
	 * @return
	 * @throws IOException
	 */
	private XnioWorker createWorker() throws IOException {
		Xnio xnio = Xnio.getInstance(Undertow.class.getClassLoader());
		return xnio.createWorker(OptionMap.builder().set(Options.THREAD_DAEMON, true).getMap());
	}

	/**
	 * 添加区域
	 *
	 * @param deployment 部署信息
	 */
	private void addLocaleMappings(DeploymentInfo deployment) {
		getLocaleCharsetMappings().forEach(
				(locale, charset) -> deployment.addLocaleCharsetMapping(locale.toString(), charset.toString()));
	}

	/**
	 * 注册部署ServletContext列表
	 *
	 * @param deployment   部署文档信息
	 * @param initializers 部署ServletContext列表
	 */
	private void registerServletContainerInitializerToDriveServletContextInitializers(DeploymentInfo deployment, Collection<ServletContextInitializer> initializers) {
		//合并ServletContext列表
		Collection<ServletContextInitializer> mergedInitializers = mergeInitializers(initializers);
		//初始化ServletContext列表容器
		Initializer initializer = new Initializer(mergedInitializers);
		//初始化Servlet容器
		deployment.addServletContainerInitializer(new ServletContainerInitializerInfo(Initializer.class, new ImmediateInstanceFactory<ServletContainerInitializer>(initializer), NO_CLASSES));
	}

	/**
	 * 获取Servlet类加载器
	 *
	 * @return
	 */
	private ClassLoader getServletClassLoader() {
		//使用模块类加载器作为Servlet的类加载器
		return (ClassLoader) this.module.getClassLoader();
	}

	/**
	 * 获取资源管理
	 *
	 * @return
	 */
	private ResourceManager getDocumentRootResourceManager() {
		//设置根目录
		this.setDocumentRoot(this.baseDirectory);
		//获取根目录
		File root = getValidDocumentRoot();
		//获取内容目录
		File docBase = getCanonicalDocumentRoot();
		//获取静态资源文件与包列表
		List<URL> metaInfResourceUrls = getUrlsOfJarsWithMetaInfResources();
		//声明包列表
		List<URL> resourceJarUrls = new ArrayList<>();
		//生命资源管理列表
		List<ResourceManager> managers = new ArrayList<>();
		//初始化根资源管理
		ResourceManager rootManager = (docBase.isDirectory() ? new FileResourceManager(docBase, 0) : new JarResourceManager(docBase));
		if (root != null) {
			rootManager = new LoaderHidingResourceManager(rootManager);
		}
		//添加部署加载隐藏资源管理
		managers.add(rootManager);
		//遍历静态资源列表
		for (URL url : metaInfResourceUrls) {
			//判断协议
			if ("file".equals(url.getProtocol())) {
				try {
					File file = new File(url.toURI());
					if (file.isFile()) {
						resourceJarUrls.add(new URL("jar:" + url + "!/"));
					} else {
						managers.add(new FileResourceManager(new File(file, "META-INF/resources"), 0));
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			} else {
				resourceJarUrls.add(url);
			}
		}
		//添加初始化元数据资源管理
		managers.add(new MetaInfResourcesResourceManager(resourceJarUrls));
		//创建复合资源管理
		return new CompositeResourceManager(managers.toArray(new ResourceManager[0]));
	}

	/**
	 * 获取docbase模块
	 *
	 * @return
	 */
	private File getCanonicalDocumentRoot() {
		File file = new File(this.getDocumentRoot().getPath() + File.separator + "docbase");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 配置错误
	 *
	 * @param servletBuilder 部署信息
	 */
	private void configureErrorPages(DeploymentInfo servletBuilder) {
		//遍历错误处理列表
		for (ghost.framework.web.context.server.ErrorPage errorPage : getErrorPages()) {
			servletBuilder.addErrorPage(getUndertowErrorPage(errorPage));
		}
	}

	/**
	 * @param errorPage
	 * @return
	 */
	private io.undertow.servlet.api.ErrorPage getUndertowErrorPage(ghost.framework.web.context.server.ErrorPage errorPage) {
		if (errorPage.getStatus() != null) {
			return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage.getStatusCode());
		}
		if (errorPage.getException() != null) {
			return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage.getException());
		}
		return new io.undertow.servlet.api.ErrorPage(errorPage.getPath());
	}

	/**
	 * 配置miniType
	 *
	 * @param servletBuilder 部署信息
	 */
	private void configureMimeMappings(DeploymentInfo servletBuilder) {
		for (MimeMappings.Mapping mimeMapping : getMimeMappings()) {
			servletBuilder.addMimeMapping(new MimeMapping(mimeMapping.getExtension(), mimeMapping.getMimeType()));
		}
	}

	/**
	 * 删除多余的映射
	 *
	 * @param deployment
	 * @param deploymentInfo
	 */
	private void removeSuperfluousMimeMappings(DeploymentImpl deployment, DeploymentInfo deploymentInfo) {
		// DeploymentManagerImpl will always add MimeMappings.DEFAULT_MIME_MAPPINGS
		// but we only want ours
		Map<String, String> mappings = new HashMap<>();
		for (MimeMapping mapping : deploymentInfo.getMimeMappings()) {
			mappings.put(mapping.getExtension().toLowerCase(Locale.ENGLISH), mapping.getMimeType());
		}
		deployment.setMimeExtensionMappings(mappings);
	}

	/**
	 * Factory method called to create the {@link UndertowServletWebServer}. Subclasses
	 * can override this method to return a different {@link UndertowServletWebServer} or
	 * apply additional processing to the {@link Builder} and {@link DeploymentManager}
	 * used to bootstrap Undertow
	 *
	 * @param builder the builder
	 * @param manager the deployment manager
	 * @param port    the port that Undertow should listen on
	 * @return a new {@link UndertowServletWebServer} instance
	 */
	protected UndertowServletWebServer getUndertowWebServer(Builder builder, DeploymentManager manager, int port) {
		return new UndertowServletWebServer(this.module, builder, manager, getContextPath(), isUseForwardHeaders(), port >= 0, getCompression(), getServerHeader());
	}

	/**
	 * @param bufferSize buffer size
	 */
	@Override
	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @param ioThreads number of IO Threads
	 */
	@Override
	public void setIoThreads(Integer ioThreads) {
		this.ioThreads = ioThreads;
	}

	/**
	 * @param workerThreads number of Worker Threads
	 */
	@Override
	public void setWorkerThreads(Integer workerThreads) {
		this.workerThreads = workerThreads;
	}

	/**
	 * @param directBuffers
	 */
	@Override
	public void setUseDirectBuffers(Boolean directBuffers) {
		this.directBuffers = directBuffers;
	}

	/**
	 * @param accessLogDirectory access log directory
	 */
	@Override
	public void setAccessLogDirectory(File accessLogDirectory) {
		this.accessLogDirectory = accessLogDirectory;
	}

	/**
	 * @param accessLogPattern access log pattern
	 */
	@Override
	public void setAccessLogPattern(String accessLogPattern) {
		this.accessLogPattern = accessLogPattern;
	}

	/**
	 * @return
	 */
	public String getAccessLogPrefix() {
		return this.accessLogPrefix;
	}

	/**
	 * @param accessLogPrefix log prefix
	 */
	@Override
	public void setAccessLogPrefix(String accessLogPrefix) {
		this.accessLogPrefix = accessLogPrefix;
	}

	/**
	 * @param accessLogSuffix access log suffix
	 */
	@Override
	public void setAccessLogSuffix(String accessLogSuffix) {
		this.accessLogSuffix = accessLogSuffix;
	}

	/**
	 * @param accessLogEnabled whether access logs are enabled
	 */
	@Override
	public void setAccessLogEnabled(boolean accessLogEnabled) {
		this.accessLogEnabled = accessLogEnabled;
	}

	/**
	 * 获取是否启用日志设置
	 *
	 * @return
	 */
	public boolean isAccessLogEnabled() {
		return this.accessLogEnabled;
	}

	/**
	 * @param accessLogRotate whether access logs rotation is enabled
	 */
	@Override
	public void setAccessLogRotate(boolean accessLogRotate) {
		this.accessLogRotate = accessLogRotate;
	}

	/**
	 * @return
	 */
	protected final boolean isUseForwardHeaders() {
		return this.useForwardHeaders;
	}

	/**
	 * @param useForwardHeaders if x-forward headers should be used
	 */
	@Override
	public void setUseForwardHeaders(boolean useForwardHeaders) {
		this.useForwardHeaders = useForwardHeaders;
	}

	/**
	 * Return if filters should be initialized eagerly.
	 *
	 * @return {@code true} if filters are initialized eagerly, otherwise {@code false}.
	 * @since 2.0.0
	 */
	public boolean isEagerInitFilters() {
		return this.eagerInitFilters;
	}

	/**
	 * Set whether filters should be initialized eagerly.
	 *
	 * @param eagerInitFilters {@code true} if filters are initialized eagerly, otherwise
	 *                         {@code false}.
	 * @since 2.0.0
	 */
	public void setEagerInitFilters(boolean eagerInitFilters) {
		this.eagerInitFilters = eagerInitFilters;
	}

	/**
	 * {@link ResourceManager} that exposes resource in {@code META-INF/resources}
	 * directory of nested (in {@code BOOT-INF/lib} or {@code WEB-INF/lib}) jars.
	 */
	private static final class MetaInfResourcesResourceManager implements ResourceManager {

		private final List<URL> metaInfResourceJarUrls;

		private MetaInfResourcesResourceManager(List<URL> metaInfResourceJarUrls) {
			this.metaInfResourceJarUrls = metaInfResourceJarUrls;
		}

		@Override
		public void close() throws IOException {
		}

		@Override
		public Resource getResource(String path) {
			for (URL url : this.metaInfResourceJarUrls) {
				URLResource resource = getMetaInfResource(url, path);
				if (resource != null) {
					return resource;
				}
			}
			return null;
		}

		@Override
		public boolean isResourceChangeListenerSupported() {
			return false;
		}

		@Override
		public void registerResourceChangeListener(ResourceChangeListener listener) {
		}

		@Override
		public void removeResourceChangeListener(ResourceChangeListener listener) {

		}

		private URLResource getMetaInfResource(URL resourceJar, String path) {
			try {
				String urlPath = URLEncoder.encode(ENCODED_SLASH.matcher(path).replaceAll("/"), "UTF-8");
				URL resourceUrl = new URL(resourceJar + "META-INF/resources" + urlPath);
				URLResource resource = new URLResource(resourceUrl, path);
				if (resource.getContentLength() < 0) {
					return null;
				}
				return resource;
			} catch (Exception ex) {
				return null;
			}
		}

	}

	/**
	 * {@link ServletContainerInitializer} to initialize {@link ServletContextInitializer
	 * ServletContextInitializers}.
	 */
	private static class Initializer implements ServletContainerInitializer {

		private final Collection<ServletContextInitializer> initializers;

		Initializer(ServletContextInitializer[] initializers) {
			this.initializers = Arrays.asList(initializers);
		}

		Initializer(Collection<ServletContextInitializer> initializers) {
			this.initializers = initializers;
		}

		@Override
		public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
			for (ServletContextInitializer initializer : this.initializers) {
				initializer.onStartup(servletContext);
			}
		}
	}

	/**
	 * 加载隐藏资源管理
	 */
	private static final class LoaderHidingResourceManager implements ResourceManager {

		private final ResourceManager delegate;

		/**
		 * 初始化加载隐藏资源管理
		 *
		 * @param delegate
		 */
		private LoaderHidingResourceManager(ResourceManager delegate) {
			this.delegate = delegate;
		}

		@Override
		public Resource getResource(String path) throws IOException {
			if (path.startsWith("/ghost/framework/undertow/web/module")) {
				return null;
			}
			return this.delegate.getResource(path);
		}

		@Override
		public boolean isResourceChangeListenerSupported() {
			return this.delegate.isResourceChangeListenerSupported();
		}

		@Override
		public void registerResourceChangeListener(ResourceChangeListener listener) {
			this.delegate.registerResourceChangeListener(listener);
		}

		@Override
		public void removeResourceChangeListener(ResourceChangeListener listener) {
			this.delegate.removeResourceChangeListener(listener);
		}

		@Override
		public void close() throws IOException {
			this.delegate.close();
		}

	}

	private static class AccessLogShutdownListener implements ServletContextListener {

		private final XnioWorker worker;

		private final DefaultAccessLogReceiver accessLogReceiver;

		AccessLogShutdownListener(XnioWorker worker, DefaultAccessLogReceiver accessLogReceiver) {
			this.worker = worker;
			this.accessLogReceiver = accessLogReceiver;
		}

		@Override
		public void contextInitialized(ServletContextEvent sce) {
		}

		@Override
		public void contextDestroyed(ServletContextEvent sce) {
			try {
				this.accessLogReceiver.close();
				this.worker.shutdown();
				this.worker.awaitTermination(30, TimeUnit.SECONDS);
			} catch (IOException ex) {
				throw new IllegalStateException(ex);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
}