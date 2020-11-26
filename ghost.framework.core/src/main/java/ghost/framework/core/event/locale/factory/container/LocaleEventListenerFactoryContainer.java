package ghost.framework.core.event.locale.factory.container;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.core.event.locale.ILocaleEventTargetHandle;

/**
 * package: ghost.framework.core.event.locale.factory.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认区域事件监听工厂容器
 * @Date: 17:48 2020/1/20
 */
@Component
public class LocaleEventListenerFactoryContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends ILocaleEventTargetHandle<O, T>,
                L extends Object
                >
        extends AbstractBeanFactoryContainer<L>
        implements ILocaleEventListenerFactoryContainer<O, T, E, L> {
    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public LocaleEventListenerFactoryContainer(@Application @Autowired @Nullable ILocaleEventListenerFactoryContainer<O, T, E, L> parent) {
        this.parent = parent;
    }

    /**
     * 父级类事件监听容器
     */
    private ILocaleEventListenerFactoryContainer<O, T, E, L> parent;

    /**
     *
     * @return
     */
    @Override
    public ILocaleEventListenerFactoryContainer<O, T, E, L> getParent() {
        return parent;
    }

    /**
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().info("loader:" + event.toString());
        //执行父级
        if (this.parent != null) {
//            this.parent.loader(event);
            //判断事件是否已经处理
            if (event.isHandle()) {
                return;
            }
        }
//        //遍历事件监听容器列表
//        for (L l : this.getEventExecuteList()) {
//            //判断是否执行接口
//            if (l instanceof Class) {
//                ILocaleEventFactory factory = (ILocaleEventFactory) event.getOwner().getBean((Class<?>) l);
//                //执行事件
//                factory.loader(event);
//                //判断事件是否已经处理
//                if (event.isHandle()) {
//                    return;
//                }
//            }
//            //对象类型处理
//            if (ILocaleEventFactory.class.isAssignableFrom(l.getClass())) {
//                //执行对象绑定
//                ((ILocaleEventFactory) l).loader(event);
//                //判断事件是否已经处理
//                if (event.isHandle()) {
//                    return;
//                }
//            }
//        }
    }
}