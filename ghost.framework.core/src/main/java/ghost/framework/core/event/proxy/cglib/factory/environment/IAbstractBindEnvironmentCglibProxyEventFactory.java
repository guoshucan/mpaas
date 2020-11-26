package ghost.framework.core.event.proxy.cglib.factory.environment;

import ghost.framework.context.environment.IEnvironment;
import ghost.framework.core.event.proxy.cglib.factory.IAbstractCglibProxyEventFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 17:44 2020/1/2
 */
public interface IAbstractBindEnvironmentCglibProxyEventFactory<O, T, C extends Class<?>, R> extends IAbstractCglibProxyEventFactory<O, T, C, R> {
    /**
     * 获取绑定env对象
     *
     * @return
     */
    IEnvironment getEnvironment();
}