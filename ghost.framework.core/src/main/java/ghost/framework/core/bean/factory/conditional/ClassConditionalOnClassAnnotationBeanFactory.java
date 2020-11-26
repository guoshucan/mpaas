package ghost.framework.core.bean.factory.conditional;

import ghost.framework.beans.annotation.conditional.ConditionalOnClass;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.conditional.IClassConditionalOnClassAnnotationBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.bean.factory.injection.method.bean.built
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认处理 {@link ConditionalOnClass} 注释事件工厂类
 * @Date: 2020/1/8:20:12
 * @param <O> 发起方对象
 * @param <T> 目标类型
 * @param <E> 类型绑定事件目标处理类型
 * @param <V> 返回类型
 */
@Component
public class ClassConditionalOnClassAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassConditionalOnClassAnnotationBeanFactory<O, T, E, V> {

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = ConditionalOnClass.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassConditionalOnClassAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 加载事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取注释对象
        ConditionalOnClass onClass = this.getAnnotation(event);
        //
        for (Class<?> c : onClass.depend()) {
            this.getLog().debug("loader>class:" + event.getTarget().getName() + ">ConditionalOnClass>depend:" + c.getName());
            event.getExecuteOwner().addBean(c);
        }
        for (Class<?> c : onClass.value()) {
            if (event.getExecuteOwner().containsBean(c)) {
                this.getLog().debug("loader>class:" + event.getTarget().getName() + ">ConditionalOnClass:" + c.getName());
                event.getExecuteOwner().addBean(c);
            }
        }
    }
}