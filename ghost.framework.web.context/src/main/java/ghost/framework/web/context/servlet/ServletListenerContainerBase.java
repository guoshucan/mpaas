package ghost.framework.web.context.servlet;

import ghost.framework.beans.utils.OrderAnnotationUtil;

import java.util.*;
import java.util.function.Predicate;

/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Servlet内容会话事件监听容器
 * @Date: 2020/5/2:13:02
 */
public abstract class ServletListenerContainerBase<T extends EventListener> extends AbstractCollection<T> implements IServletContextListenerContainerBase {
    private List<T> listeners = new ArrayList<>();

    @Override
    public Iterator<T> iterator() {
        return listeners.iterator();
    }

    @Override
    public int size() {
        return listeners.size();
    }
    /**
     * 删除监听
     *
     * @param listenerClass 监听类型
     * @return
     */
    @Override
    public boolean remove(Class<? extends EventListener> listenerClass) {
        synchronized (listeners) {
            T del = null;
            try {
                for (T t : listeners) {
                    if (t.getClass().equals(listenerClass)) {
                        del = t;
                        break;
                    }
                }
                if (del != null) {
                    return listeners.remove(del);
                }
            } finally {
                if (del != null) {
                    OrderAnnotationUtil.listSort(listeners);
                }
            }
        }
        return false;
    }
    /**
     * 删除监听
     *
     * @param className 监听类型名称
     * @return
     */
    @Override
    public boolean remove(String className) {
        synchronized (listeners) {
            T del = null;
            try {
                for (T t : listeners) {
                    if (t.getClass().getName().equals(className)) {
                        del = t;
                        break;
                    }
                }
                if (del != null) {
                    return listeners.remove(del);
                }
            } finally {
                if (del != null) {
                    OrderAnnotationUtil.listSort(listeners);
                }
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        synchronized (listeners) {
            try {
                return listeners.remove(o);
            } finally {
                OrderAnnotationUtil.listSort(listeners);
            }
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (listeners) {
            try {
                return listeners.removeAll(c);
            } finally {
                OrderAnnotationUtil.listSort(listeners);
            }
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        synchronized (listeners) {
            try {
                return listeners.removeIf(filter);
            } finally {
                OrderAnnotationUtil.listSort(listeners);
            }
        }
    }

    @Override
    public boolean add(T t) {
        synchronized (listeners) {
            try {
                return listeners.add(t);
            } finally {
                OrderAnnotationUtil.listSort(listeners);
            }
        }
    }
}