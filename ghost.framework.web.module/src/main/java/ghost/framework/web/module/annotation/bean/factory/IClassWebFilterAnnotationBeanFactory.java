package ghost.framework.web.module.annotation.bean.factory;

import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;

/**
 * package: ghost.framework.web.module.annotation.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理 {@link javax.servlet.annotation.WebFilter} 注释事件工厂接口
 * @Date: 0:45 2020/1/17
 * @param <O> 发起方对象
 * @param <T> 目标类型
 * @param <E> 类型绑定事件目标处理类型
 * @param <V> 返回类型
 */
@FirstOrder
public interface IClassWebFilterAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
}
