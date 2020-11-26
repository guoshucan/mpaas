//package ghost.framework.beans.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * package: ghost.framework.beans.annotation
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注入类型
// * @Date: 2020/2/1:22:40
// */
//@Target({ElementType.FIELD, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface AutowiredClass {
//    /**
//     * 是否为单实例
//     * 一般注入对象类型为[]或list或map类型参数对象注入为true
//     * @return
//     */
//    boolean singleInstance() default true;
//    /**
//     * 关联加载类参数
//     *
//     * @return
//     */
//    Class<?>[] depend() default {};
//
//    /**
//     * 注入接口
//     *
//     * @return
//     */
//    Class<?>[] value();
//
//    /**
//     * value参数注入条件设置
//     *
//     * @return
//     */
//    ValueCondition condition() default ValueCondition.donIgnore;
//
//    /**
//     * value参数注入条件枚举
//     */
//    enum ValueCondition {
//        /**
//         * 不忽略
//         * 每个类型都必须有对象
//         */
//        donIgnore,
//        /**
//         * 忽略
//         * 可以忽略找不到对象类型或接口
//         */
//        ignore
//    }
//}
