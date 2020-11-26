//package ghost.framework.core.event.annotation.factory.other;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.execute.annotation.DevTest;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.ApplicationOwnerEventFactory;
//import ghost.framework.context.event.annotation.container.IClassAnnotationEventFactoryContainer;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//
//import java.lang.annotation.Annotation;
///**
// * package: ghost.framework.core.event.annotation.factory.root
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:处理 {@link ghost.framework.beans.execute.annotation.DevTest} 注释事件工厂类
// * @Date: 15:07 2020/1/29
// * @param <O> 发起方对象
// * @param <T> 目标类型
// * @param <E> 类型绑定事件目标处理类型
// * @param <V> 返回类型
// */
//public class DefaultClassDevTestAnnotationEventFactory <
//        O extends ICoreInterface,
//        T extends Class<?>,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V extends Object
//        >
//        extends ApplicationOwnerEventFactory<O, T, E>
//        implements IClassDevTestAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 初始化类型 {@link ghost.framework.beans.annotation.stereotype.Service} 注释事件工厂类
//     *
//     * @param app 应用接口
//     */
//    public DefaultClassDevTestAnnotationEventFactory(@Application @Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 注释类型
//     */
//    private final Class<? extends Annotation> annotation = DevTest.class;
//
//    /**
//     * 重写获取注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotation() {
//        return annotation;
//    }
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//        this.getLog().debug("loader>class:" + event.getTarget().getName());
//        //获取注入注释对象
//        DevTest devTest = this.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(this.annotation));
//        //获取核心接口
//        this.positionOwner(event);
//        //判断是否拥有者是否为开发测试
//        if (!event.getExecuteOwner().isDev()) {
//            event.setHandle(true);
//            return;
//        }
//        //绑定依赖类型
//        for (Class<?> c : devTest.value()) {
//            event.getExecuteOwner().addBean(c);
//        }
//        //已经处理
//        event.setHandle(true);
//        //重复执行注释流程
//        event.getExecuteOwner().getBean(IClassAnnotationEventFactoryContainer.class).loader(event);
//    }
//
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//        this.getLog().info("unloader:" + event.toString());
//        //获取注入注释对象
//        DevTest devTest = this.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(this.annotation));
//        //获取核心接口
//        this.positionOwner(event);
//        //判断是否拥有者是否为开发测试
//        if (!event.getExecuteOwner().isDev()) {
//            event.setHandle(true);
//            return;
//        }
//        //已经处理
//        event.setHandle(true);
//        //重复执行注释流程
//        event.getExecuteOwner().getBean(IClassAnnotationEventFactoryContainer.class).unloader(event);
//    }
//}