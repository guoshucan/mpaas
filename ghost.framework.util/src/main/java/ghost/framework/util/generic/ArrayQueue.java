package ghost.framework.util.generic;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:24 2019-06-08
 */
public class ArrayQueue<T> implements Queue<T> {
    protected Array<T> array;

    public ArrayQueue(int capacity) {
        array = new Array<>(capacity);
    }

    @Override
    public int index(T t) {
        for (int i = 0; i < array.getSize(); i++) {
            if (array.get(i).equals(t)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayQueue() {
        array = new Array<>();
    }

    @Override
    public void add(int index, T t) {
        array.add(index, t);
    }

    @Override
    public T remove(int index) {
        return array.remove(index);
    }

    @Override
    public int getSize() {
        return array.getSize();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    public int getCapacity() {
        return array.getCapacity();
    }

    @Override
    public void enqueue(T t) {
        array.addLast(t);
    }

    @Override
    public T dequeue() {
        return array.removeFirst();
    }

    @Override
    public T getFront() {
        return array.getFirst();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Queue:");
        //队头
        res.append("front [");
        for (int i = 0; i < array.getSize(); i++) {
            res.append(array.get(i));
            if (i != array.getSize() - 1)
                res.append(", ");
        }
        //队尾
        res.append("] tail");
        return res.toString();
    }
}
