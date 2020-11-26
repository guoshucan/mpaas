package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:配置函数注释。
// * @Date: 2:55 2018/5/12
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.METHOD})
//public @interface ConfigMethod {
//    /**
//     * 模块名称。
//     * @return
//     */
//    String value() default "";
//    /**
//     * 模块的指令顺序。
//     * 从小到大执行顺序，负数比小更优先执行。
//     * 在一个指令多个模块时体现优先执行。
//     * @return
//     */
//    int order() default Integer.MAX_VALUE;
//    /**
//     * 配置版本。
//     * @return
//     */
//    String version() default "";
//    /**
//     * 配置描述。
//     * @return
//     */
//    String description() default "";
//}
