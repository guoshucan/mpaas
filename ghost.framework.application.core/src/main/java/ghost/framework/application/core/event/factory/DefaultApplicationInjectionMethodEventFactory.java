//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.bean.IBean;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.bean.factory.injection.factory.method.AbstractInjectionMethodEventFactory;
//import ghost.framework.core.bean.factory.injection.container.IInjectionMethodEventFactoryContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.context.module.exception.ModuleInvalidException;
//import ghost.framework.util.StringUtil;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用注入函数事件工厂
// * @Date: 22:29 2019/12/29
// */
//@Order
//@BeanListContainer(IInjectionMethodEventFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationInjectionMethodEventFactory<O extends IBean, T extends Object, E extends IEventTargetHandle<O, T>> extends AbstractInjectionMethodEventFactory<O, T, E> {
//    public DefaultApplicationInjectionMethodEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//
//    private IApplication app;
//
//    /**
//     * 注入函数事件
//     *
//     * @param event 注入事件
//     */
//    @Override
//    public void injectionMethod(E event) {
//        this.log.info("method " + event.toString());
//        try {
//            for (Method method : event.getTarget().getClass().getDeclaredMethods()) {
//                if (Modifier.isStatic(method.getModifiers())) {
//                    continue;
//                }
//                //判断是否注入
//                if (method.isAnnotationPresent(Autowired.class)) {
//                    //使用应用注入
//                    //调用函数
//                    method.invoke(event.getTarget(), this.app.newInstanceParameters(method));
//                    continue;
//                }
//                //判断是否注入
//                if (method.isAnnotationPresent(ModuleAutowired.class)) {
//                    //获取注入注释
//                    ModuleAutowired a = method.getAnnotation(ModuleAutowired.class);
//                    //是否使用模块注入
//                    if (a.name().equals("")) {
//                        //使用应用注入
//                        //调用函数
//                        method.invoke(event.getTarget(), this.app.newInstanceParameters(method));
//                    } else {
//                        //使用模块注入
//                        IModule c = this.app.getModule(a.name());
//                        if (c == null) {
//                            throw new ModuleInvalidException(a.name());
//                        }
//                        //调用函数
//                        method.invoke(event.getTarget(), this.app.newModuleMethodParameters(c, method));
//                    }
//                    continue;
//                }
//            }
//        } catch (Exception e) {
//            if (this.log.isDebugEnabled()) {
//                e.printStackTrace();
//                this.log.debug(e.getMessage());
//            } else {
//                this.log.error(e.getMessage());
//            }
//        }
//    }
//}
