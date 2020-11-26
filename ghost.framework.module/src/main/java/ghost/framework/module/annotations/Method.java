package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:模块函数注释。
// * @Date: 15:42 2018/4/22
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.METHOD)
//@Inherited
//public @interface Method {
//    /**
//     * 模块的指令顺序。
//     * 从小到大执行顺序，负数比小更优先执行。
//     * 在一个指令多个模块时体现优先执行。
//     * @return
//     */
//    int order() default Integer.MAX_VALUE;
////    /**
////     * 函数Id。
////     * @return
////     */
////    String value();
//    /**
//     * 委托名称。
//     * @return
//     */
//    String value() default "";
//    /**
//     * 指令列表。
//     * @return
//     */
//    //CommandSign[] commandSigns();
//    /**
//     * 指令函数版本。
//     * @return
//     */
//    String version() default "";
//    /**
//     * 描述。
//     * @return
//     */
//    String description() default "";
//
//    /**
//     * 验证会话Id。
//     * @return
//     */
//    boolean isVerifySession() default false;
//    /**
//     * 验证密码。
//     * @return
//     */
//    boolean isVerifyPassword() default false;
//
//    /**
//     * 通信模式。
//     * @return
//     */
//    CommunicateMode communicateMode() default CommunicateMode.none;
////    /**
////     * 权限名称。
////     * @return
////     */
////    String permissions() default "";
//}
