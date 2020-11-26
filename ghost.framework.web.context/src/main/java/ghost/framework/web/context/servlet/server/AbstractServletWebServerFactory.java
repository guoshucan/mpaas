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

package ghost.framework.web.context.servlet.server;

import ghost.framework.web.context.servlet.context.ServletContextInitializer;
import ghost.framework.web.context.server.AbstractConfigurableWebServerFactory;
import ghost.framework.web.context.server.MimeMappings;
import ghost.framework.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Abstract base class for {@link ConfigurableServletWebServerFactory} implementations.
 *
 * @author Phillip Webb
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Ivan Sopov
 * @author Eddú Meléndez
 * @author Brian Clozel
 * @since 2.0.0
 */
public abstract class AbstractServletWebServerFactory extends AbstractConfigurableWebServerFactory implements ConfigurableServletWebServerFactory {

	protected final Log logger = LogFactory.getLog(getClass());

	private String contextPath = "";
	/**
	 * 显示名称
	 */
	private String displayName = "default";

	private Session session = new Session();

	private boolean registerDefaultServlet = true;

	private MimeMappings mimeMappings = new MimeMappings(MimeMappings.DEFAULT);

	private List<ServletContextInitializer> initializers = new ArrayList<>();

	private Jsp jsp = new Jsp();
	/**
	 * 区域列表
	 */
	private Map<Locale, Charset> localeCharsetMappings = new HashMap<>();
	/**
	 * 初始化参数
	 */
	private Map<String, String> initParameters = Collections.emptyMap();
	/**
	 * web文档引导目录
	 */
	private final DocumentRoot documentRoot = new DocumentRoot(this.logger);

	private final StaticResourceJars staticResourceJars = new StaticResourceJars();

	/**
	 * Create a new {@link AbstractServletWebServerFactory} instance.
	 */
	public AbstractServletWebServerFactory() {
	}

	/**
	 * Create a new {@link AbstractServletWebServerFactory} instance with the specified
	 * port.
	 *
	 * @param port the port number for the web server
	 */
	public AbstractServletWebServerFactory(int port) {
		super(port);
	}

	/**
	 * Create a new {@link AbstractServletWebServerFactory} instance with the specified
	 * context path and port.
	 *
	 * @param contextPath the context path for the web server
	 * @param port        the port number for the web server
	 */
	public AbstractServletWebServerFactory(String contextPath, int port) {
		super(port);
		checkContextPath(contextPath);
		this.contextPath = contextPath;
	}

	/**
	 * Returns the context path for the web server. The path will Before with "/" and not
	 * After with "/". The root context is represented by an empty string.
	 *
	 * @return the context path
	 */
	public String getContextPath() {
		return this.contextPath;
	}

	@Override
	public void setContextPath(String contextPath) {
		checkContextPath(contextPath);
		this.contextPath = contextPath;
	}

	private void checkContextPath(String contextPath) {
		Assert.notNull(contextPath, "ContextPath must not be null");
		if (!contextPath.isEmpty()) {
			if ("/".equals(contextPath)) {
				throw new IllegalArgumentException("Root ContextPath must be specified using an empty string");
			}
			if (!contextPath.startsWith("/") || contextPath.endsWith("/")) {
				throw new IllegalArgumentException("ContextPath must Before with '/' and not After with '/'");
			}
		}
	}

	/**
	 * 获取显示名称
	 * @return
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * 设置显示名称
	 * @param displayName the displayName to set
	 */
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Flag to indicate that the default servlet should be registered.
	 *
	 * @return true if the default servlet is to be registered
	 */
	public boolean isRegisterDefaultServlet() {
		return this.registerDefaultServlet;
	}

	@Override
	public void setRegisterDefaultServlet(boolean registerDefaultServlet) {
		this.registerDefaultServlet = registerDefaultServlet;
	}

	/**
	 * Returns the mime-depend mappings.
	 *
	 * @return the mimeMappings the mime-depend mappings.
	 */
	public MimeMappings getMimeMappings() {
		return this.mimeMappings;
	}

	@Override
	public void setMimeMappings(MimeMappings mimeMappings) {
		this.mimeMappings = new MimeMappings(mimeMappings);
	}

	/**
	 * Returns the document root which will be used by the web context to serve static
	 * files.
	 *
	 * @return the document root
	 */
	public File getDocumentRoot() {
		return this.documentRoot.getDirectory();
	}

	/**
	 * 设置web根目录
	 * @param documentRoot the document root or {@code null} if not required
	 */
	@Override
	public void setDocumentRoot(File documentRoot) {
		this.documentRoot.setDirectory(documentRoot);
	}

	@Override
	public void setInitializers(List<? extends ServletContextInitializer> initializers) {
		Assert.notNull(initializers, "Initializers must not be null");
		this.initializers = new ArrayList<>(initializers);
	}

	@Override
	public void addInitializers(Collection<ServletContextInitializer> initializers) {
		Assert.notNull(initializers, "Initializers must not be null");
		this.initializers.addAll(initializers);
	}

	public Jsp getJsp() {
		return this.jsp;
	}

	@Override
	public void setJsp(Jsp jsp) {
		this.jsp = jsp;
	}

	/**
	 * 获取会话
	 * @return
	 */
	public Session getSession() {
		return this.session;
	}

	/**
	 * 设置会话
	 * @param session the session configuration
	 */
	@Override
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Return the Locale to Charset mappings.
	 *
	 * @return the charset mappings
	 */
	public Map<Locale, Charset> getLocaleCharsetMappings() {
		return this.localeCharsetMappings;
	}

	@Override
	public void setLocaleCharsetMappings(Map<Locale, Charset> localeCharsetMappings) {
		Assert.notNull(localeCharsetMappings, "localeCharsetMappings must not be null");
		this.localeCharsetMappings = localeCharsetMappings;
	}

	@Override
	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}

	public Map<String, String> getInitParameters() {
		return this.initParameters;
	}

	/**
	 * Utility method that can be used by subclasses wishing to combine the specified
	 * {@link ServletContextInitializer} parameters with those defined in this instance.
	 *
	 * @param initializers the initializers to merge
	 * @return a complete set of merged initializers (with the specified parameters
	 * appearing first)
	 */
	protected final Collection<ServletContextInitializer> mergeInitializers(Collection<ServletContextInitializer> initializers) {
		Collection<ServletContextInitializer> mergedInitializers = new ArrayList<>();
		//合并ServletContext参数
		mergedInitializers.add((servletContext) -> this.initParameters.forEach(servletContext::setInitParameter));
		//会话配置初始化
		mergedInitializers.add(new SessionConfiguringInitializer(this.session));
		mergedInitializers.addAll(initializers);
		mergedInitializers.addAll(this.initializers);
		return mergedInitializers;
	}

	/**
	 * Returns whether or not the JSP servlet should be registered with the web server.
	 *
	 * @return {@code true} if the servlet should be registered, otherwise {@code false}
	 */
	protected boolean shouldRegisterJspServlet() {
		return this.jsp != null && this.jsp.getRegistered() && this.isPresent(this.jsp.getClassName());
	}

	/**
	 * Returns the absolute document root when it points to a valid directory, logging a
	 * warning and returning {@code null} otherwise.
	 *
	 * @return the valid document root
	 */
	protected final File getValidDocumentRoot() {
		return this.documentRoot.getValidDirectory();
	}

	/**
	 * 获取静态资源文件与包列表
	 * @return
	 */
	protected final List<URL> getUrlsOfJarsWithMetaInfResources() {
		return this.staticResourceJars.getUrls();
	}

	/**
	 * 获取会话存储目录
	 * @return
	 */
	protected final File getValidSessionStoreDir() {
		return getValidSessionStoreDir(true);
	}

	/**
	 * 获取会话存储目录
	 * @param mkdirs 是否创建目录
	 * @return
	 */
	protected final File getValidSessionStoreDir(boolean mkdirs) {
		return this.session.getSessionStoreDirectory().getValidDirectory(mkdirs);
	}

	/**
	 * {@link ServletContextInitializer} to apply appropriate parts of the {@link Session}
	 * configuration.
	 */
	private static class SessionConfiguringInitializer implements ServletContextInitializer {
		/**
		 * 会话设置
		 */
		private final Session session;

		/**
		 *
		 * @param session
		 */
		SessionConfiguringInitializer(Session session) {
			this.session = session;
		}

		/**
		 * 启动ServletContext内容
		 * @param servletContext the {@code ServletContext} to initialize
		 * @throws ServletException
		 */
		@Override
		public void onStartup(ServletContext servletContext) throws ServletException {
			//判断是否启用Session跟踪模式
			if (this.session.getTrackingModes() != null) {
				//设置跟踪模式
				servletContext.setSessionTrackingModes(unwrap(this.session.getTrackingModes()));
			}
			//配置会话Cookie设置
			configureSessionCookie(servletContext.getSessionCookieConfig());
		}

		/**
		 * 配置会话Cookie设置
		 * @param config
		 */
		private void configureSessionCookie(SessionCookieConfig config) {
			Session.Cookie cookie = this.session.getCookie();
			if (cookie.getName() != null) {
				config.setName(cookie.getName());
			}
			if (cookie.getDomain() != null) {
				config.setDomain(cookie.getDomain());
			}
			if (cookie.getPath() != null) {
				config.setPath(cookie.getPath());
			}
			if (cookie.getComment() != null) {
				config.setComment(cookie.getComment());
			}
			if (cookie.getHttpOnly() != null) {
				config.setHttpOnly(cookie.getHttpOnly());
			}
			if (cookie.getSecure() != null) {
				config.setSecure(cookie.getSecure());
			}
			if (cookie.getMaxAge() != null) {
				config.setMaxAge((int) cookie.getMaxAge().getSeconds());
			}
		}

		/**
		 * servlet3 有cookie、url、ssl三种模式。
		 * 拆开Session跟踪模式
		 * @param modes
		 * @return
		 */
		private Set<javax.servlet.SessionTrackingMode> unwrap(Set<Session.SessionTrackingMode> modes) {
			if (modes == null) {
				return null;
			}
			Set<javax.servlet.SessionTrackingMode> result = new LinkedHashSet<>();
			for (Session.SessionTrackingMode mode : modes) {
				result.add(javax.servlet.SessionTrackingMode.valueOf(mode.name()));
			}
			return result;
		}
	}
}