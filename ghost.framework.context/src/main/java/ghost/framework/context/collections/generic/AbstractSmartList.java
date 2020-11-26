package ghost.framework.context.collections.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.context.collections.generic
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:异步简单列表基础类
 * @Date: 2020/6/14:16:09
 */
public abstract class AbstractSmartList<L> implements ISmartList<L> {
    public AbstractSmartList() {
        list = new ArrayList<>(10);
    }

    public AbstractSmartList(int initialCapacity) {
        list = new ArrayList<>(initialCapacity);
    }

    /**
     * 区域域列表
     */
    private List<L> list;
    @Override
    public List<L> getList() {
        return list;
    }

    @Override
    public boolean add(L l) {
        return list.add(l);
    }

    @Override
    public boolean remove(L l) {
        return list.remove(l);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(L l) {
        return list.contains(l);
    }
}