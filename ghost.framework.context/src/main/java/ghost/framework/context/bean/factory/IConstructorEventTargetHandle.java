package ghost.framework.context.bean.factory;

import java.lang.reflect.Constructor;

/**
 * package: ghost.framework.context.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/10:0:35
 */
public interface IConstructorEventTargetHandle
        <O, T, C extends Constructor, P>
        extends IParametersBeanTargetHandle<O, T, P>,
        IAnnotationBeanTargetHandle<O, T>,
        IOwnerBeanTargetHandle<O, T>,
        IBeanTargetHandle<O, T> {
    C getConstructor();
    void setConstructor(C constructor);
}