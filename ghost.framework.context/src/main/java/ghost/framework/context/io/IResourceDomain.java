package ghost.framework.context.io;

import ghost.framework.context.IGetAnnotationDomain;
import ghost.framework.context.IGetDomain;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * package: ghost.framework.web.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源域接口，作为jar的包信息域
 * @Date: 2020/3/14:17:35
 */
public interface IResourceDomain extends IGetDomain, IGetAnnotationDomain {
    /**
     * 获取资源域缓存列表
     * 作为资源域缓存列表
     * 一般在资源域卸载的时候此列表作为卸载列表
     * @return 返回资源域缓存列表
     */
    default List<String> getCacheList() {
        throw new UnsupportedOperationException(IResourceDomain.class.getName() + "#getCacheList()");
    }

    /**
     * 获取资源解析器所在的域
     * 一般作为jar的域所在位置
     * {@link AccessControlContext::getContext()}
     *
     * @return
     */
    default ProtectionDomain getProtectionDomain() {
        throw new UnsupportedOperationException(IResourceDomain.class.getName() + "#getProtectionDomain()");
    }

    /**
     * 获取资源域
     * 资源所属于拥有者的域
     *
     * @return
     */
    @Override
    default Object getDomain() {
        throw new UnsupportedOperationException(IResourceDomain.class.getName() + "#getDomain()");
    }

    /**
     * 获取资源URL
     *
     * @return
     * @throws IOException
     */
    URL getURL();
    /**
     * 获取url虚拟路径
     * @return
     */
    String getVirtualPath();
    /**
     * 获取路径
     *
     * @return
     */
    String[] getClassPaths();
    /**
     * 资源文件
     */
    default File getFile() throws IOException {
        throw new UnsupportedOperationException(IResourceDomain.class.getName() + "#getFile()");
    }
}