package ghost.framework.context.base;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * package: ghost.framework.core.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:基础构建对象接口
 * @Date: 2019/12/10:7:06
 */
public interface IInstance {
    /**
     * 绑定对象事件
     *
     * @param o 绑定对象
     */
    void newInstanceObjectEvent(Object o);
    /**
     * 创建实例
     *
     * @param className 构建类名称
     * @return
     */
    @NotNull
    Object newInstance(@NotNull String className);

    /**
     * 创建实例
     *
     * @param c 析构类型
     * @return
     */
    @NotNull
    Object newInstance(@NotNull Class<?> c);

    /**
     * 构建类
     *
     * @param constructor 析构者
     * @return
     */
    @NotNull
    Object newInstance(@NotNull Constructor constructor);

    /**
     * 后去构建类型参数列表
     *
     * @param constructor 析构者
     * @return
     */
    @Nullable
    Object[] newInstanceParameters(@NotNull Constructor constructor);

    /**
     * 获取析构数组参数
     * @param target
     * @param constructor
     * @param method
     * @param parameters
     * @return
     */
    @Nullable
    Object[] newInstanceTargetParameters(@NotNull Object target, @Nullable Constructor constructor, @Nullable Method method, @NotNull Parameter[] parameters);

    /**
     * 获取析构参数对象
     * @param target
     * @param parameter
     * @return
     */
    @Nullable
    Object newInstanceParameter(@NotNull Object target, @NotNull Parameter parameter);

    /**
     * 获取函数数组参数
     * @param target
     * @param method
     * @return
     */
    @NotNull
    Object[] newInstanceParameters(@NotNull Object target, @NotNull Method method);

    /**
     * 获取自定义参数析构函数
     *
     * @param c          析构类型
     * @param parameters 指定析构参数
     * @return 返回析构对象
     */
    @NotNull
    Object newInstance(@NotNull Class<?> c, @NotNull Object[] parameters);
    /**
     * 获取自定义参数析构函数
     *
     * @param className          析构类型名称
     * @param parameters 指定析构参数
     * @return 返回析构对象
     */
    @NotNull
    Object newInstance(@NotNull String className, @NotNull Object[] parameters);
    /**
     * 获取自定义参数析构函数
     * 如果参数有注释的将必须放在开头参数位置，在没注释后面的参数将由parameters参数列表填充
     * @param constructor 析构者
     * @param parameters  数组自定义参数
     * @return 返回析构对象
     */
    @NotNull
    Object newInstanceParameters(@NotNull Constructor constructor, @NotNull Object[] parameters);

    /**
     * 获取自定义参数析构函数
     * 如果参数有注释的将必须放在开头参数位置，在没注释后面的参数将由parameters参数列表填充
     * @param constructor 析构者
     * @param parameters  数组自定义参数
     * @return 返回数组析构参数
     */
    @Nullable
    Object[] newInstanceCustomParameters(@NotNull Constructor constructor, @NotNull Object[] parameters);

    /**
     * 获取类型构建数组参数对象
     *
     * @param c 构建类型
     * @return 返回构建类型的数组参数对象，如果没有构建参数侧返回null
     */
    @Nullable
    Object[] newInstanceParameters(@NotNull Class<?> c);

    /**
     * 获取类型构建数组参数对象
     *
     * @param c          构建类型
     * @param parameters 数组自定义参数
     * @return 返回构建类型的数组参数对象，如果没有构建参数侧返回null
     */
    @Nullable
    Object[] newInstanceParameters(@NotNull Class<?> c, @NotNull Object[] parameters);

    /**
     * 获取类型构建参数
     * @param c 要获取构建参数的类型
     * @return 返回构建参数类型与值
     */
    Map<Class<?>, Object> newInstanceMapParameters(@NotNull Class<?> c);

    /**
     *
     * @param constructor
     * @return
     */
    Map<Class<?>, Object> newInstanceMapParameters(@NotNull Constructor constructor);

    /**
     *
     * @param parameters
     * @return
     */
    Map<Class<?>, Object> newInstanceMapParameters(Parameter[] parameters);
}