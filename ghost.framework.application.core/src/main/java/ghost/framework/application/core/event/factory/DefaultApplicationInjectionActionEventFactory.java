//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.bean.utils.BeanUtil;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.IInstance;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.bean.factory.injection.factory.action.DefaultInjectionActionEventFactory;
//import ghost.framework.core.bean.factory.injection.factory.action.IInjectionActionEventFactoryContainer;
//import ghost.framework.context.exception.InjectionBeforeException;
//import ghost.framework.util.Assert;
//
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.app.core.event.factory
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用注入动作事件工厂类
// * @Date: 2019/12/30:20:24
// * @param <O> 事件拥有者类型
// * @param <T> 事件目标对象类型
// * @param <E> 事件对象类型
// */
//@Order
//@BeanListContainer(IInjectionActionEventFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationInjectionActionEventFactory<O extends IInstance, T extends Object, E extends IEventTargetHandle<O, T>> extends DefaultInjectionActionEventFactory<O, T, E> {
//    /**
//     * 初始化默认应用注入动作事件工厂类
//     *
//     * @param app 注入应用接口
//     */
//    public DefaultApplicationInjectionActionEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//
//    /**
//     * 应用接口
//     */
//    private IApplication app;
//
//    /**
//     * 注入前事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void Before(E event) {
//        this.log.info("Before " + event.toString());
//        Assert.notNull(event.positionOwner(), "IInstance is null");
//        Assert.notNull(event.getTarget(), "Object is null");
//        //判断是否有初始化调用函数
//        for (Method m : BeanUtil.getInjectionBeforeInvokeMethodActionList(event.getTarget())) {
//            //调用初始化注释函数
//            try {
//                m.setAccessible(true);
//                m.invoke(event.getTarget(), event.positionOwner().newInstanceParameters(m));
//            } catch (Exception e) {
//                throw new InjectionBeforeException(e);
//            }
//        }
//        event.setHandle(true);
//    }
//
//    /**
//     * 注入后事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void After(E event) {
//        this.log.info("After " + event.toString());
//        Assert.notNull(event.positionOwner(), "IInstance is null");
//        Assert.notNull(event.getTarget(), "Object is null");
//        //判断是否有初始化调用函数
//        for (Method m : BeanUtil.getInjectionAfterInvokeMethodActionList(event.getTarget())) {
//            //调用初始化注释函数
//            try {
//                m.setAccessible(true);
//                m.invoke(event.getTarget(), event.positionOwner().newInstanceParameters(m));
//            } catch (Exception e) {
//                throw new InjectionBeforeException(e);
//            }
//        }
//        event.setHandle(true);
//    }
//}
