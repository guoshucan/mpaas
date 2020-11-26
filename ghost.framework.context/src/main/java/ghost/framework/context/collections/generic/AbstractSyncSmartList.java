package ghost.framework.context.collections.generic;

import ghost.framework.context.thread.GetSyncRoot;

/**
 * package: ghost.framework.context.collections.generic
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:同步简单列表基础类
 * @Date: 2020/6/14:16:09
 */
public abstract class AbstractSyncSmartList<L> extends AbstractSmartList<L> implements GetSyncRoot {
    public AbstractSyncSmartList() {
        super();
    }

    public AbstractSyncSmartList(int initialCapacity) {
        super(initialCapacity);
    }
    @Override
    public boolean add(L l) {
        synchronized (root) {
            return super.add(l);
        }
    }

    @Override
    public boolean remove(L l) {
        synchronized (root) {
            return super.remove(l);
        }
    }

    @Override
    public int size() {
        synchronized (root) {
            return super.size();
        }
    }

    @Override
    public boolean contains(L l) {
        synchronized (root) {
            return super.contains(l);
        }
    }
    private final Object root = new Object();

    @Override
    public final Object getSyncRoot() {
        return root;
    }
}