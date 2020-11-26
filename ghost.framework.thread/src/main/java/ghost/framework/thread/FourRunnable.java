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
 * @param <C> 泛型对象。
 * @param <D> 泛型对象。
 */
public abstract class FourRunnable<A, B ,C, D> extends ThreeRunnable<A, B ,C> {
    public FourRunnable(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }

    private D d;

    public D getD() {
        return d;
    }
}
