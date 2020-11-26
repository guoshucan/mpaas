package ghost.framework.thread;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:返回对象。
 * @Date: 16:10 2018-06-16
 */
public final class ReturnObject {
    /**
     * 返回对象。
     */
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * 是否完成。
     */
    private boolean complete;

    public synchronized boolean isComplete() {
        return complete;
    }

    public synchronized void setComplete(boolean complete) {
        this.complete = complete;
    }
}
