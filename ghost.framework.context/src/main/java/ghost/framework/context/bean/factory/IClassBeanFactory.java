package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.ProxyInstance;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationEnvironment;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.module.environment.IModuleEnvironment;
import ghost.framework.context.proxy.IMethodInvocationHandler;

/**
 * package: ghost.framework.context.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释事件工厂接口
 * @Date: 18:10 2020/1/20
 * @param <O> 发起方核心接口
 * @param <T> 目标类型
 * @param <E> 注释事件目标处理接口
 */
public interface IClassBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassBeanTargetHandle<O, T, V, String, Object>,
        V
        >
        extends IApplicationExecuteOwnerBeanFactory<O, T, E>, ILoader<O, T, E> {
    /**
     * 获取拥有者的env接口
     * 此函数必须在调用positionOwner函数定位好拥有者之后
     *
     * @param event 事件对象
     * @return
     */
    default IEnvironment getEnvironment(E event) {
        //判断env拥有者类型
        if (event.getExecuteOwner() instanceof IApplication) {
            //获取应用env
            return event.getExecuteOwner().getBean(IApplicationEnvironment.class);
        }
        //获取模块env
        return event.getExecuteOwner().getBean(IModuleEnvironment.class);
    }

    /**
     * 获取绑定注释类型
     *
     * @return
     */
    Class<?>[] getTargetClasss();

    /**
     * 判断构建类型是否使用代理模式构建
     *
     * @param event
     * @return
     */
    default boolean isProxyInstance(E event) {
        return event.getTarget().isAnnotationPresent(ProxyInstance.class);
    }

    /**
     * 判断目标类型是否可以加载
     *
     * @param event 事件对象
     * @return 返回是否可以加载目标类型
     */
    default boolean isLoader(E event) {
        //遍历目标类型
        for (Class<?> c : this.getTargetClasss()) {
            //判断目标类型是否为接口类型
            if (c.isInterface()) {
                if (c.isAssignableFrom(event.getTarget())) {
                    //事件目标类型继承类目标类型接口
                    return true;
                }
            } else {
                if (c.equals(event.getTarget())) {
                    //事件类型与目标类型相同
                    return true;
                }
            }
        }
        //返回事件类型与目标类型不符合
        return false;
    }

    /**
     * 创建实例
     *
     * @param event   事件对象
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newCglibInstance(event, handler);
    }

    /**
     * 创建实例
     *
     * @param event   事件对象
     * @param i       实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newCglibInstance(event, i, handler);
    }

    /**
     * 创建实例
     *
     * @param event      事件对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     */
    default void newCglibInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newCglibInstance(event, interfaces, handler);
    }

    /**
     * 创建实例
     *
     * @param event   事件对象
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newJavassistInstance(event, handler);
    }

    /**
     * 创建实例
     *
     * @param event   事件对象
     * @param i       实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newJavassistInstance(event, i, handler);
    }

    /**
     * 创建实例
     *
     * @param event      事件对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     */
    default void newJavassistInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newJavassistInstance(event, interfaces, handler);
    }
//    /**
//     * jdk创建代理对象
//     * @param event
//     * @param handler
//     */
//    default void newJdkInstance(E event, InvocationHandler handler){
//        //构建类型实例
//        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newJdkInstance(event, handler);
//    }

    /**
     * 创建实例
     *
     * @param event 事件对象
     */
    default void newInstance(E event) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassBeanFactoryContainer.class).newInstance(event);
    }
}