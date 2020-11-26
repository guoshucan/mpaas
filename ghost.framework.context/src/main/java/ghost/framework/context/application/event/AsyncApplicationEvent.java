package ghost.framework.context.application.event;

import ghost.framework.beans.application.event.GenericApplicationEvent;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:异步应用事件
 * @Date: 2020/2/16:1:54
 */
public class AsyncApplicationEvent
        extends GenericApplicationEvent
        implements IAsyncApplicationEvent {
    private static final long serialVersionUID = 7657118616483867140L;


    public AsyncApplicationEvent(Object source, String topic) {
        super(source, topic);
    }


    @Override
    public boolean isAsync() {
        return true;
    }
}
