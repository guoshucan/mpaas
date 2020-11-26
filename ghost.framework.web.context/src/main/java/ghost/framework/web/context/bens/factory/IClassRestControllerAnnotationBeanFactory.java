package ghost.framework.web.context.bens.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.context.bind.annotation.RestController;

/**
 * package: ghost.framework.web.module.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理 {@link RestController} 注释事件工厂接口
 * @Date: 13:21 2020/1/31
 */
public interface IClassRestControllerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends ICglibWebClassAnnotationBeanFactory<O, T, E, V> {
}