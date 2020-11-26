//package ghost.framework.core.proxy.cglib;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//import org.apache.log4j.Logger;
//
//import java.lang.reflect.Method;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:cglib代理拦截器类
// * @Date: 4:29 2019/11/23
// */
//public class CglibProxyFactory implements MethodInterceptor {
//    public CglibProxyFactory(Object target){
//        this.target = target;
//    }
//    private Object target;
//
//
//
//    private Logger logger = Logger.getLogger(CglibProxyFactory.class);
//    /**
//     * 函数调用前处理
//     *
//     * @param o
//     * @param method
//     * @param objects
//     * @param methodProxy
//     */
//    private void before(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("before:" + o.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length));
//        }
//    }
//
//    @Override
//    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//        Object r = null;
//        Exception exception = null;
//        try {
//            this.before(o, method, objects, methodProxy);
//            r = methodProxy.invokeSuper(o, objects);
//        } catch (Exception e) {
//            exception = e;
//            this.throwing(o, method, objects, methodProxy, e);
//        } finally {
//            this.after(o, method, objects, methodProxy, exception);
//        }
//        return r;
//    }
//
//    /**
//     * 函数调用后处理
//     *
//     * @param o
//     * @param method
//     * @param objects
//     * @param methodProxy
//     * @param exception   是否调用过程有出现错误，如果未出现错误为null
//     */
//    private void after(Object o, Method method, Object[] objects, MethodProxy methodProxy, Exception exception) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("after:" + o.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length) + ">exception:" + (exception == null ? "" : exception.toString()));
//        }
//    }
//
//    /**
//     * 调用函数错误
//     *
//     * @param o
//     * @param method
//     * @param objects
//     * @param methodProxy
//     * @param exception   错误对象
//     */
//    private void throwing(Object o, Method method, Object[] objects, MethodProxy methodProxy, Exception exception) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("throwing:" + o.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length) + ">exception:" + (exception == null ? "" : exception.toString()));
//        }
//    }
//}