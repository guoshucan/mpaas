package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:类配置注释。
// * @Date: 2:50 2018/5/12
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.TYPE })
//@Inherited
//public @interface Config {
//    /**
//     * 模块的指令顺序。
//     * 从小到大执行顺序，负数比小更优先执行。
//     * 在一个指令多个模块时体现优先执行。
//     *
//     * @return
//     */
//    int order() default Integer.MAX_VALUE;
//
//    /**
//     * 配置类名称。
//     * @return
//     */
//    String value() default "";
//    /**
//     * 类型Id。
//     * @return
//     */
//    Class<?> value();
//    /**
//     * 是否需要实例类。
//     * @return
//     */
//    boolean instance() default false;
//    /**
//     * 配置类版本。
//     * @return
//     */
//    String version() default "";
//
//    /**
//     * 配置类描述。
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
