package ghost.framework.core.event.proxy.cglib.factory;

import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;

/**
 * package: ghost.framework.core.event.proxy.cglib.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019/12/31:21:05
 */
public interface ICglibProxyEventFactoryContainer<O, T, C extends Class<?>, L extends ICglibProxyEventFactory<O, T, C, R>, E extends IBeanTargetHandle<O, T>, R> extends IBeanFactoryContainer<L> {
    /**
     * 获取级
     *
     * @return
     */
    ICglibProxyEventFactoryContainer<O, T, C, L, E, R> getParent();
}