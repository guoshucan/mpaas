//package ghost.framework.beans.annotation.event;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * package: ghost.framework.beans.annotation.event
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用事件监听注释，作为监听对象类型主体的注释
// * @Date: 2020/2/16:5:48
// */
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface BeanApplicationEventListener {
////    /**
////     * 事件类型
////     * 默认为 {@link ApplicationEventType::All} 全部事件类型
////     * @return
////     */
////    ApplicationEventType eventType() default ApplicationEventType.All;
//    /**
//     * 消息主题
//     * 不设置任何主题时将全部接收
//     * @return
//     */
//    String[] topic() default {"*"};
//}
