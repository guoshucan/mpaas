package ghost.framework.data.hibernate;
import ghost.framework.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.*;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.transform.Transformers;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Hibernate工具类
 * @Date: 2020/3/22:17:42
 */
public final class HibernateUtils {
    /**
     * 获取连接池
     *
     * @param sessionFactory
     * @return
     */
    public static DriverManagerConnectionProviderImpl.PooledConnections getPool(SessionFactory sessionFactory) {
        //获取连接对象
        ConnectionProvider connectionProvider = sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class);
        Object o = ReflectUtil.findField(connectionProvider, "state");
        //获取连接池
        DriverManagerConnectionProviderImpl.PooledConnections connections = ReflectUtil.findField(o, "pool");
        return connections;
    }

    public static <T> T deproxy(T obj) {
        if (obj == null)
            return obj;
        if (obj instanceof HibernateProxy) {
            // Unwrap Proxy;
            //      -- loading, if necessary.
            HibernateProxy proxy = (HibernateProxy) obj;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return (T) li.getImplementation();
        }
        return obj;
    }


    public static boolean isProxy(Object obj) {
        if (obj instanceof HibernateProxy)
            return true;
        return false;
    }

    // ----------------------------------------------------------------------------------


    public static boolean isEqual(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;
        Object d1 = deproxy(o1);
        Object d2 = deproxy(o2);
        if (d1 == d2 || d1.equals(d2))
            return true;
        return false;
    }

    public static boolean notEqual(Object o1, Object o2) {
        return !isEqual(o1, o2);
    }

    // ----------------------------------------------------------------------------------

    public static boolean isSame(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;
        Object d1 = deproxy(o1);
        Object d2 = deproxy(o2);
        if (d1 == d2)
            return true;
        return false;
    }

    public static boolean notSame(Object o1, Object o2) {
        return !isSame(o1, o2);
    }

    /**
     * 返回类型总行数。
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> Long count(Session session, Class<T> c) {
        DetachedCriteria dc = DetachedCriteria.forClass(c);
        Criteria criteria = dc.getExecutableCriteria(session);
        return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }


    public static <T> int update(Session session, CriteriaUpdate<T> update) {
        return session.createQuery(update).executeUpdate();
    }


    public static <T> List<T> list(Session session, DetachedCriteria criteria, int start, int length) {
        return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).list();
    }

    public static <T> List<T> list(Session session, DetachedCriteria criteria, int start, int length, Class<T> entity) {
        if (entity == null) {
            return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.aliasToBean(entity)).list();
    }

    public static <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length) {
        return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public static <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, String[] projectionList) {
        ProjectionList projections = Projections.projectionList();
        for (String p : projectionList) {
            if (p.indexOf(".") == -1) {
                if (p.indexOf(" as ") == -1) {
                    projections.add(Projections.property(p).as(p));
                } else {
                    String[] as = p.split("\\s+");
                    projections.add(Projections.property(as[0]).as(as[2]));
                }
            } else {
                String[] strings = StringUtils.split(p, ".");
                if (p.indexOf(" as ") == -1) {
                    projections.add(Projections.property(p).as(strings[strings.length - 1]));
                } else {
                    String[] as = p.split("\\s+");
                    projections.add(Projections.property(as[0]).as(as[2]));
                }
            }
        }
        criteria.setProjection(projections);
        return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public static <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, String[] propertys, String[] ass) {
        ProjectionList projectionList = Projections.projectionList();
        int i = 0;
        for (String p : propertys) {
            projectionList.add(Projections.property(p).as(ass[i]));
            i++;
        }
        criteria.setProjection(projectionList);
        return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public static <T> List<T> mapSelect(Session session, DetachedCriteria criteria, int start, int length, Class<?> map) {
        if (map == null) {
            return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return criteria.getExecutableCriteria(session).setFirstResult(start).setMaxResults(length).setResultTransformer(Transformers.aliasToBean(map)).list();
    }
    public static <T> List<T> mapSelect(Session session, DetachedCriteria criteria, String[] projectionList) {
        session.setCacheMode(CacheMode.GET);
        ProjectionList pl = Projections.projectionList();
        int i = 0;
        for (String p : projectionList) {
            pl.add(Projections.property(p));
            i++;
        }
        criteria.setProjection(pl);
        return criteria.getExecutableCriteria(session).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    public static <T> List<T> mapSelect(Session session, DetachedCriteria criteria, Class<?> map) {
        session.setCacheMode(CacheMode.GET);
        List<T> list;
        if (map == null) {
            list = criteria.getExecutableCriteria(session).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } else {
            list = criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(map)).list();
        }
        return list;
    }

    public static <T> List<T> select(Session session, DetachedCriteria criteria) {
        session.setCacheMode(CacheMode.GET);
        List<T> list = criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
        return list;
    }

    public static <T> T get(Session session, Class<T> entity, Map<String, Object> where) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            dc.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }
        List<T> l;
        session.setCacheMode(CacheMode.GET);
        l = dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (l.size() == 0) return null;
        return deproxy(l.get(0));
    }

    public static <T> T get(Session session, Class<T> entity, Object id) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        dc.add(Restrictions.eq(getEntityIdName(entity), id));
        session.setCacheMode(CacheMode.GET);
        List<T> l = dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (l == null || l.size() == 0) return null;
        return deproxy(l.get(0));
    }

    public static <T> T get(Session session, Class<T> entity, String name, Serializable id) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        dc.add(Restrictions.eq(name, id));
        List<T> l;
        session.setCacheMode(CacheMode.GET);
        l = dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (l == null || l.size() == 0) return null;
        return deproxy(l.get(0));
    }

    public static <T> long count(Session session, Class<T> entity, Serializable id) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaQuery<Long> q = qb.createQuery(Long.class);
        Root root = q.from(entity);
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        q.select(qb.countDistinct(root));
        return session.createQuery(q).getSingleResult().longValue();
    }

    public static <T> T get(Session session, DetachedCriteria criteria) {
        session.setCacheMode(CacheMode.GET);
        List<T> list = criteria.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (list == null || list.size() == 0) return null;
        return deproxy(list.get(0));
    }

    /**
     * 返回单例名称值。
     *
     * @param entity
     * @param id
     * @param uniqueName
     * @param <T>
     * @return
     */
    public static <T> T uniqueResult(Session session, Class<?> entity, Object id, String uniqueName) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaQuery<?> q = qb.createQuery(entity);
        Root root = q.from(entity);
        q.select(root.get(uniqueName));
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        return (T) session.createQuery(q).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public static long count(Session session, DetachedCriteria dc) {
        Criteria criteria = dc.getExecutableCriteria(session);
        return (long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    public static <T> boolean nameExists(Session session, Class<T> entity, String name, Serializable id) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaQuery<T> q = qb.createQuery(entity);
        Root root = q.from(entity);
        q.select(root.get(name));
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        return session.createQuery(q).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult() != null;
    }


    public static <T> Object uniqueResult(Session session, Class<T> entity, String resultName, Map<String, Object> where) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaQuery<T> q = qb.createQuery(entity);
        Root root = q.from(entity);
        q.select(root.get(resultName));
        int i = 0;
        Predicate[] predicateList = new Predicate[where.size()];
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            predicateList[i] = qb.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        q.where(predicateList);
        return session.createQuery(q).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public static void insert(Session session, Object entity) {
        session.save(entity);
    }

    public static <T> int update(Session session, Class<T> entity, Map<String, Object> where, Map<String, Object> update) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        //更新值。
        for (Map.Entry<String, Object> entry : update.entrySet())
            q.set(root.get(entry.getKey()), entry.getValue());
        //条件。
        Predicate[] predicateList = new Predicate[where.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            predicateList[i] = qb.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        q.where(predicateList);
        return session.createQuery(q).executeUpdate();
    }

    public static <T> int updateUnique(Session session, Class<T> entity, Serializable id, String updateName, Object updateValue) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        //更新值。
        q.set(root.get(updateName), updateValue);
        //条件。
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        return session.createQuery(q).executeUpdate();
    }

    public static void update(Session session, Object entity) {
        session.saveOrUpdate(entity);
    }

    public static <T> T get(Session session, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        session.setCacheMode(CacheMode.GET);
        Criteria criteria = dc.getExecutableCriteria(session);
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        criteria.setFetchSize(1);
        List<T> l = criteria.list();
        if (l.size() == 0) return null;
        return deproxy(l.get(0));
    }

    public static <T> boolean nameAndValueExists(Session session, Class<T> entity, String name, Object value) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        dc.add(Restrictions.eq(name, value));
        Criteria criteria = dc.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        criteria.setFetchSize(1);
        return criteria.uniqueResult() != null;
    }

    /**
     * 删除列表
     *
     * @param session 指定会话
     * @param entity  自定实体，会按照实体类型获取主键做条件判断
     * @param list    删除主键id列表
     * @param <T>     实体类型
     * @param <V>     主键类型
     * @return 返回完成删除数量
     */
    public static <T, V> int delete(Session session, Class<T> entity, List<V> list) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        delete.where(root.get(getEntityIdName(entity)).in(list));
        return session.createQuery(delete).executeUpdate();
    }

    /**
     * 删除单个。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public static <T> int delete(Session session, Class<T> entity, Object id) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        delete.where(builder.equal(root.get(getEntityIdName(entity)), id));
        return session.createQuery(delete).executeUpdate();
    }


    public static <T> int delete(Session session, Class<T> entity, Map<String, Object> map) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        Predicate[] predicates = new Predicate[map.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            predicates[i] = builder.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        delete.where(predicates);
        return session.createQuery(delete).executeUpdate();
    }

    public static <T> boolean exists(Session session, Class<T> entity) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        dc.setProjection(Projections.id());
        return dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult() != null;
    }

    public static <T> Object maxResult(Session session, Class<T> entity, String name, Map<String, Object> map) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        dc.setProjection(Projections.max(name));
        dc.add(Restrictions.allEq(map));
        return dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public static <T> T get(Session session, Class<T> entity) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        List<T> l = dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (l == null || l.size() == 0) return null;
        return deproxy(l.get(0));
    }

    public static <T> int update(Session session, Class<T> entity, Serializable id, String name, Object update) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        q.set(root.get(name), update);
        return session.createQuery(q).executeUpdate();
    }

    public static <T> int update(Session session, Class<T> entity, Map<String, Object> where, String name, Object update) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        Predicate[] predicates = new Predicate[where.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            predicates[i] = qb.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        q.where(predicates);
        q.set(root.get(name), update);
        return session.createQuery(q).executeUpdate();
    }

    /**
     * 删除指定条件。
     *
     * @param session
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public static <T> int delete(Session session, Class<T> entity, String[] names, Object[] values) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        Predicate[] predicates = new Predicate[names.length];
        int i = 0;
        for (String name : names) {
            predicates[i] = builder.equal(root.get(name), values[i]);
            i++;
        }
        delete.where(predicates);
        return session.createQuery(delete).executeUpdate();
    }

    public static <T, V> int delete(Session session, Class<T> entity, String name, List<V> list) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        delete.where(root.get(name).in(list));
        return session.createQuery(delete).executeUpdate();
    }

    public static <T> int delete(Session session, Class<T> entity, String name, Object value) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        delete.where(builder.equal(root.get(name), value));
        return session.createQuery(delete).executeUpdate();
    }

    /**
     * @param session
     * @param exists
     * @return
     */
    public static boolean exists(Session session, DetachedCriteria exists) {
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }


    public static <T> T uniqueResult(Session session, Class<?> entity, Map<String, Object> w, String resultName) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        exists.add(Restrictions.allEq(w));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.property(resultName));
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return (T) criteria.uniqueResult();
    }

    public static <T> int update(Session session, Class<T> entity, Map<String, Object> w, List<String> list, String name, Object value) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        Predicate[] predicates = new Predicate[w.size() + 1];
        int i = 0;
        for (Map.Entry<String, Object> entry : w.entrySet()) {
            predicates[i] = qb.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        predicates[i] = root.get(getEntityIdName(entity)).in(list);
        q.where(predicates);
        q.set(root.get(name), value);
        return session.createQuery(q).executeUpdate();
    }

    public static <T> int update(Session session, Class<T> entity, List<String> list, String name, Object value) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        q.where(qb.in(root.get(getEntityIdName(entity)).in(list)));
        q.set(root.get(name), value);
        return session.createQuery(q).executeUpdate();
    }

    public static <T> int update(Session session, Class<T> entity, Object id, String name, Object value) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        q.set(root.get(name), value);
        return session.createQuery(q).executeUpdate();
    }

    public static <T> boolean exists(Session session, Class<T> entity, Object id, String name, Object value) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        exists.add(Restrictions.eq(getEntityIdName(entity), id));
        exists.add(Restrictions.eq(name, value));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }

    public static <T> boolean exists(Session session, Class<T> entity, String name, Object value) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        exists.add(Restrictions.eq(name, value));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }

    /**
     * 获取返回唯一字段
     *
     * @param session    会话对象
     * @param entity     实体类型
     * @param names      查询字段
     * @param values     查询字段值
     * @param uniqueName 返回字段名称
     * @param <T>
     * @return
     */
    public static <T> T uniqueResult(Session session, Class<?> entity, String[] names, Object[] values, String uniqueName) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            exists.add(Restrictions.eq(name, values[i]));
            i++;
        }
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.property(uniqueName));
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return (T) criteria.uniqueResult();
    }

    public static <T> T uniqueResult(Session session, Class<?> entity, String name, Object value, String uniqueName) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        exists.add(Restrictions.eq(name, value));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.property(uniqueName));
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return (T) criteria.uniqueResult();
    }

    public static <T> int update(Session session, Class<T> entity, Object id, Map<String, Object> update) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<T> u = builder.createCriteriaUpdate(entity);
        Root<T> root = u.from(entity);
        u.where(builder.equal(root.get(getEntityIdName(entity)), id));
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            u.set(root.get(entry.getKey()), entry.getValue());
        }
        return session.createQuery(u).executeUpdate();
    }

    /**
     * 获取指定条件数量。
     *
     * @param session
     * @param entity
     * @param names
     * @param values
     * @param nodeIds
     * @param <T>
     * @return
     */
    public static <T> long count(Session session, Class<T> entity, String[] names, Object[] values, List<String> nodeIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        criteria.add(Restrictions.in(getEntityIdName(entity), nodeIds));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.getExecutableCriteria(session).uniqueResult();
    }

    /**
     * 判断排除主键的例是否存在
     *
     * @param session 会话对象
     * @param entity  实体类型
     * @param id      主键id
     * @param name    例名称
     * @param value   例值
     * @param <T>     实体类型
     * @return
     */
    public static <T> boolean notIdExists(Session session, Class<T> entity, Object id, String name, Object value) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        exists.add(Restrictions.eq(name, value));
        exists.add(Restrictions.not(Restrictions.eq(getEntityIdName(entity), id)));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }

    /**
     * 判断排除主键的例是否存在
     *
     * @param session 会话对象
     * @param entity  实体类型
     * @param id      主键id
     * @param names   数组例名称
     * @param values  数组例值
     * @param <T>     实体类型
     * @return
     */
    public static <T> boolean notIdExists(Session session, Class<T> entity, Object id, String[] names, Object[] values) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            exists.add(Restrictions.eq(name, values[i]));
            i++;
        }
        exists.add(Restrictions.not(Restrictions.eq(getEntityIdName(entity), id)));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }

    public static <T> int delete(Session session, Class<T> entity, String[] names, Object[] values, List<String> deletes) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        Predicate[] predicates = new Predicate[names.length + 1];
        int i = 0;
        for (String name : names) {
            predicates[i] = builder.equal(root.get(name), values[i]);
            i++;
        }
        predicates[i] = builder.in(root.get(getEntityIdName(entity))).in(deletes);
        delete.where(predicates);
        return session.createQuery(delete).executeUpdate();
    }

    public static <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        return dc.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
    }

    public static <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, Order order) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        dc.addOrder(order);
        return dc.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
    }

    public static <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, String[] propertyNames) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        for (String name : propertyNames) {
            dc.setProjection(Projections.property(name));
        }
        return dc.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
    }

    public static <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, Order order, String[] propertyNames) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        dc.addOrder(order);
        for (String name : propertyNames) {
            dc.setProjection(Projections.property(name));
        }
        return dc.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
    }

    public static <T> long count(Session session, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.getExecutableCriteria(session).uniqueResult();
    }

    /**
     * 获取返回唯一字段
     *
     * @param session    会话对象
     * @param entityName 实体类型名称
     * @param id         主键id
     * @param uniqueName 返回字段
     * @param <T>
     * @return
     */
    public static <T> T uniqueResult(Session session, String entityName, Object id, String uniqueName) {
        DetachedCriteria exists = DetachedCriteria.forEntityName(entityName);
        exists.add(Restrictions.eq(getEntityIdName(entityName), id));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.property(uniqueName));
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return (T) criteria.uniqueResult();
    }

    public static <T> T uniqueResult(Session session, DetachedCriteria criteria) {
        return (T) criteria.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public static Object[] uniqueResult(Session session, Class<?> entity, Object id, String[] resultNames) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getEntityIdName(entity), id));
        ProjectionList projectionList = Projections.projectionList();
        for (String name : resultNames) {
            projectionList.add(Projections.property(name));
        }
        criteria.setProjection(projectionList);
        List<Object[]> list = criteria.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (list.size() == 0) return new Object[]{};
        return list.get(0);
    }

    public static <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, String propertyName) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        dc.setProjection(Projections.property(propertyName));
        return dc.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
    }

    public static <T> List<T> list(Session session, Class<T> entity, String[] names, Object[] values, Order order, String propertyName) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        dc.addOrder(order);
        dc.setProjection(Projections.property(propertyName));
        return dc.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(HashMap.class)).list();
    }

    public static <T> boolean exists(Session session, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            dc.add(Restrictions.eq(name, values[i]));
            i++;
        }
        dc.setProjection(Projections.id());
        return dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult() != null;
    }

    /**
     * 更新实体
     *
     * @param session 会话对象
     * @param entity  实体类型
     * @param value   实体对象
     * @param <T>     实体类型
     */
    public static <T> void update(Session session, Class<T> entity, Object value) {
        session.saveOrUpdate(value);
    }

    /**
     * 获取列表
     *
     * @param session 会话对象
     * @param entity  实体类型
     * @param id      主键id
     * @param <T>     实体类型
     * @return 返回实体列表
     */
    public static <T> List<T> select(Session session, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getEntityIdName(entity), id));
        return criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(entity)).list();
    }

    public static <T> List<T> select(Session session, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        return criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(entity)).list();
    }


    public static <T> List<T> select(Session session, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        return criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(entity)).list();
    }

    public static <T> int update(Session session, Class<T> entity, Serializable id, String[] names, Object[] values) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        //更新值。
        int i = 0;
        for (String name : names) {
            q.set(root.get(name), values[i]);
            i++;
        }
        //条件。
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        return session.createQuery(q).executeUpdate();
    }

    public static <T> int delete(Session session, Class<T> entity, String name, String id, List<Object> list) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaDelete<T> q = qb.createCriteriaDelete(entity);
        Root root = q.from(entity);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = qb.equal(root.get(name), id);
        predicates[1] = qb.in(root.get(getEntityIdName(entity))).in(list);
        q.where(predicates);
        return session.createQuery(q).executeUpdate();
    }

    public static <T> int delete(Session session, Class<T> entity, Serializable id) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaDelete<T> q = qb.createCriteriaDelete(entity);
        Root root = q.from(entity);
        q.where(qb.equal(root.get(getEntityIdName(entity)), id));
        return session.createQuery(q).executeUpdate();
    }

    public static <T> boolean exists(Session session, Class<T> entity, Serializable id) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        exists.add(Restrictions.eq(getEntityIdName(entity), id));
        exists.setProjection(Projections.id());
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }

    public static <T> boolean exists(Session session, Class<T> entity, Object id) {
        Object r = null;
        DetachedCriteria dc = DetachedCriteria.forClass(entity);
        dc.add(Restrictions.eq(getEntityIdName(entity), id));
        dc.setProjection(Projections.id());
        Transaction transaction = session.beginTransaction();
        try {
            r = dc.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            throw e;
        }
        return r != null;
    }

    public static <T> boolean notNameExists(Session session, Class<T> entity, String notName, Object value, String[] names, Object[] values) {
        DetachedCriteria exists = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            exists.add(Restrictions.eq(name, values[i]));
            i++;
        }
        exists.add(Restrictions.not(Restrictions.eq(notName, value)));
        Criteria criteria = exists.getExecutableCriteria(session);
        criteria.setProjection(Projections.id());
        criteria.setFetchSize(1).setFirstResult(0).setMaxResults(1);
        return criteria.uniqueResult() != null;
    }

    public static <T> int update(Session session, Class<T> entity, Map<String, Object> map) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaUpdate<T> q = qb.createCriteriaUpdate(entity);
        Root root = q.from(entity);
        //更新值。
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            q.set(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        return session.createQuery(q).executeUpdate();
    }

    public static <T> T uniqueResult(Session session, Class<?> entity, String uniqueName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.setProjection(Projections.property(uniqueName));
        return (T) criteria.getExecutableCriteria(session).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public static <T> int delete(Session session, Class<T> entity) {
        CriteriaBuilder qb = session.getCriteriaBuilder();
        CriteriaDelete<T> q = qb.createCriteriaDelete(entity);
        Root root = q.from(entity);
        return session.createQuery(q).executeUpdate();
    }


    public static <T> List<T> list(Session session, Class<T> entity, String name, Object v) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(name, v));
        return criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(entity)).list();
    }

    public static <T> List<T> select(Session session, Class<T> entity, String[] names, Object[] values, List<String> notIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        if (notIds != null && notIds.size() > 0) {
            criteria.add(Restrictions.not(Restrictions.in(getEntityIdName(entity), notIds)));
        }
        return criteria.getExecutableCriteria(session).setResultTransformer(Transformers.aliasToBean(entity)).list();
    }

    /**
     * 获取实体主键Id字段属性名称。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> String getEntityIdName(Class<T> entity) {
        //该类所有的属性
        for (Field field : entity.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Id.class)) {
                    return field.getName();
                }
            } catch (NullPointerException e) {

            }
        }
        return null;
    }

    /**
     * 获取实体指定例的名称。
     *
     * @param entity
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> String getEntityFieldName(Class<T> entity, String fieldName) {
        //该类所有的属性
        for (Field field : entity.getDeclaredFields()) {
            try {
                if (field.getName().equals(fieldName) && field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
            } catch (NullPointerException e) {

            }
        }
        return null;
    }

    public static <T> String getEntityTableName(Class<T> entity) {
        try {
            if (entity.isAnnotationPresent(Table.class)) {
                return entity.getAnnotation(Table.class).name();
            }
        } catch (NullPointerException e) {
        }
        return null;
    }

    public static String getEntityIdName(String entityName) {
        Object entity = null;
        try {
            entity = Class.forName(entityName);
        } catch (ClassNotFoundException not) {
            return null;
        }
        //该类所有的属性
        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Id.class)) {
                    return field.getName();
                }
            } catch (NullPointerException e) {

            }
        }
        return null;
    }

    public static <T> int update(Session session, Class<T> entity, String name, Object value) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<T> u = builder.createCriteriaUpdate(entity);
        Root<T> root = u.from(entity);
        u.set(root.get(name), value);
        return session.createQuery(u).executeUpdate();
    }

    /**
     * 获取实体map对象
     *
     * @param session
     * @param criteria
     * @param projectionList 返回参数列表
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> map(Session session, DetachedCriteria criteria, String[] projectionList) {
        List<Map<K, V>> list = mapSelect(session, criteria, 0, 1, projectionList);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public static <T> boolean notExists(Session session, Class<T> entity, String name, Object value) {
        return !exists(session, entity, name, value);
    }

    public static <O> void insertAll(Session session, List<O> entityList) {
        for (Object entity : entityList) {
            session.save(entity);
        }
    }

    public static <T> List<T> list(Session session, Class<T> entity) {
        return DetachedCriteria.forClass(entity).getExecutableCriteria(session).list();
    }
}