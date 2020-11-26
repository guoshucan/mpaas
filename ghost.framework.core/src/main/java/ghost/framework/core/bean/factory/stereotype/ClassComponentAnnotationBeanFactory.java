package ghost.framework.core.bean.factory.stereotype;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.stereotype.IClassComponentAnnotationBeanFactory;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.core.event.annotation.factory.entrance
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link Component} 注释事件工厂类
 * @Date: 2020/1/10:15:32
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
//@Component
public class ClassComponentAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassComponentAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = Component.class;

    /**
     * 重新注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassComponentAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 绑定事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
        Component component = this.getAnnotation(event);
        //绑定依赖类型
        for (Class<?> c : component.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        //构建类型实例
        this.newInstance(event);
        //绑定入容器中
        event.getExecuteOwner().addBean(event.getValue());
    }

    /**
     * 删除绑定事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        this.getLog().info("unloader:" + event.toString());
        //获取注入注释对象
        Component component = event.getTarget().getAnnotation(Component.class);
        //获取核心接口
        this.positionOwner(event);
//        //构建类型
//        event.setValue((V) event.getExecuteOwner().getBean((Class<?>) event.getTarget()));
//        //锁定呀删除的对象
//        synchronized (event.getValue()) {
//            //
//            for (Class<?> c : component.depend()) {
//                event.getExecuteOwner().removeBean(c);
//            }
//            //删除本身
//            event.getExecuteOwner().removeBean(event.getTarget());
//        }
//        //添加加载注释排除
//        event.getExcludeAnnotationList().add(this.annotation);
    }
}