package ghost.framework.web.angular1x.context.resource;


import ghost.framework.beans.resource.annotation.Resource;

import java.net.URL;
import java.util.Map;

/**
 * package: ghost.framework.web.angular1x.context.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:存储来自 {@link ghost.framework.web.context.bens.resource.WebResource} 注释的资源目录
 * @Date: 2020/3/13:18:20
 */
public interface IWebResourceItem extends Map<String, IWebResourceFile> {
    /**
     * 获取web资源注释
     *
     * @return
     */
    Resource getWebResource();

    /**
     * 获取资源所在的url
     *
     * @return
     */
    URL getResourceURL();

    /**
     * 获取资源注释的路径
     *
     * @return
     */
    String getResourcePath();
}