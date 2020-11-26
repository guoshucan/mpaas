//package ghost.framework.module.util;
//
//import ghost.framework.module.reflect.ClassNotFoundEventListener;
//
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.net.URLStreamHandlerFactory;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:依赖加载处理缺包问题。
// * @Date: 22:48 2018-07-03
// */
//public final class RelyURLClassLoader extends URLClassLoader {
//
//    private ClassNotFoundEventListener foundEventListener;
//
//    public RelyURLClassLoader(URL[] urls, ClassLoader parent, ClassNotFoundEventListener foundEventListener) {
//        super(urls, parent);
//        this.foundEventListener = foundEventListener;
//    }
//
//    /**
//     * 设置监听缺包事件。
//     *
//     * @param foundEventListener
//     */
//    public void setFoundEventListener(ClassNotFoundEventListener foundEventListener) {
//        this.foundEventListener = foundEventListener;
//    }
//
//    public RelyURLClassLoader(URL[] urls, ClassLoader parent) {
//        super(urls, parent);
//    }
//
//    public RelyURLClassLoader(URL[] urls) {
//        super(urls);
//    }
//
//    public RelyURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
//        super(urls, parent, factory);
//    }
//
//    @Override
//    protected Class<?> findClass(String value) throws ClassNotFoundException {
//        try {
//            return super.findClass(value);
//        } catch (ClassNotFoundException e) {
//            //处理缺包问题。
//            if (this.foundEventListener == null) {
//                throw new ClassNotFoundException(value);
//            } else {
//                this.foundEventListener.classNotFound(value);
//            }
//        }
//        return null;
//    }
//}
