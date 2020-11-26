package ghost.framework.context.proxy;

import ghost.framework.context.bean.factory.IBeanTargetHandle;

/**
 * package: ghost.framework.context.proxy
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/12:13:06
 */
public interface IProxyEventTargetHandle<O, T> extends IBeanTargetHandle<O, T> {
    IProxyInfo getProxyInfo();
    void setProxyInfo(IProxyInfo proxyInfo);
}