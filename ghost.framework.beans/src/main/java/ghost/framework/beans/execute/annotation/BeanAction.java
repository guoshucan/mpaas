//package ghost.framework.beans.execute.annotation;
//
//import ghost.framework.beans.enums.Action;
//
//import java.lang.annotation.*;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:绑定动作注释
// * @Date: 17:42 2019-07-28
// */
//@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface BeanAction {
//    /**
//     * 初始化注释调用模式
//     * 默认未结束时调用初始化函数模式
//     * @return
//     */
//    Action value() default Action.After;
//}