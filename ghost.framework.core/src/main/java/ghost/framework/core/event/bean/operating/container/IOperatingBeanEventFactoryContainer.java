package ghost.framework.core.event.bean.operating.container;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IOperatingBeanBeanTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.core.event.bean.operating.factory.IOperatingBeanAnnotationEventFactory;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;

/**
 * package: ghost.framework.core.event.bean.operating.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定事件监听工厂容器接口
 * @Date: 2020/1/13:13:22
 * @param <O> 发起方类型
 * @param <T> 绑定定义接口类型
 * @param <L> 容器对象类型，支持类型与对象两种模式处理
 * @param <E> 事件目标处理类型
 */
public interface IOperatingBeanEventFactoryContainer
        <
                O extends ICoreInterface,
                T extends IBeanDefinition,
                E extends IOperatingBeanBeanTargetHandle<O, T>,
                L extends IOperatingBeanAnnotationEventFactory<O, T, E>
                >
        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
    /**
     * 获取父级
     *
     * @return
     */
    IOperatingBeanEventFactoryContainer<O, T, E, L> getParent();
}