//package ghost.framework.core.application.event;
//
//import com.google.common.base.Objects;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.application.*;
//import ghost.framework.context.application.event.AbstractApplicationEventContainer;
//import ghost.framework.beans.application.event.AbstractApplicationEvent;
//import ghost.framework.context.application.event.ApplicationEventListener;
//import ghost.framework.context.module.IModule;
//import ghost.framework.context.module.IModuleEventProcessorContainer;
//
//import java.util.function.Consumer;
//
///**
// * package: ghost.framework.core.application.event
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块事件处理器容器
// * @Date: 2020/2/16:0:12
// */
//@Component
//public class ModuleEventProcessorContainer
//        extends AbstractApplicationEventContainer
//        implements IModuleEventProcessorContainer {
//    /**
//     * 初始化默认事件监听容器
//     *
//     * @param module 模块接口
//     */
//    public ModuleEventProcessorContainer(@Autowired IApplication app, @Autowired IModule module) {
////        super();
//        this.app = app;
//        this.module = module;
//    }
//
//    private IModule module;
//    private IApplication app;
//
//    /**
//     * 推送事件
//     * @param event 事件对象
//     */
//    @Override
//    public void publishEvent(AbstractApplicationEvent event) {
//        switch (event.getEventType()) {
//            case ModuleInternal:
//                if (log.isDebugEnabled()) {
//                    log.debug("publishEvent:ModuleInternal:" + event.toString());
//                }
//                onApplicationEvent(event);
//                return;
//            default:
//                if (log.isDebugEnabled()) {
//                    log.debug("publishEvent:default:" + event.toString());
//                }
//                this.app.publishEvent(event);
//                return;
//        }
//    }
//
//    /**
//     * 接收时间
//     * @param event 事件对象
//     */
//    @Override
//    public void onApplicationEvent(AbstractApplicationEvent event) {
//        synchronized (eventListeners) {
//            eventListeners.forEach(new Consumer<ApplicationEventListener>() {
//                @Override
//                public void accept(ApplicationEventListener eventListener) {
//                    switch (event.getEventType()) {
//                        case All:
//                            if (log.isDebugEnabled()) {
//                                log.debug("onApplicationEvent:All:" + event.toString() + ":" + eventListener.toString());
//                            }
//                            //接收源主题解析
//                            onApplicationEventSource(event, eventListener);
//                            return;
//                        case Module:
//                            if (log.isDebugEnabled()) {
//                                log.debug("onApplicationEvent:Module:" + event.toString() + ":" + eventListener.toString());
//                            }
//                            //接收源主题解析
//                            onApplicationEventSource(event, eventListener);
//                            return;
//                        case ModuleInternal:
//                            if (log.isDebugEnabled()) {
//                                log.debug("onApplicationEvent:ModuleInternal:" + event.toString() + ":" + eventListener.toString());
//                            }
//                            //接收源主题解析
//                            onApplicationEventSource(event, eventListener);
//                            return;
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ModuleEventProcessorContainer that = (ModuleEventProcessorContainer) o;
//        return Objects.equal(module, that.module) &&
//                Objects.equal(app, that.app);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(module, app);
//    }
//
//    @Override
//    public String toString() {
//        return "ModuleEventListenerContainer{" +
//                "module=" + (module == null ? "" : module.getName()) +
//                ", app=" + (app == null ? "" : app.getName()) +
//                ", eventListeners=" + eventListeners.size() +
//                ", eventPublishers=" + eventPublishers.size() +
//                '}';
//    }
//}