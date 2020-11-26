package ghost.framework.web.module;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.event.BeanMethodEventListener;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplicationEnvironment;
import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.beans.application.event.ApplicationEventTopic;
import ghost.framework.context.application.event.ApplicationEventListener;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.server.IConfigurableServletWebServerFactoryContainer;
import ghost.framework.web.context.server.IWebServerContainer;
import ghost.framework.web.context.server.WebServer;
import ghost.framework.web.context.server.WebServerException;
import ghost.framework.web.context.servlet.server.ConfigurableServletWebServerFactory;
import org.apache.log4j.Logger;

/**
 * package: ghost.framework.web.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用事件处理
 * @Date: 2020/2/16:5:54
 */
@Component
public class ApplicationEventDispatch<E extends AbstractApplicationEvent> implements ApplicationEventListener<E> {
    /**
     * 注入模块接口
     */
    @Autowired
    private IModule module;
    /**
     * 注入应用env接口
     */
    @Application
    @Autowired
    private IApplicationEnvironment environment;
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(ApplicationEventDispatch.class);
    /**
     * 事件回调
     *
     * @param event
     */
    @BeanMethodEventListener
    public void onApplicationEvent(E event) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(event.getTopic());
        }
        //处理应用启动完成事件
        if (event.getTopic().equals(ApplicationEventTopic.AppLaunchCompleted.name())) {
            //设置事件已经处理
            event.setHandle(true);
            //获取启动服务默认服务端口
            int port = this.environment.getInt("ghost.framework.web.default.server.port");
            //设置内容目录
//            String contextPath = this.environment.getString("ghost.framework.web.default.server.context.path");
            //从web模块获取配置web服务工厂容器接口
            IConfigurableServletWebServerFactoryContainer webServerFactoryContainer = this.module.getBean(IConfigurableServletWebServerFactoryContainer.class);
            //声明要启动的服务
            WebServer webServer = null;
            //判断是否只有一个配置web服务
            if (webServerFactoryContainer.size() == 1) {
                //获取第一个wen配置服务创建web服务接口
                ConfigurableServletWebServerFactory webServerFactory = webServerFactoryContainer.get(0);
                //设置启动端口
                webServerFactory.setPort(port);
//                webServerFactory.setContextPath(contextPath);
                //获取服务
                webServer = webServerFactory.getWebServer();
                //将web服务接口添加入web服务容器中
                this.module.getBean(IWebServerContainer.class).add(webServer);
            }
            try {
                //启动服务
                webServer.start();
            } catch (WebServerException e) {
                e.printStackTrace();
                webServer.stop();
            }
            return;
        }
        //其它事件处理
    }
}