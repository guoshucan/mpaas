package ghost.framework.context.proxy;

/**
 * package: ghost.framework.context.proxy
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/12:13:04
 */
public interface IProxyInfo {
    static IProxyInfo createCglib() {
        return new ProxyInfo(ProxyType.Cglib);
    }
    ProxyType getProxyType();
    void setProxyType(ProxyType proxyType);
}