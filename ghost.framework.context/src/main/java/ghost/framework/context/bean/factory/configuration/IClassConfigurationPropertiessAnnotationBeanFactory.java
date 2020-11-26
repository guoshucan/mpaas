package ghost.framework.context.bean.factory.configuration;

import ghost.framework.beans.annotation.configuration.properties.ConfigurationPropertiess;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;

/**
 * package: ghost.framework.core.bean.factory.configuration
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link ConfigurationPropertiess} 注释事件工厂接口
 * @Date: 12:44 2020/1/11
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
public interface IClassConfigurationPropertiessAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V
        >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
}