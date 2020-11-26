package ghost.framework.context.bean;

import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.bean.exception.BeanClassNotFoundException;
import ghost.framework.beans.BeanException;
import ghost.framework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * package: ghost.framework.beans
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:基础绑定接口
 * @Date: 2019/12/10:0:21
 */
public interface IBean extends IContainsBean, IGetClassLoader {
    /**
     * 绑定事件
     *
     * @param o    绑定对象
     * @param name 绑定名称，如果未指定绑定名称侧为null
     */
    void beanEvent(Object o, String name);

    /**
     * 获取绑定地图
     *
     * @return
     */
    Map<String, IBeanDefinition> getBeanMap();

    /**
     * 获取绑定父级
     *
     * @return
     */
    default IBean getBeanParent() {
        return null;
    }

    /**
     * 获取是否自动绑定
     * 自动绑定主要是在类型调用getBean函数时如果未绑定在容器中时先绑定该类型后再返回该类型的绑定对象
     *
     * @return
     */
    boolean isAutoBean();

    /**
     * 带构建参数的绑定对象
     *
     * @param className 绑定类型名称
     * @param <T>
     * @return
     */
    default <T> T addBean(@NotNull String className) {
        Assert.notNullOrEmpty(className, "addBean null className error");
        try {
            return this.addBean(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new BeanClassNotFoundException(className, e);
        }
    }

    /**
     * 带构建参数的绑定对象
     *
     * @param className  绑定类型名称
     * @param parameters 数组绑定类型构建参数
     * @param <T>
     * @return
     */
    default <T> T addBean(@NotNull String className, @NotNull Object[] parameters) {
        Assert.notNullOrEmpty(className, "addBean null className error");
        Assert.notNull(parameters, "addBean null parameters error");
        try {
            return this.addBean(Class.forName(className), parameters);
        } catch (ClassNotFoundException e) {
            throw new BeanClassNotFoundException(className, e);
        }
    }

    /**
     * 带构建参数的绑定对象
     *
     * @param name      绑定名称
     * @param className 绑定类型名称
     * @param <T>
     * @return
     */
    default <T> T addBean(@NotNull String name, @NotNull String className) {
        Assert.notNullOrEmpty(name, "addBean null name error");
        Assert.notNullOrEmpty(className, "addBean null className error");
        try {
            return this.addBean(name, Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new BeanClassNotFoundException(className, e);
        }
    }

    /**
     * 带构建参数的绑定对象
     *
     * @param name       绑定名称
     * @param className  绑定类型名称
     * @param parameters 数组绑定类型构建参数
     * @param <T>
     * @return
     */
    default <T> T addBean(@NotNull String name, @NotNull String className, @NotNull Object[] parameters) {
        Assert.notNullOrEmpty(name, "addBean null name error");
        Assert.notNullOrEmpty(className, "addBean null className error");
        Assert.notNull(parameters, "addBean null parameters error");
        try {
            return this.addBean(name, Class.forName(className), parameters);
        } catch (ClassNotFoundException e) {
            throw new BeanClassNotFoundException(className, e);
        }
    }

    /**
     * 带构建参数的绑定对象
     *
     * @param name       绑定名称
     * @param c          绑定类型
     * @param parameters 数组绑定类型构建参数
     * @param <T>
     * @return
     */
    <T> T addBean(@NotNull String name, @NotNull Class<?> c, @NotNull Object[] parameters);

    /**
     * 带构建参数的绑定对象
     *
     * @param c          绑定类型
     * @param parameters 数组绑定类型构建参数
     * @param <T>
     * @return
     */
    <T> T addBean(@NotNull Class<?> c, @NotNull Object[] parameters);

    /**
     * 获取可空绑定类型，如果返回null不引发错误
     *
     * @param name
     * @param <T>
     * @return
     */
    <T> T getNullableBean(@NotNull String name);

    /**
     * 获取可空绑定类型，如果返回null不引发错误
     *
     * @param c
     * @param <T>
     * @return
     */
    <T> T getNullableBean(@NotNull Class<?> c);

    /**
     * 指定注释获取绑定对象
     *
     * @param a 注释类型
     * @return
     */
    <T> T getAnnotationBean(@NotNull Class<? extends Annotation> a);

    /**
     * @param a
     * @param <T>
     * @return
     */
    <T> List<T> getAnnotationBeanList(@NotNull Class<? extends Annotation> a);

    /**
     * @param a
     * @param <T>
     * @return
     */
    <T> T getAnnotationNullableBean(@NotNull Class<? extends Annotation> a);

    /**
     * 指定名称添加对象绑定定义
     *
     * @param name 绑定指定名称
     * @param o    绑定定义对象
     * @return
     */
    void addBean(@NotNull String name, @NotNull Object o);

    /**
     * 添加绑定类型
     *
     * @param c   绑定类型
     * @param <T> 返回类型
     * @return 返回添加绑定对象
     */
    <T> T addBean(@NotNull Class<?> c);

    /**
     * 添加绑定定义
     *
     * @param definition 绑定定义对象
     */
    void addBean(@NotNull IBeanDefinition definition);

    /**
     * 添加指定名称构建类型绑定
     *
     * @param name 绑定名称
     * @param c    绑定类型
     * @param <T>  返回类型
     * @return 返回绑定对象
     */
    <T> T addBean(@NotNull String name, @NotNull Class<?> c);

    /**
     * 获取绑定定义
     *
     * @param name 绑定id
     * @return
     */
    IBeanDefinition getBeanDefinition(@NotNull String name);

    /**
     * 获取绑定定义
     *
     * @param c 绑定类型
     * @return
     */
    IBeanDefinition getBeanDefinition(@NotNull Class<?> c);

    /**
     * 使用bean名称删除绑定
     *
     * @param name 绑定name
     */
    void removeBean(@NotNull String name);

    /**
     * 使用bean定义删除绑定
     *
     * @param definition 绑定定义
     */
    void removeBean(@NotNull IBeanDefinition definition);

    /**
     * 使用bean类型删除绑定
     *
     * @param c 绑定类型
     */
    void removeBean(@NotNull Class<?> c);

    /**
     * 获取绑定定义对象
     *
     * @param name 绑定id，如果没有指定绑定id侧为绑定类的全称
     * @return
     */
    <T> T getBean(@NotNull String name);

    /**
     * 添加绑定对象
     *
     * @param o 绑定对象
     */
    void addBean(@NotNull Object o);

    /**
     * 使用bean对象删除绑定
     *
     * @param o 绑定对象
     */
    void removeBean(@NotNull Object o);

    /**
     * 获取绑定类型对象
     *
     * @param c 绑定类型
     * @return
     */
    <T> T getBean(@NotNull Class<T> c);

    /**
     * 判断类型是否存在
     *
     * @param className 类型名称
     * @return 返回是否有类型
     */
    default boolean isPresent(@NotNull String className) {
        Assert.notNullOrEmpty(className, "isPresent null className error");
        try {
            this.forName(className);
            return true;
        } catch (IllegalAccessError error) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + error.getMessage(), error);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取类型
     *
     * @param className 类型名称
     * @return 返回类型
     */
    default Class<?> forName(@NotNull String className) {
        Assert.notNullOrEmpty(className, "forName null className error");
        try {
            return Class.forName(className, false, (ClassLoader) this.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new BeanException(className, e);
        }
    }

    /**
     * 获取绑定对象后调用函数
     *
     * @param c          绑定类型
     * @param methodName 调用函数名称
     * @param parameters 调用函数参数
     * @param <T>
     * @return
     */
    <T> T getBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getNullableBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getBeanInvoke(@NotNull String className, @NotNull String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getNullableBeanInvoke(@NotNull String className, String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getNullableBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getBeanInvoke(@NotNull String className, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    <T> T getNullableBeanInvoke(@NotNull String className, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException;

    /**
     * 获取类型列表
     * @param c
     * @param <T>
     * @return
     */
    <T> Collection<T> getBeans(Class<T> c);
}