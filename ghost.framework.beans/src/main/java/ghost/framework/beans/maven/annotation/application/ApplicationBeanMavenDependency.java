//package ghost.framework.beans.maven.annotation.application;
//
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.beans.maven.annotation.MavenDependency;
//import ghost.framework.beans.maven.annotation.bean.BeanMavenDependency;
//
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用绑定maven依赖注释
// * @Date: 19:06 2019/12/24
// */
//@Repeatable(ApplicationBeanMavenDependencys.class)
//@Target({
//        ElementType.PACKAGE,
//        ElementType.TYPE,
//        ElementType.PARAMETER,
//        ElementType.ANNOTATION_TYPE,
//        ElementType.FIELD,
//        ElementType.METHOD,
//        ElementType.TYPE_USE,
//        ElementType.CONSTRUCTOR
//})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ApplicationBeanMavenDependency {
//    /**
//     * 绑定数组包
//     * @return
//     */
//    MavenDependency[] dependencys() default {};
//
//    /**
//     * 绑定maven注释
//     * @return
//     */
//    @NotNull
//    BeanMavenDependency[] beanDependency();
//    /**
//     *
//     * @return
//     */
//    ConfigurationProperties[] properties() default {};
//}