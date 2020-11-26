package ghost.framework.context.proxy;

/**
 * package: ghost.framework.web.module.proxy
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取代理目标对象接口
 * @Date: 2020/2/28:15:27
 */
public interface IProxyTarget<T> {
    /**
     * 获取代理目标对象
     * @return
     */
    T getTarget();

    /**
     * 设置代理目标对象
     * @param target
     */
    void setTarget(T target);
}
