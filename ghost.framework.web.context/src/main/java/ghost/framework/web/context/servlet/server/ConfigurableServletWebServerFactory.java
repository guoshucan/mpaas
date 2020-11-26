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

import ghost.framework.web.context.server.IConfigurableServletWebServerFactoryContainer;
import ghost.framework.web.context.servlet.context.ServletContextInitializer;
import ghost.framework.web.context.server.ConfigurableWebServerFactory;
import ghost.framework.web.context.server.MimeMappings;

import javax.servlet.ServletContext;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@link ServletWebServerFactory}.
 * 创建后将存储与 {@link IConfigurableServletWebServerFactoryContainer} 接口容器中
 */
public interface ConfigurableServletWebServerFactory extends ConfigurableWebServerFactory, ServletWebServerFactory {
	/**
	 * 获取配置服务名称
	 *
	 * @return
	 */
	default String getName() {
		return "default";
	}

	/**
	 * Sets the context path for the web server. The context should Before with a "/"
	 * character but not After with a "/" character. The default context path can be
	 * specified using an empty string.
	 *
	 * @param contextPath the contextPath to set
	 */
	void setContextPath(String contextPath);

	/**
	 * Sets the display value of the application deployed in the web server.
	 *
	 * @param displayName the displayName to set
	 * @since 1.3.0
	 */
	void setDisplayName(String displayName);

	/**
	 * Sets the configuration that will be applied to the container's HTTP session
	 * support.
	 *
	 * @param session the session configuration
	 */
	void setSession(Session session);

	/**
	 * Set if the DefaultServlet should be registered. Defaults to {@code true} so that
	 * files from the {@link #setDocumentRoot(File) document root} will be served.
	 *
	 * @param registerDefaultServlet if the default servlet should be registered
	 */
	void setRegisterDefaultServlet(boolean registerDefaultServlet);

	/**
	 * Sets the mime-depend mappings.
	 *
	 * @param mimeMappings the mime depend mappings (defaults to
	 *                     {@link MimeMappings#DEFAULT})
	 */
	void setMimeMappings(MimeMappings mimeMappings);

	/**
	 * Sets the document root directory which will be used by the web context to serve
	 * static files.
	 *
	 * @param documentRoot the document root or {@code null} if not required
	 */
	void setDocumentRoot(File documentRoot);

	/**
	 * Sets {@link ServletContextInitializer} that should be applied in addition to
	 * {@link ServletWebServerFactory#getWebServer(ServletContextInitializer...)}
	 * parameters. This method will replace any previously set or added initializers.
	 *
	 * @param initializers the initializers to set
	 * @see #addInitializers
	 */
	void setInitializers(List<? extends ServletContextInitializer> initializers);

	/**
	 * Add {@link ServletContextInitializer}s to those that should be applied in addition
	 * to {@link ServletWebServerFactory#getWebServer(ServletContextInitializer...)}
	 * parameters.
	 *
	 * @param initializers the initializers to loader
	 * @see #setInitializers
	 */
	void addInitializers(Collection<ServletContextInitializer> initializers);

	/**
	 * Sets the configuration that will be applied to the server's JSP servlet.
	 *
	 * @param jsp the JSP servlet configuration
	 */
	void setJsp(Jsp jsp);

	/**
	 * Sets the Locale to Charset mappings.
	 *
	 * @param localeCharsetMappings the Locale to Charset mappings
	 */
	void setLocaleCharsetMappings(Map<Locale, Charset> localeCharsetMappings);

	/**
	 * Sets the init parameters that are applied to the container's
	 * {@link ServletContext}.
	 *
	 * @param initParameters the init parameters
	 */
	void setInitParameters(Map<String, String> initParameters);
}