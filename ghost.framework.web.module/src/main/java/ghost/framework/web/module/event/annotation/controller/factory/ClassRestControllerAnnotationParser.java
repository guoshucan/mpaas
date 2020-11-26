//package ghost.framework.web.module.event.annotation.controller.factory;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.IEventFactory;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.web.context.bind.annotation.RestController;
//import ghost.framework.web.context.http.IHttpRestControllerContainer;
//import ghost.framework.web.module.parser.annotation.controller.AbstractClassRestControllerAnnotationParser;
//import ghost.framework.util.StringUtil;
//
///**
// * package: ghost.framework.web.module.event.annotation.controller.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认类型控制器注释解析器
// * @Date: 15:29 2020/2/2
// */
//public class ClassRestControllerAnnotationParser
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//                V extends Object
//                >
//        extends AbstractClassRestControllerAnnotationParser<O, T, E, V> {
//    /**
//     * 加载事件
//     * @param eventFactory 事件工厂
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(IEventFactory eventFactory, E event) {
//        IClassRestControllerAnnotationEventFactory factory = (IClassRestControllerAnnotationEventFactory)eventFactory;
//        //获取注释对象
//        RestController restController = (RestController)factory.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(factory.getAnnotation()));
//        //定位拥有者
//        factory.positionOwner(event);
//        //创建类型对象
//        factory.newInstance(event);
//        //添加绑定
//        if (restController.name().equals("")) {
//            event.getExecuteOwner().addBean(event.getValue());
//        } else {
//            event.getExecuteOwner().addBean(restController.name(), event.getValue());
//        }
//        //将控制器对象添加入容器
//        event.getExecuteOwner().getBean(IHttpRestControllerContainer.class).add(event.getValue());
//        //添加加载注释排除
////        event.getExcludeAnnotationList().add(factory.getAnnotation());
//        //返回已经处理
//        event.setHandle(true);
//    }
//}