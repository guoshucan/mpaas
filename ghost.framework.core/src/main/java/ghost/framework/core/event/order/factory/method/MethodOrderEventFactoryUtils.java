//package ghost.framework.core.event.order.factory.method;
//
//import ghost.framework.context.bean.utils.BeanUtil;
//import ghost.framework.context.bean.BeanMethod;
//import ghost.framework.beans.annotation.order.FirstOrder;
//import ghost.framework.beans.annotation.order.LastOrder;
//import ghost.framework.beans.utils.OrderAnnotationUtil;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.order.IMethodOrderEventTargetHandle;
//import ghost.framework.core.event.order.container.IOrderEventListenerFactoryContainer;
//import ghost.framework.util.ReflectUtil;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
///**
// * package: ghost.framework.core.event.method.container
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:函数排序事件监听工厂工具
// * @Date: 16:34 2020/1/26
// */
//public final class MethodOrderEventFactoryUtils {
//    /**
//     * 排序事件
//     *
//     * @param event 事件对象
//     */
//    public static final <O extends ICoreInterface, T extends Object, E extends IMethodOrderEventTargetHandle<O, T>> List<List<BeanMethod>> orderEvent(E event) {
//        //获取排序事件监听工厂容器接口
//        IOrderEventListenerFactoryContainer container = event.getOwner().getNullableBean(IOrderEventListenerFactoryContainer.class);
//        //判断是否有容器接口
//        if (container == null) {
//            //没有容器接口处理
//            return orderListSort(classOrderEvent(ReflectUtil.getObjectClass(event.getTarget())));
//        }
//        //函数排序事件处理
//        container.orderEvent(event);
//        event.setHandle(true);
//        //返回遍历函数执行列表
//        return event.getBeanMethodList();
//    }
//
//    /**
//     * @param c
//     * @return
//     */
//    public static List<BeanMethod> classOrderEvent(Class<?> c) {
//        List<BeanMethod> list = new ArrayList<>();
//        //获取对象函数列表
//        for (Method method : c.getDeclaredMethods()) {
//            //判断只有注释的函数才添加
//            if (ReflectUtil.invalidAnnotationMethod(method)) {
//                continue;
//            }
//            list.add(new BeanMethod(method));
//        }
//        return list;
//    }
//
//    /**
//     * 获取绑定函数执行位置列表排序列表
//     *
//     * @param methodList
//     * @return
//     */
//    public static List<List<BeanMethod>> orderListSort(List<BeanMethod> methodList) {
//        List<List<BeanMethod>> list = new ArrayList<>();
//        //a
//        List<BeanMethod> a = new ArrayList<>();
//        list.add(a);
//        for (BeanMethod method : methodList) {
//            if (method.getMethod().isAnnotationPresent(FirstOrder.class)) {
//                a.add(method);
//            }
//        }
//        BeanUtil.beanMethodListSort(a);
//        //b
//        List<BeanMethod> b = new ArrayList<>();
//        list.add(b);
//        for (BeanMethod method : methodList) {
//            if (method.getMethod().isAnnotationPresent(FirstOrder.class) || method.getMethod().isAnnotationPresent(LastOrder.class)) {
//                continue;
//            }
//            b.add(method);
//        }
//        BeanUtil.beanMethodListSort(b);
//        //c
//        List<BeanMethod> c = new ArrayList<>();
//        list.add(c);
//        for (BeanMethod method : methodList) {
//            if (method.getMethod().isAnnotationPresent(LastOrder.class)) {
//                c.add(method);
//            }
//        }
//        BeanUtil.beanMethodListSort(c);
//        return list;
//    }
//}