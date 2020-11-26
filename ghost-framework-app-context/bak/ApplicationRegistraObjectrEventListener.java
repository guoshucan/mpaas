package ghost.framework.app.context.event.bak;//package ghost.framework.module.events;
//
//import ghost.framework.module.util.OrderUtil;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用注册对象事件监听接口
// * @Date: 18:06 2019-06-08
// */
//public interface ApplicationRegistraObjectrEventListener extends ModuleEventListener {
//    /**
//     * 类注对象
//     *
//     * @param value 对象列
//     */
//    default void registrarObjectEvent(Object value) {
//
//    }
//
//    /**
//     * 类注对象
//     *
//     * @param values 对象列表
//     */
//    default void registrarObjectsEvent(List<Object> values) {
//        for (Object o : OrderUtil.getObjectOrderList(values)) {
//            this.registrarObjectEvent(o);
//        }
//    }
//
//    /**
//     * 类注对象
//     *
//     * @param values 对象列表
//     */
//    default void registrarObjectsEvent(Object[] values) {
//        for (Object o : OrderUtil.getObjectOrderList(values)) {
//            this.registrarObjectEvent(o);
//        }
//    }
//}
