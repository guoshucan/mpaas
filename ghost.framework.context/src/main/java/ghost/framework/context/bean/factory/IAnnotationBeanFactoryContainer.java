package ghost.framework.context.bean.factory;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.loader.ILoader;

import java.lang.annotation.Annotation;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释绑定事件监听工厂容器接口
 * 处理类型注释有指定注释绑定事件工厂处理
 * @Date: 7:45 2019/12/27
 * @param <O> 发起方
 * @param <T> 注释类型
 * @param <E> 注释绑定事件目标处理
 * @param <L> {@link IAnnotationBeanFactory <O, T, E>} 接口对象
 */
public interface IAnnotationBeanFactoryContainer
        <
        O extends ICoreInterface,
        T,
        E extends IAnnotationBeanTargetHandle<O, T>,
        L extends IAnnotationBeanFactory<O, T, E>
        >
        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
    /**
     * 获取注释事件工厂接口
     *
     * @param annotation 注释类型
     * @param <R>        返回继承 {@link IAnnotationBeanFactory <O, T, E>} 接口类型
     * @return 返回注释事件工厂
     */
    <R extends IAnnotationBeanFactory<O, T, E>> R getAnnotationEventFactory(Class<? extends Annotation> annotation);
}