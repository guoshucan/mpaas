package ghost.framework.core.bean.factory.container;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.container.BeanMapInterfaceContainer;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.BeanClassInterfaceException;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.container.IMapInterfaceContainerAnnotationBeanFactory;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link java.util.Map} 列表类型容器事件工厂类
 * @Date: 19:02 2020/1/14
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class MapInterfaceContainerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IMapInterfaceContainerAnnotationBeanFactory<O, T, E, V> {

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = BeanMapInterfaceContainer.class;

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
        return "MapInterfaceContainerAnnotationBeanFactory{" +
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
        //判断是否有注释
        //获取容器注释
        BeanMapInterfaceContainer container = this.getAnnotation(event);
        BeanMapContainer mapContainer = container.container();
        this.isInterface(container);
        //判断是否无效处理引发错误
        if (mapContainer.error()) {
            event.getExecuteOwner().getBean(mapContainer.value()).put(container.value(), event.getTarget());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("loader:" + container.value().getName() + " add(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
        //处理可以忽略找不到容器接口
        Map map = event.getExecuteOwner().getNullableBean(mapContainer.value());
        if (map != null) {
            map.put(container.value(), event.getTarget());
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
        BeanMapInterfaceContainer container = (BeanMapInterfaceContainer) event.getTarget().getAnnotation(this.annotation);
        BeanMapContainer mapContainer = container.container();
        this.isInterface(container);
        //判断是否无效处理引发错误
        if (mapContainer.error()) {
            event.getExecuteOwner().getBean(mapContainer.value()).remove(container.value());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("unloader:" + container.value().getName() + " remove(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
        //处理可以忽略找不到容器接口
        Map map = event.getExecuteOwner().getNullableBean(mapContainer.value());
        if (map != null) {
            map.remove(container.value());
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
    private void isInterface(BeanMapInterfaceContainer container) {
        if (!container.value().isInterface()) {
            throw new BeanClassInterfaceException(container.value().toString());
        }
    }
}