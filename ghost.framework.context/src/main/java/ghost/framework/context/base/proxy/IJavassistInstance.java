package ghost.framework.context.base.proxy;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.asm.ParameterUtils;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.context.proxy.javassist.JavassistClassWrapperPool;
import ghost.framework.context.proxy.javassist.JavassistInstanceClassException;
import ghost.framework.context.proxy.javassist.JavassistTypeWrapper;
import ghost.framework.util.Assert;
import ghost.framework.util.CollectionUtils;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * package: ghost.framework.context.base.proxy
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:javassist代理构建接口，主要构建
 * @Date: 2020/5/13:5:53
 */
public interface IJavassistInstance extends IGetClassLoader {
    /**
     * 使用Javassist代理创建类型对象
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
    default <T> T newJavassistInstance(@NotNull String className, @NotNull Class<?> i, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), new Class[]{i}, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param className 构建类型名称
     * @param i         实现代理构建对象继承的接口
     * @param handler   代理回调对象
     * @param <T>       返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull String className, @NotNull Class<?> i, @NotNull javassist.util.proxy.MethodHandler handler) throws ClassNotFoundException {
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), new Class[]{i}, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
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
    default <T> T newJavassistInstance(@NotNull String className, @Nullable Class<?>[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), interfaces, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
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
    default <T> T newJavassistInstance(@NotNull String className, @Nullable String[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        Assert.isTrue(parameters == null || parameters.length == 0, "newJavassistInstance is parameters null error");
        Class[] classes = null;
        if (!CollectionUtils.isEmpty(interfaces)) {
            classes = new Class[interfaces.length];
            int i = 0;
            for (String ic : interfaces) {
                classes[i] = this.getClassLoader().loadClass(ic);
                i++;
            }
        }
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), classes, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
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
    default <T> T newJavassistInstance(@NotNull Class<?> c, @Nullable String[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        Assert.isTrue(CollectionUtils.isEmpty(parameters), "newJavassistInstance is parameters null error");
        return this.newJavassistInstanceConstructor(c, null, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param className  构建类型名称
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull String className, @Nullable Class[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler) throws ClassNotFoundException {
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), interfaces, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param className  构建类型名称
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull String className, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) throws ClassNotFoundException {
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), null, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param className 构建类型名称
     * @param handler   代理回调对象
     * @param <T>       返回类型
     * @return 返回创建的代理对象
     * @throws ClassNotFoundException {@link IClassLoader#loadClass(String)}
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull String className, @NotNull javassist.util.proxy.MethodHandler handler) throws ClassNotFoundException {
        return this.newJavassistInstanceConstructor(this.getClassLoader().loadClass(className), null, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c       构建类型
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Class<?> c, @NotNull javassist.util.proxy.MethodHandler handler) {
        return this.newJavassistInstanceConstructor(c, null, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c          构建类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Class<?> c, @Nullable Class[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler) {
        return this.newJavassistInstanceConstructor(c, interfaces, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c       构建类型
     * @param i       实现代理构建对象继承的接口
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Class<?> c, @NotNull Class<?> i, @NotNull javassist.util.proxy.MethodHandler handler) {
        return this.newJavassistInstanceConstructor(c, new Class[]{i}, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
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
    default <T> T newJavassistInstance(@NotNull Class<?> c, @Nullable Class[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) {
        return this.newJavassistInstanceConstructor(c, interfaces, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
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
    default <T> T newJavassistInstance(@NotNull Class<?> c, @NotNull Class<?> i, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) {
        return this.newJavassistInstanceConstructor(c, new Class[]{i}, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
     * 获取自定义参数构建函数
     *
     * @param c          构建类型
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Class<?> c, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) {
        return this.newJavassistInstanceConstructor(c, null, handler, null, null, parameters);
    }

    /**
     *
     * @param c
     * @param interfaces
     * @param handler
     * @param argumentTypes
     * @param argumentNames
     * @param parameters
     * @param <T>
     * @return
     */
    default <T> T newJavassistInstanceConstructor(@NotNull Class<?> c,
                                                  @Nullable Class[] interfaces,
                                                  @NotNull javassist.util.proxy.MethodHandler handler,
                                                  @Nullable Class[] argumentTypes,
                                                  @Nullable String[] argumentNames,
                                                  @Nullable Object[] parameters){
        Constructor[] constructors = c.getConstructors();
        if (!CollectionUtils.isEmpty(constructors)) {
            for (Constructor constructor : constructors) {
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    return this.createJavassistEnhancer(constructor, interfaces, handler, argumentTypes, argumentNames, parameters);
                }
            }
        }
        return this.createJavassistEnhancer(c, interfaces, handler, null, null, parameters);
    }
    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c          构建对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Constructor c, @Nullable Class[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) {
        return this.createJavassistEnhancer(c, interfaces, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c          构建对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Constructor c, @Nullable Class[] interfaces, @NotNull javassist.util.proxy.MethodHandler handler) {
        return this.createJavassistEnhancer(c, interfaces, handler, null, null, null);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c          构建对象
     * @param handler    代理回调对象
     * @param parameters 指定构建参数
     * @param <T>        返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Constructor c, @NotNull javassist.util.proxy.MethodHandler handler, @NotNull Object[] parameters) {
        return this.createJavassistEnhancer(c, null, handler, null, null, parameters);
    }

    /**
     * 使用Javassist代理创建类型对象
     *
     * @param c       构建对象
     * @param handler 代理回调对象
     * @param <T>     返回类型
     * @return 返回创建的代理对象
     */
    @NotNull
    default <T> T newJavassistInstance(@NotNull Constructor c, @NotNull javassist.util.proxy.MethodHandler handler) {
        return this.createJavassistEnhancer(c, null, handler, null, null, null);
    }

    /**
     * 创建Javassist代理对象
     *
     * @param constructor   构建
     * @param interfaces    实现代理构建对象继承的接口
     * @param handler       代理回调对象
     * @param argumentTypes 指定构建数组参数类型
     * @param argumentNames 指定构建数组参数名称
     * @param parameters    指定构建数组参数值
     * @param <T>           返回类型
     * @return 返回代理构建对象
     */
    default <T> T createJavassistEnhancer(@NotNull Constructor constructor,
                                          @Nullable Class[] interfaces,
                                          @NotNull javassist.util.proxy.MethodHandler handler,
                                          @Nullable Class[] argumentTypes,
                                          @Nullable String[] argumentNames,
                                          @Nullable Object[] parameters) {
        //判断构建函数参数类型与参数名称是否已经有，如果没有重新初始化参数类型与参数名称
        if (CollectionUtils.isEmpty(argumentTypes) && CollectionUtils.isEmpty(argumentNames)) {
            //获取构建数组参数名称
            argumentNames = ParameterUtils.getConstructorParamNames(constructor);
            //初始化构建数组参数类型
            argumentTypes = new Class<?>[parameters.length];
            int i = 0;
            for (Parameter parameter : constructor.getParameters()) {
                argumentTypes[i] = parameter.getType();
                i++;
            }
        }
        return createJavassistEnhancer(constructor.getDeclaringClass(), interfaces, handler, argumentTypes, argumentNames, parameters);
    }

    /**
     * 创建Javassist代理对象
     *
     * @param c             构建类型
     * @param interfaces    实现代理构建对象继承的接口
     * @param handler       代理回调对象
     * @param argumentTypes 指定构建数组参数类型
     * @param argumentNames 指定构建数组参数名称
     * @param parameters    指定构建数组参数值
     * @param <T>           返回类型
     * @return 返回代理构建对象
     */
    default <T> T createJavassistEnhancer(@NotNull Class<?> c,
                                          @Nullable Class[] interfaces,
                                          @NotNull javassist.util.proxy.MethodHandler handler,
                                          @Nullable Class[] argumentTypes,
                                          @Nullable String[] argumentNames,
                                          @Nullable Object[] parameters) {
        // 创建代理类的
        ProxyObject proxyObject = null;
        try {
            //构建类型
            JavassistClassWrapperPool classWrapperPool;
            if (CollectionUtils.isEmpty(parameters)) {
                classWrapperPool = JavassistTypeWrapper.createClassWrapperPool(c, interfaces);
            } else {
                classWrapperPool = JavassistTypeWrapper.createClassWrapperPool(c, interfaces, argumentTypes, argumentNames, parameters);
            }
            //初始化继承接口函数
            classWrapperPool.initMethod();
            //获取构建的类型
            Class newClass = classWrapperPool.createClassWrapper();
            // 创建代理工厂
            ProxyFactory proxyFactory = new ProxyFactory();
            // 设置被代理类的类型
            proxyFactory.setSuperclass(newClass);
//        if (interfaces != null || interfaces.length > 0) {
//            proxyFactory.setInterfaces(interfaces);
//        }
            Class<?> proxyClass = null;
            if (argumentTypes != null && argumentTypes.length > 0) {
                proxyObject = (ProxyObject) proxyFactory.create(argumentTypes, parameters, handler);
            } else {
                proxyClass = proxyFactory.createClass();
                proxyFactory.setHandler(handler);
            }
        } catch (Exception e) {
            throw new JavassistInstanceClassException(e);
        }
        //返回构建器
        return (T) proxyObject;
    }
}