package ghost.framework.context.bean.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.log.IGetLog;
import ghost.framework.context.proxy.IMethodInvocationHandler;
import ghost.framework.util.CollectionUtils;

/**
 * package: ghost.framework.context.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:构建类型绑定工厂接口
 * {@link IGetLog#getLog()}
 * @Date: 2020/6/5:19:11
 * @param <O>
 *     {@link ICoreInterface}
 * @param <T>
 *     {@link Class<?>}
 * @param <E>
 *     {@link IBeanTargetHandle}
 *     {@link IValueBeanTargetHandle}
 *     {@link IParametersBeanTargetHandle}
 *     {@link IExecuteOwnerBeanTargetHandle}
 * @param <P>
 *     {@link IParametersBeanTargetHandle#getParameters()}
 *     {@link IParametersBeanTargetHandle#setParameters(Object[])}
 * @param <V>
 *     {@link Object}
 *     {@link IValueBeanTargetHandle#setValue(Object)} ()}
 *     {@link IValueBeanTargetHandle#getValue()}
 */
public interface INewInstanceBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IBeanTargetHandle<O, T>,
                P,
                V
                >
        extends IGetLog {
    /**
     * 构建代理对象
     *
     * @param event   事件对象
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, IMethodInvocationHandler handler) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newCglibInstance:" + event.toString());
        //构建类型
        this.newInstance(event);
        //设置代理目标对象
        handler.setTarget(value.getValue());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newCglibInstance(event.getTarget(), handler));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newCglibInstance(event.getTarget(), handler, parameters.getParameters()));
        }
    }

    /**
     * 创建实例
     *
     * @param event   事件对象
     * @param i       实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newCglibInstance:" + event.toString());
        //构建类型
        this.newInstance(event);
        //设置代理目标对象
        handler.setTarget(value.getValue());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newCglibInstance(event.getTarget(), i, handler));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newCglibInstance(event.getTarget(), i, handler, parameters.getParameters()));
        }
    }

    /**
     * 创建实例
     *
     * @param event      事件对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     */
    default void newCglibInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newCglibInstance:" + event.toString());
        //构建类型
        this.newInstance(event);
        //设置代理目标对象
        handler.setTarget(value.getValue());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newCglibInstance(event.getTarget(), interfaces, handler));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newCglibInstance(event.getTarget(), interfaces, handler, parameters.getParameters()));
        }
    }

    /**
     * 构建代理对象
     *
     * @param event   事件对象
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, IMethodInvocationHandler handler) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newJavassistInstance:" + event.toString());
//        //构建类型
//        this.newInstance(event);
//        //设置代理目标对象
//        handler.setTarget(event.getValue());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newJavassistInstance(event.getTarget(), handler));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newJavassistInstance(event.getTarget(), handler, parameters.getParameters()));
        }
    }

    /**
     * 创建实例
     *
     * @param event   事件对象
     * @param i       实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newJavassistInstance:" + event.toString());
//        //构建类型
//        this.newInstance(event);
//        //设置代理目标对象
//        handler.setTarget(event.getValue());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newJavassistInstance(event.getTarget(), i, handler));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newJavassistInstance(event.getTarget(), i, handler, parameters.getParameters()));
        }
    }

    /**
     * 创建实例
     *
     * @param event      事件对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     */
    default void newJavassistInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newJavassistInstance:" + event.toString());
//        //构建类型
//        this.newInstance(event);
//        //设置代理目标对象
//        handler.setTarget(event.getValue());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newJavassistInstance(event.getTarget(), interfaces, handler));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newJavassistInstance(event.getTarget(), interfaces, handler, parameters.getParameters()));
        }
    }

    /**
     * 构建对象
     *
     * @param event 事件对象
     */
    default void newInstance(E event) {
        IValueBeanTargetHandle<O, T, V> value = (IValueBeanTargetHandle) event;
        IParametersBeanTargetHandle<O, T, P> parameters = (IParametersBeanTargetHandle) event;
        IExecuteOwnerBeanTargetHandle<O, T> executeOwner = (IExecuteOwnerBeanTargetHandle) event;
        //判断是否已经创建对象
        if (value.getValue() != null) {
            return;
        }
        this.getLog().info("newInstance:" + event.toString());
        //构建类型
        if (CollectionUtils.isEmpty(parameters.getParameters())) {
            //没有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newInstance(event.getTarget()));
        } else {
            //有参数构建对象
            value.setValue((V) executeOwner.getExecuteOwner().newInstance(event.getTarget(), parameters.getParameters()));
        }
        //处理env替换注释参数
//        event.getExecuteOwner().getBean(IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer.class).reflect(event);
        //构建对象事件
        executeOwner.getExecuteOwner().newInstanceObjectEvent(value.getValue());
        //对象注入
        executeOwner.getExecuteOwner().injection(value.getValue());
    }
}