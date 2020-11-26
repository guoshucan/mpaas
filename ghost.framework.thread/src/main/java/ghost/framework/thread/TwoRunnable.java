package ghost.framework.thread;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:
 * @Date: 13:15 2018/4/15
 */
/**
 * 线程运行接口。
 * @param <A> 泛型对象。
 * @param <B> 泛型对象。
 */
public abstract class TwoRunnable<A, B> extends OneRunnable<A> {
    public TwoRunnable(A a, B b) {
        super(a);
        this.b = b;
    }

    private B b;

    /**
     * 获取B对象。
     *
     * @return
     */
    public B getB() {
        return b;
    }
}
