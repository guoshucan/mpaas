//package ghost.framework.beans.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注入注释
// * @Date: 14:52 2019/12/21
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.TYPE, ElementType.METHOD})
//public @interface In {
//    /**
//     * 注入模式
//     * @return
//     */
//    Mode value() default Mode.Module;
//
//    /**
//     * 注入模式枚举
//     */
//    enum Mode {
//        /**
//         * 注入应用Bean容器
//         */
//        Application,
//        /**
//         * 注入模块Bean容器
//         */
//        Module
//    }
//}
