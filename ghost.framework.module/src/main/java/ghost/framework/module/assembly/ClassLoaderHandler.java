package ghost.framework.module.assembly;//package ghost.framework.module.assembly;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 23:24 2019/6/10
// */
//public final class ClassLoaderHandler implements InvocationHandler {
//    public ClassLoaderHandler(Object object) {
//        this.object = object;
//    }
//
//    private Object object;
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        Object result = null;
//        this.Before();
//        result = method.invoke(object, args);
//        this.After();
//        return result;
//    }
//
//    private void After() {
//        System.out.println("After");
//    }
//
//    private void Before() {
//        System.out.println("Before");
//    }
//}