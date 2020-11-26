package ghost.framework.core.event.locale.factory.container;

import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.core.event.locale.ILocaleEventTargetHandle;
import ghost.framework.context.loader.ILoader;

/**
 * package: ghost.framework.core.event.locale.factory.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域事件监听工厂容器类
 * @Date: 17:47 2020/1/20
 */
public interface ILocaleEventListenerFactoryContainer<
        O,
        T extends Object,
        E extends ILocaleEventTargetHandle<O, T>,
        L extends Object
        >
        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
    ILocaleEventListenerFactoryContainer<O, T, E, L> getParent();
}