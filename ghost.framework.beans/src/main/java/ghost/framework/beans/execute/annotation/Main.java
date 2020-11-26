//package ghost.framework.beans.execute.annotation;
//
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.beans.configuration.annotation.ClassProperties;
//import ghost.framework.util.StringUtil;
//
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:运行函数注释
// * @Date: 10:20 2019-05-18
// */
//@Target({ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface Main {
//    /**
//     * main注释配置
//     * @return
//     */
//    ConfigurationProperties[] properties() default {};
//
//    /**
//     * 是否使用线程
//     * @return
//     */
//    boolean thread() default false;
//
//    /**
//     * 线程名称
//     * @return
//     */
//    String threadName() default "";
//    /**
//     * 选择配置
//     * @return
//     */
//    ClassProperties[] select() default {};
//}
