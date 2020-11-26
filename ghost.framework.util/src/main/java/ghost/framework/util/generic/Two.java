package ghost.framework.util.generic;

import java.util.Objects;

/**
 * 两个元素。
 * @param <A>
 * @param <B>
 */
public class Two<A, B> {
    /**
     * 初始化两个元素。
     *
     * @param a
     * @param b
     */
    public Two(A a, B b) {
        this.a = a;
        this.b = b;
    }

    private A a;
    private B b;

    /**
     * 获取A对象。
     *
     * @return
     */
    public A getA() {
        return a;
    }

    /**
     * 获取B对象。
     *
     * @return
     */
    public B getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Two<?, ?> two = (Two<?, ?>) o;
        return Objects.equals(a, two.a) &&
                Objects.equals(b, two.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "Two{" +
                "a=" + (a == null ? "" : a.toString()) +
                ", b=" + (b == null ? "" : b.toString()) +
                '}';
    }
}