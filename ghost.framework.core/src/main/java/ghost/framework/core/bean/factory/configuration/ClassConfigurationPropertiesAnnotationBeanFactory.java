package ghost.framework.core.bean.factory.configuration;

import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.configuration.IClassConfigurationPropertiesAnnotationBeanFactory;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.core.bean.factory.configuration
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link ConfigurationProperties} 注释事件工厂类
 * @Date: 12:33 2020/1/11
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
@Component
public class ClassConfigurationPropertiesAnnotationBeanFactory<
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassConfigurationPropertiesAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = ConfigurationProperties.class;

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
        return "ClassConfigurationPropertiesAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
        ConfigurationProperties properties = this.getAnnotation(event);
    }
    /**
     * 删除绑定
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        //注释类型不对与已经执行的注释时错误
        this.isLoader(event);
        this.getLog().info("unloader:" + event.toString());
    }
}