package ghost.framework.context.bean.factory.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link java.util.Map} 列表类型容器事件工厂接口
 * @Date: 19:01 2020/1/14
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
public interface IMapInterfaceContainerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
}