package ghost.framework.util.generic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 7:14 2018-06-16
 */
public class HashMapLock<V, K> extends HashMap<V, K> {
    private Lock lock = new ReentrantLock();

    @Override
    public K replace(V key, K value) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.replace(key, value);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.replace(key, value);
    }

    @Override
    public K put(V key, K value) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.put(key, value);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.put(key, value);
    }

    @Override
    public boolean replace(V key, K oldValue, K newValue) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.replace(key, oldValue, newValue);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.replace(key, oldValue, newValue);
    }

    @Override
    public K merge(V key, K value, BiFunction<? super K, ? super K, ? extends K> remappingFunction) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.merge(key, value, remappingFunction);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.merge(key, value, remappingFunction);
    }

    @Override
    public K remove(Object key) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.remove(key);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends V, ? extends K> m) {
        if (this.isLock) {
            this.lock.lock();
            try {
                super.putAll(m);
            } finally {
                this.lock.unlock();
            }
        } else
            super.putAll(m);
    }

    @Override
    public K putIfAbsent(V key, K value) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.putIfAbsent(key, value);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.putIfAbsent(key, value);
    }

    @Override
    public void clear() {
        if (this.isLock) {
            this.lock.lock();
            try {
                super.clear();
            } finally {
                this.lock.unlock();
            }
        } else
            super.clear();
    }

    @Override
    public void replaceAll(BiFunction<? super V, ? super K, ? extends K> function) {
        if (this.isLock) {
            this.lock.lock();
            try {
                super.replaceAll(function);
            } finally {
                this.lock.unlock();
            }
        } else
            super.replaceAll(function);
    }
    @Override
    public boolean remove(Object key, Object value) {
        if (this.isLock) {
            this.lock.lock();
            try {
                return super.remove(key, value);
            } finally {
                this.lock.unlock();
            }
        } else
            return super.remove(key, value);
    }
    /**
     * 是否锁定。
     */
    private boolean isLock;

    /**
     * 开始锁定。
     */
    public synchronized void lock() {
        this.isLock = true;
        this.lock.lock();
    }

    /**
     * 解除锁定。
     */
    public synchronized void unlock() {
        this.lock.unlock();
        this.isLock = false;
    }
}