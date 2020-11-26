package ghost.framework.web.context.bens.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;

import javax.servlet.annotation.WebListener;

/**
 * package: ghost.framework.web.context.bens.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link WebListener} 注释绑定工厂接口
 * @Date: 2020/5/2:16:33
 */
public interface IClassWebListenerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IWebClassAnnotationBeanFactory<O, T, E, V>
{
}