package ghost.framework.data.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;

import javax.persistence.criteria.CriteriaUpdate;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:重新包装会话工厂接口
 * @Date: 2020/3/13:15:09
 */
public interface ISessionFactory extends SessionFactory {
    <T, V> int deleteByIds(Session session, Class<T> entity, String name, List<V> values);

    <T> int currentUpdate(Class<T> entity, Map<String, Object> where, Map<String, Object> update) throws SQLException;

    <T> int currentUpdate(CriteriaUpdate<T> update);

    <T> int currentDelete(Class<T> entity, Map<String, Object> w);

    <T> int currentUpdate(Class<T> entity, Map<String, Object> w, String name, Object value) throws SQLException;

    <T> int currentUpdate(Class<T> entity, Map<String, Object> w, List<String> list, String name, Object value);

    <T> int currentUpdate(Class<T> entity, Object id, String name, Object value);

    <T> int currentUpdate(Class<T> entity, Object id, Map<String, Object> update);

    <T> int currentUpdate(Class<T> entity, Serializable id, String[] names, Object[] values);

//    <T> long count(Session session, DetachedCriteria criteria);

    <T> long count(Session session, Class<T> entity);

    <T> List<T> select(Session session, DetachedCriteria criteria, int start, int length);

    <T> List<T> select(Session session, DetachedCriteria criteria, int start, int length, Class<T> entity);

//    <T> List<T> select(DetachedCriteria criteria, int start, int length) throws SQLException;

    /**
     * 返回查询器类型总行数
     *
     * @param criteria
     * @param <T>
     * @return
     */
    <T> long currentCount(DetachedCriteria criteria);

    /**
     * 返回类型总行数
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> long currentCount(Class<T> entity);

    /**
     * @param update
     * @param <T>
     * @return
     */
    <T> int update(Session session, CriteriaUpdate<T> update);

    /**
     * @param criteria
     * @param start
     * @param length
     * @param <T>
     * @return
     */
    <T> List<T> select(DetachedCriteria criteria, int start, int length) throws SQLException;

    /**
     * @param criteria
     * @param start
     * @param length
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> select(DetachedCriteria criteria, int start, int length, Class<T> entity);

    <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length, Class<?> map) throws SQLException;

    <T> List<T> mapSelect(DetachedCriteria criteria, Class<?> map) throws Exception;

    <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length) throws SQLException;

    <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, Class<?> map);

    <T> List<T> mapSelect(Session session, DetachedCriteria criteria, Class<?> map) throws SQLException;

//    <T> List<T> select(DetachedCriteria criteria) throws SQLException;

    <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length);

    <T> List<T> select(Class<T> entity, String[] names, Object[] values, List<String> notIds) throws SQLException;

    <T> List<T> select(Session session, Class<T> entity, String[] names, Object[] values, List<String> notIds);

    <T> List<T> select(Session session, Class<T> entity, String name, Object v);

    <T> List<T> select(Class<T> entity, String name, Object v) throws SQLException;

    <T> boolean exists(Session session, Class<T> entity, String name, Object v);

    <T> boolean exists(Class<T> entity, String name, Object v) throws SQLException;

    <T> int delete(Session session, Class<T> entity);

    <T> int delete(Class<T> entity) throws SQLException;

    <T> T uniqueResult(Session session, Class<?> entity, String uniqueName);

    <T> T uniqueResult(Class<?> entity, String uniqueName) throws SQLException;

    <T> int update(Session session, Class<T> entity, Map<String, Object> map);

    <T> int update(Class<T> entity, Map<String, Object> map) throws SQLException;

    <T> int update(Session session, Class<T> entity, String name, Object value);

    <T> int update(Class<T> entity, String name, Object value) throws SQLException;

    <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length, String[] propertys, String[] ass) throws SQLException;

    <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, String[] propertys, String[] ass);

    /**
     * @param criteria
     * @param start
     * @param length
     * @param propertys 返回字段
     * @param <T>
     * @return
     */
    <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length, String[] propertys) throws SQLException;

    <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, String[] propertys);

    /**
     * @param entity
     * @param where
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T get(Class<T> entity, Map<String, Object> where) throws SQLException;


    /**
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T get(Class<T> entity, String name, Serializable id) throws SQLException;

    /**
     * @param session
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> long count(Session session, Class<T> entity, Serializable id);

    /**
     * @param session
     * @param criteria
     * @param <T>
     * @return
     */
    <T> T get(Session session, DetachedCriteria criteria);

    /**
     * 返回单例名称值
     *
     * @param entity
     * @param id
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T uniqueResult(Class<?> entity, Object id, String uniqueName) throws SQLException;

    /**
     * @param session
     * @param criteria
     * @return
     */
    long count(Session session, DetachedCriteria criteria);

    /**
     * @param session
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    <T> boolean nameExists(Session session, Class<T> entity, String name, Serializable id);

    /**
     * @param session
     * @param entity
     * @param where
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T uniqueResult(Session session, Class<?> entity, Map<String, Object> where, String uniqueName);

    /**
     * @param session
     * @param entity
     * @throws Exception
     */
    void insert(Session session, Object entity) throws Exception;

    /**
     * @param entity
     * @param where
     * @param update
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int update(Class<T> entity, Map<String, Object> where, Map<String, Object> update) throws SQLException;

    /**
     * @param session
     * @param entity
     * @param id
     * @param updateName
     * @param updateValue
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int updateUnique(Session session, Class<T> entity, Serializable id, String updateName, Object updateValue);

    /**
     * @param session
     * @param entity
     * @throws Exception
     */
    void update(Session session, Object entity) throws Exception;

    /**
     * @param session
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T get(Session session, Class<T> entity, String[] names, Object[] values) throws Exception;

    /**
     * @param session
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean nameAndValueExists(Session session, Class<T> entity, String name, Object value);

    /**
     * @param session
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean exists(Session session, Class<T> entity);

    /**
     * @param session
     * @param entity
     * @param name
     * @param map
     * @param <T>
     * @return
     */
    <T> Object maxResult(Session session, Class<T> entity, String name, Map<String, Object> map);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> T currentGet(Class<T> entity, String[] names, Object[] values);

    /**
     * @param entity
     * @param where
     * @param update
     * @param <T>
     * @return
     */
    <T> int update(Session session, Class<T> entity, Map<String, Object> where, Map<String, Object> update) throws SQLException;

    /**
     * @param entity
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> T get(Class<T> entity) throws SQLException;

    /**
     * @param entity
     * @param id
     * @param name
     * @param update
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int update(Class<T> entity, Serializable id, String name, Object update) throws Exception;

    /**
     * @param session
     * @param entity
     * @param where
     * @param name
     * @param update
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int update(Session session, Class<T> entity, Map<String, Object> where, String name, Object update) throws SQLException;

    /**
     * 删除指定条件。
     *
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> int delete(Session session, Class<T> entity, String[] names, Object[] values);

    /**
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> int deleteById(Session session, Class<T> entity, String name, Object value);

    /**
     * @param criteria
     * @param <T>
     * @return
     */
    <T> List<T> select(DetachedCriteria criteria) throws SQLException;

    /**
     * @param exists
     * @return
     */
    boolean currentExists(DetachedCriteria exists);

    /**
     * @param entity
     * @param id
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T currentUniqueResult(Class<?> entity, Serializable id, String uniqueName);

    /**
     * @param entity
     * @param w
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T currentUniqueResult(Class<?> entity, Map<String, Object> w, String uniqueName);

    /**
     * @param entity
     * @param w
     * @param <T>
     * @return
     */
    <T> int delete(Session session, Class<T> entity, Map<String, Object> w);

//    /**
//     * @param entity
//     * @param w
//     * @param name
//     * @param value
//     * @param <T>
//     * @return
//     */
//    <T> int update(Session session, Class<T> entity, Map<String, Object> w, String name, Object value) throws SQLException;

    /**
     * @param entity
     * @param w
     * @param list
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> int update(Session session, Class<T> entity, Map<String, Object> w, List<String> list, String name, Object value);

    /**
     * @param entity
     * @param list
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> int update(Session session, Class<T> entity, List<String> list, String name, Object value);

    /**
     * @param entity
     * @param id
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> int update(Session session, Class<T> entity, Object id, String name, Object value);

    /**
     * @param entity
     * @param id
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, Object id, String name, Object value);

    /**
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, String name, Object value);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, String[] names, Object[] values);

    /**
     * @param entity
     * @param names
     * @param values
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T currentUniqueResult(Class<?> entity, String[] names, Object[] values, String uniqueName);

    /**
     * @param entity
     * @param id
     * @param update
     * @param <T>
     * @return
     */
    <T> int update(Session session, Class<T> entity, Object id, Map<String, Object> update);

    /**
     * @param entity
     * @param id
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T currentUniqueResult(Class<?> entity, Object id, String uniqueName);

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T currentGet(Class<T> entity, Object id);

    /**
     * @param entity
     * @param map
     * @param <T>
     * @return
     */
    <T> T currentGet(Class<T> entity, Map<String, Object> map);

    /**
     * 获取指定条件数量。
     *
     * @param entity
     * @param names
     * @param values
     * @param nodeIds
     * @param <T>
     * @return
     */
    <T> long currentCount(Class<T> entity, String[] names, Object[] values, List<String> nodeIds);

    /**
     * @param entity
     * @param id
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> boolean currentNotIdExists(Class<T> entity, Object id, String[] names, Object[] values);

    <T> boolean currentNotIdExists(Class<T> entity, Object id, String name, Object value);

    /**
     * @param entity
     * @param names
     * @param values
     * @param deletes
     * @param <T>
     * @return
     */
    <T> int deleteByIds(Session session, Class<T> entity, String[] names, Object[] values, List<String> deletes);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> List<T> currentList(Class<T> entity, String[] names, Object[] values);

    /**
     * @param entity
     * @param names
     * @param values
     * @param order
     * @param <T>
     * @return
     */
    <T> List<T> currentList(Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order);

    /**
     * @param entity
     * @param names
     * @param values
     * @param propertyNames
     * @param <T>
     * @return
     */
    <T> List currentList(Class<T> entity, String[] names, Object[] values, String[] propertyNames);

    /**
     * @param entity
     * @param names
     * @param values
     * @param order
     * @param propertyNames
     * @param <T>
     * @return
     */
    <T> List currentList(Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order, String[] propertyNames);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> long currentCount(Class<T> entity, String[] names, Object[] values);

    <T> long count(Session session, Class<T> entity, String[] names, Object[] values);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> long count(Class<T> entity, String[] names, Object[] values) throws SQLException;

    /**
     * @param entityName
     * @param id
     * @param uniqueName
     * @return
     */
    <T> T currentUniqueResult(String entityName, String id, String uniqueName);

    /**
     * @param session
     * @param entityName
     * @param id
     * @param uniqueName
     * @return
     */
    <T> T uniqueResult(Session session, String entityName, String id, String uniqueName);

    /**
     * @param criteria
     * @return
     */
    <T> T uniqueResult(DetachedCriteria criteria) throws SQLException;

    /**
     * @param entity
     * @param names
     * @param values
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T uniqueResult(Class<?> entity, String[] names, Object[] values, String uniqueName) throws SQLException;


    /**
     * @param entity
     * @param id
     * @param uniqueNames
     * @return
     */
    Object[] currentUniqueResult(Class<?> entity, Object id, String[] uniqueNames);

    Object[] uniqueResult(Session session, Class<?> entity, Object id, String[] uniqueNames);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int delete(Class<T> entity, String[] names, Object[] values) throws SQLException;

    <T> List list(Session session, Class<T> entity, String[] names, Object[] values, String propertyName);

    /**
     * @param entity
     * @param names
     * @param values
     * @param propertyName
     * @param <T>
     * @return
     */
    <T> List currentList(Class<T> entity, String[] names, Object[] values, String propertyName);

    <T> List<T> currentSelect(Class<T> entity, String[] names, Object[] values);

    <T> Object currentMaxResult(Class<T> entity, String name, Map<String, Object> map);

    <T> boolean currentNotNameExists(Class<T> entity, String notName, Object value, String[] names, Object[] values);

    <T> boolean exists(Session session, Class<T> entity, Object id);

    <T> boolean exists(Class<T> entity, Object id) throws SQLException;

    <T> boolean currentExists(Class<T> entity, Serializable id);

    <T> int deleteById(Session session, Class<T> entity, Serializable id);

    /**
     * @param entity
     * @param names
     * @param values
     * @param order
     * @param propertyName
     * @param <T>
     * @return
     */
    <T> List currentList(Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order, String propertyName);

    <T> boolean currentExists(Session session, Class<T> entity, String[] names, Object[] values);

    <T> T uniqueResult(Session session, DetachedCriteria criteria);

    /**
     * @param criteria
     * @return
     */
    <T> T currentUniqueResult(DetachedCriteria criteria);

    /**
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity);

    /**
     * @param entity
     * @param value
     * @param <T>
     * @throws Exception
     */
    <T> void update(Class<T> entity, Object value) throws SQLException;

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> List<T> select(Class<T> entity, Object id) throws SQLException;

    <T> boolean exists(Session session, Class<T> entity, String[] names, Object[] values);

    <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order, String propertyName);

    /**
     * @param entity
     * @param id
     * @param update
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int update(Class<T> entity, Object id, Map<String, Object> update) throws SQLException;

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> List<T> select(Class<T> entity, String[] names, Object[] values) throws SQLException;

    /**
     * @param entity
     * @param id
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> int update(Session session, Class<T> entity, Serializable id, String[] names, Object[] values);

    /**
     * @param entity
     * @param name
     * @param id
     * @param list
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> int deleteByIds(Class<T> entity, String name, String id, List<Object> list) throws SQLException;


    <T, V> int deleteByIds(Session session, Class<T> entity, List<V> list);

    /**
     * @param entity
     * @param list
     * @param <T>
     * @return
     * @throws Exception
     */
    <T, V> int deleteByIds(Class<T> entity, List<V> list) throws SQLException;

    <T> int delete(Class<T> entity, Map<String, Object> map) throws SQLException;

    <T> int deleteById(Class<T> entity, Object id) throws SQLException;

    /**
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    <T> T currentGet(Class<T> entity, String name, Serializable id);

    /**
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    <T> T findById(Session session, Class<T> entity, String name, Serializable id);

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> boolean exists(Session session, Class<T> entity, Serializable id);

    /**
     * @param entity
     * @param notName
     * @param value
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> boolean notNameExists(Session session, Class<T> entity, String notName, Object value, String[] names, Object[] values);

    /**
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> List<T> select(Session session, Class<T> entity, String[] names, Object[] values);
//
//    /**
//     * @param entity
//     * @param name
//     * @param map
//     * @param <T>
//     * @return
//     */
//    <T> Object maxResult(Session session, Class<T> entity, String name, Map<String, Object> map);

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T findById(Class<T> entity, Object id) throws SQLException;

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T findById(Session session, Class<T> entity, Object id);

    /**
     * 获取map对象
     *
     * @param criteria
     * @param projectionList
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Map<K, V> map(DetachedCriteria criteria, String[] projectionList) throws SQLException;

    /**
     * 获取实体对象
     *
     * @param entity 实体类型
     * @param names  字段名称
     * @param values 字段对象
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> T get(Class<T> entity, String[] names, Object[] values) throws SQLException;

    /**
     * 删除实体
     *
     * @param entity 实体类型
     * @param name   主键名称
     * @param ids    删除主键列表
     * @param <T>
     * @param <I>
     * @return
     * @throws SQLException
     */
    <T, I> int deleteByIds(Class<T> entity, String name, List<I> ids) throws SQLException;

    /**
     * @param session
     * @param entity
     * @param name
     * @param value
     * @param uniqueName
     * @param <T>
     * @return
     */
    <T> T uniqueResult(Session session, Class<?> entity, String name, Object value, String uniqueName);

    /**
     * 获取实体列表
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> findAll(Class<T> entity) throws SQLException;

    void insert(Object entity) throws SQLException;

    void update(Object entity) throws SQLException;

    <O> void insertAll(List<O> entityList) throws SQLException;

    <T> long count(Class<T> entity) throws SQLException;

    long count(DetachedCriteria criteria) throws SQLException;

    <T> List<T> list(Class<T> entity) throws SQLException;
//    <E, K, V, I> List<Map<K, V>> mapSelect(Class<E> entity, List<I> ids, String[] projectionList) throws SQLException;
}