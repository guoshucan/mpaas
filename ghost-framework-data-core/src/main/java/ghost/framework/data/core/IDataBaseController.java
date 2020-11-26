package ghost.framework.data.core;

import ghost.framework.module.reflect.IGetObjectId;
import org.apache.commons.logging.Log;
import org.hibernate.criterion.DetachedCriteria;

import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库控制器接口。
 * @Date: 22:42 2018-06-14
 */
public interface IDataBaseController extends IGetObjectId {
    void setLogger(Log logger);

    Log getLogger();

    void lock();

    void unlock();

    /**
     * 删除表
     * 把表从数据库中删除
     * 包括数据一起删除
     * @param tables 要删除表列表
     * @return
     */
    boolean deleteTables(Object[] tables);
    /**
     * 打开数据库。
     *
     * @return
     */
    IDataBase openDataBase();

    /**
     * 打开正常与只写数据库。
     *
     * @return
     */
    IDataBase openEnableOrWriteDataBase();

    /**
     * 获取当前线程正常与只读数据库。
     *
     * @return
     */
    IDataBase getCurrentEnableOrReadDataBase();

    IDataBase openDataBase(DataBaseStatus[] statuses);

    /**
     * 当前线程会话插入实体。
     *
     * @param entity 实体对象。,
     * @return
     */
    DataBaseInsertResults currentInsert(Object entity) throws InterruptedException;

    /**
     * 当前线程会话查询数据
     *
     * @param criteriaQuery
     * @param start
     * @param size
     * @param <T>
     * @return
     */
    <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery, int start, int size);

    /**
     * 当前线程会话查询数据。
     *
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery);

    /**
     * 当前线程会话查询一行数据。
     *
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    <T> T currentSingleResult(CriteriaQuery<T> criteriaQuery);

    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    <T> DataBaseUpdateResults currentUpdate(CriteriaUpdate<T> criteriaUpdate) throws InterruptedException;

    /**
     * 当前线程会话验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    boolean currentExists(DetachedCriteria criteria);

    /**
     * 当前线程会话统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    long currentCount(DetachedCriteria criteria);

    /**
     * 当前线程会话删除指定实体名称指定的Id。
     *
     * @param entity 实体类型。
     * @param id     实体主键Id。
     * @param <T>    泛型实体。
     * @return 返回删除行数。
     * @throws IllegalAccessException
     */
    <T> DataBaseDeleteResults currentDeleteById(Class<T> entity, Object id) throws InterruptedException;

    /**
     * 当前线程会话删除实体。
     *
     * @param entity 实体。
     */
    void currentDelete(Object entity);

    /**
     * 当前线程会话删除。
     *
     * @param criteria
     * @param <T>
     * @return
     */
    <T> int currentDelete(CriteriaDelete<T> criteria);

    /**
     * 当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> currentSelect(DetachedCriteria criteria);

    /**
     * 当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param pageSize 选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    <T> List<T> currentSelect(DetachedCriteria criteria, int start, int pageSize);

    /**
     * 当前线程会话获取实体数据。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> T currentSingleResult(Class<T> entity, Object id);

    /**
     * 当前线程会话更新实体属性列表。
     *
     * @param entity 实体。
     * @param update 实体属性列表，如果字段包含主键侧自动指定主键条件。
     * @param <T>
     * @return
     */
    <T> DataBaseUpdateResults currentUpdate(Class<T> entity, HashMap<String, Object> update) throws InterruptedException;

    /**
     * 当前线程会话验证实体Id的键值是否存在。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> boolean currentExistsById(Class<T> entity, Object id);

    /**
     * 当前线程会话验证实体条件是否存在。
     *
     * @param entity 实体对象。
     * @param name   例名称。
     * @param value  例值。
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, String name, Object value);

    /**
     * 当前线程会话验证实体条件是否存在。
     *
     * @param entity 实体对象。
     * @param names  例名称。
     * @param values 例值。
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, String[] names, Object[] values);

    /**
     * 当前线程会话验证实体条件是否存在。
     *
     * @param entity 实体对象。
     * @param map    例列表。
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity, HashMap<String, Object> map);

    /**
     * 当前线程会话验证实体参数的键值是否存在。
     *
     * @param entity
     * @param map
     * @param <T>
     * @return
     */
    <T> boolean currentExistsByNotId(Class<T> entity, Object id, HashMap<String, Object> map);

    /**
     * 当前线程会话 验证实体是否已经存在数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> boolean currentExists(Class<T> entity);

    /**
     * 当前线程会话获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T currentSingleResult(Class<T> entity);

    /**
     * 当前线程会话获取该实体数据库所有数据行。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> currentSelect(Class<T> entity);

    /**
     * 当前线程会话删除实体全部数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> int currentDelete(Class<T> entity);

    /**
     * 当前线程会话查询单行。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> List<T> currentSelect(Class<T> entity, Object id);

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T openSingleResult(Class<T> entity);

    /**
     * 获取实体分页。
     *
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> List<T> openSelect(Class<T> entity, String[] names, Object[] values);

    /**
     * 当前线程会话获取实体指定字段唯一键。
     *
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    <T> Object openTransactionUniqueResult(Class<T> entity, String name, Object id);

    /**
     * 使用事务读取数据。
     *
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    <T> List<T> openTransactionSelect(Class<T> entity, String[] names, Object[] values);

    /**
     * 获取当前数据库。
     *
     * @return
     */
    IDataBase getCurrentDataBase();

    /**
     * 获取是否为调试模式。
     *
     * @return
     */
    boolean isDebug();

    /**
     * 错误。
     *
     * @param dataBase 错误发生数据库。
     * @param source   错误源。
     * @param e        错误信息。
     */
    void error(IDataBase dataBase, Object source, Exception e);

    /**
     * 获取数据库控制器实体列表。
     *
     * @return
     */
    List<Class> getEntityList();

    /**
     * 打开数据库会话。
     *
     * @return
     */
    IDataSession openDataSession();

    <T> T openSingleResultTransformer(Class<?> result, DetachedCriteria criteria) throws Exception;

    <T> DataBaseUpdateResults openUpdate(Class<T> entity, String name, Object value) throws InterruptedException;

    <T> DataBaseUpdateResults openUpdate(Class<T> entity, String[] names, Object[] values) throws InterruptedException;

    <T, R> R openUniqueResult(Class<T> entity, String name, String[] names, Object[] values);

    /**
     * 按照数据库开始位置的作为数据源复制到指定数据库。
     *
     * @param fillDataBase 指定要填充数据的数据库。
     */
    void rootToCopy(IDataBase fillDataBase) throws Exception;

    /**
     * 指定源数据库位置复制到指定数据库。
     *
     * @param sourceId     源数据库Id。
     * @param fillDataBase 要填充的数据库。
     */
    void toCopy(String sourceId, IDataBase fillDataBase) throws Exception;

    /**
     * 指定源数据库复制到指定数据库。
     *
     * @param sourceDataBase 源数据库位置。
     * @param fillDataBase   要填充的数据库。
     * @throws Exception
     */

    void toCopy(IDataBase sourceDataBase, IDataBase fillDataBase) throws Exception;

    /**
     * 清除数据库数据。
     *
     * @param dataBase
     */
    void clearData(IDataBase dataBase) throws Exception;

    /**
     * 获取root数据库。
     *
     * @return
     */
    IDataBase getRoot();

    /**
     * 获取正常与写入数据库列表。
     *
     * @param excludeId 指定不纳入列表的数据库Id。
     * @return
     */
    List<IDataBase> getEnableOrWriteDataBase(String excludeId);

    /**
     * 获取正常与写入数据库列表。
     *
     * @return
     */
    List<IDataBase> getEnableOrWriteDataBase();

    <T> boolean openExists(Class<T> entity, String[] names, Object[] values);

    DataBaseInsertResults openInsert(Object entity) throws InterruptedException, IllegalArgumentException;

    <R> R openUniqueResult(DetachedCriteria criteria);

    <T> List<T> openSelect(DetachedCriteria criteria);

    <T> DataBaseDeleteResults openDelete(Class<T> entity, String[] names, Object[] values) throws InterruptedException;

    /**
     * 重置数据库控制器。
     *
     * @throws DataException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    void reset() throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

    /**
     * 获取实体指定例值的数据行。
     *
     * @param entity 指定实体。
     * @param name   指定例名称。
     * @param value  指定例值。
     * @param <T>
     * @return 返回指定实体数据行。
     */
    <T> T openSingleResult(Class<T> entity, String name, Object value);

    /**
     * @param entity
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean openExists(Class<T> entity, Object value);

    /**
     * 设置数据库控制器实体列表。
     *
     * @param entityList
     */
    void setEntityList(List<Class> entityList);

    /**
     * 创建数据库。
     *
     * @param propertie 数据库配置。
     * @return
     * @throws DataException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    IDataBase create(IDataBaseProperties propertie) throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

    /**
     * 验证实体是否存在。
     *
     * @param entity 实体。
     * @param name   指定例名称。
     * @param value  指定例值。
     * @param <T>
     * @return 返回是否存在。
     */
    <T> boolean openExists(Class<T> entity, String name, Object value);

    /**
     * 打开新会话获取实体列表。
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> openSelect(Class<T> entity);

    /**
     * 获取指定实体一行数据。
     *
     * @param entity 指定实体。
     * @param value  指定实体主键值。
     * @param <T>
     * @return 返回实体指定主键的一行数据，如果没有侧返回null。
     */
    <T> T openSingleResult(Class<T> entity, Object value);

    /**
     * 获取数据库列表。
     *
     * @return
     */
    HashMap<String, IDataBase> getMap();

    /**
     * 添加包加载器。
     *
     * @param loader
     */
    void addClassLoader(ClassLoader loader);

    /**
     * 删除包加载器。
     *
     * @param loader
     */
    void removeClassLoader(ClassLoader loader);

    long openCount(DetachedCriteria criteria);

    <T> List<T> openSelect(DetachedCriteria criteria, int start, int length);

    <T> Object openByIdNameUniqueResult(Class<T> entity, Object id, String name);

    /**
     * 删除行数据
     *
     * @param entity
     * @param id     主键
     */
    <T> DataBaseDeleteResults openDeleteById(Class<T> entity, Object id) throws InterruptedException;

    /**
     * 删除数据库
     *
     * @param id
     */
    void remove(String id);

    /**
     * 获取数据库
     *
     * @param id
     * @return
     */
    IDataBase get(String id);

    <T> DataBaseUpdateResults openUpdateById(Class<T> entity, Object id, String name, Object value) throws InterruptedException;

    <T> DataBaseUpdateResults openUpdate(Class<T> entity, HashMap<String, Object> map) throws InterruptedException;

    <T> List<T> openSelect(Class<T> entity, int start, int size);
}
