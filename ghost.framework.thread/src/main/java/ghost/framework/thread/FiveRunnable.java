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
 * @param <E> 泛型对象。
 */
public abstract class FiveRunnable <A, B ,C, D, E> extends FourRunnable<A, B ,C, D> {
    public FiveRunnable(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        this.e = e;
    }

    private E e;

    public E getE() {
        return e;
    }
}