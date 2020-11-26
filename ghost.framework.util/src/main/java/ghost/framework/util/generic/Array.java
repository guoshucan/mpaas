package ghost.framework.util.generic;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:22 2019-06-08
 */
public class Array<T>{
    private T[] data;
    private int size;

    //构造函数
    public Array(int capacity) {
        data = (T[]) new Object[capacity];
        size = 0;
    }

    //无参数的构造函数，默认数组的容量为10
    public Array() {
        this(10);
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getCapacity() {
        return data.length;
    }

    // O(1)
    public void addLast(T t) {
        add(size, t);
    }

    // O(n)
    public void addFirst(T t) {
        add(0, t);
    }

    // O(n/2) = O(n)
    public void add(int index, T t) {
        if (size >= data.length)
            resize(2 * data.length);
        if (index < 0 || index > size)
            throw new IllegalArgumentException("Add failed.index is error.");
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }
        data[index] = t;
        size++;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("Array: size = %d, capacity = %d\n", size, data.length));
        res.append("[");
        for (int i = 0; i < size; i++) {
            res.append(data[i]);
            if (i != size - 1)
                res.append(", ");
        }
        res.append("]");
        return res.toString();
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed. Index is illegal");
        return data[index];
    }

    public T getFirst() {
        return get(size - 1);
    }

    public T getLast() {
        return get(0);
    }

    void set(int index, T t) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed. Index is illegal");
        data[index] = t;
    }

    public boolean contains(T t) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(t))
                return true;
        }
        return false;
    }

    public int find(T t) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(t))
                return i;
        }
        return -1;
    }

    public T remove(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed. Index is illegal");
        T res = data[index];
        for (int i = index; i < size; i++) {
            data[i] = data[i + 1];
        }
        size--;
        //释放空间，也可以不写
        //loitering objects != memory leak
        data[size] = null;
        if (size == data.length / 4 && data.length / 2 != 0)
            resize(data.length / 2);
        return res;
    }

    public T removeFirst() {
        return remove(0);
    }

    public T removeLast() {
        return remove(size - 1);
    }

    //只删除了一个e，并不能保证删除了全部e
    public void removeElement(T t) {
        int index = find(t);
        if (index != -1)
            remove(index);
    }

    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }
}
