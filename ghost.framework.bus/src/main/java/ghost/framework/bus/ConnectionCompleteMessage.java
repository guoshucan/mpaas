package ghost.framework.bus;

/**
 * package: ghost.framework.bus
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:连接完成消息事件
 * @Date: 2020/6/13:9:29
 */
public class ConnectionCompleteMessage extends MessageEvent{
    private static final long serialVersionUID = -6037314982516068351L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ConnectionCompleteMessage(Object source) {
        super(source);
    }
}
