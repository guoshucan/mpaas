package ghost.framework.context.event.container;
import java.io.Serializable;
/**
 * package: ghost.framework.core.event.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:基础事件类
 * @Date: 20:09 2020/2/2
 */
public abstract class AbstractEvent  implements IEvent, Serializable {
    private static final long serialVersionUID = 4769092275284078321L;
//    /**
//     * 初始化事件类
//     */
//    public AbstractEvent(O source) {
//        super(source);
//    }

    /**
     * 初始化事件类
     */
    public AbstractEvent() {

    }

    /**
     * 初始化事件类
     *
     * @param handle 事件是否已经处理
     */
    public AbstractEvent(boolean handle) {
        this.handle = handle;
    }

    /**
     * 初始化事件类
     *
     * @param handle           事件是否已经处理
     * @param applicationEvent 是否为应用事件
     * @param asyncEvent       是否为异步事件
     */
    public AbstractEvent(boolean handle, boolean applicationEvent, boolean asyncEvent) {
        this.handle = handle;
        this.applicationEvent = applicationEvent;
        this.asyncEvent = asyncEvent;
    }

    /**
     * 初始化事件类
     *
     * @param applicationEvent 是否为应用事件
     * @param asyncEvent       是否为异步事件
     */
    public AbstractEvent(boolean applicationEvent, boolean asyncEvent) {
        this.applicationEvent = applicationEvent;
        this.asyncEvent = asyncEvent;
    }

    /**
     * 初始化事件类
     *
     * @param topic            事件主题
     * @param applicationEvent 是否为应用事件
     * @param asyncEvent       是否为异步事件
     */
    public AbstractEvent(String[] topic, boolean applicationEvent, boolean asyncEvent) {
        this.topic = topic;
        this.applicationEvent = applicationEvent;
        this.asyncEvent = asyncEvent;
    }

    /**
     * 初始化事件类
     *
     * @param topic            事件主题
     * @param applicationEvent 是否为应用事件
     * @param asyncEvent       是否为异步事件
     */
    public AbstractEvent(String topic, boolean applicationEvent, boolean asyncEvent) {
        this.topic = new String[]{topic};
        this.applicationEvent = applicationEvent;
        this.asyncEvent = asyncEvent;
    }

    /**
     * 设置事件主题
     *
     * @param topic
     */
    @Override
    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    /**
     * 事件主题
     */
    private String[] topic;

    /**
     * 获取事件主题
     *
     * @return
     */
    @Override
    public String[] getTopic() {
        return topic;
    }

    /**
     * 获取是否为应用事件
     *
     * @return
     */
    @Override
    public boolean isApplicationEvent() {
        return applicationEvent;
    }

    /**
     * 是否为应用事件
     */
    private boolean applicationEvent;

    /**
     * 设置是否为应用事件
     *
     * @param applicationEvent
     */
    @Override
    public void setApplicationEvent(boolean applicationEvent) {
        this.applicationEvent = applicationEvent;
    }

    /**
     * 获取是否为异步事件
     *
     * @return
     */
    @Override
    public boolean isAsyncEvent() {
        return asyncEvent;
    }

    /**
     * 是否为异步事件
     */
    private boolean asyncEvent;

    /**
     * 设置是否为异步事件
     *
     * @param asyncEvent
     */
    @Override
    public void setAsyncEvent(boolean asyncEvent) {
        this.asyncEvent = asyncEvent;
    }

    /**
     * 获取事件是否已经处理
     *
     * @return
     */
    @Override
    public boolean isHandle() {
        return handle;
    }

    /**
     * 是否已经处理
     */
    private boolean handle;

    /**
     * 设置事件是否已经处理
     *
     * @param handle
     */
    @Override
    public void setHandle(boolean handle) {
        this.handle = handle;
    }
}