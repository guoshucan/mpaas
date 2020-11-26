//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.injection.Value;
//import ghost.framework.beans.annotation.injection.TempDirectory;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.beans.annotation.module.annotation.ModuleTempDirectory;
//import ghost.framework.beans.annotation.module.annotation.ModuleValue;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.bean.factory.injection.field.AbstractInjectionFieldEventFactory;
//import ghost.framework.core.bean.factory.injection.field.IInjectionFieldEventFactoryContainer;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用注入声明事件工厂
// * @Date: 22:28 2019/12/29
// */
//@Order
//@BeanListContainer(IInjectionFieldEventFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationInjectionFieldEventFactory<O extends Object, T extends Object, E extends IEventTargetHandle<O, T>> extends AbstractInjectionFieldEventFactory<O, T, E> {
//    public DefaultApplicationInjectionFieldEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//    private IApplication app;
//
//    /**
//     * 注入声明事件
//     * @param event 注入事件
//     */
//    @Override
//    public void field(E event) {
//        this.log.info("field " + event.toString());
//        //遍历注册对象列表
//        for (Field field : event.getTarget().getClass().getDeclaredFields()) {
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//            //判断是否注入
//            if (field.isAnnotationPresent(Autowired.class)) {
//                //使用应用注入
//                this.app.injectionAutowired(event.getTarget(), field);
//                continue;
//            }
//            //判断是否注入
//            if (field.isAnnotationPresent(ModuleAutowired.class)) {
//                this.app.injectionModuleAutowired(null, event.getTarget(), field);
//                continue;
//            }
//            //判断不为静态熟悉声明注入值
//            if (field.isAnnotationPresent(Value.class)) {
//                this.app.injectionValue(event.getTarget(), field);
//                continue;
//            }
//            //判断不为静态熟悉声明注入值
//            if (field.isAnnotationPresent(ModuleValue.class)) {
//                this.app.injectionModuleValue(null, event.getTarget(), field);
//                continue;
//            }
//            //注入模块临时目录
//            if (field.isAnnotationPresent(ModuleTempDirectory.class)) {
//                this.app.injectionModuleTempDirectory(null, event.getTarget(), field);
//                continue;
//            }
//            //注入应用临时模块
//            if (field.isAnnotationPresent(TempDirectory.class)) {
//                this.app.injectionApplicationTempDirectory(event.getTarget(), field);
//                continue;
//            }
//        }
//    }
//}
