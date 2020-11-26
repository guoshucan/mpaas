//package ghost.framework.context.io;
//
//import ghost.framework.context.IGetDomain;
//import ghost.framework.util.Assert;
//
//import java.net.URL;
//import java.util.Arrays;
//import java.util.Objects;
//
///**
// * package: ghost.framework.web.context.io
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:资源键
// * @Date: 2020/3/15:18:48
// */
//public class ResourceKey implements IGetDomain {
//    /**
//     * 使用url路径作为key参数的资源构建类型
//     *
//     * @param domain 所属域对象
//     * @param key
//     */
//    public ResourceKey(Object domain, String... key) {
//        Assert.notNull(domain, "ResourceKey in domain null error");
//        Assert.notNullOrEmpty(key, "ResourceKey in key null error");
//        this.key = key;
//        this.domain = domain;
//        this.url = null;
//    }
//
//    /**
//     * 使用jar包的ProtectionDomain作为key参数的资源构建类型
//     *
//     * @param domain 所属域对象
//     * @param url
//     */
//    public ResourceKey(Object domain, URL url) {
//        Assert.notNull(domain, "ResourceKey in domain null error");
//        Assert.notNull(url, "ResourceKey in url null error");
//        this.key = null;
//        this.domain = domain;
//        this.url = url;
//    }
//
//    private final URL url;
//
//    public URL getUrl() {
//        return url;
//    }
//
//    public final String[] key;
//
//    public String[] getKey() {
//        return key;
//    }
//
//    private final Object domain;
//
//    @Override
//    public Object getDomain() {
//        return domain;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ResourceKey that = (ResourceKey) o;
//        if (url == null && that.url == null) {
//            return Arrays.equals(key, that.key) &&
//                    domain.equals(that.domain);
//        }
//        if (url != null && that.url == null) {
//            return false;
//        }
//        if (url == null && that.url != null) {
//            return false;
//        }
//        return url.equals(that.url) && domain.equals(that.domain);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(key, domain, url);
//    }
//
//    @Override
//    public String toString() {
//        return "ResourceKey{" +
//                "protectionDomain=" + (url == null ? "" : url.toString()) +
//                ", key='" + (key == null ? "" : Arrays.toString(key)) + '\'' +
//                ", domain=" + domain.toString() +
//                '}';
//    }
//}