//package ghost.framework.core.event.order.container;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.bean.factory.AbstractEventFactoryContainer;
//import ghost.framework.core.event.order.IOrderEventTargetHandle;
//import ghost.framework.core.event.order.factory.IOrderEventFactory;
//
//import java.util.ArrayList;
//
///**
// * package: ghost.framework.core.event.order.container
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:排序事件监听事件容器类
// * @Date: 15:05 2020/1/26
// */
//@Component
//public class DefaultOrderEventListenerFactoryContainer<
//        O extends ICoreInterface,
//        T extends Object,
//        E extends IOrderEventTargetHandle<O, T>,
//        L extends Object
//        >
//        extends AbstractEventFactoryContainer<L>
//        implements IOrderEventListenerFactoryContainer<O, T, E, L> {
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param parent 父级类事件监听容器
//     */
//    public DefaultOrderEventListenerFactoryContainer(@Application @Autowired @Nullable IOrderEventListenerFactoryContainer<O, T, E, L> parent) {
//        this.parent = parent;
//    }
//
//    /**
//     * 排序事件监听事件容父级接口
//     */
//    private IOrderEventListenerFactoryContainer<O, T, E, L> parent;
//
//    /**
//     * 获取排序事件监听事件容父级接口
//     *
//     * @return
//     */
//    @Override
//    public IOrderEventListenerFactoryContainer<O, T, E, L> getParent() {
//        return parent;
//    }
//
//    /**
//     * 排序事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void orderEvent(E event) {
//        this.getLog().info("loader:" + event.toString());
//        //获取执行列表
//            //遍历事件监听容器列表
//            for (L l : new ArrayList<>(this)) {
//                //判断是否执行接口
//                if (l instanceof Class) {
//                    //获取排序事件工厂接口
//                    //判断是否有效获取到排序事件工厂接口，比如在模块的拥有者去获取接口，但是接口又属
//                    //IOrderEventFactory factory = event.getOwner().getNullableBean((Class<?>) l);
//                    //判断是否有效获取到排序事件工厂接口，比如在模块的拥有者去获取接口，但是接口又属
//                    //if (factory != null) {
//                    IOrderEventFactory factory = (IOrderEventFactory) event.getOwner().getBean((Class<?>) l);
//                    //执行事件
//                    factory.orderEvent(event);
//                    //判断事件是否已经处理
//                    if (event.isHandle()) {
//                        return;
//                    }
//                    //}
//                }
//                //对象类型处理
//                if (IOrderEventFactory.class.isAssignableFrom(l.getClass())) {
//                    //执行对象绑定
//                    ((IOrderEventFactory) l).orderEvent(event);
//                    //判断事件是否已经处理
//                    if (event.isHandle()) {
//                        return;
//                    }
//            }
//        }
//        //执行父级
//        if (this.parent != null) {
//            this.parent.orderEvent(event);
//        }
//    }
//}