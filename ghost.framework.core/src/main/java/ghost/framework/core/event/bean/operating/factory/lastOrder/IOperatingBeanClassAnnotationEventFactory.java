package ghost.framework.core.event.bean.operating.factory.lastOrder;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.core.event.bean.operating.factory.IOperatingBeanAnnotationEventFactory;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.bean.operating.factory.lastOrder
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link ClassAnnotationBeanFactory} 列表容器事件工厂接口
 * @Date: 2020/1/31:17:16
 */
public interface IOperatingBeanClassAnnotationEventFactory<O, T extends IBeanDefinition, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends IOperatingBeanAnnotationEventFactory<O, T, E> {
}