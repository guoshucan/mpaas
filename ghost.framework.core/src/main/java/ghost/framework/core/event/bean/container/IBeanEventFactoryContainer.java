package ghost.framework.core.event.bean.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IItemBeanTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.core.event.bean.factory.IBeanEventFactory;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定事件监听工厂容器接口
 * @Date: 8:55 2019/12/27
 * @param <O> 发起发对象
 * @param <T> 绑定定义接口
 * @param <E> 绑定事件目标处理接口
 * @param <S> 绑定名称类型
 * @param <L> 继承 {@link IBeanEventFactory} 接口对象
 */
public interface IBeanEventFactoryContainer
        <
        O extends ICoreInterface,
        T extends Object,
        E extends IItemBeanTargetHandle<O, T, S>,
        S extends String,
        L extends IBeanEventFactory<O, T, E, S>
        >
        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
}