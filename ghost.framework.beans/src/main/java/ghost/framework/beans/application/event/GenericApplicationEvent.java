package ghost.framework.beans.application.event;

//import ghost.framework.context.event.IHandle;
//import ghost.framework.context.message.Topic;

import java.io.Serializable;
import java.util.EventObject;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:通用消息类
 * 继承 {@link EventObject} 以便消息序列化与类型基础服务java消息对象
 * 继承 {@link IHandle} 处理接口
 * 继承 {@link Topic} 主题接口
 * 继承 {@link Serializable} 序列化接口
 * @Date: 2020/2/16:0:26
 */
public class GenericApplicationEvent
        extends AbstractApplicationEvent {
    private static final long serialVersionUID = -4131924509177985483L;
    /**
     * 初始化应用事件基础类
     *
     * @param source  消息源对象
     * @param topic 指定事件主题
     */
    public GenericApplicationEvent(Object source, String topic) {
        super(source, topic);
    }
}