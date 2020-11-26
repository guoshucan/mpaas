package ghost.framework.web.module.event.annotation.interceptor.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;

/**
 * package: ghost.framework.web.module.event.annotation.interceptor
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理 {@link ghost.framework.web.context.bens.annotation.HttpInterceptor} 注释事件工厂接口
 * @Date: 2020/2/1:19:35
 */
public interface IClassHttpInterceptorAnnotationEventFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V>
{
}
