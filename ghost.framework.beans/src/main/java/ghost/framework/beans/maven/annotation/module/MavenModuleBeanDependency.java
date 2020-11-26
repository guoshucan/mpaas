//package ghost.framework.beans.maven.annotation.module;
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.beans.maven.annotation.MavenDependency;
//import ghost.framework.beans.maven.annotation.bean.BeanMavenDependency;
//
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块绑定maven依赖注释
// * @Date: 21:13 2019/12/4
// */
//@Repeatable(MavenModuleBeanDependencys.class)
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
//public @interface MavenModuleBeanDependency {
//    /**
//     * 绑定数组包
//     * @return
//     */
//    MavenDependency[] dependencys();
//    /**
//     * 绑定maven注释
//     * @return
//     */
//    BeanMavenDependency[] beanDependency();
//    /**
//     * 注入绑定类型列表
//     * @return
//     */
//    String[] value() default {};
//    /**
//     *
//     * @return
//     */
//    ConfigurationProperties[] properties() default {};
//    /**
//     * 配置类
//     * @return
//     */
//    String[] propertiesClass() default {};
//}