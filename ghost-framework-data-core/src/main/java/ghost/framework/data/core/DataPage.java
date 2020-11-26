package ghost.framework.data.core;

import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:分页列表对象
 * @Date: 21:59 2018-12-09
 */
public class DataPage<T> {
    private List<T> list;
    /**
     * 初始化分页列表
     *
     * @param count 统计总数
     * @param pages 计算分页总数
     * @param list  对象列表
     */
    public DataPage(long count, long pages, List<T> list) {
        this.count = count;
        this.pages = pages;
        this.list = list;
    }

    /**
     * 初始化分页列表
     *
     * @param count     统计总数
     * @param pages     计算分页总数
     * @param pageBegin 计算分页页码显示位置
     * @param list      对象列表
     */
    public DataPage(long count, long pages, int pageBegin, List<T> list) {
        this.count = count;
        this.pages = pages;
        this.pageBegin = pageBegin;
        this.list = list;
    }

    private int pageBegin;

    /**
     * 获取分页显示页码开始位置
     *
     * @return
     */
    public int getPageBegin() {
        return pageBegin;
    }

    /**
     * 设置分页显示页码开始位置
     *
     * @param pageBegin
     */
    public void setPageBegin(int pageBegin) {
        this.pageBegin = pageBegin;
    }

    /**
     * 获取分页列表
     *
     * @return
     */
    public List<T> getList() {
        return list;
    }

    private long pages;

    /**
     * 获取分页数量
     *
     * @return
     */
    public long getPages() {
        return pages;
    }

    private long count;

    /**
     * 获取总行数
     *
     * @return
     */
    public long getCount() {
        return count;
    }
}