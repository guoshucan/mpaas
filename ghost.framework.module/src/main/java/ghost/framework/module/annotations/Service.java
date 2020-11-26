package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:服务注释，作为类实例注入的类注释。
// * @Date: 21:33 2018-06-22
// */
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface Service {
//    /**
//     * 类型Id。
//     * @return
//     */
//    Class<?> value();
//    /**
//     * 模块的指令顺序。
//     * 从小到大执行顺序，负数比小更优先执行。
//     * 在一个指令多个模块时体现优先执行。
//     * @return
//     */
//    int order() default Integer.MAX_VALUE;
//    /**
//     * 服务名称。
//     * @return
//     */
//    String value() default "";
//    /**
//     * 是否为全局服务。
//     * 如果不为全局侧值在模块内部属性注入。
//     * 是全局侧可以跨模块，侧只在模块内部。
//     * @return
//     */
//    boolean isGlobal() default false;
//    /**
//     * 指令模块版本。
//     * @return
//     */
//    String version() default "";
//    /**
//     * 描述。
//     * @return
//     */
//    String description() default "";
//}
