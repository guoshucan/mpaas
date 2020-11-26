package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:适配器注释。
// * @Date: 21:01 2018-06-25
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE)
//@Inherited
//public @interface Adapter {
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
//     * 模块名称。
//     * @return
//     */
//    String value() default "";
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
//    /**
//     * 是否为全局服务。
//     * 如果不为全局侧值在模块内部属性注入。
//     * 是全局侧可以跨模块，侧只在模块内部。
//     * @return
//     */
//    boolean isGlobal() default false;
//}
