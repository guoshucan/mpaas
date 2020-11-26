package ghost.framework.core.bean.factory.container;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.container.BeanClassContainer;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.BeanMethodException;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.container.IClassContainerAnnotationBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.event.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定类型名称容器事件工厂类
 * @Date: 10:54 2020/1/25
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassContainerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassContainerAnnotationBeanFactory<O, T, E, V> {
    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = BeanClassContainer.class;
    /**
     * 获取绑定注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassContainerAnnotationBeanFactory{" +
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
        //获取容器注释
        BeanClassContainer container = this.getAnnotation(event);
        try {
            //判断是否无效处理引发错误
            if (container.error()) {
                event.getExecuteOwner().getBeanInvoke(container.value(), container.loaderMethod(), event.getValue());
                if (this.getLog().isDebugEnabled()) {
                    this.getLog().debug("loader:" + container.value() + ">" + container.loaderMethod() + "(" + event.getValue().getClass().getName() + ")");
                }
                return;
            }
            //处理可以忽略找不到容器接口
            event.getExecuteOwner().getNullableBeanInvoke(container.value(), container.loaderMethod(), event.getValue());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("loader:" + container.value() + ">" + container.loaderMethod() + "(" + event.getValue().getClass().getName() + ")");
            }
        }catch (Exception e){
            throw new BeanMethodException(event.getValue().getClass().getName(), e);
        }
    }

    /**
     * 卸载事件
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        //获取容器注释
        BeanClassContainer container = (BeanClassContainer)event.getTarget().getAnnotation(this.annotation);
        try {
            //判断是否无效处理引发错误
            if (container.error()) {
                event.getExecuteOwner().getBeanInvoke(container.value(), container.unLoaderMethod(), event.getValue());
                if (this.getLog().isDebugEnabled()) {
                    this.getLog().debug("unloader:" + container.value() + ">" + container.unLoaderMethod() + "(" + event.getValue().getClass().getName() + ")");
                }
                return;
            }
            //处理可以忽略找不到容器接口
            event.getExecuteOwner().getNullableBeanInvoke(container.value(), container.unLoaderMethod(), event.getValue());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("unloader:" + container.value() + ">" + container.unLoaderMethod() + "(" + event.getValue().getClass().getName() + ")");
            }
        }catch (Exception e){
            throw new BeanMethodException(event.getValue().getClass().getName(), e);
        }
    }
}