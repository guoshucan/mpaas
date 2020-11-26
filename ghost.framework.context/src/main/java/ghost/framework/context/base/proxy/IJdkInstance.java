package ghost.framework.context.base.proxy;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * package: ghost.framework.context.base.proxy
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:jdk代理构建接口，一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
 * @Date: 2020/2/28:13:07
 */
public interface IJdkInstance extends IGetClassLoader {
    /**
     * 使用Jdk代理创建类型对象
     *
     * @param className 构建类型名称
     * @param handler   代理回调对象
     * @param <T>       返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newJdkInstance(@NotNull String className, @NotNull InvocationHandler handler) throws ClassNotFoundException {
        return this.newJdkInstance(new Class[]{this.getClassLoader().loadClass(className)}, handler);
    }

    /**
     * 使用Jdk代理创建类型对象
     *
     * @param classNames 构建类型名称
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newJdkInstance(@NotNull String[] classNames, @NotNull InvocationHandler handler) throws ClassNotFoundException {
        Assert.isTrue(classNames == null || classNames.length == 0, "newJdkInstance null classNames error");
        Class[] classes = new Class[classNames.length];
        int i = 0;
        for (String className : classNames) {
            classes[i] = this.getClassLoader().loadClass(className);
            i++;
        }
        return this.newJdkInstance(classes, handler);
    }

    /**
     * 使用Jdk代理创建类型对象
     *
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJdkInstance(@Nullable Class<?>[] interfaces, @NotNull InvocationHandler handler) {
        Assert.notNull(interfaces, "newJdkInstance null interfaces error");
        Assert.notNull(handler, "newJdkInstance null handler error");
        return this.createJdkEnhancer(interfaces, handler);
    }

    /**
     * 使用Jdk代理创建类型对象
     *
     * @param c       构建类型
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJdkInstance(@NotNull Class<?> c, @NotNull InvocationHandler handler) {
        return this.newJdkInstance(new Class[]{c}, handler);
    }

    /**
     * 创建Cglib代理对象
     *
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回代理构建对象
     */
    default <T> T createJdkEnhancer(@Nullable Class<?>[] interfaces, @NotNull InvocationHandler handler) {
        return (T) Proxy.newProxyInstance((ClassLoader) this.getClassLoader(), interfaces, handler);
    }
}