package ghost.framework.util.generic;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:23 2019-06-08
 */
public interface Queue<T> {
    int getSize();

    boolean isEmpty();

    void enqueue(T t);

    T dequeue();

    T getFront();

    T remove(int index);

    int index(T t);

    void add(int index, T t);
}
