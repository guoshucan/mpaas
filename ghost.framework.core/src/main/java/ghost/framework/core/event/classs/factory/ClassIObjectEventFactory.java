//package ghost.framework.core.event.classs.factory;
//
//import ghost.framework.beans.annotation.Name;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.context.annotation.ExecutionAnnotation;
//import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.factory.IClassAnnotationBeanFactoryContainer;
//import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
//import ghost.framework.context.bean.factory.ClassAnnotationBeanTargetHandle;
//import ghost.framework.core.event.classs.IClassEventTargetHandle;
//import ghost.framework.core.event.object.container.IObjectEventFactoryContainer;
//import ghost.framework.core.event.object.factory.IObjectEventFactory;
//
//import java.lang.annotation.Annotation;
//import java.util.ArrayList;
//import java.util.Map;
//
///**
// * package: ghost.framework.core.event.classs.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:实现继承 {@link IObjectEventFactory} 接口类型构建Bean完成后像 {@link IObjectEventFactoryContainer} 容器添加添加事件工厂类
// * @Date: 2020/2/11:13:09
// */
//@BeanCollectionContainer(IClassEventFactoryContainer.class)
//@Component
//public class ClassIObjectEventFactory
//        <
//        O extends ICoreInterface,
//        T extends Class<?>,
//        C extends Class<?>,
//        E extends IClassEventTargetHandle<O, T, V, String, Object>,
//        V extends Object
//        >
//        implements IClassIObjectEventFactory<O, T, C, E, V> {
//    /**
//     * 初始化应用注释注入基础类
//     *
//     * @param app 设置应用接口
//     */
//    public ClassIObjectEventFactory(@Autowired IApplication app) {
//        this.app = app;
//    }
//
//    private IApplication app;
//    private final C targetClass = (C) IObjectEventFactory.class;
//
//    /**
//     * 重写操作类型
//     *
//     * @return
//     */
//    @Override
//    public C getTargetClass() {
//        return targetClass;
//    }
//
//    /**
//     * 加载类型
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        //判断类型
//        if (!this.isLoader(event)) {
//            return;
//        }
//        //定位拥有者
//        this.positionOwner(event);
//        //获取类型注释事件工厂容器接口
//        IClassAnnotationBeanFactoryContainer classAnnotationEventFactoryContainer = this.app.getBean(IClassAnnotationBeanFactoryContainer.class);
//        //处理类型注释
//        //获取类型注释执行链
//        Map<Class<? extends Annotation>, ExecutionAnnotation> executionAnnotationChain = this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getTarget().getDeclaredAnnotations());
//        //判断是否有效执行注释链列表
//        if (executionAnnotationChain != null) {
//            //设置事件注释执行链
//            for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : executionAnnotationChain.entrySet()) {
//                //查找注释类型
//                for (IClassAnnotationBeanFactory factory : new ArrayList<IClassAnnotationBeanFactory>(classAnnotationEventFactoryContainer)) {
//                    if (factory.getAnnotationClass().equals(entry.getKey())) {
//                        factory.loader(new ClassAnnotationBeanTargetHandle(event.getOwner(), event.getTarget()));
//                    }
//                }
//            }
//        }
//        //构建类型实例
//        this.newInstance(event);
//        //绑定
//        if (event.getTarget().isAnnotationPresent(Name.class)) {
//            //指定名称绑定
//            event.getExecuteOwner().getBean(IObjectEventFactoryContainer.class).add(event.getExecuteOwner().addBean(event.getTarget().getAnnotation(Name.class).value(), event.getTarget()));
//        } else {
//            //未指定名称绑定
//            event.getExecuteOwner().getBean(IObjectEventFactoryContainer.class).add(event.getExecuteOwner().addBean(event.getTarget()));
//        }
//        //完成处理
//        event.setHandle(true);
//    }
//}