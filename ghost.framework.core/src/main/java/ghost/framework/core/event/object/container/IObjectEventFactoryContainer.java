package ghost.framework.core.event.object.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.core.event.object.factory.IObjectEventFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象事件监听工厂容器接口
 * @Date: 8:37 2019/12/27
 * @param <O>
 * @param <T>
 * @param <L>
 * @param <E>
 */
public interface IObjectEventFactoryContainer
        <
        O extends ICoreInterface,
        T extends Object,
        E extends IBeanTargetHandle<O, T>,
        L extends IObjectEventFactory<O, T, E>
        >
        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
    IObjectEventFactoryContainer<O, T, E, L> getParent();
}