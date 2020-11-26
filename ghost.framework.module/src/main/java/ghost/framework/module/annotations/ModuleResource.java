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
// * @Description:模块资源注释
// * @Date: 13:12 2019-02-12
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.PACKAGE})
//public @interface ModuleResource {
//    /**
//     * 资源包所属模块id，如果多个模块资源包属于一个模块id的在加载时会合并到一起。
//     *
//     * @return
//     */
//    String value() default "";
//
//    /**
//     * web内容包。
//     *
//     * @return
//     */
//    String content();
//}