package ghost.framework.data.hibernate.plugin;

import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.util.Assert;
import org.hibernate.*;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.data.hibernate.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:重新包装会话工厂
 * 主要用途在于重构实体后新旧会话交替执行
 * @Date: 2020/3/13:15:02
 */
final class SqlSessionFactory implements ISessionFactory {
    /**
     * 当前执行的会话工厂
     */
    private SessionFactory current;
    /**
     * 准备为当前执行的会话工厂
     * 当重新配置实体完成后设置到currentd当前会话工厂
     */
    private SessionFactory old;

    public SessionFactory getOld() {
        return old;
    }

    public void setCurrent(SessionFactory current) {
        Assert.notNull(current, "setCurrent is current null error");
        this.old = this.current;
        synchronized (this) {
            this.current = current;
        }
        //获取连接池
        DriverManagerConnectionProviderImpl.PooledConnections connections = HibernateUtils.getPool(this.old);
        //判断旧的连接池是否存在连接
        if (connections.size() == 0) {
            //关闭资源
            this.old.getSessionFactoryOptions().getServiceRegistry().close();
        }
    }

    public SessionFactory getCurrent() {
        return current;
    }

    public SqlSessionFactory(SessionFactory current) {
        Assert.notNull(current, "SqlSessionFactory is current null error");
        this.current = current;
    }

    @Override
    public SessionFactoryOptions getSessionFactoryOptions() {
        return current.getSessionFactoryOptions();
    }

    @Override
    public SessionBuilder withOptions() {
        return current.withOptions();
    }

    @Override
    public Session openSession() throws HibernateException {
        return current.openSession();
    }

    @Override
    public Session getCurrentSession() throws HibernateException {
        return current.getCurrentSession();
    }

    @Override
    public StatelessSessionBuilder withStatelessOptions() {
        return current.withStatelessOptions();
    }

    @Override
    public StatelessSession openStatelessSession() {
        return current.openStatelessSession();
    }

    @Override
    public StatelessSession openStatelessSession(Connection connection) {
        return current.openStatelessSession(connection);
    }

    @Override
    public Statistics getStatistics() {
        return current.getStatistics();
    }

    @Override
    public void close() throws HibernateException {
        current.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return current.getProperties();
    }

    @Override
    public boolean isClosed() {
        return current.isClosed();
    }

    @Override
    public Cache getCache() {
        return current.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return current.getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String s, javax.persistence.Query query) {
        current.addNamedQuery(s, query);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return current.unwrap(aClass);
    }

    @Override
    public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {
        current.addNamedEntityGraph(s, entityGraph);
    }

    @Override
    public Set getDefinedFilterNames() {
        return current.getDefinedFilterNames();
    }

    @Override
    public FilterDefinition getFilterDefinition(String s) throws HibernateException {
        return current.getFilterDefinition(s);
    }

    @Override
    public boolean containsFetchProfileDefinition(String s) {
        return current.containsFetchProfileDefinition(s);
    }

    @Override
    public TypeHelper getTypeHelper() {
        return current.getTypeHelper();
    }

    @Override
    public ClassMetadata getClassMetadata(Class aClass) {
        return current.getClassMetadata(aClass);
    }

    @Override
    public ClassMetadata getClassMetadata(String s) {
        return current.getClassMetadata(s);
    }

    @Override
    public CollectionMetadata getCollectionMetadata(String s) {
        return current.getCollectionMetadata(s);
    }

    @Override
    public Map<String, ClassMetadata> getAllClassMetadata() {
        return current.getAllClassMetadata();
    }

    @Override
    public Map getAllCollectionMetadata() {
        return current.getAllCollectionMetadata();
    }

    @Override
    public Reference getReference() throws NamingException {
        return current.getReference();
    }

    @Override
    public SessionFactoryImplementor getSessionFactory() {
        return current.getSessionFactory();
    }

    @Override
    public <T> List<EntityGraph<? super T>> findEntityGraphsByType(Class<T> aClass) {
        return current.findEntityGraphsByType(aClass);
    }

    @Override
    public EntityManager createEntityManager() {
        return current.createEntityManager();
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        return current.createEntityManager(map);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return current.createEntityManager(synchronizationType);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return current.createEntityManager(synchronizationType, map);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return current.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return current.getMetamodel();
    }

    @Override
    public String getEntityManagerFactoryName() {
        return current.getEntityManagerFactoryName();
    }

    @Override
    public EntityType getEntityTypeByName(String entityName) {
        return current.getEntityTypeByName(entityName);
    }

    @Override
    public boolean isOpen() {
        return current.isOpen();
    }

    /**
     * 返回查询器类型总行数。
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> long currentCount(DetachedCriteria criteria) {
        return HibernateUtils.count(this.getCurrentSession(), criteria);
    }

    /**
     * 返回类型总行数。
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> long currentCount(Class<T> entity) {
        return HibernateUtils.count(this.getCurrentSession(), entity);
    }

    @Override
    public <T> long count(Session session, Class<T> entity) {
        return HibernateUtils.count(session, entity);
    }

    @Override
    public <T> int update(Session session, CriteriaUpdate<T> update) {
        return HibernateUtils.update(session, update);
    }

    @Override
    public <T> int currentUpdate(CriteriaUpdate<T> update) {
        return HibernateUtils.update(this.getCurrentSession(), update);
    }

    @Override
    public <T> List<T> select(Session session, DetachedCriteria criteria, int start, int length) {
        return HibernateUtils.list(this.getCurrentSession(), criteria, start, length);
    }


    @Override
    public <T> List<T> select(Session session, DetachedCriteria criteria, int start, int length, Class<T> entity) {
        return HibernateUtils.list(session, criteria, start, length, entity);
    }

    @Override
    public <T> List<T> select(DetachedCriteria criteria, int start, int length, Class<T> entity) {
        return HibernateUtils.list(this.getCurrentSession(), criteria, start, length, entity);
    }

    @Override
    public <T> List<T> select(DetachedCriteria criteria, int start, int length) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.list(session, criteria, start, length);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length, String[] propertys) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.mapSelect(session, criteria, start, length, propertys);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, String[] propertys) {
        return HibernateUtils.mapSelect(session, criteria, start, length, propertys);
    }

    @Override
    public <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length, String[] propertys, String[] ass) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.mapSelect(session, criteria, start, length, propertys, ass);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, String[] propertys, String[] ass) {
        return HibernateUtils.mapSelect(session, criteria, start, length, propertys, ass);
    }

    @Override
    public <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, Class<?> map) {
        return HibernateUtils.mapSelect(session, criteria, start, length, map);
    }

    @Override
    public <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length, Class<?> map) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.mapSelect(session, criteria, start, length, map);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> mapSelect(Session session, DetachedCriteria criteria, Class<?> map) throws SQLException {
        return HibernateUtils.mapSelect(session, criteria, map);
    }

    @Override
    public <T> List<T> mapSelect(DetachedCriteria criteria, Class<?> map) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.mapSelect(session, criteria, map);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> mapSelect(DetachedCriteria criteria, int start, int length) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.mapSelect(session, criteria, start, length);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length) {
        return HibernateUtils.mapSelect(session, criteria, start, length);
    }

    @Override
    public <T> T get(Class<T> entity, Map<String, Object> where) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.get(session, entity, where);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T get(Class<T> entity, String name, Serializable id) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.get(session, entity, name, id);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> long count(Session session, Class<T> entity, Serializable id) {
        return HibernateUtils.count(session, entity, id);
    }

    @Override
    public <T> T get(Session session, DetachedCriteria criteria) {
        return HibernateUtils.get(session, criteria);
    }

    /**
     * 返回单例名称值。
     *
     * @param entity
     * @param uniqueName
     * @param id
     * @param <T>
     * @return
     */
    @Override
    public <T> T uniqueResult(Class<?> entity, Object id, String uniqueName) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.uniqueResult(session, entity, id, uniqueName);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public long count(Session session, DetachedCriteria criteria) {
        return HibernateUtils.count(session, criteria);
    }

    @Override
    public <T> boolean nameExists(Session session, Class<T> entity, String name, Serializable id) {
        return HibernateUtils.nameExists(session, entity, name, id);
    }

    @Override
    public <T> T uniqueResult(Session session, Class<?> entity, Map<String, Object> where, String uniqueName) {
        return HibernateUtils.uniqueResult(session, entity, where, uniqueName);
    }

    @Override
    public void insert(Session session, Object entity) throws Exception {
        HibernateUtils.insert(session, entity);
    }

    @Override
    public <T> int update(Class<T> entity, Map<String, Object> where, Map<String, Object> update) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.update(session, entity, where, update);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int updateUnique(Session session, Class<T> entity, Serializable id, String updateName, Object updateValue) {
        return HibernateUtils.updateUnique(session, entity, id, updateName, updateValue);
    }

    @Override
    public void update(Session session, Object entity) throws Exception {
        HibernateUtils.update(session, entity);
    }

    @Override
    public <T> T get(Session session, Class<T> entity, String[] names, Object[] values) throws Exception {
        return HibernateUtils.get(session, entity, names, values);
    }

    @Override
    public <T> boolean nameAndValueExists(Session session, Class<T> entity, String name, Object value) {
        return HibernateUtils.nameAndValueExists(session, entity, name, value);
    }

    @Override
    public <T, V> int deleteByIds(Class<T> entity, List<V> list) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity, list);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    /**
     * 删除单个。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    @Override
    public <T> int deleteById(Class<T> entity, Object id) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity, id);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int delete(Class<T> entity, Map<String, Object> map) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity, map);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> boolean exists(Session session, Class<T> entity) {
        return HibernateUtils.exists(session, entity);
    }

    @Override
    public <T> T currentGet(Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.get(this.getCurrentSession(), entity, names, values);
    }

    @Override
    public <T> int currentUpdate(Class<T> entity, Map<String, Object> where, Map<String, Object> update) throws SQLException {
        return HibernateUtils.update(this.getCurrentSession(), entity, where, update);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Map<String, Object> where, Map<String, Object> update) throws SQLException {
        return HibernateUtils.update(session, entity, where, update);
    }

    @Override
    public <T> T get(Class<T> entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.get(session, entity);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int update(Class<T> entity, Serializable id, String name, Object update) throws Exception {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.update(session, entity, id, name, update);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    /**
     * 删除指定条件。
     *
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    @Override
    public <T> int delete(Session session, Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.delete(session, entity, names, values);
    }

    @Override
    public <T> int deleteById(Session session, Class<T> entity, String name, Object value) {
        return HibernateUtils.delete(session, entity, name, value);
    }

    @Override
    public <T, V> int deleteByIds(Session session, Class<T> entity, String name, List<V> values) {
        return HibernateUtils.delete(session, entity, name, values);
    }

    @Override
    public <T> List<T> select(DetachedCriteria criteria) {
        return HibernateUtils.select(this.getCurrentSession(), criteria);
    }

    /**
     * @param exists
     * @return
     */
    @Override
    public boolean currentExists(DetachedCriteria exists) {
        return HibernateUtils.exists(this.getCurrentSession(), exists);
    }

    /**
     * @param entity
     * @param id
     * @param uniqueName
     * @param <T>
     * @return
     */
    @Override
    public <T> T currentUniqueResult(Class<?> entity, Serializable id, String uniqueName) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), entity, id, uniqueName);
    }

    @Override
    public <T> T currentUniqueResult(Class<?> entity, Map<String, Object> w, String uniqueName) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), entity, w, uniqueName);
    }

    @Override
    public <T> int currentDelete(Class<T> entity, Map<String, Object> w) {
        return HibernateUtils.delete(this.getCurrentSession(), entity, w);
    }

    /**
     * @param entity
     * @param w
     * @param <T>
     * @return
     */
    @Override
    public <T> int delete(Session session, Class<T> entity, Map<String, Object> w) {
        return HibernateUtils.delete(session, entity, w);
    }

    @Override
    public <T> int currentUpdate(Class<T> entity, Map<String, Object> w, String name, Object value) throws SQLException {
        return HibernateUtils.update(this.getCurrentSession(), entity, w, name, value);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Map<String, Object> w, String name, Object value) throws SQLException {
        return HibernateUtils.update(session, entity, w, name, value);
    }

    @Override
    public <T> int currentUpdate(Class<T> entity, Map<String, Object> w, List<String> list, String name, Object value) {
        return HibernateUtils.update(this.getCurrentSession(), entity, w, list, name, value);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Map<String, Object> w, List<String> list, String name, Object value) {
        return HibernateUtils.update(session, entity, w, list, name, value);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, List<String> list, String name, Object value) {
        return HibernateUtils.update(session, entity, list, name, value);
    }

    @Override
    public <T> int currentUpdate(Class<T> entity, Object id, String name, Object value) {
        return HibernateUtils.update(this.getCurrentSession(), entity, id, name, value);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Object id, String name, Object value) {
        return HibernateUtils.update(session, entity, id, name, value);
    }

    @Override
    public <T> boolean currentExists(Class<T> entity, Object id, String name, Object value) {
        return HibernateUtils.exists(this.getCurrentSession(), entity, id, name, value);
    }

    @Override
    public <T> boolean currentExists(Class<T> entity, String name, Object value) {
        return HibernateUtils.exists(this.getCurrentSession(), entity, name, value);
    }

    @Override
    public <T> boolean currentExists(Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.exists(this.getCurrentSession(), entity, names, values);
    }

    @Override
    public <T> T currentUniqueResult(Class<?> entity, String[] names, Object[] values, String uniqueName) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), entity, names, values, uniqueName);
    }

    @Override
    public <T> int currentUpdate(Class<T> entity, Object id, Map<String, Object> update) {
        return HibernateUtils.update(this.getCurrentSession(), entity, id, update);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Object id, Map<String, Object> update) {
        return HibernateUtils.update(session, entity, id, update);
    }

    @Override
    public <T> T currentUniqueResult(Class<?> entity, Object id, String uniqueName) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), entity, id, uniqueName);
    }

    @Override
    public <T> T currentGet(Class<T> entity, Object id) {
        return HibernateUtils.get(this.getCurrentSession(), entity, id);
    }

    @Override
    public <T> T currentGet(Class<T> entity, Map<String, Object> map) {
        return HibernateUtils.get(this.getCurrentSession(), entity, map);
    }

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
    @Override
    public <T> long currentCount(Class<T> entity, String[] names, Object[] values, List<String> nodeIds) {
        return HibernateUtils.count(this.getCurrentSession(), entity, names, values, nodeIds);
    }

    @Override
    public <T> boolean currentNotIdExists(Class<T> entity, Object id, String[] names, Object[] values) {
        return HibernateUtils.notIdExists(this.getCurrentSession(), entity, id, names, values);
    }

    @Override
    public <T> boolean currentNotIdExists(Class<T> entity, Object id, String name, Object value) {
        return HibernateUtils.notIdExists(this.getCurrentSession(), entity, id, name, value);
    }

    @Override
    public <T> int deleteByIds(Session session, Class<T> entity, String[] names, Object[] values, List<String> deletes) {
        return HibernateUtils.delete(session, entity, names, values, deletes);
    }

    @Override
    public <T> List<T> currentList(Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.list(this.getCurrentSession(), entity, names, values);
    }

    @Override
    public <T> List<T> currentList(Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order) {
        return HibernateUtils.list(this.getCurrentSession(), entity, names, values, order);
    }

    @Override
    public <T> List currentList(Class<T> entity, String[] names, Object[] values, String[] propertyNames) {
        return HibernateUtils.list(this.getCurrentSession(), entity, names, values, propertyNames);
    }

    @Override
    public <T> List currentList(Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order, String[] propertyNames) {
        return HibernateUtils.list(this.getCurrentSession(), entity, names, values, order, propertyNames);
    }

    @Override
    public <T> long currentCount(Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.count(this.getCurrentSession(), entity, names, values);
    }

    @Override
    public <T> long count(Session session, Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.count(session, entity, names, values);
    }

    @Override
    public <T> long count(Class<T> entity, String[] names, Object[] values) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            long l = HibernateUtils.count(session, entity, names, values);
            transaction.commit();
            return l;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T currentUniqueResult(String entityName, String id, String uniqueName) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), entityName, id, uniqueName);
    }

    @Override
    public <T> T uniqueResult(Session session, String entityName, String id, String uniqueName) {
        return HibernateUtils.uniqueResult(session, entityName, id, uniqueName);
    }

    @Override
    public <T> T uniqueResult(Session session, DetachedCriteria criteria) {
        return HibernateUtils.uniqueResult(session, criteria);
    }

    @Override
    public <T> T uniqueResult(DetachedCriteria criteria) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.uniqueResult(session, criteria);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T uniqueResult(Class<?> entity, String[] names, Object[] values, String uniqueName) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.uniqueResult(session, entity, names, values, uniqueName);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public Object[] currentUniqueResult(Class<?> entity, Object id, String[] uniqueNames) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), entity, id, uniqueNames);
    }

    @Override
    public Object[] uniqueResult(Session session, Class<?> entity, Object id, String[] uniqueNames) {
        return HibernateUtils.uniqueResult(session, entity, id, uniqueNames);
    }

    @Override
    public <T> int delete(Class<T> entity, String[] names, Object[] values) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity, names, values);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, String propertyName) {
        return HibernateUtils.list(session, entity, names, values, propertyName);
    }

    @Override
    public <T> List<T> currentList(Class<T> entity, String[] names, Object[] values, String propertyName) {
        return HibernateUtils.list(this.getCurrentSession(), entity, names, values, propertyName);
    }

    @Override
    public <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order, String propertyName) {
        return HibernateUtils.list(session, entity, names, values, order, propertyName);
    }

    @Override
    public <T> List<T> currentList(Class<T> entity, String[] names, Object[] values, org.hibernate.criterion.Order order, String propertyName) {
        return HibernateUtils.list(this.getCurrentSession(), entity, names, values, order, propertyName);
    }

    @Override
    public <T> boolean currentExists(Session session, Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.exists(session, entity, names, values);
    }


    @Override
    public <T> boolean exists(Session session, Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.exists(session, entity, names, values);
    }

    @Override
    public <T> T currentUniqueResult(DetachedCriteria criteria) {
        return HibernateUtils.uniqueResult(this.getCurrentSession(), criteria);
    }

    @Override
    public <T> boolean currentExists(Class<T> entity) {
        return HibernateUtils.exists(this.getCurrentSession(), entity);
    }

    @Override
    public <T> void update(Class<T> entity, Object value) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            HibernateUtils.update(session, entity, value);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> select(Class<T> entity, Object id) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.select(session, entity, id);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int update(Class<T> entity, Object id, Map<String, Object> update) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.update(session, entity, id, update);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> select(Class<T> entity, String[] names, Object[] values) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.select(session, entity, names, values);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Serializable id, String[] names, Object[] values) {
        return HibernateUtils.update(session, entity, id, names, values);
    }

    @Override
    public <T> int currentUpdate(Class<T> entity, Serializable id, String[] names, Object[] values) {
        return HibernateUtils.update(this.getCurrentSession(), entity, id, names, values);
    }

    @Override
    public <T> int deleteByIds(Class<T> entity, String name, String id, List<Object> list) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity, name, id, list);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int deleteById(Session session, Class<T> entity, Serializable id) {
        return HibernateUtils.delete(session, entity, id);
    }

    @Override
    public <T, V> int deleteByIds(Session session, Class<T> entity, List<V> list) {
        return HibernateUtils.delete(session, entity, list);
    }

    @Override
    public <T> T currentGet(Class<T> entity, String name, Serializable id) {
        return HibernateUtils.get(this.getCurrentSession(), entity, name, id);
    }

    @Override
    public <T> T findById(Session session, Class<T> entity, String name, Serializable id) {
        return HibernateUtils.get(session, entity, name, id);
    }

    @Override
    public <T> boolean exists(Session session, Class<T> entity, Serializable id) {
        return HibernateUtils.exists(session, entity, id);
    }

    @Override
    public <T> boolean currentExists(Class<T> entity, Serializable id) {
        return HibernateUtils.exists(this.getCurrentSession(), entity, id);
    }

    @Override
    public <T> boolean exists(Session session, Class<T> entity, Object id) {
        return HibernateUtils.exists(session, entity, id);
    }

    @Override
    public <T> boolean exists(Class<T> entity, Object id) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            boolean b = HibernateUtils.exists(session, entity, id);
            transaction.commit();
            return b;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> boolean currentNotNameExists(Class<T> entity, String notName, Object value, String[] names, Object[] values) {
        return HibernateUtils.notNameExists(this.getCurrentSession(), entity, notName, value, names, values);
    }

    @Override
    public <T> boolean notNameExists(Session session, Class<T> entity, String notName, Object value, String[] names, Object[] values) {
        return HibernateUtils.notNameExists(session, entity, notName, value, names, values);
    }

    @Override
    public <T> List<T> currentSelect(Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.select(this.getCurrentSession(), entity, names, values);
    }

    @Override
    public <T> List<T> select(Session session, Class<T> entity, String[] names, Object[] values) {
        return HibernateUtils.select(session, entity, names, values);
    }

    @Override
    public <T> Object currentMaxResult(Class<T> entity, String name, Map<String, Object> map) {
        return HibernateUtils.maxResult(this.getCurrentSession(), entity, name, map);
    }

    @Override
    public <T> Object maxResult(Session session, Class<T> entity, String name, Map<String, Object> map) {
        return HibernateUtils.maxResult(session, entity, name, map);
    }

    @Override
    public <T> int update(Session session, Class<T> entity, String name, Object value) {
        return HibernateUtils.update(session, entity, name, value);
    }

    /**
     * 更新指定名称的例所有行值。
     *
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public <T> int update(Class<T> entity, String name, Object value) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.update(session, entity, name, value);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int update(Session session, Class<T> entity, Map<String, Object> map) {
        return HibernateUtils.update(session, entity, map);
    }

    @Override
    public <T> int update(Class<T> entity, Map<String, Object> map) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.update(session, entity, map);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T uniqueResult(Session session, Class<?> entity, String uniqueName) {
        return HibernateUtils.uniqueResult(session, entity, uniqueName);
    }

    @Override
    public <T> T uniqueResult(Class<?> entity, String uniqueName) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.uniqueResult(session, entity, uniqueName);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> int delete(Session session, Class<T> entity) {
        return HibernateUtils.delete(session, entity);
    }

    @Override
    public <T> int delete(Class<T> entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> boolean exists(Session session, Class<T> entity, String name, Object v) {
        return HibernateUtils.exists(session, entity, name, v);
    }

    @Override
    public <T> boolean exists(Class<T> entity, String name, Object v) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            boolean b = HibernateUtils.exists(session, entity, name, v);
            transaction.commit();
            return b;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> select(Session session, Class<T> entity, String name, Object v) {
        return HibernateUtils.list(session, entity, name, v);
    }

    @Override
    public <T> List<T> select(Class<T> entity, String name, Object v) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.list(session, entity, name, v);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> select(Session session, Class<T> entity, String[] names, Object[] values, List<String> notIds) {
        return HibernateUtils.select(session, entity, names, values, notIds);
    }

    @Override
    public <T> List<T> select(Class<T> entity, String[] names, Object[] values, List<String> notIds) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.select(session, entity, names, values, notIds);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T findById(Session session, Class<T> entity, Object id) {
        return HibernateUtils.get(session, entity, id);
    }

    @Override
    public <K, V> Map<K, V> map(DetachedCriteria criteria, String[] projectionList) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            Map<K, V> map = HibernateUtils.map(session, criteria, projectionList);
            transaction.commit();
            return map;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T get(Class<T> entity, String[] names, Object[] values) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = HibernateUtils.get(session, entity, names, values);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T findById(Class<T> entity, Object id) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            T t = this.findById(session, entity, id);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

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
    @Override
    public <T, I> int deleteByIds(Class<T> entity, String name, List<I> ids) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            int i = HibernateUtils.delete(session, entity, name, ids);
            transaction.commit();
            return i;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    /**
     * @param session
     * @param entity
     * @param name
     * @param value
     * @param uniqueName
     * @param <T>
     * @return
     */
    @Override
    public <T> T uniqueResult(Session session, Class<?> entity, String name, Object value, String uniqueName) {
        return HibernateUtils.uniqueResult(session, entity, name, value, uniqueName);
    }

    /**
     * @param entity
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> findAll(Class<T> entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> list = HibernateUtils.select(session, entity);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public void insert(Object entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            HibernateUtils.insert(session, entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public void update(Object entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            HibernateUtils.update(session, entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <O> void insertAll(List<O> entityList) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            HibernateUtils.insertAll(session, entityList);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> long count(Class<T> entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            long c = HibernateUtils.count(session, entity);
            transaction.commit();
            return c;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public long count(DetachedCriteria criteria) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            long c = HibernateUtils.count(session, criteria);
            transaction.commit();
            return c;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

    @Override
    public <T> List<T> list(Class<T> entity) throws SQLException {
        Transaction transaction = null;
        try (Session session = this.openSession()) {
            transaction = session.beginTransaction();
            List<T> c = HibernateUtils.list(session, entity);
            transaction.commit();
            return c;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException(e);
        }
    }

//    @Override
//    public <E, K, V, I> List<Map<K, V>> mapSelect(Class<E> entity, List<I> ids, String[] projectionList) throws SQLException {
//        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
//        criteria.add(Restrictions.in(getEntityIdName(entity), ids));
//        Transaction transaction = null;
//        try (Session session = this.openSession()) {
//            transaction = session.beginTransaction();
//            List<Map<K, V>> list = HibernateUtils.mapSelect(session, criteria, projectionList);
//            transaction.commit();
//            return list;
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            throw new SQLException(e);
//        }
//    }
}