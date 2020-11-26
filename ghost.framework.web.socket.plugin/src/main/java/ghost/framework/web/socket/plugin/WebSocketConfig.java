package ghost.framework.web.socket.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.filter.IDispatcherFilter;
import ghost.framework.web.context.servlet.filter.IWsFilter;
import ghost.framework.web.socket.plugin.bean.factory.ClassClientEndpointAnnotationBeanFactory;
import ghost.framework.web.socket.plugin.bean.factory.ClassServerEndPointAnnotationBeanFactory;
import ghost.framework.web.socket.plugin.bean.factory.ParameterPathParamAnnotationInjectionFactory;
import ghost.framework.web.socket.plugin.proxy.cglib.WsMethodInvocationHandler;
import ghost.framework.web.socket.plugin.server.WsFilter;

import javax.servlet.ServletException;

/**
 * package: ghost.framework.web.socket.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:WebSocket插件配置
 * @Date: 2020/5/1:15:21
 */
@Configuration
public final class WebSocketConfig {
    @Autowired
    private IModule module;

    @Loader
    public void loader() throws ServletException {
        //初始化代理回调类型
        this.module.addBean(WsMethodInvocationHandler.class);
        //初始化类型注释绑定工厂
        this.module.addBean(ClassClientEndpointAnnotationBeanFactory.class);
        this.module.addBean(ClassServerEndPointAnnotationBeanFactory.class);
        //初始化参数注释注入工厂
        this.module.addBean(ParameterPathParamAnnotationInjectionFactory.class);
        //初始化过滤器
        IDispatcherFilter dispatcherFilter = this.module.getBean(IDispatcherFilter.class);
        IWsFilter wsFilter = this.module.addBean(WsFilter.class);
        dispatcherFilter.add(wsFilter);
    }

    @Unloader
    public void unloader() {

    }
}
