package ghost.framework.context.bean.factory.method;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.method.factory.order
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/14:8:39
 */
public interface IMethodLoaderAnnotationBeanFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends MethodAnnotationBeanFactory<O, T, E> {
}
