package ghost.framework.web.module.server;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.server.IConfigurableServletWebServerFactoryContainer;
import ghost.framework.web.context.server.IWebServerContainer;
import ghost.framework.web.context.server.WebServer;

import java.util.ArrayList;

/**
 * package: ghost.framework.web.module.server
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置 {@link WebServer} 接口的存储容器
 * 通过 {@link IConfigurableServletWebServerFactoryContainer} 获取默认或指定配置服务创建 {@link WebServer} 存储与本容器中
 * @Date: 2020/2/20:16:45
 */
@Component
public class WebServerContainer extends ArrayList<WebServer> implements IWebServerContainer {

}