package ghost.framework.module.assembly;

import ghost.framework.core.assembly.URLPaths;

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.security.AccessControlContext;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:53 2019/7/16
 */
final class ModuleURLPaths extends URLPaths {
    public ModuleURLPaths(URL[] urls, URLStreamHandlerFactory urlStreamHandlerFactory, AccessControlContext accessControlContext) {
        super(urls, urlStreamHandlerFactory, accessControlContext);
    }

    public ModuleURLPaths(URL[] urls) {
        super(urls);
    }

    public ModuleURLPaths(AccessControlContext accessControlContext) {
        super(accessControlContext);
    }

    public ModuleURLPaths(URL[] urls, AccessControlContext accessControlContext) {
        super(urls, accessControlContext);
    }


    public synchronized void addURL(ModuleClassLoader loader, URL url) {
        super.addURL(url);
    }


    public void remove(ModuleClassLoader loader, URL url) throws IllegalArgumentException, IllegalAccessException, IOException {
        super.remove(url);
    }
}