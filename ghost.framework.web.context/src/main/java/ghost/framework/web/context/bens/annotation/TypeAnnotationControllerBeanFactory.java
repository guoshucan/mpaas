//package ghost.framework.web.context.bens.annotation;
//
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.web.context.http.IHttpControllerContainer;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * package: ghost.framework.web.context.bens.annotation
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型控制器注释绑定工厂注释
// * @Date: 2020/6/1:11:42
// */
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@BeanCollectionContainer(
//        value = IHttpControllerContainer.class,
//        addMethod = "addController",
//        removeMethod = "removeController",
//        containsMethod = "containsController")
//public @interface TypeAnnotationControllerBeanFactory {
//}