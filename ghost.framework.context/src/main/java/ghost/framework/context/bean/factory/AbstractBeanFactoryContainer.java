package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定工厂容器基础类
 * @Date: 23:42 2019/12/21
 * @param <L> 目标类型
 */
public abstract class AbstractBeanFactoryContainer<L> extends AbstractCollection<L> implements IBeanFactoryContainer<L> {
    /**e
     * 日志
     */
    private Log log = LogFactory.getLog(this.getClass());
    /**
     * 获取日志
     * @return
     */
    @Override
    public Log getLog() {
        return log;
    }
    /**
     * 事件接口列表
     * Map<IEventListener 接口类型, List<IEventListenerFactory 继承事件监听接口对象>>
     */
    private List<L> list = new ArrayList<>();

    /**
     *
     * @return
     */
    @Override
    public Iterator<L> iterator() {
        return list.iterator();
    }

    /**
     *
     * @return
     */
    @Override
    public int size() {
        return list.size();
    }
        /**
     * 删除事件监听工厂
     * @param factory 事件监听接口
     */
    @Override
    public boolean remove(Object factory) {
        Assert.notNull(factory, "remove null factory error");
        this.log.info("remove:" + factory.toString());
        synchronized (this.list) {
            return super.remove(factory);
        }
    }
    /**
     * 添加事件监听工厂
     * @param factory 事件监听接口
     */
    @Override
    public boolean add(@NotNull L factory) {
        Assert.notNull(factory, "add null listener factory");
        this.log.info("add:" + factory.toString());
        synchronized (this.list) {
            if (!this.list.contains(factory)) {
                this.list.add(factory);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends L> c) {
        Assert.notNull(c, "addAll null factory error");
        return super.addAll(c);
    }

    /**
     * 判断是否有事件监听接口
     * @param factory 事件监听接口
     * @return
     */
    @Override
    public boolean contains(@NotNull Object factory) {
        Assert.notNull(factory, "contains null listener factory");
        return super.contains(factory);
    }
    /**
     * 释放资源
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        synchronized (this.list) {
            this.clear();
        }
    }
}