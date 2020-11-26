package ghost.framework.thread;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:
 * @Date: 13:14 2018/4/15
 */
/**
 * 线程运行接口。
 * @param <A> 泛型对象。
 * @param <B> 泛型对象。
 * @param <C> 泛型对象。
 */
public abstract class ThreeRunnable<A, B ,C> extends TwoRunnable<A, B>{
    public ThreeRunnable(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    private C c;

    public C getC() {
        return c;
    }
}
