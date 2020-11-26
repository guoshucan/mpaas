package ghost.framework.thread;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:21 2018-07-21
 */

/**
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @param <F>
 */
public abstract class SixRunnable <A, B ,C, D, E, F> extends FiveRunnable <A, B ,C, D, E> {
    public SixRunnable(A a, B b, C c, D d, E e, F f) {
        super(a, b, c, d, e);
        this.f = f;
    }

    private F f;

    public F getF() {
        return f;
    }
}
