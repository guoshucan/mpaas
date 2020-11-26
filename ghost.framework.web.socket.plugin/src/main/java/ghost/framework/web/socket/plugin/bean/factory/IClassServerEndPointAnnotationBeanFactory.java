package ghost.framework.web.socket.plugin.bean.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link javax.websocket.server.ServerEndpoint} 注释绑定工厂接口
 * @Date: 2020/1/27:19:56
 * @param <O>
 * @param <T>
 * @param <E>
 * @param <V>
 */
public interface IClassServerEndPointAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V>
{
}