package ghost.framework.util.generic;

/**
 * 四个元素。
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class Four<A, B ,C, D> extends Three<A, B ,C> {
    /**
     * 初始化四个元素。
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public Four(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }

    private D d;

    public D getD() {
        return d;
    }
}
