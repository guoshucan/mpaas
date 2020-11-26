package ghost.framework.util.generic;

/**
 * 三元素。
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class Three<A, B ,C> extends Two<A, B> {
    /**
     * 初始化三元素。
     *
     * @param a
     * @param b
     * @param c
     */
    public Three(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    private C c;

    public C getC() {
        return c;
    }
}
