//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.IInstance;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.bean.factory.injection.factory.action.DefaultInjectionActionEventFactory;
//import ghost.framework.core.bean.factory.injection.factory.action.IInjectionActionEventFactoryContainer;
//import ghost.framework.context.module.IModule;
//
///**
// * package: ghost.framework.app.core.event.factory
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2019/12/30:20:24
// */
//@Order
//@BeanListContainer(IInjectionActionEventFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleInjectionActionEventFactory<O extends IInstance, T extends Object, E extends IEventTargetHandle<O, T>>
//        extends DefaultInjectionActionEventFactory<O, T, E> {
//    public DefaultModuleInjectionActionEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//    private IApplication app;
//    private IModule module;
//    /**
//     * 注入前事件
//     * @param event 事件对象
//     */
//    @Override
//    public void Before(E event) {
//        this.log.info("Before " + event.toString());
////        Assert.notNull(event.positionOwner(), "IInstance is null");
////        Assert.notNull(event.getTarget(), "Object is null");
////        //判断是否有初始化调用函数
////        for (Method m : BeanUtil.getInjectionBeforeInvokeMethodActionList(event.getTarget())) {
////            //调用初始化注释函数
////            try {
////                m.setAccessible(true);
////                m.invoke(event.getTarget(), event.positionOwner().newInstanceParameters(m));
////            } catch (Exception e) {
////                if (this.log.isDebugEnabled()) {
////                    e.printStackTrace();
////                    this.log.debug(e.getMessage());
////                } else {
////                    this.log.error(e.getMessage());
////                }
////            }
////        }
//    }
//
//    /**
//     * 注入后事件
//     * @param event 事件对象
//     */
//    @Override
//    public void After(E event) {
//        this.log.info("After " + event.toString());
////        Assert.notNull(event.positionOwner(), "IInstance is null");
////        Assert.notNull(event.getTarget(), "Object is null");
////        //判断是否有初始化调用函数
////        for (Method m : BeanUtil.getInjectionAfterInvokeMethodActionList(event.getTarget())) {
////            //调用初始化注释函数
////            try {
////                m.setAccessible(true);
////                m.invoke(event.getTarget(), event.positionOwner().newInstanceParameters(m));
////            } catch (Exception e) {
////                if (this.log.isDebugEnabled()) {
////                    e.printStackTrace();
////                    this.log.debug(e.getMessage());
////                } else {
////                    this.log.error(e.getMessage());
////                }
////            }
////        }
//    }
//}
