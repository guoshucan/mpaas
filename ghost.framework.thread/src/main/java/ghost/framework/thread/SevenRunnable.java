package ghost.framework.thread;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:41 2018-07-21
 */

/**
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 * @param <F>
 * @param <G>
 */
public abstract class SevenRunnable <A, B ,C, D, E, F, G> extends SixRunnable <A, B ,C, D, E, F> {
    public SevenRunnable(A a, B b, C c, D d, E e, F f, G g) {
        super(a, b, c, d, e, f);
        this.g = g;
    }

    private G g;

    public G getG() {
        return g;
    }
}
