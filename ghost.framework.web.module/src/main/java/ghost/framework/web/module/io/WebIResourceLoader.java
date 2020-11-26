package ghost.framework.web.module.io;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.io.IResourceClassLoader;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.context.io.ResourceBytes;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.web.context.io.IWebResource;
import ghost.framework.web.context.io.WebResource;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
/**
 * package: ghost.framework.web.module.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web资源加载器
 * web资源加载接口
 * {@link IResourceClassLoader}
 * {@link IResourceClassLoader#getClassLoader()}
 * {@link ghost.framework.web.context.io.WebIResourceLoader}
 * {@link ghost.framework.web.context.io.WebIResourceLoader#getResource(String)}
 * {@link ghost.framework.web.context.io.WebIResourceLoader#add(Object)}
 * {@link ghost.framework.web.context.io.WebIResourceLoader#remove(Object)}
 * 释放资源接口
 * {@link AutoCloseable}
 * {@link AutoCloseable#close()}
 * @Date: 2020/3/14:16:48
 * @param <V> 资源域，作为jar域资源域信息的对象
 *     {@link IResourceDomain}
 *     {@link IResourceDomain#getDomain()}
 *     {@link IResourceDomain#getFile()}
 *     {@link IResourceDomain#getClassPaths()}
 *     {@link IResourceDomain#getProtectionDomain()}
 *     {@link IResourceDomain#getURL()}
 */
@Component
public class WebIResourceLoader<V extends IResourceDomain>
        implements ghost.framework.web.context.io.WebIResourceLoader<V>, AutoCloseable {
    /**
     * 这里缓存的键是http请求的资源路径
     * 资源缓存地图
     */
    private Map<String, IWebResource> cacheMap = new HashMap<>(100);
    /**
     * 资源域地图
     */
    private IResourceDomain[] list = new IResourceDomain[0];

    /**
     * 添加资源域
     *
     * @param v 资源域
     * @return 返回是否有效添加资源域
     */
    @Override
    public boolean add(V v) {
        boolean is;
        List<IResourceDomain> l = new ArrayList<>(Arrays.asList(list));
        is = l.add(v);
        if (is) {
            synchronized (list) {
                list = l.toArray(new IResourceDomain[l.size()]);
            }
        }
        return is;
    }

    /**
     * 删除资源域
     *
     * @param v 资源域
     * @return 返回是否有效删除资源域
     */
    @Override
    public boolean remove(V v) {
        boolean is;
        List<IResourceDomain> l = new ArrayList<>(Arrays.asList(list));
        is = l.remove(v);
        if (is) {
            synchronized (list) {
                list = l.toArray(new IResourceDomain[l.size()]);
            }
        }
        return is;
    }

    /**
     * 获取资源
     *
     * @param path 资源路径
     * @return 返回web资源接口
     */
    @Override
    public IWebResource getResource(String path) {
        //判断缓存是否有资源文件
        IWebResource r = cacheMap.get(path);
        if (r != null) {
            return r;
        }
        //缓存没有资源文件，遍历包获取资源文件
        for (IResourceDomain domain : list) {
            //声明资源接口
            ResourceBytes resource;
            try {
                //判断虚拟路径前缀
                if (domain.getVirtualPath() != null) {
                    if (!path.startsWith(domain.getVirtualPath())) {
                        continue;
                    }
                }
                //遍历包本地路径
                for (String s : domain.getClassPaths()) {
                    //验证包本地路径
                    if (StringUtils.isEmpty(s)) {
                        resource = AssemblyUtil.getResourceBytes(domain.getURL(), null, path);
                    } else {
                        resource = AssemblyUtil.getResourceBytes(domain.getURL(), s, path);
                    }
                    //判断是否有效资源
                    if (resource != null) {
                        //创建资源，并添加缓存
                        IWebResource webResource = new WebResource(path, resource.lastModified(), resource.getBytes(), domain);
                        cacheMap.put(path, webResource);
                        //添加资源域缓存路径
                        domain.getCacheList().add(path);
                        return webResource;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 释放资源
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {

    }

    @Override
    public int size() {
        return list.length;
    }

    @Override
    public boolean contains(V v) {
        return new ArrayList<>(Arrays.asList(list)).contains(v);
    }
}