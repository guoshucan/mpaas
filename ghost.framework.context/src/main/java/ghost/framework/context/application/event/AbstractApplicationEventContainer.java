//package ghost.framework.context.application.event;
//
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.EventListener;
//import java.util.List;
//
///**
// * package: ghost.framework.context.application
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用事件容器基础类
// * 继承 {@link ApplicationEventPublisher} 事件推送接口
// * 继承 {@link ApplicationEventListener} 事件监听接口
// * 继承 {@link EventListener} java自带默认事件监听接口
// * @Date: 2020/2/16:1:17
// */
//public abstract class AbstractApplicationEventContainer
//        implements IApplicationEventContainer {
////    /**
////     * 初始化应用事件容器基础类
////     */
////    protected AbstractApplicationEventContainer() {
////        this.addEventContainer(this);
////    }
//
//    /**
//     * 日志
//     */
//    protected Logger log = Logger.getLogger(this.getClass());
//    /**
//     * 应用事件监听列表
//     */
//    protected List<ApplicationEventListener> eventListeners = new ArrayList<>(5);
//    /**
//     * 应用事件推送列表
//     */
//    protected List<ApplicationEventPublisher> eventPublishers = new ArrayList<>(5);
//
//    /**
//     * 添加事件容器接口
//     *
//     * @param eventContainer 事件容器接口
//     */
//    @Override
//    public void addEventContainer(IApplicationEventContainer eventContainer) {
//        this.addEventPublisher(eventContainer);
//        this.addEventListener(eventContainer);
//    }
//
//    /**
//     * 删除事件容器接口
//     *
//     * @param eventContainer 事件容器接口
//     * @return
//     */
//    @Override
//    public IApplicationEventContainer removeEventContainer(IApplicationEventContainer eventContainer) {
//        this.removeEventPublisher(eventContainer);
//        this.removeEventListener(eventContainer);
//        return eventContainer;
//    }
//
//    /**
//     * 删除事件推送接口
//     *
//     * @param eventPublisher 事件推送接口
//     * @return
//     */
//    @Override
//    public boolean removeEventPublisher(ApplicationEventPublisher eventPublisher) {
//        if (this.log.isDebugEnabled()) {
//            this.log.debug("removeEventPublisher:" + eventPublisher.toString());
//        }
//        synchronized (eventPublishers) {
//            return eventPublishers.remove(eventPublisher);
//        }
//    }
//
//    /**
//     * 添加事件推送接口
//     *
//     * @param eventPublisher 事件推送接口
//     * @return
//     */
//    @Override
//    public boolean addEventPublisher(ApplicationEventPublisher eventPublisher) {
//        if (this.log.isDebugEnabled()) {
//            this.log.debug("addEventPublisher:" + eventPublisher.toString());
//        }
//        synchronized (eventPublishers) {
//            return eventPublishers.add(eventPublisher);
//        }
//    }
//
//    /**
//     * 删除事件监听接口
//     *
//     * @param eventListener 事件监听接口
//     * @return
//     */
//    @Override
//    public boolean removeEventListener(ApplicationEventListener eventListener) {
//        if (this.log.isDebugEnabled()) {
//            this.log.debug("removeEventListener:" + eventListener.toString());
//        }
//        synchronized (eventListeners) {
//            return eventListeners.remove(eventListener);
//        }
//    }
//
//    /**
//     * 添加事件监听接口
//     *
//     * @param eventListener 事件监听接口
//     * @return
//     */
//    @Override
//    public boolean addEventListener(ApplicationEventListener eventListener) {
//        if (this.log.isDebugEnabled()) {
//            this.log.debug("addEventListener:" + eventListener.toString());
//        }
//        synchronized (eventListeners) {
//            return eventListeners.add(eventListener);
//        }
//    }
//
//    protected void onApplicationEventSource(AbstractApplicationEvent event, ApplicationEventListener eventListener) {
//        eventListener.onApplicationEvent(event);
//    }
//}