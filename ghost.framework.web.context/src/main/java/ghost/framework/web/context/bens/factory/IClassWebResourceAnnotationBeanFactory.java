package ghost.framework.web.context.bens.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;

/**
 * package: ghost.framework.web.context.bens.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/13:23:45
 */
public interface IClassWebResourceAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IWebClassAnnotationBeanFactory<O, T, E, V>
{
}