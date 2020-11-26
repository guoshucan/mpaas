//package ghost.framework.core.proxy.javasist;
//import javassist.util.proxy.MethodHandler;
//import javassist.util.proxy.ProxyFactory;
//import javassist.util.proxy.ProxyObject;
//
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.core.proxy.javasist
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/11:23:16
// */
//public final class JavasistUtils {
//    public static <T> T create(Class<T> classs) throws Exception {
//        ProxyFactory factory = new ProxyFactory();
//        factory.setSuperclass(classs);
//        Class clazz = factory.createClass();
//        MethodHandler handler = new MethodHandler() {
//            private String value;
//            @Override
//            public Object invoke(Object self, Method overridden, Method forwarder, Object[] args) throws Throwable {
//                System.out.println("hit decorator");
//                return forwarder.invoke(self, args);
//            }
//        };
//        Object instance = clazz.newInstance();
//        ((ProxyObject) instance).setHandler(handler);
//        return (T) instance;
//    }
//}
