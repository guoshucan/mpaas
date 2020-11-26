package ghost.framework.web.module.event.method;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactory;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.web.module.event.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/19:12:13
 */
public interface IMethodRequestMappingAnnotationEventFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends MethodAnnotationBeanFactory<O, T, E>
{
}
