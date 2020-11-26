package ghost.framework.context.collections.generic;

import java.util.List;

/**
 * package: ghost.framework.context.collections.generic
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:简单列表接口
 * @Date: 2020/5/28:17:27
 * @param <T>
 */
public interface ISmartList<T> {
    /**
     * 获取列表
     *
     * @return
     */
    default List<T> getList() {
        throw new UnsupportedOperationException(ISmartList.class.getName() + "#getList()");
    }

    /**
     * 添加
     *
     * @param t
     */
    boolean add(T t);

    /**
     * 删除
     *
     * @param t
     * @return
     */
    boolean remove(T t);

    /**
     * 获取大小
     *
     * @return
     */
    int size();

    /**
     * 判断是否存在
     *
     * @param t
     * @return
     */
    boolean contains(T t);
}