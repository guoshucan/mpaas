package ghost.framework.context.bean.factory.configuration;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactory;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.context.event.method.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/18:21:40
 */
public interface IMethodConfigurationPropertiesAnnotationEventFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends MethodAnnotationBeanFactory<O, T, E> {
}
