package ghost.framework.util.generic;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:25 2019-06-08
 */
public class LoopQueue<T> implements Queue<T>{
    private T[] data;
    int front, tail;
    private int size;
    public LoopQueue(int capacity) {
        data = (T[])new Object[capacity+1];
        front = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public void add(int index, T t) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int index(T t) {
        return 0;
    }

    public LoopQueue() {
        this(10);
    }
    public int getCapacity() {
        return data.length-1;
    }
    @Override
    public boolean isEmpty() {
        return front == tail;
    }
    @Override
    public int getSize() {
        return size;
    }
    @Override
    public void enqueue(T t) {
        if((tail + 1) % data.length == front) {
            resize(getCapacity() * 2);
        }
        data[tail] = t;
        tail = (tail + 1) % data.length;
        size ++;
    }
    private void resize(int newCapacity) {
        T[] newData = (T[])new Object[newCapacity + 1];
        for(int i = 0 ; i < size ; i++)
            newData[i] = data[(i + front) % data.length];
        data = newData;
        front = 0;
        tail = size;
    }
    @Override
    public T dequeue() {
        if(isEmpty()) {
            throw new IllegalArgumentException("Cannot dequeue from an empty queue.");
        }
        T ret = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size --;
        if(size == getCapacity() / 4 && getCapacity() / 2 != 0) {
            resize(getCapacity()/2);
        }
        return ret;
    }
    @Override
    public T getFront() {
        if(isEmpty()) {
            throw new IllegalArgumentException("Queue is empty.");
        }
        return data[front];
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("Queue: size = %d, capacity = %d\n", size, getCapacity()));
        res.append("front [");
        for(int i = 0 ; i != tail ; i = (i+1)%data.length) {
            res.append(data[i]);
            if((i+1)%data.length != tail)
                res.append(", ");
        }
        res.append("] tail");
        return res.toString();
    }
}
