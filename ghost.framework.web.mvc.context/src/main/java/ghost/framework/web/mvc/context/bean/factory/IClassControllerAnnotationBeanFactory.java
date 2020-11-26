package ghost.framework.web.mvc.context.bean.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.context.bens.factory.ICglibWebClassAnnotationBeanFactory;
import ghost.framework.web.mvc.context.bind.annotation.Controller;

/**
 * package: ghost.framework.web.mvc.context.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理 {@link Controller} 注释事件工厂接口
 * @Date: 13:21 2020/1/31
 */
public interface IClassControllerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends ICglibWebClassAnnotationBeanFactory<O, T, E, V> {
}