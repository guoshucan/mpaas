//package ghost.framework.beans.maven.annotation.bean;
//
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.beans.maven.annotation.MavenDependency;
//
//import java.lang.annotation.*;
//
///**
// * package: ghost.framework.beans.maven.annotation.bean
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:绑定maven注释
// * @Date: 2020/1/14:15:16
// */
//@Repeatable(BeanMavenDependencys.class)
//@Target({
//        ElementType.ANNOTATION_TYPE
//})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface BeanMavenDependency {
//    /**
//     * 模块依赖注释
//     * @return
//     */
//    @NotNull
//    MavenDependency dependency();
//    /**
//     * 注入绑定类型列表
//     * @return
//     */
//    String[] value() default {};
//}
