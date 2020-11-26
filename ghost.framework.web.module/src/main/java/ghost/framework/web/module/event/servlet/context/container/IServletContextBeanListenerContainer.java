package ghost.framework.web.module.event.servlet.context.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.web.module.event.servlet.context.IWebServletContextDestroyedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.IWebServletContextInitializedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.factory.IWebServletContextEventFactory;

import javax.servlet.ServletContextEvent;

/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:WebServlet内容事件监听容器接口
 * @Date: 2020/1/27:19:56
 * @param <O>
 * @param <T>
 * @param <D>
 * @param <I>
 * @param <L>
 */
public interface IServletContextBeanListenerContainer
        <
                O extends ICoreInterface,
                T extends ServletContextEvent,
                D extends IWebServletContextDestroyedEventTargetHandle<O, T>,
                I extends IWebServletContextInitializedEventTargetHandle<O, T>,
                L extends IWebServletContextEventFactory<O, T, D, I>
                >
        extends IBeanFactoryContainer<L>, IWebServletContextEventFactory<O, T, D, I> {
    /**
     * 获取父级接口
     * @return
     */
    IServletContextBeanListenerContainer<O, T, D, I, L> getParent();
}