package ghost.framework.core.proxy.cglib;
import net.sf.cglib.proxy.*;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.proxy.cglib
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:cglib代理类
 * @Date: 2019/12/31:20:50
 */
public class CglibProxy implements MethodInterceptor {
    /**
     * 创建代理对象
     * @param c
     * @return 返回代理对象
     */
    public Object creatProxyedObject(Class<?> c) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        enhancer.setCallback(this);
//        enhancer.setCallback(new LazyLoader() {
//            @Override
//            public Object loadObject() throws Exception {
////                System.out.println("prepare loading");
////                Car car = new Car();
////                car.value = "this is a car";
////                System.out.println("After loading");
//                return null;
//            }
//        });
//        enhancer.setCallback(new Dispatcher() {
//            @Override
//            public Object loadObject() throws Exception {
////                System.out.println("prepare loading");
////                Car car = new Car();
////                car.value = "this is a car";
////                System.out.println("After loading");
//                return null;
//            }
//        });
//        enhancer.setCallback(new InvocationHandler() {
//            @Override
//            public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
//                if (method.getReturnType() == void.class) {
//                    System.out.println("hack");
//                }
//                return null;
//            }
//        });
//        enhancer.setCallback(new FixedValue() {
//            @Override
//            public Object loadObject() throws Exception {
//                return "hack!";
//            }
//        });
        return enhancer.createClass();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(o, objects);
    }
}