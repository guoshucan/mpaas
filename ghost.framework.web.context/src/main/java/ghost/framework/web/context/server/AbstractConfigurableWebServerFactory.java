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

package ghost.framework.web.context.server;

import ghost.framework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Abstract base class for {@link ConfigurableWebServerFactory} implementations.
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
public abstract class AbstractConfigurableWebServerFactory implements ConfigurableWebServerFactory {

	private int port = 8080;

	private InetAddress address;

	private Set<ErrorPage> errorPages = new LinkedHashSet<>();

	private Ssl ssl;

	private SslStoreProvider sslStoreProvider;

	private Http2 http2;
	/**
	 * 简单的压缩配置
	 */
	private Compression compression;
	/**
	 * 服务器头
	 */
	private String serverHeader;

	/**
	 * Create a new {@link AbstractConfigurableWebServerFactory} instance.
	 */
	public AbstractConfigurableWebServerFactory() {
	}

	/**
	 * Create a new {@link AbstractConfigurableWebServerFactory} instance with the
	 * specified port.
	 * @param port the port number for the web server
	 */
	public AbstractConfigurableWebServerFactory(int port) {
		this.port = port;
	}

	/**
	 * The port that the web server listens on.
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * 设置端口
	 * @param port the port to set
	 */
	@Override
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Return the address that the web server binds to.
	 * @return the address
	 */
	public InetAddress getAddress() {
		return this.address;
	}

	/**
	 * 设置ip地址
	 * @param address the address to set (defaults to {@code null})
	 */
	@Override
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * Returns a mutable set of {@link ErrorPage ErrorPages} that will be used when
	 * handling exceptions.
	 * @return the error pages
	 */
	public Set<ErrorPage> getErrorPages() {
		return this.errorPages;
	}

	/**
	 * 设置错误处理页
	 * @param errorPages the error pages
	 */
	@Override
	public void setErrorPages(Set<? extends ErrorPage> errorPages) {
		Assert.notNull(errorPages, "ErrorPages must not be null");
		this.errorPages = new LinkedHashSet<>(errorPages);
	}

	/**
	 * 添加错误处理页
	 * @param errorPages the error pages
	 */
	@Override
	public void addErrorPages(ErrorPage... errorPages) {
		Assert.notNull(errorPages, "ErrorPages must not be null");
		this.errorPages.addAll(Arrays.asList(errorPages));
	}

	/**
	 * 获取ssl设置
	 * @return
	 */
	public Ssl getSsl() {
		return this.ssl;
	}

	/**
	 * 设置ssl设置
	 * @param ssl the SSL configuration
	 */
	@Override
	public void setSsl(Ssl ssl) {
		this.ssl = ssl;
	}

	/**
	 * 获取ssl存储提供者
	 * @return
	 */
	public SslStoreProvider getSslStoreProvider() {
		return this.sslStoreProvider;
	}

	/**
	 * 设置ssl存储提供者
	 * @param sslStoreProvider the SSL store provider
	 */
	@Override
	public void setSslStoreProvider(SslStoreProvider sslStoreProvider) {
		this.sslStoreProvider = sslStoreProvider;
	}

	/**
	 * 获取http2
	 * @return
	 */
	public Http2 getHttp2() {
		return this.http2;
	}

	/**
	 * 设置http2
	 * @param http2 the HTTP/2 configuration
	 */
	@Override
	public void setHttp2(Http2 http2) {
		this.http2 = http2;
	}

	/**
	 * 获取简单的压缩配置
	 * @return
	 */
	public Compression getCompression() {
		return this.compression;
	}

	/**
	 * 设置简单的压缩配置
	 * @param compression the compression configuration
	 */
	@Override
	public void setCompression(Compression compression) {
		this.compression = compression;
	}

	/**
	 * 获取服务器头
	 * @return
	 */
	public String getServerHeader() {
		return this.serverHeader;
	}

	/**
	 * 设置服务器头
	 * @param serverHeader the server header value
	 */
	@Override
	public void setServerHeader(String serverHeader) {
		this.serverHeader = serverHeader;
	}

	/**
	 * Return the absolute temp dir for given web server.
	 * @param prefix server value
	 * @return the temp dir for given server.
	 */
	protected final File createTempDir(String prefix) {
		try {
			File tempDir = File.createTempFile(prefix + ".", "." + getPort());
			tempDir.delete();
			tempDir.mkdir();
			tempDir.deleteOnExit();
			return tempDir;
		} catch (IOException ex) {
			throw new WebServerException(
					"Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
		}
	}
	/**
	 * 验证类型是否存在
	 * @param className 类型名称
	 * @return
	 */
	protected boolean isPresent(String className){
		//没重写函数无效操作错误
		throw new UnsupportedOperationException("isPresent");
	}
}
