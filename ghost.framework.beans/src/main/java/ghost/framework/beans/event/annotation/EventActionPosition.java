//package ghost.framework.beans.event.annotation;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:事件动作位置注释
// * @Date: 22:42 2019/12/21
// */
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface EventActionPosition {
//    /**
//     * 添加事件监听位置注释
//     * 默认为 {@link Position ::Before}
//     *
//     * @return
//     */
//    Position add() default Position.before;
//    /**
//     * 删除事件监听位置注释
//     * 默认为 {@link Position ::Before}
//     *
//     * @return
//     */
//    Position remove() default Position.before;
//
//    /**
//     * 事件监听位置枚举
//     */
//    enum Position {
//        /**
//         * 事件前
//         */
//        before,
//        /**
//         * 事件后
//         */
//        after,
//        /**
//         * 不做任何bean操作
//         */
//        not
//    }
//}