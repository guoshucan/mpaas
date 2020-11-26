package ghost.framework.data.core;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库会话接口。
 * @Date: 15:48 2018-06-18
 */
public interface IDataSession extends AutoCloseable {
    /**
     * 关闭连接。
     */
    void close() throws SQLException;

    /**
     * 事务回滚。
     */
    void rollback();

    TransactionStatus getStatus();

    int getTimeout();

    boolean getRollbackOnly();

    void setTimeout(int seconds);

    /**
     * 删除表数据。
     *
     * @param entity 指定实体。
     * @param name   指定名称。
     * @param id     指定键值。
     * @param <T>
     * @return
     */
    <T> int delete(Class<T> entity, String name, Object id);

    /**
     * 删除表数据。
     *
     * @param entity 指定实体。
     * @param id     指定键值。
     * @param <T>
     * @return
     */
    <T> int delete(Class<T> entity, Object id);

    void commit() throws InterruptedException;

    /**
     * 完成事务。
     *
     * @param mode 指定完成事务模式。
     * @throws InterruptedException
     */
    void commit(DataTransactionMode mode) throws InterruptedException;
    /**
     * 插入表数据。
     *
     * @param entity
     */
    void insert(Object entity);

    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    void update(Object entity);

    Session getSession();

    int update(Class<?> entity, Object id, HashMap<String, Object> update);

    boolean exists(Class<?> entity, String name, Object value);

    boolean exists(Class<?> entity, String[] names, Object[] values);

    long count(Class<?> entity);

    Object uniqueResult(DetachedCriteria criteria);

    boolean idNotExists(Class<?> entity, Object id, String name, Object value);

    boolean idNotExists(Class<?> entity, Object id, String[] names, Object[] values);

    <T> T get(Class<T> entity);

    Blob createBlob(byte[] bytes);

    <T> boolean exists(Class<T> entity);
}
