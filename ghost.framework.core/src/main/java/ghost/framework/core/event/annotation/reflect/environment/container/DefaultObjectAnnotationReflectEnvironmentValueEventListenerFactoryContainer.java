//package ghost.framework.core.event.annotation.reflect.environment.container;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.core.event.annotation.reflect.environment.factory.IObjectAnnotationReflectEnvironmentValueEventFactory;
//import ghost.framework.core.event.factory.AbstractEventListenerFactoryContainer;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注入替换env事件监听工厂容器
// * @Date: 22:16 2020/1/5
// */
//public class DefaultObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer<
//        O extends ICoreInterface,
//        T extends Class<?>,
//        L extends Object,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V>
//        extends AbstractEventListenerFactoryContainer<L>
//        implements IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer<O, T, L, E, V> {
//    /**
//     * 应用接口
//     */
//    private IApplication app;
//
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param parent 父级类事件监听容器
//     */
//    public DefaultObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer(@Application @Autowired IApplication app, @Application @Autowired @Nullable IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer<O, T, L, E, V> parent) {
//        this.app = app;
//        this.parent = parent;
//        if (parent == null) {
//            this.getLog().info("~" + this.getClass().getName());
//        } else {
//            this.getLog().info("~" + this.getClass().getName() + "(parent:" + parent.getClass().getName() + ")");
//        }
//    }
//
//    /**
//     * 父级类事件监听容器
//     */
//    private IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer<O, T, L, E, V> parent;
//
//    /**
//     * @return
//     */
//    @Override
//    public IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer getParent() {
//        return parent;
//    }
//
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void reflect(E event) {
//        this.getLog().info("reflect:" + event.toString());
//        //遍历注释绑定注入器接口类型列表
//        for (L l : this.getEventExecuteList()) {
//            //判断是否执行接口
//            if (l instanceof Class) {
//                //获取接口类型对象
//                IObjectAnnotationReflectEnvironmentValueEventFactory factory = (IObjectAnnotationReflectEnvironmentValueEventFactory) event.getOwner().getBean((Class<?>) l);
//                //执行事件
//                factory.reflect(event);
//                //判断事件是否已经处理
//                if (event.isHandle()) {
//                    return;
//                }
//            }
//            //对象类型处理
//            if (IObjectAnnotationReflectEnvironmentValueEventFactory.class.isAssignableFrom(l.getClass())) {
//                ((IObjectAnnotationReflectEnvironmentValueEventFactory) l).reflect(event);
//                //判断事件是否已经处理
//                if (event.isHandle()) {
//                    return;
//                }
//            }
//        }
//        //
//        if (this.parent != null) {
////            this.parent.reflect(event);
//            if (event.isHandle()) {
//                return;
//            }
//        }
//    }
//}