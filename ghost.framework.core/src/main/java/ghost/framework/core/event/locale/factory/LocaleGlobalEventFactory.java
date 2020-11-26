//package ghost.framework.core.event.locale.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.beans.locale.annotation.Global;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.AbstractApplicationOwnerEventFactory;
//import ghost.framework.core.event.locale.ILocaleEventTargetHandle;
//import ghost.framework.core.event.locale.factory.container.ILocaleEventListenerFactoryContainer;
//import ghost.framework.core.locale.GlobalContainer;
//import ghost.framework.context.locale.IGlobalContainer;
//import ghost.framework.core.parser.locale.ILocaleParserContainer;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.core.event.locale.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:区域国际化事件工厂类
// * @Date: 17:58 2020/1/20
// */
//@Component
//@BeanCollectionContainer(ILocaleEventListenerFactoryContainer.class)
//public class LocaleGlobalEventFactory<O extends ICoreInterface, T extends Object, E extends ILocaleEventTargetHandle<O, T>>
//        extends AbstractApplicationOwnerEventFactory<O, T, E>
//        implements ILocaleGlobalEventFactory<O, T, E> {
//    /**
//     * 初始化应用注释注入基础类
//     *
//     * @param app 设置应用接口
//     */
//    public LocaleGlobalEventFactory(@Application @Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 国际化注释
//     */
//    private final Class<? extends Annotation> annotation = Global.class;
//
//    /**
//     * 获取国际化注释
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotationClass() {
//        return annotation;
//    }
//
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        this.getLog().info("loader:" + event.toString());
//        //判断是否未类型
//        if (event.getTarget() instanceof Class) {
//            //获取目标类型
//            Class<?> c = (Class<?>) event.getTarget();
//            //判断是否有注释
//            if (!c.isAnnotationPresent(this.annotation)) {
//                return;
//            }
//            //定位拥有者
//            this.positionOwner(event);
//            //获取国际化注释
//            Global global = event.getOwner().getProxyAnnotationObject(c.getAnnotation(this.annotation));
//            //获取国际化语言容器接口
//            IGlobalContainer container = event.getOwner().getNullableBean(IGlobalContainer.class);
//            //判断是否已经初始化国际化语言容器
//            if (container == null) {
//                //添加绑定国际化语言容器
//                container = event.getOwner().addBean(GlobalContainer.class);
//            }
//            //设置默认语言
////            container.setDefaultLanguage(global.defaultLanguage());
//            //加载语言资源
//            this.getApp().getBean(ILocaleParserContainer.class).loader(
//                    c.getProtectionDomain().getCodeSource().getLocation(),
//                    container,
//                    global.resource(),
//                    event.getOwner().isDev());
//        }
//    }
//}