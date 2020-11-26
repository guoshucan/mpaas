package ghost.framework.core.event.environment.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.core.event.environment.IEnvironmentEventTargetHandle;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 8:51 2019/12/27
 */
public interface IEnvironmentEventListenerFactoryContainer<
        O extends ICoreInterface,
        T extends IEnvironment,
        L extends IEnvironmentEventFactory<O, T, E>,
        E extends IEnvironmentEventTargetHandle<O, T>>
        extends IBeanFactoryContainer<L>, IEnvironmentEventFactory<O, T, E> {
}