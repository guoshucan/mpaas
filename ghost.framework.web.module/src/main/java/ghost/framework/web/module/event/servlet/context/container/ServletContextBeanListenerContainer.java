package ghost.framework.web.module.event.servlet.context.container;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.web.module.event.servlet.context.IWebServletContextDestroyedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.IWebServletContextInitializedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.factory.IWebServletContextEventFactory;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:WebServlet内容事件监听容器类
 * @Date: 2020/1/27:19:50
 * @param <O>
 * @param <T>
 * @param <D>
 * @param <I>
 * @param <L>
 */
@Component
public class ServletContextBeanListenerContainer
        <
                O extends ICoreInterface,
                T extends ServletContextEvent,
                D extends IWebServletContextDestroyedEventTargetHandle<O, T>,
                I extends IWebServletContextInitializedEventTargetHandle<O, T>,
                L extends IWebServletContextEventFactory<O, T, D, I>
                >
        extends AbstractBeanFactoryContainer<L>
        implements IServletContextBeanListenerContainer<O, T, D, I, L> {
    /**
     * 初始化WebServlet内容事件监听容器类
     *
     * @param parent 父级接口
     */
    public ServletContextBeanListenerContainer(@Application @Autowired @Nullable IServletContextBeanListenerContainer<O, T, D, I, L> parent) {
        this.parent = parent;
        if (parent == null) {
            this.getLog().info("~" + this.getClass().getName());
        } else {
            this.getLog().info("~" + this.getClass().getName() + "(parent:" + parent.getClass().getName() + ")");
        }
    }

    /**
     * 父级接口
     */
    private IServletContextBeanListenerContainer<O, T, D, I, L> parent;

    /**
     * 获取父级接口
     *
     * @return
     */
    @Override
    public IServletContextBeanListenerContainer<O, T, D, I, L> getParent() {
        return parent;
    }

    /**
     * 释放事件
     *
     * @param event 事件对象
     */
    @Override
    public void destroyed(D event) {
        this.getLog().info("destroyed:" + event.toString());
        //遍历事件
        for (L l : new ArrayList<>(this)) {
            //对象类型处理
            if (IWebServletContextEventFactory.class.isAssignableFrom(l.getClass())) {
                //执行对象绑定
                l.destroyed(event);
                //判断事件是否已经处理
                if (event.isHandle()) {
                    return;
                }
            }
        }
        //处理父级
        if (this.parent != null) {
            this.parent.destroyed(event);
        }
    }

    /**
     * 初始化事件
     *
     * @param event 事件对象
     */
    @Override
    public void initialized(I event) {
        this.getLog().info("initialized:" + event.toString());
        //遍历事件
        for (L l : new ArrayList<>(this)) {

            //对象类型处理
            if (IWebServletContextEventFactory.class.isAssignableFrom(l.getClass())) {
                //执行对象绑定
                l.initialized(event);
                //判断事件是否已经处理
                if (event.isHandle()) {
                    return;
                }
            }
        }
        //处理父级
        if (this.parent != null) {
            this.parent.initialized(event);
        }
    }
}