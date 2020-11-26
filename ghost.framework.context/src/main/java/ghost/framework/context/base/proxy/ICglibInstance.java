package ghost.framework.context.base.proxy;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.context.proxy.cglib.CglibException;
import ghost.framework.util.Assert;
import ghost.framework.util.ReflectUtil;
import net.sf.cglib.proxy.Enhancer;
import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * package: ghost.framework.context.base.proxy
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:cglib代理基础构建对象接口
 * @Date: 2019/12/31:20:25
 */
public interface ICglibInstance extends IGetClassLoader {
    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className  构建类型名称
     * @param i          实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @NotNull Class<?> i, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        return this.newCglibInstance(this.getClassLoader().loadClass(className), new Class[]{i}, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className 构建类型名称
     * @param i         实现代理构建对象继承的接口
     * @param handler   代理回调对象
     * @param <T>       返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @NotNull Class<?> i, @NotNull net.sf.cglib.proxy.InvocationHandler handler) throws ClassNotFoundException {
        return this.newCglibInstance(this.getClassLoader().loadClass(className), new Class[]{i}, handler);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className  构建类型名称
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        return this.newCglibInstance(this.getClassLoader().loadClass(className), interfaces, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className  构建类型名称
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @Nullable String[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        Assert.isTrue(interfaces == null || interfaces.length == 0, "newCglibInstance null interfaces error");
        Class[] classes = new Class[interfaces.length];
        int i = 0;
        for (String ic : interfaces) {
            classes[i] = this.getClassLoader().loadClass(ic);
            i++;
        }
        return this.newCglibInstance(this.getClassLoader().loadClass(className), classes, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c          构建类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Class<?> c, @Nullable String[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        Assert.isTrue(interfaces == null || interfaces.length == 0, "newCglibInstance null interfaces error");
        Class[] classes = new Class[interfaces.length];
        int i = 0;
        for (String ic : interfaces) {
            classes[i] = this.getClassLoader().loadClass(ic);
            i++;
        }
        return this.newCglibInstance(c, classes, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className  构建类型名称
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler) throws ClassNotFoundException {
        return this.newCglibInstance(this.getClassLoader().loadClass(className), interfaces, handler);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className  构建类型名称
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        return this.newCglibInstance(this.getClassLoader().loadClass(className), new Class[]{}, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param className 构建类型名称
     * @param handler   代理回调对象
     * @param <T>       返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull String className, @NotNull net.sf.cglib.proxy.InvocationHandler handler) throws ClassNotFoundException {
        return this.newCglibInstance(this.getClassLoader().loadClass(className), new Class[]{}, handler);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c       构建类型
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Class<?> c, @NotNull net.sf.cglib.proxy.InvocationHandler handler) {
        return this.newCglibInstance(c, new Class[]{}, handler);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c          构建类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    <T> T newCglibInstance(@NotNull Class<?> c, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler);

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c       构建类型
     * @param i       实现代理构建对象继承的接口
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Class<?> c, @NotNull Class<?> i, @NotNull net.sf.cglib.proxy.InvocationHandler handler) {
        return this.newCglibInstance(c, new Class[]{i}, handler);
    }

    /**
     * 使用Cglib代理创建类型对象
     * 获取自定义参数构建函数
     *
     * @param c          构建类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    <T> T newCglibInstance(@NotNull Class<?> c, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters);

    /**
     * 使用Cglib代理创建类型对象
     * 获取自定义参数构建函数
     *
     * @param c          构建类型
     * @param i          实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Class<?> c, @NotNull Class<?> i, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) {
        return this.newCglibInstance(c, new Class[]{i}, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     * 获取自定义参数构建函数
     *
     * @param c          构建类型
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Class<?> c, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) {
        return this.newCglibInstance(c, new Class[]{}, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c          构建对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    <T> T newCglibInstance(@NotNull Constructor c, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters);

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c          构建对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    <T> T newCglibInstance(@NotNull Constructor c, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler);

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c          构建对象
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Constructor c, @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) {
        return this.newCglibInstance(c, new Class[]{}, handler, parameters);
    }

    /**
     * 使用Cglib代理创建类型对象
     *
     * @param c       构建对象
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newCglibInstance(@NotNull Constructor c, @NotNull net.sf.cglib.proxy.InvocationHandler handler) {
        return this.newCglibInstance(c, new Class[]{}, handler);
    }

    /**
     * 创建Cglib代理对象
     *
     * @param c             构建类型
     * @param interfaces    实现代理构建对象继承的接口
     * @param handler       代理回调对象
     * @param argumentTypes 指定构建数组参数类型
     * @param parameters    指定构建数组参数
     * @param <T>           返回类型
     * @return 返回代理构建对象
     */
    default <T> T createCglibEnhancer(@NotNull Class<?> c, @Nullable Class<?>[] interfaces,
                                      @NotNull net.sf.cglib.proxy.InvocationHandler handler,
                                      @NotNull Class<?>[] argumentTypes, @NotNull Object[] parameters) {
        //没有构建参数创建代理对象
        Enhancer enhancer = new Enhancer();
        //设置代理类型
        enhancer.setSuperclass(c);
        //判断是否有继承的接口类型
        if (interfaces != null && interfaces.length > 0) {
            //添加动态构建
            enhancer.setInterfaces(interfaces);
        }
        //判断是否需要序列化
        if (Serializable.class.isAssignableFrom(c)) {
            enhancer.setSerialVersionUID(this.getSerialVersionUID(c));
        }
        //设置类型加载器
        enhancer.setClassLoader((ClassLoader) this.getClassLoader());
        //设置代理回调接口
        enhancer.setCallback(handler);
        if (argumentTypes == null) {
            //返回构建器
            return (T) enhancer.create();
        }
        //返回构建器
        return (T) enhancer.create(argumentTypes, parameters);
    }

    /**
     * 创建代理器
     *
     * @param c          需要创建代理对象的类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回代理构建对象
     */
    default <T> T createCglibEnhancer(@NotNull Class<?> c, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler) {
        return this.createCglibEnhancer(c, interfaces, handler, null, null);
    }

    /**
     * 获取类型序列化值
     *
     * @param c 获取版本类型
     * @return 返回版本值
     */
    @NotNull
    default Long getSerialVersionUID(@NotNull Class<?> c) {
        try {
            return (Long) ReflectUtil.findStaticFieldValue(c, "serialVersionUID");
        } catch (Exception e) {
            throw new CglibException(e);
        }
    }
}