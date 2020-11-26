package ghost.framework.context.bean.factory.valid;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;

/**
 * package: ghost.framework.core.bean.factory.valid
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/1:20:59
 */
public interface IClassaValidAnnotationBeanFactory <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V
        >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
}