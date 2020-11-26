//package ghost.framework.beans.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * package: ghost.framework.beans.annotation
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释注入域，此注释只在类型，构建函数，函数注释时生效
// * 注释时对应的参数全部由该注入域的注释前缀从env中注入
// * @Date: 16:27 2020/1/25
// */
//@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface AutowiredScope {
//    /**
//     *
//     * @return
//     */
//    String prefix();
//}