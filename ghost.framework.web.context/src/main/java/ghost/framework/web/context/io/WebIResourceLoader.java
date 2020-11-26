package ghost.framework.web.context.io;

import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.io.IResourceLoader;

/**
 * package: ghost.framework.web.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web全局资源加载器接口
 * @Date: 2020/3/14:15:08
 */
public interface WebIResourceLoader<V> extends IResourceLoader, ISmartList<V> {
    /**
     * 重写返回web资源接口
     *
     * @param path 资源路径
     * @return 返回web资源接口
     */
    @Override
    IWebResource getResource(String path);
}