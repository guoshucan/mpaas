package ghost.framework.context.bean.factory.stereotype;

import ghost.framework.beans.annotation.stereotype.ApplicationBoot;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;

/**
 * package: ghost.framework.core.event.annotation.factory.applicationBoot
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link ApplicationBoot} 注释事件工厂接口
 * 作为运行类注释处理接口
 * @Date: 16:10 2020/1/15
 */
public interface IClassApplicationBootAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V>
{
}