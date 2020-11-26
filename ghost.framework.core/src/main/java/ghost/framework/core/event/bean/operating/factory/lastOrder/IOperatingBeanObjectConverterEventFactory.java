package ghost.framework.core.event.bean.operating.factory.lastOrder;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.core.event.bean.operating.factory.IOperatingBeanAnnotationEventFactory;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.bean.operating.factory.lastOrder
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/4:19:52
 */
public interface IOperatingBeanObjectConverterEventFactory<O, T extends IBeanDefinition, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends IOperatingBeanAnnotationEventFactory<O, T, E> {
}