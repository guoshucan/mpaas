//package ghost.framework.beans.resource.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * package: ghost.framework.web.context.bens.resource
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:web资源路径注释
// * @Date: 2020/3/13:20:40
// */
//@Target(ElementType.ANNOTATION_TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ResourceClassPath {
////    /**
////     * 是否为虚拟路径，如果为虚拟路径将不自定被 {@see IWebResourceLoader} 自动装载
////     * 如果为虚拟化路径将被 {@link WebNavMenu} {@link WebTopMenu} 等注释的处理加载资源
////     * @return
////     */
////    boolean virtualPath() default false;
//    /**
//     * 资源路径
//     * @return
//     */
//    String[] value() default "static";
//}