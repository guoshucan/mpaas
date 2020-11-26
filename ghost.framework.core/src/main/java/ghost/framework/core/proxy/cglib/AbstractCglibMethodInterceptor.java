//package ghost.framework.core.proxy.cglib;
//
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 11:35 2020/1/11
// */
//public abstract class AbstractCglibMethodInterceptor<R> implements MethodInterceptor {
//    /**
//     * 代理调用使用增强代理函数调用
//     *
//     * @param o           代理对象
//     * @param method      代理原始函数
//     * @param objects     调用代理函数数组参数
//     * @param methodProxy 增强函数代理调用
//     * @return 代理调用函数返回对象
//     * @throws Throwable
//     */
//    @Override
//    public  abstract R intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable ;
//}