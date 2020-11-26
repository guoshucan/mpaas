package ghost.framework.core.proxy.cglib;

import net.sf.cglib.proxy.InterfaceMaker;

import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:cglib代理工具类
 * @Date: 12:32 2019/11/23
 */
public final class CglibUtil {
    /**
     * 按照类型创建接口
     * @param c 指定要创建接口的类型
     * @return
     */
    public static Class<?> createInterfaceMaker(Class<?> c) {
        InterfaceMaker im = new InterfaceMaker();
        im.add(c);
        return im.create();
    }
    /**
     * 按照函数列表创建接口
     * @param methods 指定函数列表
     * @return
     */
    public static Class<?> createInterfaceMaker(Method[] methods) {
        InterfaceMaker im = new InterfaceMaker();
        for (Method m : methods) {
            im.add(m);
        }
        return im.create();
    }
    /**
     * 按照函数创建接口
     * @param method 指定函数
     * @return
     */
    public static Class<?> createInterfaceMaker(Method method) {
        InterfaceMaker im = new InterfaceMaker();
        im.add(method);
        return im.create();
    }
}
