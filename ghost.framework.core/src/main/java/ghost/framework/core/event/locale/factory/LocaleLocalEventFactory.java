//package ghost.framework.core.event.locale.factory;
//
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.beans.locale.annotation.Local;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.AbstractApplicationExecuteOwnerEventFactory;
//import ghost.framework.core.event.locale.ILocaleEventTargetHandle;
//import ghost.framework.core.event.locale.factory.container.ILocaleEventListenerFactoryContainer;
//import ghost.framework.context.locale.ILocalContainer;
//import ghost.framework.core.locale.LocalContainer;
//import ghost.framework.core.parser.locale.ILocaleParserContainer;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.core.event.locale.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:区域本地化事件工厂类
// * @Date: 18:00 2020/1/20
// * @param <O>
// * @param <T>
// * @param <E>
// */
//@Component
//@BeanCollectionContainer(ILocaleEventListenerFactoryContainer.class)
//public class LocaleLocalEventFactory<O extends ICoreInterface, T extends Object, E extends ILocaleEventTargetHandle<O, T>>
//        extends AbstractApplicationExecuteOwnerEventFactory<O, T, E>
//        implements ILocaleLocalEventFactory<O, T, E> {
//    /**
//     * 初始化应用注释注入基础类
//     *
//     * @param app 设置应用接口
//     */
//    public LocaleLocalEventFactory(@Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 语言注释
//     */
//    private final Class<? extends Annotation> annotation = Local.class;
//
//    /**
//     * 获取语言注释
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotationClass() {
//        return annotation;
//    }
//
//    /**
//     * 加载语言
//     *
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
//            //获取本地化注释
//            Local local = event.getOwner().getProxyAnnotationObject(c.getAnnotation(this.annotation));
//            //获取本地化语言容器接口
//            ILocalContainer container = event.getOwner().getNullableBean(ILocalContainer.class);
//            //判断是否已经初始化本地化语言容器
//            if (container == null) {
//                //添加绑定本地化语言容器
//                container = event.getOwner().addBean(LocalContainer.class);
//            }
//            //设置默认语言
////            container.setDefaultLanguage(local.defaultLanguage());
//            //加载语言资源
//            this.getApp().getBean(ILocaleParserContainer.class).loader(
//                    c.getProtectionDomain().getCodeSource().getLocation(),
//                    container,
//                    local.resource(),
//                    event.getOwner().isDev());
//        }
//    }
//}