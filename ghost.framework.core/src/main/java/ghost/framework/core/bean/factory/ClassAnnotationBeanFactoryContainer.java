package ghost.framework.core.bean.factory;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractAnnotationBeanFactoryContainer;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactoryContainer;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释事件监听工厂容器类
 * @Date: 7:45 2019/12/27
 * @param <O> 发起方
 * @param <T> 注释类型
 * @param <E> 注释绑定事件目标处理
 * @param <L> {@link IClassAnnotationBeanFactory <O, T, E, V>} 接口类型或继承 {@link IClassAnnotationBeanFactory <O, T, E, V>} 接口对象
 * @param <V> 类型创建的对象
 */
public class ClassAnnotationBeanFactoryContainer
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                L extends IClassAnnotationBeanFactory<O, T, E, V>,
                V extends Object
                >
        extends AbstractAnnotationBeanFactoryContainer<O, T, E, L>
        implements IClassAnnotationBeanFactoryContainer<O, T, E, L, V> {
    /**
     * 应用接口
     */
    private IApplication app;

    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public ClassAnnotationBeanFactoryContainer(@Autowired IApplication app, @Application @Autowired @Nullable IClassAnnotationBeanFactoryContainer<O, T, E, L, V> parent) {
        this.app = app;
        this.parent = parent;
    }

    /**
     * 父级类事件监听容器
     */
    private IClassAnnotationBeanFactoryContainer<O, T, E, L, V> parent;

    /**
     * 获取级
     *
     * @return
     */
    @Override
    public IClassAnnotationBeanFactoryContainer<O, T, E, L, V> getParent() {
        return parent;
    }

//    /**
//     * jdk创建代理对象
//     * @param event
//     * @param handler
//     */
//    @Override
//    public void newJdkInstance(E event, InvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newJdkInstance:" + event.toString());
//        //构建类型
//        if (event.getParameters() == null || event.getParameters().length == 0) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJdkInstance(event.getTarget(), handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJdkInstance(event.getTarget(), handler, event.getParameters()));
//        }
//        //处理env替换注释参数
////        event.getExecuteOwner().getBean(IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer.class).reflect(event);
//        //构建对象事件
//        event.getExecuteOwner().newInstanceObjectEvent(event.getValue());
//        //注入
//        //对象注入
//        event.getExecuteOwner().injection(event.getValue());
//    }

//    /**
//     * 构建代理对象
//     *
//     * @param event    事件对象
//     * @param handler 代理回调对象
//     */
//    @Override
//    public void newCglibInstance(E event, IMethodInvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newCglibInstance:" + event.toString());
//        //构建类型
//        this.newInstance(event);
//        //设置代理目标对象
//        handler.setTarget(event.getValue());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newCglibInstance(event.getTarget(), handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newCglibInstance(event.getTarget(), handler, event.getParameters()));
//        }
//    }
//    /**
//     * 创建实例
//     *
//     * @param event 事件对象
//     * @param i 实现代理构建对象继承的接口
//     * @param handler 代理回调对象
//     */
//    @Override
//    public void newCglibInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newCglibInstance:" + event.toString());
//        //构建类型
//        this.newInstance(event);
//        //设置代理目标对象
//        handler.setTarget(event.getValue());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newCglibInstance(event.getTarget(), i, handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newCglibInstance(event.getTarget(), i, handler, event.getParameters()));
//        }
//    }
//    /**
//     * 创建实例
//     *
//     * @param event 事件对象
//     * @param interfaces 实现代理构建对象继承的接口
//     * @param handler 代理回调对象
//     */
//    @Override
//    public void newCglibInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newCglibInstance:" + event.toString());
//        //构建类型
//        this.newInstance(event);
//        //设置代理目标对象
//        handler.setTarget(event.getValue());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newCglibInstance(event.getTarget(), interfaces, handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newCglibInstance(event.getTarget(), interfaces, handler, event.getParameters()));
//        }
//    }
//    /**
//     * 构建代理对象
//     *
//     * @param event    事件对象
//     * @param handler 代理回调对象
//     */
//    @Override
//    public void newJavassistInstance(E event, IMethodInvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newJavassistInstance:" + event.toString());
////        //构建类型
////        this.newInstance(event);
////        //设置代理目标对象
////        handler.setTarget(event.getValue());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJavassistInstance(event.getTarget(), handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJavassistInstance(event.getTarget(), handler, event.getParameters()));
//        }
//    }
//    /**
//     * 创建实例
//     *
//     * @param event 事件对象
//     * @param i 实现代理构建对象继承的接口
//     * @param handler 代理回调对象
//     */
//    @Override
//    public void newJavassistInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newJavassistInstance:" + event.toString());
////        //构建类型
////        this.newInstance(event);
////        //设置代理目标对象
////        handler.setTarget(event.getValue());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJavassistInstance(event.getTarget(), i, handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJavassistInstance(event.getTarget(), i, handler, event.getParameters()));
//        }
//    }
//    /**
//     * 创建实例
//     *
//     * @param event 事件对象
//     * @param interfaces 实现代理构建对象继承的接口
//     * @param handler 代理回调对象
//     */
//    @Override
//    public void newJavassistInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newJavassistInstance:" + event.toString());
////        //构建类型
////        this.newInstance(event);
////        //设置代理目标对象
////        handler.setTarget(event.getValue());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJavassistInstance(event.getTarget(), interfaces, handler));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newJavassistInstance(event.getTarget(), interfaces, handler, event.getParameters()));
//        }
//    }
//    /**
//     * 构建对象
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void newInstance(E event) {
//        //判断是否已经创建对象
//        if (event.getValue() != null) {
//            return;
//        }
//        this.getLog().info("newInstance:" + event.toString());
//        //构建类型
//        if (CollectionUtils.isEmpty(event.getParameters())) {
//            //没有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newInstance(event.getTarget()));
//        } else {
//            //有参数构建对象
//            event.setValue((V) event.getExecuteOwner().newInstance(event.getTarget(), event.getParameters()));
//        }
//        //处理env替换注释参数
////        event.getExecuteOwner().getBean(IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer.class).reflect(event);
//        //构建对象事件
//        event.getExecuteOwner().newInstanceObjectEvent(event.getValue());
//        //对象注入
//        event.getExecuteOwner().injection(event.getValue());
//    }

    /**
     * 添加注释
     *
     * @param event
     */
    @Override
    public void loader(E event) {
//        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //验证注释冲突
//        IAnnotationCheckContainer annotationConflictInjectorContainer = event.getOwner().getNullableBean(IAnnotationCheckContainer.class);
//        if (annotationConflictInjectorContainer != null) {
//            annotationConflictInjectorContainer.type(event.getTarget());
//        }
        //如果还没有类型注释事件工厂退出执行
        if (this.isEmpty()) {
            return;
        }
        event.setExecutionAnnotationChain(this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getTarget()));
        if (event.getExecutionAnnotationChain().isEmpty()) {
            return;
        }
        for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry: event.getExecutionAnnotationChain().entrySet()) {
            //搜索类型注释事件工厂
            for (L factory : new ArrayList<L>(this)) {
                if (factory.getAnnotationClass().equals(entry.getKey()) && !factory.isLoader(event)) {
                    //定位拥有者
                    factory.positionOwner(event);
                    //加载
                    factory.loader(event);
                    //完成执行
                    factory.setExecute(event);
                    //判断为存储类型时处理函数操作
//                    if(entry.getValue().getExecutionChain().getTag().equals(AnnotationTag.AnnotationTags.StereoType)){
//                        //处理注释有 Bean 绑定函数
//                        IMethodAnnotationEventFactoryContainer container = event.getExecuteOwner().getNullableBean(IMethodAnnotationEventFactoryContainer.class);
//                        if (container != null) {
//                            container.loader(new MethodEventTargetHandle(event.getOwner(), event.getValue()));
//                        }
//                    }
                }
            }
            //判断注释是否没有工厂处理
            ExecutionAnnotation executionAnnotation = event.getExecutionAnnotationChain().get(entry.getKey());
            if (!executionAnnotation.isExecute()) {
                this.getLog().warn("loader " + event.getTarget().getName() + " in " + entry.getKey().toString().replace("interface ", "annotation ") + " not event factory execute！");
                executionAnnotation.setExecute(true);
            }
        }
        event.checkExecutionAnnotationChainCompleted();
        //判断是否已经处理，一般执行completeExecute后就已经处理了
        if (event.isHandle()) {
            return;
        }
        //向父级加载
        if (this.parent != null) {
            this.parent.loader(event);
        }
    }
    @Override
    public void unloader(E event) {

    }
}