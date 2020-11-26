//package ghost.framework.beans.execute.annotation;
//
//import java.lang.annotation.*;
//
///**
// * 注释依赖
// */
//@Target({ElementType.ANNOTATION_TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface AnnotationDepend {
//    /**
//     * 配置依赖
//     * @return
//     */
//    Class<? extends Annotation>[] configuration() default {};
//
//    /**
//     * 注释依赖
//     * @return
//     */
//    Class<? extends Annotation>[] conditional() default {};
//}