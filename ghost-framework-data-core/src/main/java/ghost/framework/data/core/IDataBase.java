package ghost.framework.data.core;

import ghost.framework.module.reflect.IGetObjectId;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库接口。
 * @Date: 14:39 2018-06-16
 */
public interface IDataBase extends AutoCloseable, IGetObjectId {
    Log getLogger();

    @Override
    void close();

    /**
     * 获取数据库状态。
     *
     * @return
     */
    DataBaseStatus getStatus();

    /**
     * 设置数据库状态。
     *
     * @param status
     */
    void setStatus(DataBaseStatus status);

    /**
     * 打开会话。
     *
     * @return
     */
    Session openSession();

    /**
     * @param criteria
     * @return
     */
    HashMap<String, Object> openTransactionSingleResultMap(DetachedCriteria criteria) throws Exception;

    /**
     * @param criteria
     * @return
     */
    HashMap<String, Object> openSingleResultMap(DetachedCriteria criteria);


    Object openUniqueResult(DetachedCriteria criteria);

    /**
     * 返回包装对象。
     *
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T openSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws Exception;

    /**
     * 返回包装对象。
     *
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    <T> T openTransactionSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException;

    void plusSelect();

    void reduceSelect();

    void plusDelete();

    void reduceDelete();

    Session getCurrentSession();

    void plusUpdate();

    void reduceUpdate();

    /**
     * 获取数据库所在控制器。
     *
     * @return
     */
    IDataBaseController getController();

    void plusInsert();

    void reduceInsert();

    /**
     * 返回包装对象。
     *
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T currentSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    <T> int openUpdate(CriteriaUpdate<T> criteriaUpdate) throws Exception;

    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    <T> int currentUpdate(CriteriaUpdate<T> criteriaUpdate) throws Exception;

    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    void currentUpdate(Object entity);

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    boolean openExists(DetachedCriteria criteria);

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    boolean openTransactionExists(DetachedCriteria criteria) throws Exception;

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    boolean currentExists(DetachedCriteria criteria);

    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    long currentCount(DetachedCriteria criteria);

    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    long openCount(DetachedCriteria criteria);

    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    long openTransactionCount(DetachedCriteria criteria) throws Exception;

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    Object currentUniqueResult(DetachedCriteria criteria);

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    Object openTransactionUniqueResult(DetachedCriteria criteria) throws Exception;

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entity 实体类型。
     * @param id     实体主键Id。
     * @param <T>    泛型实体。
     * @return 返回删除行数。
     */
    <T> int openDeleteById(Class<T> entity, Object id);

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entity 实体类型。
     * @param id     实体主键Id。
     * @param <T>    泛型实体。
     * @return 返回删除行数。
     * @throws IllegalAccessException
     */
    <T> int currentDeleteById(Session session, Class<T> entity, Object id);

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entityName 实体名称。
     * @param id         实体主键Id。
     */
    void openDeleteById(String entityName, Object id) throws Exception;

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entityName 实体名称。
     * @param id         实体主键Id。
     */
    void currentDeleteById(String entityName, Object id);

    /**
     * 删除实体。
     *
     * @param entity 实体。
     */
    void currentDelete(Object entity);

    /**
     * 删除实体。
     *
     * @param entity 实体。
     */
    int openDelete(Object entity) throws Exception;

    /**
     * 删除实体类型。
     *
     * @param entity 实体类型。
     * @return
     */
    int openDelete(Class<?> entity) throws Exception;

    /**
     * 删除。
     *
     * @param criteria
     */
    <T> int openDelete(CriteriaDelete<T> criteria) throws Exception;

    /**
     * 删除。
     *
     * @param criteria
     * @param <T>
     * @return
     */
    <T> int currentDelete(CriteriaDelete<T> criteria);

    /**
     * @return
     */
    CriteriaBuilder currentCriteriaBuilder();

    /**
     * 打开当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> currentSelect(DetachedCriteria criteria);

    /**
     * 打开当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param size     选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> currentSelect(DetachedCriteria criteria, int start, int size);

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> openSelect(DetachedCriteria criteria);

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> openTransactionSelect(DetachedCriteria criteria) throws Exception;

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param size     选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> openSelect(DetachedCriteria criteria, int start, int size);

    <T> List<T> openSelect(CriteriaQuery<T> criteriaQuery, int start, int size);

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param size     选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> openTransactionSelect(DetachedCriteria criteria, int start, int size) throws Exception;

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T currentSingleResult(Class<T> entity, Object id);

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T openSingleResult(Class<T> entity, Object id);

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T openTransactionSingleResult(Class<T> entity, Object id) throws Exception;

    /**
     * 验证实体Id的键值是否存在。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, Object id);

    /**
     * 验证实体是否存在数据行。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean openExists(Class<T> entity);

    /**
     * 验证实体是否存在数据行。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean openTransactionExists(Class<T> entity) throws Exception;

    /**
     * 验证实体是否已经存在数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity);

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T openSingleResult(Class<T> entity);

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T openTransactionSingleResult(Class<T> entity) throws Exception;

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T currentSingleResult(Class<T> entity);

    /**
     * 获取数据库Id。
     *
     * @return
     */
    String getId();

    <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery, int start, int index);

    <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery);

    <T> List<T> openSelect(CriteriaQuery<T> criteriaQuery);

    <T> T currentSingleResult(CriteriaQuery<T> criteriaQuery);

    <T> T openSingleResult(CriteriaQuery<T> criteriaQuery);

    <T> int openUpdate(Class<T> entity, HashMap<String, Object> update) throws Exception;

    void openInsert(Object entity);

    int openUpdate(Class<?> entity, String name, Object value) throws Exception;

    boolean isClose();

    void openUpdate(Object entity) throws Exception;

    <T> int openUpdateById(Class<T> entity, Object id, String name, Object value) throws Exception;

    <T> int openUpdate(Class<T> entity, String[] names, Object[] values) throws Exception;

    <T> int openUpdateById(Class<T> entity, Object id, String[] names, Object[] values) throws Exception;

    String getUsername();

    String getIp();

    int getPort();

    String getDialect();

    String getDataBaseName();

    long openCount(Class<?> entity);

    <T> List<T> openSelect(Class<T> entity, int start, int size);

    <T> boolean openExists(Class<T> entity, String name, Object value);

    String getPassword();

    String getHbm2ddlAuto();

    boolean isShowSql();

    boolean isUseQueryCache();

    boolean isUseSecondLevelCache();

    String getFactoryClass();

    String getProviderClass();

    void setRoot(boolean root);

    boolean isRoot();

//    <T> T openById(Class<T> entity);

    <T> List<T> openSelect(Class<T> entity);

    <T> int openDelete(Class<T> entity, String[] names, Object[] values) throws Exception;

    /**
     * 重置数据库。
     */
    void reset();

    /**
     * 获取会话工厂。
     *
     * @return
     */
    SessionFactory getSessionFactory();

    /**
     * 获取一行数据
     *
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> T openSingleResult(Class<T> entity, String[] names, Object[] values);

    <T> boolean openExistsByNotId(Class<T> entity, Object id, String name, Object value);

    <T> boolean currentExistsByNotId(Session session, Class<T> entity, Object id, String name, Object value);

    <T> boolean currentExistsById(Session session, Class<T> entity, Object id, String name, Object value);

    <T> int currentUpdateById(Session session, Class<T> entity, Object id, String[] names, Object[] values);

    <T> boolean currentExistsById(Session session, Class<T> entity, Object id);

    <T> int currentUpdateById(Session session, Class<T> entity, Object id, String name, Object value);

    <T> boolean openExistsById(Class<T> entity, Object id, String name, Object value);

    <T, I> int currentDeleteByIds(Session session, Class<T> entity, List<I> list);

    <T> DataPage<T> openSqlitePage(DetachedCriteria criteriaCount, DetachedCriteria criteria, int start, int size);

    <T> int openUpdateByNotId(Class<T> entity, Object id, String name, Object value) throws Exception;

    <T, I> int openDeleteByIds(Class<T> entity, List<I> list) throws Exception;

    /**
     * 删除主键列表
     * @param entitys 实体列表
     * @param list 主键Id列表
     * @param <I>
     * @return
     * @throws Exception
     */
    <I> int openDeleteByIds(Class<?>[] entitys, List<I> list) throws Exception;

    /**
     * 删除主键
     * @param entitys 实体列表
     * @param id 主键Id
     * @return
     * @throws Exception
     */
    int openDeleteById(Class<?>[] entitys, Object id) throws Exception;
    /**
     * 获取事务超时时间
     *
     * @return
     */
    int getTransactionTimeout();

    /**
     * 设置事务超时时间
     *
     * @param transactionTimeout
     */
    void setTransactionTimeout(int transactionTimeout);
    <T> boolean openExists(Class<T> entity, String[] names, Object[] values, boolean isTransaction);
    <T> boolean openExists(Class<T> entity, String[] names, Object[] values);
    <T>  boolean openExistsByNotId(Class<T> entity, Object id, String[] names, Object[] values, boolean isTransaction);
    <T>  boolean openExistsByNotId(Class<T> entity, Object id, String[] names, Object[] values);
    <T> List<T> openSelect(Class<T> entity, String name, Object value) throws Exception;
    <T> List<T> openSelect(Class<T> entity, String name, Object value, boolean isTransaction) throws Exception;
}
