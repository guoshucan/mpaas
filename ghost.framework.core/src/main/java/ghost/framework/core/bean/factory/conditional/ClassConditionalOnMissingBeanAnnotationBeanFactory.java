package ghost.framework.core.bean.factory.conditional;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingBean;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.conditional.IClassConditionalOnMissingBeanAnnotationBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.event.annotation.factory.built
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认处理 {@link ConditionalOnMissingBean} 注释事件工厂类
 * @Date: 0:44 2020/1/17
 * @param <O> 发起方对象
 * @param <T> 目标类型
 * @param <E> 类型绑定事件目标处理类型
 * @param <V> 返回类型
 */
@Component
public class ClassConditionalOnMissingBeanAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassConditionalOnMissingBeanAnnotationBeanFactory<O, T, E, V> {

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = ConditionalOnMissingBean.class;

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
        return "ClassConditionalOnMissingBeanAnnotationBeanFactory{" +
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
        ConditionalOnMissingBean onMissingBean = this.getAnnotation(event);
        for (Class<?> c : onMissingBean.depend()) {
            event.getExecuteOwner().addBean(c);
            this.getLog().debug("loader>class:" + event.getTarget().getName() + ">ConditionalOnMissingBean>depend:" + c.getName());
        }
        Class<?>[] cs = onMissingBean.value();
        String[] name = onMissingBean.name();
        for (int i = 0; i <= name.length; i++) {
            if (!event.getExecuteOwner().containsBean(name[i])) {
                this.getLog().debug("loader>class:" + event.getTarget().getName() + ">ConditionalOnMissingBean:" + name[i] + ":" + cs[i].getName());
                event.getExecuteOwner().addBean(name[i], cs[i]);
            }
        }
    }
}