package ghost.framework.context.proxy;

/**
 * package: ghost.framework.context.proxy
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/12:13:05
 */
public class ProxyInfo implements IProxyInfo {

    public ProxyInfo(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    @Override
    public ProxyType getProxyType() {
        return proxyType;
    }

    private ProxyType proxyType = ProxyType.Cglib;

    @Override
    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }
}