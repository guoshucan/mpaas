package ghost.framework.core.bean.factory.container;
import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.beans.BeanException;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.container.IMapContainerAnnotationBeanFactory;
import ghost.framework.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link java.util.Map} 列表容器事件工厂类
 * @Date: 19:01 2020/1/14
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class MapContainerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IMapContainerAnnotationBeanFactory<O, T, E, V> {

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = BeanMapContainer.class;

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
        return "MapContainerAnnotationBeanFactory{" +
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
        BeanMapContainer container = this.getAnnotation(event);
        //获取注释函数
        Method method = ReflectUtil.findMethod(event.getTarget(), BeanMapContainer.Name.class);
        try {
            //判断是否无效处理引发错误
            if (container.error()) {
                if (method == null) {
                    event.getExecuteOwner().getBean(container.value()).put(event.getTarget().getName(), event.getValue());
                } else {
                    event.getExecuteOwner().getBean(container.value()).put(method.getAnnotation(BeanMapContainer.Name.class).prefix() + method.invoke(event.getValue(), event.getExecuteOwner().newInstanceParameters(event.getValue(), method)).toString(), event.getValue());
                }
                if (this.getLog().isDebugEnabled()) {
                    this.getLog().debug("loader:" + container.value().getName() + " add(" + event.getValue().getClass().getName() + ")");
                }
                return;
            }
            //处理可以忽略找不到容器接口
            Map map = event.getExecuteOwner().getNullableBean(container.value());
            if (map != null) {
                if (method == null) {
                    event.getExecuteOwner().getBean(container.value()).put(event.getTarget().getName(), event.getValue());
                } else {
                    event.getExecuteOwner().getBean(container.value()).put(method.getAnnotation(BeanMapContainer.Name.class).prefix() + method.invoke(event.getValue(), event.getExecuteOwner().newInstanceParameters(event.getValue(), method)).toString(), event.getValue());
                }
                if (this.getLog().isDebugEnabled()) {
                    this.getLog().debug("loader:" + container.value().getName() + " add(" + event.getValue().getClass().getName() + ")");
                }
                return;
            }
        } catch (Exception e) {
            if (this.getLog().isDebugEnabled()) {
                e.printStackTrace();
                this.getLog().debug(e.getMessage(), e);
            } else {
                this.getLog().error(e.getMessage(), e);
            }
            throw new BeanException(e);
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
        BeanMapContainer container = (BeanMapContainer) event.getTarget().getAnnotation(this.annotation);
        //判断是否无效处理引发错误
        if (container.error()) {
            event.getExecuteOwner().getBean(container.value()).remove(event.getExecuteOwner().findAnnotationMethodInvoke(event.getValue(), BeanMapContainer.Name.class).toString());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("unloader:" + container.value().getName() + " remove(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
        //处理可以忽略找不到容器接口
        Map map = event.getExecuteOwner().getNullableBean(container.value());
        if (map != null) {
            map.remove(event.getExecuteOwner().findAnnotationMethodInvoke(event.getValue(), BeanMapContainer.Name.class).toString());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("unloader:" + container.value().getName() + " remove(" + event.getValue().getClass().getName() + ")");
            }
            return;
        }
    }
}