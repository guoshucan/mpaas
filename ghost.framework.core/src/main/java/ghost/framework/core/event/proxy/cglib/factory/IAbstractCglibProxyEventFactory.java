package ghost.framework.core.event.proxy.cglib.factory;

/**
 * package: ghost.framework.core.event.proxy.cglib.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019/12/31:22:20
 * @param <O> 事件拥有者对象
 * @param <T> 事件目标类型
 * @param <C> 事件目标类型
 * @param <R> 代理返回对象类型
 */
public interface IAbstractCglibProxyEventFactory<O, T, C extends Class<?>, R> extends ICglibProxyEventFactory<O, T, C, R> {
}