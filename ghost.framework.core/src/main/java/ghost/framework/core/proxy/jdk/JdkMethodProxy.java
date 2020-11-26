package ghost.framework.core.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:jdk函数代理
 * @Date: 4:53 2019/11/23
 */
public class JdkMethodProxy implements InvocationHandler {


    /**
     * 调用函数
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //判断是否调用类函数
        if (Object.class.equals(method.getDeclaringClass())) {
            //调用类函数
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        //调用接口函数
        return this.invokeInterface(method, args);
    }

    /**
     * 调用接口函数
     * @param method
     * @param args
     * @return
     */
    private Object invokeInterface(Method method, Object[] args) {
        return null;
    }
}