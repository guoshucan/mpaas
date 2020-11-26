package ghost.framework.context.bean.factory;

import ghost.framework.context.base.ICoreInterface;

import java.lang.annotation.Annotation;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释事件监听工厂容器类
 * @Date: 7:45 2019/12/27
 * @param <O> 发起方
 * @param <T> 注释类型
 * @param <E> 注释绑定事件目标处理
 * @param <L> {@link IClassAnnotationBeanFactory <O, T, E>} 接口类型或继承 {@link IClassAnnotationBeanFactory <O, T, E>} 接口对象
 */
public abstract class AbstractAnnotationBeanFactoryContainer
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IAnnotationBeanTargetHandle<O, T>,
                L extends IAnnotationBeanFactory<O, T, E>
                >
        extends AbstractBeanFactoryContainer<L>
        implements IAnnotationBeanFactoryContainer<O, T, E, L> {
    /**
     * 获取注释事件工厂接口
     *
     * @param annotation 注释类型
     * @param <R>        返回继承 {@link IAnnotationBeanFactory <O, T, E>} 接口类型
     * @return 返回注释事件工厂
     */
    @Override
    public <R extends IAnnotationBeanFactory<O, T, E>> R getAnnotationEventFactory(Class<? extends Annotation> annotation) {
        //遍历注释事件工厂列表
        for (IAnnotationBeanFactory<O, T, E> eventFactory : this) {
            //比对注释类型
            if (eventFactory.getAnnotationClass().equals(annotation)) {
                return (R)eventFactory;
            }
        }
        return null;
    }
}