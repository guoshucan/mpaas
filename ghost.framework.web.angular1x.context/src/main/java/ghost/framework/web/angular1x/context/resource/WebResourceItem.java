package ghost.framework.web.angular1x.context.resource;

import ghost.framework.util.Assert;
import ghost.framework.beans.resource.annotation.Resource;

import java.net.URL;
import java.util.*;

/**
 * package: ghost.framework.web.angular1x.context.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web资源对象，按照一个包一个对应资源对象
 * @Date: 2020/3/13:18:21
 */
public class WebResourceItem extends AbstractMap<String, IWebResourceFile> implements IWebResourceItem {
    private Map<String, IWebResourceFile> map = new HashMap<>();

    @Override
    public Set<Entry<String, IWebResourceFile>> entrySet() {
        return map.entrySet();
    }

    @Override
    public IWebResourceFile put(String key, IWebResourceFile value) {
        return map.put(key, value);
    }

    /**
     * 初始化web资源对象，按照一个包一个对应资源对象
     *
     * @param resource     包资源注释
     * @param resourceURL  包路径
     * @param resourcePath 包资源路径
     */
    public WebResourceItem(Resource resource, URL resourceURL, String resourcePath) {
        Assert.notNull(resource, "WebResourceItem in resource null error");
        Assert.notNull(resourceURL, "WebResourceItem in resourceURL null error");
        Assert.notNullOrEmpty(resourcePath, "WebResourceItem in resourcePath null error");
        this.resource = resource;
        this.resourceURL = resourceURL;
        this.resourcePath = resourcePath;
    }

    /**
     * 资源路径
     * 来自 {@link Resource#value()} 注释的值
     */
    private String resourcePath;

    /**
     * 获取资源注释的路径
     *
     * @return
     */
    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public Resource getWebResource() {
        return resource;
    }

    private final Resource resource;
    private final URL resourceURL;

    /**
     * 获取资源所在的url
     *
     * @return
     */
    @Override
    public URL getResourceURL() {
        return resourceURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebResourceItem that = (WebResourceItem) o;
        return resourcePath.equals(that.resourcePath) &&
                resource.equals(that.resource) &&
                resourceURL.equals(that.resourceURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourcePath, resource, resourceURL);
    }

    @Override
    public String toString() {
        return "WebResourceItem{" +
                "resourcePath='" + resourcePath + '\'' +
                ", resource=" + resource.toString() +
                ", resourceURL=" + resourceURL.toString() +
                '}';
    }
}