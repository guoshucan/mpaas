package ghost.framework.core.event.bean.operating.factory;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.bean.operating.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/16:16:47
 */
public interface IOperatingBeanApplicationEventProcessorAnnotationEventFactory
        <O, T extends IBeanDefinition, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends IOperatingBeanAnnotationEventFactory<O, T, E>
{
}