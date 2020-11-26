package ghost.framework.thread;

/**
 * 线程运行接口。
 * @param <A> 泛型对象。
 */
public abstract class OneRunnable<A> implements Runnable {
    /**
     * 初始化线程运行接口。
     * @param a
     */
    public OneRunnable(A a) {
        this.a = a;
    }
    private A a;

    /**
     * 获取泛型对象。
     * @return
     */
    public A getA() {
        return a;
    }
}
