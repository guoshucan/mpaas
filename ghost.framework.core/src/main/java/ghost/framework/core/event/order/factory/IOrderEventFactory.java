package ghost.framework.core.event.order.factory;

import ghost.framework.context.bean.factory.IBeanFactory;
import ghost.framework.context.bean.factory.IOwnerBeanTargetHandle;

/**
 * package: ghost.framework.core.event.order.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:排序事件工厂接口
 * @Date: 15:03 2020/1/26
 */
public interface IOrderEventFactory<O, T, E extends IOwnerBeanTargetHandle<O, T>> extends IBeanFactory {
    /**
     * 排序事件
     * @param event 事件对象
     */
    void orderEvent(E event);
}