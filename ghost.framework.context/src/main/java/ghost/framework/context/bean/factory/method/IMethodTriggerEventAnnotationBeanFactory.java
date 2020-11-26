package ghost.framework.context.bean.factory.method;

import ghost.framework.beans.event.annotation.TriggerEvent;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.method.factory.lastOrder
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link TriggerEvent} 列表容器事件工厂接口
 * @Date: 19:18 2020/2/2
 */
public interface IMethodTriggerEventAnnotationBeanFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends MethodAnnotationBeanFactory<O, T, E> {
}