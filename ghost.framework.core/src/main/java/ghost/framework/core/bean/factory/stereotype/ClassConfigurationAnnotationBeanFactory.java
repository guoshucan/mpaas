package ghost.framework.core.bean.factory.stereotype;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.configuration.IClassConfigurationAnnotationBeanFactory;
import ghost.framework.util.StringUtils;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.core.event.annotation.factory.entrance
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link Configuration} 注释事件工厂类
 * @Date: 2020/1/10:17:02
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
@Component
public class ClassConfigurationAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassConfigurationAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = Configuration.class;

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
        return "ClassConfigurationAnnotationBeanFactory{" +
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
        Configuration configuration = this.getAnnotation(event);
        //绑定依赖类型
        for (Class<?> c : configuration.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        //没有初始化时构建对象
        //构建类型实例
        this.newInstance(event);
        //判断服务注释是否指定名称
        if (configuration.name().equals("")) {
            //未指定绑定服务名称
        } else {
            //指定绑定的服务名称
            event.setName(configuration.name());
        }
        //绑定入容器中
        if (StringUtils.isEmpty(event.getName())) {
            event.getExecuteOwner().addBean(event.getValue());
        } else {
            event.getExecuteOwner().addBean(event.getName(), event.getValue());
        }
    }
    /**
     * 删除绑定事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        if (!event.getTarget().isAnnotationPresent(this.annotation)) {
            return;
        }
        this.getLog().info("unloader:" + event.toString());
        //获取注入注释对象
        Configuration configuration = (Configuration) event.getTarget().getAnnotation(this.annotation);
        //获取核心接口
        this.positionOwner(event);
        //构建类型
        event.setValue((V) event.getExecuteOwner().getBean((Class<?>) event.getTarget()));
        //判断服务注释是否指定名称
        if (configuration.name().equals("")) {
            //未指定绑定服务名称
        } else {
            //指定绑定的服务名称
            event.setName(configuration.name());
        }
        //删除依赖类型
        for (Class<?> c : configuration.depend()) {
            event.getExecuteOwner().removeBean(c);
        }
        //删除本身
        if (StringUtils.isEmpty(event.getName())) {
            event.getExecuteOwner().removeBean(event.getTarget());
        } else {
            event.getExecuteOwner().removeBean(event.getName());
        }
    }
}