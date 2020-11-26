package ghost.framework.util.generic;

/**
 * 五元素。
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 */
public class Five<A, B ,C, D, E> extends Four<A, B ,C, D> {
    /**
     * 初始化五元素。
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     */
    public Five(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        this.e = e;
    }

    private E e;

    public E getE() {
        return e;
    }
}
