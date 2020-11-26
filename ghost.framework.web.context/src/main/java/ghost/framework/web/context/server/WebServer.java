package ghost.framework.web.context.server;

import ghost.framework.context.module.IModule;

/**
 * web服务接口
 * 将创建后存储于 {@link IWebServerContainer} 接口容器中
 */
public interface WebServer {
	/**
	 * 获取模块接口，表示此web服务接口属于此模块所有
	 * @return
	 */
	IModule getModule();
	/**
	 * 获取服务名称
	 * @return
	 */
	default String getName(){
		return "default";
	}
	/**
	 * Starts the web server. Calling this method on an already started server has no
	 * effect.
	 * @throws WebServerException if the server cannot be started
	 */
	void start() throws WebServerException;

	/**
	 * Stops the web server. Calling this method on an already stopped server has no
	 * effect.
	 * @throws WebServerException if the server cannot be stopped
	 */
	void stop() throws WebServerException;

	/**
	 * Return the port this server is listening on.
	 * @return the port (or -1 if none)
	 */
	int getPort();
}