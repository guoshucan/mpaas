package ghost.framework.context.bean.factory;

import ghost.framework.context.base.ICoreInterface;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释绑定事件监听工厂容器接口
 * 处理类型注释有指定注释绑定事件工厂处理
 * {@link INewInstanceBeanFactory} 主要实现构建对象绑定工厂接口
 * @Date: 7:45 2019/12/27
 * @param <O> 发起方
 * @param <T> 注释类型
 * @param <E> 注释绑定事件目标处理
 * @param <L> {@link IClassAnnotationBeanFactory <O, T, E, V>} 接口对象
 * @param <V> 类型创建的对象
 */
public interface IClassAnnotationBeanFactoryContainer<
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        L extends IClassAnnotationBeanFactory<O, T, E, V>,
        V>
        extends IAnnotationBeanFactoryContainer<O, T, E, L>, INewInstanceBeanFactory<O, T, E, Object, V> {
    /**
     * 获取级
     *
     * @return
     */
    IClassAnnotationBeanFactoryContainer<O, T, E, L, V> getParent();
}