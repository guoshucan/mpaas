//package ghost.framework.core.application.event;
//
//import com.google.common.base.Objects;
//import ghost.framework.context.application.*;
//import ghost.framework.context.application.event.*;
//
//import java.util.function.Consumer;
//
///**
// * package: ghost.framework.core.application.event.container
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用事件处理器容器
// * 处理应用事件收发的容器类
// * @Date: 2020/2/3:12:23
// */
//public class ApplicationEventProcessorContainer extends AbstractApplicationEventContainer implements IApplicationEventProcessorContainer {
//    /**
//     * 初始化默认事件监听容器
//     *
//     * @param app 应用接口
//     */
//    public ApplicationEventProcessorContainer(IApplication app) {
////        super();
//        this.app = app;
//    }
//
//    /**
//     * 应用接口
//     */
//    private IApplication app;
//
//    /**
//     * 推送事件
//     *
//     * @param event the event to publish
//     */
//    @Override
//    public void publishEvent(AbstractApplicationEvent event) {
//        //判断是否为异步事件
//        if(event instanceof IAsyncApplicationEvent){
//            //异步事件处理
//            AsyncApplicationEvent applicationEvent = (AsyncApplicationEvent)event;
//        }else {
//            //同步事件处理
//            synchronized (eventListeners) {
//                eventListeners.forEach(new Consumer<ApplicationEventListener>() {
//                    @Override
//                    public void accept(ApplicationEventListener eventListener) {
//                        if (event.isHandle()) {
//                            return;
//                        }
//                        eventListener.onApplicationEvent(event);
//                    }
//                });
//            }
//        }
////        switch (event.getEventType()) {
////            case All:
////                if (log.isDebugEnabled()) {
////                    log.debug("publishEvent:All:" + event.toString());
////                }
////                synchronized (eventListeners) {
////                    eventListeners.forEach(new Consumer<ApplicationEventListener>() {
////                        @Override
////                        public void accept(ApplicationEventListener eventListener) {
////                            if (event.isHandle()) {
////                                return;
////                            }
////                            eventListener.onApplicationEvent(event);
////                        }
////                    });
////                }
////                return;
////            case Application:
////                if (log.isDebugEnabled()) {
////                    log.debug("publishEvent:Application:" + event.toString());
////                }
////                synchronized (eventListeners) {
////                    eventListeners.forEach(new Consumer<ApplicationEventListener>() {
////                        @Override
////                        public void accept(ApplicationEventListener eventListener) {
////                            if (eventListener instanceof IApplicationEventProcessorContainer) {
////                                return;
////                            }
////                            if (event.isHandle()) {
////                                return;
////                            }
////                            eventListener.onApplicationEvent(event);
////                        }
////                    });
////                }
////                return;
////            case Module:
////                if (log.isDebugEnabled()) {
////                    log.debug("publishEvent:Module:" + event.toString());
////                }
////                synchronized (eventListeners) {
////                    eventListeners.forEach(new Consumer<ApplicationEventListener>() {
////                        @Override
////                        public void accept(ApplicationEventListener eventListener) {
////                            //排除模块事件监听容器
////                            if (eventListener instanceof IApplicationEventProcessorContainer) {
////                                return;
////                            }
////                            if (event.isHandle()) {
////                                return;
////                            }
////                            eventListener.onApplicationEvent(event);
////                        }
////                    });
////                }
////                return;
////        }
//    }
//
////    @Override
////    public void onApplicationEvent(AbstractApplicationEvent event) {
////        //同步事件处理
////        synchronized (eventListeners) {
////            eventListeners.forEach(new Consumer<ApplicationEventListener>() {
////                @Override
////                public void accept(ApplicationEventListener eventListener) {
////                    if (event.isHandle()) {
////                        return;
////                    }
////                    eventListener.onApplicationEvent(event);
////                }
////            });
////        }
////        synchronized (eventListeners) {
////            eventListeners.forEach(new Consumer<ApplicationEventListener>() {
////                @Override
////                public void accept(ApplicationEventListener eventListener) {
////                    switch (event.getEventType()) {
////                        case All:
////                            if (log.isDebugEnabled()) {
////                                log.debug("onApplicationEvent:All:" + event.toString() + ":" + eventListener.toString());
////                            }
////                            //接收源主题解析
////                            onApplicationEventSource(event, eventListener);
////                            return;
////                        case Module:
////                            if (log.isDebugEnabled()) {
////                                log.debug("onApplicationEvent:Module:" + event.toString() + ":" + eventListener.toString());
////                            }
////                            //接收源主题解析
////                            onApplicationEventSource(event, eventListener);
////                            return;
////                    }
////                }
////            });
////        }
////    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ApplicationEventProcessorContainer that = (ApplicationEventProcessorContainer) o;
//        return Objects.equal(app, that.app);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(app);
//    }
//
//    @Override
//    public String toString() {
//        return "ApplicationEventListenerContainer{" +
//                "app=" + (app == null ? "" : app.getName()) +
//                ", eventListeners=" + eventListeners.size() +
//                ", eventPublishers=" + eventPublishers.size() +
//                '}';
//    }
//
//    @Override
//    public void onApplicationEvent(AbstractApplicationEvent event) {
//
//    }
//}