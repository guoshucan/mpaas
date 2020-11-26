package ghost.framework.bus;

import java.util.EventObject;

/**
 * package: ghost.framework.bus
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:消息事件
 * @Date: 2020/6/12:20:09
 */
public class MessageEvent extends EventObject {
    private static final long serialVersionUID = 8622699043489379387L;

    @Override
    public Object getSource() {
        return source;
    }
    private Object source;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MessageEvent(Object source) {
        super(source);
        this.source = source;
    }
}
