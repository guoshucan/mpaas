//package ghost.framework.core.bean;
//
//import ghost.framework.context.bean.IBean;
//import ghost.framework.beans.execute.annotation.BeanAction;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.beans.lang.SimpleList;
//import ghost.framework.context.bean.utils.BeanUtil;
//import ghost.framework.context.event.IEventTargetHandle;
//import org.slf4j.Logger;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:内容绑定工具类型
// * @Date: 11:31 2020/1/3
// */
//public class ContextBeanUtil{
////    /**
////     * 绑定列表容器操作
////     * @param event 事件对象
////     * @param action 绑定动作枚举
////     * @param log 日志
////     * @param <O> 发起者类型
////     * @param <T> 绑定定义类型
////     */
////    public static <O extends IBean, T extends Object> void beanListContainer(IEventTargetHandle<O, T> event, BeanAction.Action action, Logger log) {
////        //获取绑定定义类型
////        Class<?> c = event.getTarget().getClass();
////        //判断绑定类型注释，SimpleList容器注释处理
////        if (c.isAnnotationPresent(BeanListContainer.class)) {
////            //获取绑定列表容器注释
////            BeanListContainer listContainer = c.getAnnotation(BeanListContainer.class);
////            //判断是否注释绑定动作
////            if (BeanUtil.notExistBeanListContainerAction(listContainer, action)) {
////                //绑定动作无效
////                return;
////            }
////            SimpleList list = event.positionOwner().getNullableBean(listContainer.value());
////            if (listContainer.error() && list == null) {
////                throw new IllegalArgumentException("BeanListContainer get bean null error:" + listContainer.value().getName());
////            }
////            if (list.contains(event.getTarget())) {
////                log.warn("BeanListContainer is:" + listContainer.value().getName());
////            } else {
////                list.put(event.getTarget());
////            }
////        }
////    }
////    /**
////     * 添加注释绑定
////     *
////     * @param bean 绑定接口
////     * @param c    绑定类型
////     */
////    public static void addAnnotationBeanEvent(IBean bean, Class<?> c) {
////        //获取事件监听工厂容器接口
////        IAnnotationBeanEventListenerFactoryContainer annotationEventListenerFactoryContainer = bean.getNullableBean(IAnnotationBeanEventListenerFactoryContainer.class);
////        //判断是否有事件处理接口
////        if (annotationEventListenerFactoryContainer != null) {
////            //执行注释事件
////            annotationEventListenerFactoryContainer.put(new EventTargetHandle(bean, c));
////        }
////    }
//
////    /**
////     * 添加绑定对象事件
////     *
////     * @param bean 绑定接口
////     * @param o    绑定对象
////     */
////    public static void addBeanObjectEvent(IBean bean, Object o) {
////        //获取创建对象事件监听容器
////        IObjectEventListenerFactoryContainer objectEventListenerFactoryContainer = bean.getNullableBean(IObjectEventListenerFactoryContainer.class);
////        //新建对象前事件
////        if (objectEventListenerFactoryContainer == null) {
////            bean.addBean(o);
////        } else {
////            objectEventListenerFactoryContainer.newInstanceObject(new EventTargetHandle(bean, o));
////        }
////    }
//
////    /**
////     * 添加绑定事件
////     *
////     * @param bean 绑定接口
////     * @param o    绑定对象
////     * @param name 绑定名称，如果未指定绑定名称侧为null
////     */
////    public static void addBeanEvent(IBean bean, Object o, String name){
////        //获取绑定工厂容器接口
////        IBeanEventListenerFactoryContainer beanEventListenerFactoryContainer = bean.getNullableBean(IBeanEventListenerFactoryContainer.class);
////        //判断是否指定使用绑定工厂容器进行绑定
////        if (beanEventListenerFactoryContainer == null || beanEventListenerFactoryContainer.isEmpty()) {
////            //使用默认绑定
////            bean.addBean(o);
////        } else {
////            //使用绑定工厂容器绑定
////            beanEventListenerFactoryContainer.bean(new BeanEventTargetHandle(bean, o, name));
////        }
////    }
//}