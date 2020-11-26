package ghost.framework.context.bean.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.loader.ILoader;

/**
 * package: ghost.framework.context.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型绑定工厂容器接口
 * {@link INewInstanceBeanFactory} 主要实现构建对象绑定工厂接口
 * @Date: 2020/6/3:23:14
 */
public interface IClassBeanFactoryContainer
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassBeanTargetHandle<O, T, V, String, Object>,
                L extends IClassBeanFactory<O, T, E, V>,
                V>
        extends IBeanFactoryContainer<L>, ILoader<O, T, E>, INewInstanceBeanFactory<O, T, E, Object, V> {
    /**
     * 获取级
     *
     * @return
     */
    IClassBeanFactoryContainer<O, T, E, L, V> getParent();
}