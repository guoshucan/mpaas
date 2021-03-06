package ghost.framework.core.bean.factory.container;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.beans.annotation.container.BeanCollectionInterfaceContainer;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.BeanClassInterfaceException;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.container.ICollectionInterfaceContainerAnnotationBeanFactory;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * package: ghost.framework.core.event.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link java.util.Collection} 列表类型容器事件工厂类
 * @Date: 10:55 2020/1/25
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class CollectionInterfaceContainerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements ICollectionInterfaceContainerAnnotationBeanFactory<O, T, E, V> {

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = BeanCollectionInterfaceContainer.class;

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
        return "CollectionInterfaceContainerAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 添加绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取容器注释
        BeanCollectionInterfaceContainer container = this.getAnnotation(event);
        BeanCollectionContainer listContainer = container.container();
        this.isInterface(container);
        //判断是否无效处理引发错误
        if (listContainer.error()) {
            event.getExecuteOwner().getBean(listContainer.value()).add(container.value());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("loader:" + container.value().getName() + " add(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
        //处理可以忽略找不到容器接口
        Collection list = event.getExecuteOwner().getNullableBean(listContainer.value());
        if (list != null) {
            list.add(container.value());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("loader:" + container.value().getName() + " add(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
    }

    /**
     * 删除绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        //获取容器注释
        BeanCollectionInterfaceContainer container = (BeanCollectionInterfaceContainer) event.getTarget().getAnnotation(this.annotation);
        BeanCollectionContainer listContainer = container.container();
        this.isInterface(container);
        //判断是否无效处理引发错误
        if (listContainer.error()) {
            event.getExecuteOwner().getBean(listContainer.value()).remove(container.value());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("unloader:" + container.value().getName() + " remove(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
        //处理可以忽略找不到容器接口
        Collection list = event.getExecuteOwner().getNullableBean(listContainer.value());
        if (list != null) {
            list.remove(container.value());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("unloader:" + container.value().getName() + " remove(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
    }

    /**
     * 判断容器对象是否为有效的接口类型
     *
     * @param container
     */
    private void isInterface(BeanCollectionInterfaceContainer container) {
        if (!container.value().isInterface()) {
            throw new BeanClassInterfaceException(container.value().toString());
        }
    }
}