package ghost.framework.context.io;
import ghost.framework.util.Assert;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * package: ghost.framework.web.module.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源域
 * @Date: 2020/3/14:17:37
 */
public class ResourceDomain implements IResourceDomain {
    /**
     * @param domain
     * @param url
     * @param paths
     */
    public ResourceDomain(Object domain, Class<? extends Annotation> annotationDomain, URL url, String[] paths, String virtualPath) {
        Assert.notNull(domain, "ResourceDomain in domain null error");
        Assert.notNull(domain, "ResourceDomain in annotationDomain null error");
        Assert.notNull(url, "ResourceDomain in url null error");
        Assert.notNull(paths, "ResourceDomain in paths null error");
        this.domain = domain;
        this.annotationDomain = annotationDomain;
        this.url = url;
        this.paths = paths;
        if (StringUtils.isEmpty(virtualPath)) {
            this.virtualPath = null;
        } else {
            if (virtualPath.startsWith("/")) {
                this.virtualPath = virtualPath;
            } else {
                this.virtualPath = "/" + virtualPath;
            }
        }
    }

    /**
     * @param domain
     * @param url
     * @param path
     */
    public ResourceDomain(Object domain, Class<? extends Annotation> annotationDomain, URL url, String path, String virtualPath) {
        this(domain, annotationDomain, url, new String[]{path}, virtualPath);
    }

    /**
     * url虚拟路径
     */
    private final String virtualPath;

    /**
     * 获取url虚拟路径
     *
     * @return
     */
    @Override
    public String getVirtualPath() {
        return virtualPath;
    }

    /**
     * 资源域注释域
     */
    private final Class<? extends Annotation> annotationDomain;

    /**
     * 获取注释域
     *
     * @return 返回注释域注释类型
     */
    @Override
    public Class<? extends Annotation> getAnnotationDomain() {
        return annotationDomain;
    }

    /**
     * 资源加载缓存列表
     */
    private List<String> cacheList = new ArrayList<>(5);

    /**
     * 获取资源域缓存列表
     * 作为资源域缓存列表
     * 一般在资源域卸载的时候此列表作为卸载列表
     *
     * @return 返回资源域缓存列表
     */
    @Override
    public List<String> getCacheList() {
        return cacheList;
    }

    /**
     * 资源域域对象
     */
    private final Object domain;

    /**
     * 获取资源域域对象
     *
     * @return 返回资源域域对象
     */
    @Override
    public Object getDomain() {
        return domain;
    }

    /**
     * 资源域包url
     */
    private final URL url;

    /**
     * 获取资源域包url
     *
     * @return
     */
    @Override
    public URL getURL() {
        return url;
    }

    /**
     * 作为jar包的目录路径
     */
    private final String[] paths;

    /**
     * 获取jar包的目录路径
     *
     * @return
     */
    @Override
    public String[] getClassPaths() {
        return paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDomain that = (ResourceDomain) o;
        //有路径比对
        return domain.equals(that.domain) && annotationDomain.equals(that.annotationDomain) &&
                url.equals(that.url) &&
                Arrays.equals(paths, that.paths) && Objects.equals(virtualPath, that.virtualPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, annotationDomain, url, paths, virtualPath);
    }

    @Override
    public String toString() {
        return "ResourceDomain{" +
                "domain=" + domain.toString() +
                "annotationDomain=" + annotationDomain.toString() +
                ", url=" + url.toString() +
                ", path=" + Arrays.toString(paths) +
                ", virtualPath=" + virtualPath +
                '}';
    }
}