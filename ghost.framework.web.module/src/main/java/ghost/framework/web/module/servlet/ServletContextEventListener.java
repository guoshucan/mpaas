package ghost.framework.web.module.servlet;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.context.module.IModule;
import ghost.framework.web.module.event.servlet.context.WebServletContextDestroyedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.WebServletContextInitializedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.container.IServletContextBeanListenerContainer;
import ghost.framework.web.module.event.servlet.context.container.ServletContextBeanListenerContainer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:WebServlet内容事件监听包装类
 * @Date: 2020/1/27:20:37
 */
@BeanCollectionContainer(IServletContextBeanListenerContainer.class)//注释注入容器接口
@ConditionalOnMissingClass(ServletContextBeanListenerContainer.class)//注释构建此类型优先构建此注释类型
@Component
public class ServletContextEventListener implements ServletContextListener {
    /**
     * 模块接口
     */
    @Autowired
    private IModule module;
    /**
     * 是否资源事件
     * @param event 事件对象
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        this.module.getBean(IServletContextBeanListenerContainer.class).destroyed(new WebServletContextDestroyedEventTargetHandle(this.module, event));
    }
    /**
     * 初始化资源事件
     * @param event 事件对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.module.getBean(IServletContextBeanListenerContainer.class).initialized(new WebServletContextInitializedEventTargetHandle(this.module, event));
    }
}