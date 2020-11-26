package ghost.framework.context.module.thread;

import ghost.framework.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块线程上下文
 * @Date: 12:11 2019/12/15
 */
public final class ModuleThreadLocal {
    /**
     * 模块线程上下文
     */
    private static ThreadLocal<IModuleThread> local = new ThreadLocal<IModuleThread>();

    /**
     * 设置模块线程上下文
     *
     * @param thread
     */
    public static void set(IModuleThread thread) {
        local.set(thread);
    }

    /**
     * 删除模块线程上线文
     */
    public static void remove() {
        local.remove();
    }

    /**
     * 删除指定线程
     *
     * @param thread
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void remove(Thread thread) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method method = ReflectUtil.findMethod(ThreadLocal.class, "getMap");
        method.setAccessible(true);
        Object localMap = method.invoke(local, new Object[]{thread});
        method = ReflectUtil.findMethod(localMap.getClass(), "remove");
        method.setAccessible(true);
        method.invoke(localMap, new Object[]{thread});
    }

    /**
     * 获取模块线程上下文
     *
     * @return
     */
    public static IModuleThread get() {
        return local.get();
    }
}