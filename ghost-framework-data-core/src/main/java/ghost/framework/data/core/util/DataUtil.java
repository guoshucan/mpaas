package ghost.framework.data.core.util;
import ghost.framework.data.core.DataPage;
import ghost.framework.data.core.IDataBase;
import ghost.framework.data.core.IDataBaseError;
import ghost.framework.data.core.IDataSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import javax.persistence.Id;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据操作工具类。
 * @Date: 11:47 2018-07-22
 */
public final class DataUtil {
    /**
     * 获取实体主键值。
     *
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getIdValue(Object entity) throws IllegalArgumentException, IllegalAccessException {
        Class entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field.get(entity);
                }
            } catch (NullPointerException e) {
            }
        }
        return null;
    }

    /**
     * 获取实体主键名称。
     *
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> String getIdName(Class<T> entity) throws IllegalArgumentException {
        for (Field field : entity.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Id.class)) {
                    return field.getName();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return null;
    }

    public static int openDelete(IDataBase d, Object entity) throws Exception {
        return openDeleteById(d, entity.getClass(), getIdValue(entity));
    }

    public static int openDelete(IDataBase d, Class<?> entity) throws Exception {
        int n = 0;
        Session s = d.openSession();
        Transaction t = s.beginTransaction();
        try {
            d.plusDelete();
            begin(d, t);
            n = currentDelete(s, entity);
            commit(s, t);
        } catch (Exception e) {
            t.rollback();
            d.getController().error(d, new Object[]{entity}, e);
        } finally {
            deleteClose(d, s);
        }
        return n;
    }

    /**
     * 创建统计操作器。
     *
     * @param s
     * @param criteria
     * @return
     */
    public static long currentCount(Session s, DetachedCriteria criteria) {
        Criteria c = criteria.getExecutableCriteria(s);
        c.setProjection(Projections.rowCount());
        return (long) c.uniqueResult();
    }

    /**
     * 删除。
     *
     * @param s
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public static <T> int currentDeleteById(Session s, Class<T> entity, Object id) {
        return s.createQuery(createCriteriaDelete(s, entity, id)).executeUpdate();
    }

    public static <T> int currentDelete(Session s, Class<T> entity, List<Object> arrays) {
        return s.createQuery(createCriteriaDelete(s, entity, arrays)).executeUpdate();
    }

    /**
     * 删除。
     *
     * @param s
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    public static <T> int currentDelete(Session s, Class<T> entity, String name, Object id) {
        return s.createQuery(createCriteriaDelete(s, entity, name, id)).executeUpdate();
    }

    /**
     * 删除指定实体。
     *
     * @param s
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public static <T> int currentDelete(Session s, Class<T> entity, String[] names, Object[] values) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(entity);
        Root<T> root = query.from(entity);
        Predicate[] predicates = new Predicate[names.length];
        int i = 0;
        for (String name : names) {
            predicates[i] = builder.equal(root.get(name), values[i]);
            i++;
        }
        query.where(predicates);
        return s.createQuery(query).executeUpdate();
    }

    /**
     * 验证实体是否存在数据。
     *
     * @param s
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> boolean currentExists(Session s, Class<T> entity) throws HibernateException {
        return isExists(DetachedCriteria.forClass(entity).getExecutableCriteria(s));
    }

    /**
     * 验证实体是否存在指定属性内容数据。
     *
     * @param s
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public static <T> boolean currentExists(Session s, Class<T> entity, String[] names, Object[] values) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        return isExists(criteria.getExecutableCriteria(s));
    }

    public static <T> Object currentUniqueResult(Session s, Class<T> entity, HashMap<String, Object> w) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.allEq(w));
        return currentUniqueResult(s, criteria);
    }

    /**
     * 创建单例查询。
     *
     * @param s
     * @param criteria
     * @return
     */
    public static boolean currentExists(Session s, DetachedCriteria criteria) throws HibernateException {
        return isExists(criteria.getExecutableCriteria(s));
    }

    /**
     * 创建单例查询。
     *
     * @param s
     * @param criteria
     * @return
     */
    public static <R> R currentUniqueResult(Session s, DetachedCriteria criteria) throws HibernateException {
        Criteria c = criteria.getExecutableCriteria(s);
        c.setFirstResult(0);
        c.setMaxResults(1);
        c.setFetchSize(1);
        Object o = c.uniqueResult();
        if (o == null) return null;
        return (R) o;
    }

    /**
     * 获取指定表列表。
     *
     * @param s
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public static <T> List<T> currentSelect(Session s, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        return criteria.getExecutableCriteria(s).list();
    }

    /**
     * 获取指定表列表。
     *
     * @param s
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public static <T> List<T> currentSelect(Session s, Class<T> entity, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        setValue(criteria, name, value);
        return criteria.getExecutableCriteria(s).list();
    }

    /**
     * 设置值
     *
     * @param criteria
     * @param name
     * @param value
     */
    private static void setValue(DetachedCriteria criteria, String name, Object value) {
        if (value == null) {
            criteria.add(Restrictions.isNull(name));
        } else {
            if (value.equals("")) {
                criteria.add(Restrictions.isEmpty(name));
            } else {
                criteria.add(Restrictions.eq(name, value));
            }
        }
    }

    /**
     * 验证实体是否存在。
     *
     * @param s
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public static <T> boolean currentExists(Session s, Class<T> entity, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        setValue(criteria, name, value);
        criteria.setProjection(Projections.id());
        return criteria.getExecutableCriteria(s).setFetchSize(1).setFirstResult(0).setMaxResults(1).uniqueResult() != null;
    }

    /**
     * @param s
     * @param entity
     * @param map
     * @param <T>
     * @return
     */
    public static <T> boolean currentExists(Session s, Class<T> entity, HashMap<String, Object> map) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.allEq(map));
        return isExists(criteria.getExecutableCriteria(s));
    }

    /**
     * 或取数据库全部实体对象。
     *
     * @param s
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> List<T> currentSelect(Session s, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        return criteria.getExecutableCriteria(s).list();
    }

    public static <T> List<T> currentSelect(Session s, DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(s).list();
    }

    /**
     * 验证实体指定Id是否存在。
     *
     * @param s
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean currentExists(Session s, Class<T> entity, Object id) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        return isExists(criteria.getExecutableCriteria(s));
    }

    /**
     * 按照指定排除主键验证指定例参数值是否存在。
     *
     * @param s
     * @param entity
     * @param id
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public static <T> boolean currentExistsByNotId(Session s, Class<T> entity, Object id, String name, Object value) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
        setValue(criteria, name, value);
        return isExists(criteria.getExecutableCriteria(s));
    }

    /**
     * 获取指定实体的一行数据。
     *
     * @param s
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> T currentGet(Session s, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        return HibernateUtil.deproxy(get(s, criteria));
    }

    public static <T> T get(Session s, DetachedCriteria criteria) {
        List<T> l = criteria.getExecutableCriteria(s).setFetchSize(1).setFirstResult(0).setMaxResults(1).list();
        if (l.size() == 0) return null;
        return HibernateUtil.deproxy(l.get(0));
    }

    /**
     * 按照指定排除主键验证指定例参数值是否存在。
     *
     * @param s
     * @param entity
     * @param id
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public static <T> boolean currentExistsByNotId(Session s, Class<T> entity, Object id, String[] names, Object[] values) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
        int i = 0;
        for (String name : names) {
            setValue(criteria, name, values[i]);
            i++;
        }
        return isExists(criteria.getExecutableCriteria(s));
    }

    public static boolean isExists(Criteria criteria) throws HibernateException {
        criteria.setProjection(Projections.id());
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        criteria.setFetchSize(1);
        return criteria.uniqueResult() != null;
    }

    /**
     * @param s
     * @param entity
     * @param where
     * @param update
     * @param <T>
     * @return
     */
    public static <T> int currentUpdate(Session s, Class<T> entity, HashMap<String, Object> where, HashMap<String, Object> update) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        Predicate[] predicates = new Predicate[where.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            predicates[i] = builder.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }
        query.where(predicates);
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            query.set(root.get(entry.getKey()), entry.getValue());
        }
        return s.createQuery(query).executeUpdate();
    }

    /**
     * 按照主键Id更新指定列表。
     *
     * @param s
     * @param entity
     * @param id
     * @param update
     * @param <T>
     * @return
     */
    public static <T> int currentUpdate(Session s, Class<T> entity, Object id, HashMap<String, Object> update) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        query.where(builder.equal(root.get(getIdName(entity)), id));
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            query.set(root.get(entry.getKey()), entry.getValue());
        }
        return s.createQuery(query).executeUpdate();
    }

    /**
     * 获取一行数据。
     *
     * @param s      会话对象。
     * @param entity 指定实体。
     * @param id     主键Id。
     * @param <T>
     * @return 返回一行数据，如果没有找到侧返回空。
     */
    public static <T> T currentSingleResult(Session s, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        return singleResult(criteria.getExecutableCriteria(s));
    }

    /**
     * 获取单行。
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T singleResult(Criteria c) {
        c.setFirstResult(0);
        c.setMaxResults(1);
        c.setFetchSize(1);
        List<T> list = c.list();
        if (list.size() == 0) return null;
        return HibernateUtil.deproxy(list.get(0));
    }

    /**
     * 获取一行数据。
     *
     * @param s      会话对象。
     * @param entity 指定实体。
     * @param <T>
     * @return 返回一行数据，如果没有找到侧返回空。
     */
    public static <T> T currentSingleResult(Session s, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        return singleResult(criteria.getExecutableCriteria(s));
    }

    /**
     * 创建获取实体一行数据查询器。
     *
     * @param s
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> CriteriaQuery<T> singleResult(Session s, Class<T> entity) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entity);
        //要创建root，不然会报Transaction was marked for rollback only; cannot commit错误。
        Root<T> root = query.from(entity);
        return query;
    }

    public static <T> boolean currentExistsByNotId(Session s, Class<T> entity, Object id, HashMap<String, Object> map) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
        criteria.add(Restrictions.allEq(map));
        return isExists(criteria.getExecutableCriteria(s));
    }

    /**
     * 创建删除操作器。
     *
     * @param s
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public static <T> CriteriaDelete createCriteriaDelete(Session s, Class<T> entity, Object id) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        //创建删除JPA2.1规范删除操作对象。
        CriteriaDelete<T> criteria = builder.createCriteriaDelete(entity);
        //获取主键名称与设置主键值。
        criteria.where(builder.equal(criteria.from(entity).get(getIdName(entity)), id));
        return criteria;
    }

    public static <T> CriteriaDelete createCriteriaDelete(Session s, Class<T> entity, List<Object> arrays) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(entity);
        Root<T> root = query.from(entity);
        query.where(root.get(getIdName(entity)).in(arrays));
        return query;
    }

    /**
     * 创建删除jpa。
     *
     * @param s
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> int currentDelete(Session s, Class<T> entity) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaDelete delete = builder.createCriteriaDelete(entity);
        Root<T> root = delete.from(entity);
        return s.createQuery(delete).executeUpdate();
    }

    /**
     * 当前线程会话查询单行。
     *
     * @param s
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public static <T> List<T> currentSelect(Session s, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        return criteria.getExecutableCriteria(s).list();
    }

    /**
     * 当前线程会话更新实体属性列表。
     *
     * @param entity 实体。
     * @param update 实体属性列表，如果字段包含主键侧自动指定主键条件。
     * @param <T>
     * @return
     */
    public static <T> int currentUpdate(Session s, Class<T> entity, HashMap<String, Object> update) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        //获取主键Id名称。
        String idName = getIdName(entity);
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            //验证是否为主键。
            if (entry.getKey().equals(idName)) {
                query.where(builder.equal(root.get(idName), entry.getValue()));
            } else {
                query.set(root.get(entry.getKey()), entry.getValue());
            }
        }
        return s.createQuery(query).executeUpdate();
    }

    /**
     * 当前线程会话获取实体指定字段唯一键。
     *
     * @param s
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    public static <T> Object currentByIdNameUniqueResult(Session s, Class<T> entity, Object id, String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        criteria.setProjection(Projections.property(name));
        return currentUniqueResult(s, criteria);
    }

    /**
     * 返回主键Id
     * @param s
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public static <T> Object currentByIdUniqueResult(Session s, Class<T> entity, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        setValue(criteria, name, value);
        criteria.setProjection(Projections.property(getIdName(entity)));
        return currentUniqueResult(s, criteria);
    }
    /**
     * 返回主键Id
     * @param s
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> Object currentByIdUniqueResult(Session s, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.setProjection(Projections.property(getIdName(entity)));
        return currentUniqueResult(s, criteria);
    }
    /**
     * 实例返回行数据包装。
     *
     * @param result
     * @param list
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T instanceSingleResultTransformer(Class<T> result, List<HashMap<String, Object>> list)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (list == null || list.size() == 0) return null;
        //map转对象。
        Object obj = result.newInstance();
//        //DateLocaleConverter 为本地化date转换器。
//        //DateConverter
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.populate(obj, HibernateUtil.deproxy(list.get(0)));
        return (T) obj;
    }

    public static void currentInsert(IDataBase d, Session s, Object entity) throws Exception {
        try {
            d.plusInsert();
            s.save(entity);
        } finally {
            d.reduceInsert();
        }
    }

    public static <T> int currentUpdate(IDataBase d, Session s, Class<T> entity, HashMap<String, Object> update) {
        try {
            d.plusUpdate();
            return currentUpdate(s, entity, update);
        } finally {
            d.reduceUpdate();
        }
    }

    public static void currentUpdate(IDataBase d, Session s, Object entity) {
        try {
            d.plusUpdate();
            s.saveOrUpdate(entity);
        } finally {
            d.reduceUpdate();
        }
    }

    public static <T> List<T> currentSelect(IDataBase currentDataBase, Session currentSession, DetachedCriteria criteria) {
        try {
            currentDataBase.plusSelect();
            return currentSelect(currentDataBase, currentSession, criteria);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> List<T> currentSelect(IDataBase currentDataBase, Session currentSession, DetachedCriteria criteria, int start, int index) {
        try {
            currentDataBase.plusSelect();
            return criteria.getExecutableCriteria(currentSession).setFirstResult(start).setMaxResults(index).list();
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> List<T> openSelect(IDataBase d, DetachedCriteria criteria) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return criteria.getExecutableCriteria(s).list();
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> List<T> openSelect(IDataBase d, DetachedCriteria criteria, int start, int index) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return criteria.getExecutableCriteria(s).setFirstResult(start).setMaxResults(index).list();
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T currentSingleResult(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) {
        try {
            currentDataBase.plusSelect();
            return currentSingleResult(currentSession, entity, id);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> T openById(IDataBase d, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        Session s = d.openSession();
        try {
            d.plusSelect();
            return openById(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T openById(Session s, DetachedCriteria criteria) {
        List<T> l = criteria.getExecutableCriteria(s).setFirstResult(0).setMaxResults(1).setFetchSize(1).list();
        if (l == null || l.size() == 0) return null;
        return HibernateUtil.deproxy(l.get(0));
    }

    public static <T> T openById(IDataBase d, Class<T> entity, String name, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(name, id));
        Session s = d.openSession();
        try {
            d.plusSelect();
            return openById(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> boolean currentExists(IDataBase currentDataBase, Session currentSession, Class<T> entity, HashMap<String, Object> map) {
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, entity, map);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean currentExists(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) {
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, entity, id);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean currentExists(IDataBase currentDataBase, Session currentSession, Class<T> entity, String name, Object id) {
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, entity, name, id);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean currentExists(IDataBase currentDataBase, Session currentSession, Class<T> entity, String[] names, Object[] values) {
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, entity, names, values);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean currentExistsByNotId(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id, HashMap<String, Object> map) {
        try {
            currentDataBase.plusSelect();
            return currentExistsByNotId(currentSession, entity, id, map);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity, Object id, HashMap<String, Object> map) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            DetachedCriteria criteria = DetachedCriteria.forClass(entity);
            criteria.add(Restrictions.eq(getIdName(entity), id));
            for (Map.Entry<String, Object> entry : map.entrySet())
                //criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
                setValue(criteria, entry.getKey(), entry.getValue());
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity, HashMap<String, Object> map) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.allEq(map));
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> List<T> currentSelect(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) {
        try {
            currentDataBase.plusSelect();
            return currentSelect(currentSession, entity, id);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean currentSelect(IDataBase currentDataBase, Session currentSession, Class<T> entity) {
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, entity);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <T> boolean currentExists(IDataBase currentDataBase, Session currentSession, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, criteria);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static boolean openExists(IDataBase d, DetachedCriteria criteria) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, entity);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T openSingleResult(IDataBase d, Class<T> entity) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentSingleResult(s, entity);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T currentSingleResult(IDataBase currentDataBase, Session currentSession, Class<T> entity) {
        try {
            currentDataBase.plusSelect();
            return currentSingleResult(currentSession, entity);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static boolean currentExists(IDataBase currentDataBase, Session currentSession, DetachedCriteria criteria) {
        try {
            currentDataBase.plusSelect();
            return currentExists(currentSession, criteria);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static long currentCount(IDataBase currentDataBase, Session currentSession, DetachedCriteria criteria) {
        try {
            currentDataBase.plusSelect();
            return currentCount(currentSession, criteria);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static long openCount(IDataBase d, Class<?> entity) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentCount(s, DetachedCriteria.forClass(entity));
        } finally {
            selectClose(d, s);
        }
    }

    public static long openCount(IDataBase d, DetachedCriteria criteria) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentCount(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static Object currentUniqueResult(IDataBase currentDataBase, Session currentSession, DetachedCriteria criteria) {
        try {
            currentDataBase.plusSelect();
            return currentUniqueResult(currentSession, criteria);
        } finally {
            currentDataBase.reduceSelect();
        }
    }

    public static <R> R openUniqueResult(IDataBase d, DetachedCriteria criteria) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentUniqueResult(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static void openDeleteById(IDataBase d, String entityName, Object id) throws Exception {
        Session s = d.openSession();
        try {
            d.plusDelete();
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                s.delete(entityName, id);
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entityName, id}, e);
            }
        } finally {
            deleteClose(d, s);
        }
    }

    public static <T> int openDeleteById(IDataBase d, Class<T> entity, Object id) {
        int c = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            //执行删除操作。
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                c = currentDeleteById(s, entity, id);
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                d.getController().error(d, new Object[]{entity, id}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    public static <T> List<T> openTransactionSelect(IDataBase d, Class<T> entity, String[] names, Object[] values) {
        List<T> list = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                list = currentSelect(s, entity, names, values);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                d.getController().error(d, new Object[]{entity, names, values}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return list;
    }

    public static <T> List<T> openSelect(IDataBase d, Class<T> entity, String[] names, Object[] values) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentSelect(s, entity, names, values);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> Object openTransactionUniqueResult(IDataBase d, Class<T> entity, String name, Object id) {
        Object o = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                o = currentByIdNameUniqueResult(s, entity, id, name);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                d.getController().error(d, new Object[]{entity, name, id}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return o;
    }

    public static <T> Object openByIdNameUniqueResult(IDataBase d, Class<T> entity, Object id, String name) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentByIdNameUniqueResult(s, entity, id, name);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T openSingleResultTransformer(IDataBase d, Class<T> result, DetachedCriteria criteria) throws Exception {
        List<HashMap<String, Object>> list = null;
        Session s = d.openSession();
        Transaction t = s.beginTransaction();
        try {
            begin(d, t);
            d.plusSelect();
            list = criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getExecutableCriteria(s).setMaxResults(1).setFirstResult(0).setFetchSize(1).list();
            commit(s, t);
            return instanceSingleResultTransformer(result, list);
        } catch (Exception e) {
            t.rollback();
            throw e;
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T openTransactionSingleResultTransformer(IDataBase d, Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<HashMap<String, Object>> list = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                list = criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getExecutableCriteria(s).setMaxResults(1).setFirstResult(0).setFetchSize(1).list();
                t.commit();
            } catch (Exception e) {
                t.rollback();
                d.getController().error(d, new Object[]{result, criteria}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return instanceSingleResultTransformer(result, list);
    }

    public static HashMap<String, Object> openSingleResultMap(IDataBase d, DetachedCriteria criteria) {
        List<HashMap<String, Object>> list = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            list = criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getExecutableCriteria(s).setMaxResults(1).setFirstResult(0).setFetchSize(1).list();
        } finally {
            selectClose(d, s);
        }
        if (list == null || list.size() == 0) return null;
        return HibernateUtil.deproxy(list.get(0));
    }

    public static HashMap<String, Object> openTransactionSingleResultMap(IDataBase d, DetachedCriteria criteria) {
        List<HashMap<String, Object>> list = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                list = criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getExecutableCriteria(s).setMaxResults(1).setFirstResult(0).setFetchSize(1).list();
                t.commit();
            } catch (Exception e) {
                t.rollback();
                d.getController().error(d, new Object[]{criteria}, e);
            }
        } finally {
            selectClose(d, s);
        }
        if (list == null || list.size() == 0) return null;
        return HibernateUtil.deproxy(list.get(0));
    }

    public static <T> T currentSingleResultTransformer(Session s, Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<HashMap<String, Object>> list = criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getExecutableCriteria(s).setMaxResults(1).setFirstResult(0).setFetchSize(1).list();
        return instanceSingleResultTransformer(result, list);
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity, String name, Object value) {
        boolean is;
        Session s = d.openSession();
        Transaction t = s.beginTransaction();
        try {
            begin(d, t);
            d.plusSelect();
            DetachedCriteria criteria = DetachedCriteria.forClass(entity);
            //criteria.add(Restrictions.eq(name, value));
            setValue(criteria, name, value);
            is = currentExists(s, criteria);
            commit(s, t);
        } catch (Exception e) {
            t.rollback();
            throw e;
        } finally {
            selectClose(d, s);
        }
        return is;
    }

    public static <T> List<T> currentSelect(Session s, CriteriaQuery<T> criteriaQuery) {
        return s.createQuery(criteriaQuery).getResultList();
    }

    public static <T> List<T> openSelect(IDataBase d, CriteriaQuery<T> criteriaQuery, int start, int index) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return s.createQuery(criteriaQuery).setFirstResult(start).setMaxResults(index).getResultList();
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> List<T> currentSelect(Session currentSession, CriteriaQuery<T> criteriaQuery, int start, int index) {
        return currentSession.createQuery(criteriaQuery).setFirstResult(start).setMaxResults(index).getResultList();
    }

    public static void openInsert(IDataBase d, Object entity) {
        Session s = d.openSession();
        try {
            d.plusInsert();
            Transaction t = s.beginTransaction();
            try {
                s.save(entity);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                d.getController().error(d, new Object[]{entity}, e);
            }
        } finally {
            insertClose(d, s);
        }
    }

    public static void insertClose(IDataBase d, Session s) {
        d.reduceInsert();
        if (s != null && s.isOpen()) s.close();
    }

    public static <T> List<T> openSelect(IDataBase d, CriteriaQuery<T> criteriaQuery) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return s.createQuery(criteriaQuery).getResultList();
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T openSingleResult(IDataBase d, CriteriaQuery<T> criteriaQuery) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return s.createQuery(criteriaQuery).getSingleResult();
        } finally {
            selectClose(d, s);
        }
    }

    public static void openUpdate(IDataBase d, Object entity) throws Exception {
        Session s = d.openSession();
        try {
            d.plusUpdate();
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                s.saveOrUpdate(entity);
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity}, e);
            }
        } finally {
            updateClose(d, s);
        }
    }

    public static void begin(IDataBase d, Transaction t) {
        t.setTimeout(d.getTransactionTimeout());
        //判断不为SQLite数据库时开始事务，SQLite数据库自动启动事务
        if (d.getDialect().indexOf(DataConstant.SQLite) == -1) {
            t.begin();
        }
    }

    public static void commit(Session s, Transaction t) {
        s.flush();
        t.commit();
    }

    public static <T> int openUpdate(IDataBase d, CriteriaUpdate<T> criteriaUpdate) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusUpdate();
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(criteriaUpdate).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteriaUpdate}, e);
            }
        } finally {
            updateClose(d, s);
        }
        return result;
    }

    public static <T> int openUpdate(IDataBase d, Class<T> entity, HashMap<String, Object> update) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusUpdate();
            String id = getIdName(entity);
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
            Root<T> root = query.from(entity);
            for (Map.Entry<String, Object> entry : update.entrySet()) {
                if (entry.getKey().equals(id)) {
                    query.where(builder.equal(root.get(entry.getKey()), id));
                } else {
                    query.set(root.get(entry.getKey()), entry.getValue());
                }
            }
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(query).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, update}, e);
            }
        } finally {
            updateClose(d, s);
        }
        return result;
    }

    public static boolean openTransactionExists(IDataBase d, DetachedCriteria criteria) throws Exception {
        boolean is = false;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                is = currentExists(s, criteria);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteria}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return is;
    }

    public static long currentCount(IDataBase d, DetachedCriteria criteria) {
        try {
            d.plusSelect();
            return currentCount(d.getCurrentSession(), criteria);
        } finally {
            d.reduceSelect();
        }
    }

    public static boolean currentExists(IDataBase d, DetachedCriteria criteria) {
        try {
            d.plusSelect();
            return currentExists(d.getCurrentSession(), criteria);
        } finally {
            d.reduceSelect();
        }
    }

    public static long openTransactionCount(IDataBase d, DetachedCriteria criteria) throws Exception {
        long c = 0;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                c = currentCount(s, criteria);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteria}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return c;
    }

    public static Object currentUniqueResult(IDataBase d, DetachedCriteria criteria) {
        try {
            d.plusSelect();
            return currentUniqueResult(d.getCurrentSession(), criteria);
        } finally {
            d.reduceSelect();
        }
    }

    public static Object openTransactionUniqueResult(IDataBase d, DetachedCriteria criteria) throws Exception {
        Object o = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                o = currentUniqueResult(s, criteria);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteria}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return o;
    }

    public static <T> int currentDeleteById(IDataBase d, Class<T> entity, Object id) throws Exception {
        int c = 0;
        Session s = d.getCurrentSession();
        try {
            d.plusDelete();
            Transaction t = s.beginTransaction();
            try {
                c = currentDeleteById(s, entity, id);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, id}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    public static <T> int currentUpdate(IDataBase d, CriteriaUpdate<T> criteriaUpdate) {
        try {
            d.plusUpdate();
            return d.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
        } finally {
            d.reduceUpdate();
        }
    }

    public static void currentUpdate(IDataBase d, Object entity) {
        try {
            d.plusUpdate();
            d.getCurrentSession().saveOrUpdate(entity);
        } finally {
            d.reduceUpdate();
        }
    }

    public static void currentDeleteById(IDataBase d, String entityName, Object id) {
        try {
            d.plusDelete();
            d.getCurrentSession().delete(entityName, id);
        } finally {
            d.reduceDelete();
        }
    }

    public static void currentDelete(IDataBase d, Object entity) {
        try {
            d.plusDelete();
            d.getCurrentSession().delete(entity);
        } finally {
            d.reduceDelete();
        }
    }

    public static <T> int openDelete(IDataBase d, CriteriaDelete<T> criteria) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(criteria).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteria}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return result;
    }

    public static void deleteClose(IDataBase d, Session s) {
        d.reduceDelete();
        if (s != null && s.isOpen()) s.close();
    }

    public static <T> int currentDelete(IDataBase d, CriteriaDelete<T> criteria) {
        try {
            d.plusDelete();
            return d.getCurrentSession().createQuery(criteria).executeUpdate();
        } finally {
            d.reduceDelete();
        }
    }

    public static <T> List<T> currentSelect(IDataBase d, DetachedCriteria criteria) {
        try {
            d.plusSelect();
            return criteria.getExecutableCriteria(d.getCurrentSession()).list();
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> List<T> currentSelect(IDataBase d, DetachedCriteria criteria, int start, int index) {
        try {
            d.plusSelect();
            return criteria.getExecutableCriteria(d.getCurrentSession()).setFirstResult(start).setMaxResults(index).list();
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> List<T> openTransactionSelect(IDataBase d, DetachedCriteria criteria) throws Exception {
        List<T> list = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                list = criteria.getExecutableCriteria(s).list();
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteria}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return list;
    }

    public static <T> List<T> openTransactionSelect(IDataBase d, DetachedCriteria criteria, int start, int index) throws Exception {
        List<T> list = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                list = criteria.getExecutableCriteria(s).setFirstResult(start).setMaxResults(index).list();
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{criteria, start, index}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return list;
    }

    public static <T> T currentSingleResult(IDataBase d, Class<T> entity, Object id) {
        try {
            d.plusSelect();
            return currentSingleResult(d.getCurrentSession(), entity, id);
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> T currentGet(Session currentSession, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        return HibernateUtil.deproxy(get(currentSession, criteria));
    }

    public static <T> T openSingleResult(IDataBase d, Class<T> entity, Object id) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentSingleResult(s, entity, id);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T openTransactionSingleResult(IDataBase d, Class<T> entity, Object id) throws Exception {
        T r = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                r = currentSingleResult(s, entity, id);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, id}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return r;
    }

    public static <T> boolean currentExists(IDataBase d, Class<T> entity, Object id) {
        try {
            d.plusSelect();
            return currentExists(d.getCurrentSession(), entity, id);
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> boolean openTransactionExists(IDataBase d, Class<T> entity) throws Exception {
        boolean is = false;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                is = currentExists(s, entity);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return is;
    }

    public static <T> boolean currentExists(IDataBase d, Class<T> entity) {
        try {
            d.plusSelect();
            return currentExists(d.getCurrentSession(), entity);
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> T openTransactionSingleResult(IDataBase d, Class<T> entity) throws Exception {
        T r = null;
        Session s = d.openSession();
        try {
            d.plusSelect();
            Transaction t = s.beginTransaction();
            try {
                r = currentSingleResult(s, entity);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity}, e);
            }
        } finally {
            selectClose(d, s);
        }
        return r;
    }

    public static <T> T currentSingleResult(IDataBase d, Class<T> entity) {
        try {
            d.plusSelect();
            return currentSingleResult(d.getCurrentSession(), entity);
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity, String[] names, Object[] values, boolean isTransaction) {
        boolean is;
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            //criteria.add(Restrictions.eq(name, values[i]));
            setValue(criteria, name, values[i]);
            i++;
        }
        Session s = d.openSession();
        Transaction t = null;
        if (isTransaction) {
            t = s.beginTransaction();
        }
        try {
            if (isTransaction) {
                begin(d, t);
            }
            d.plusSelect();
            is = currentExists(s, criteria);
            if (isTransaction) {
                commit(s, t);
            }
        } catch (Exception e) {
            if (isTransaction) {
                t.rollback();
            }
            throw e;
        } finally {
            selectClose(d, s);
        }
        return is;
    }

    public static <T> T openById(IDataBase d, Class<T> entity) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentGet(s, entity);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> int openUpdateById(IDataBase d, Class<T> entity, Object id, String name, Object value) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusUpdate();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
            Root<T> root = query.from(entity);
            query.where(builder.equal(root.get(getIdName(entity)), id));
            query.set(root.get(name), value);
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(query).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, id, name, value}, e);
            }
        } finally {
            updateClose(d, s);
        }
        return result;
    }

    public static void updateClose(IDataBase d, Session s) {
        d.reduceUpdate();
        if (s != null && s.isOpen()) s.close();
    }

    public static <T> boolean openExistsByNotId(IDataBase d, Class<T> entity, Object id, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
        //criteria.add(Restrictions.eq(name, value));
        setValue(criteria, name, value);
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static void selectClose(IDataBase d, Session s) {
        d.reduceSelect();
        if (s != null && s.isOpen()) s.close();
    }

    public static <T> int currentDelete(IDataBase currentDataBase, Session currentSession, CriteriaDelete<T> criteria) {
        try {
            currentDataBase.plusDelete();
            return currentSession.createQuery(criteria).executeUpdate();
        } finally {
            currentDataBase.reduceDelete();
        }
    }

    public static void currentDelete(IDataBase currentDataBase, Session currentSession, Object entity) {
        try {
            currentDataBase.plusDelete();
            currentSession.delete(entity);
        } finally {
            currentDataBase.reduceDelete();
        }
    }

    public static <T> int openDelete(IDataBase d, Class<T> entity, List<Object> arrays) throws Exception {
        int c = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            //执行删除操作。
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                c = currentDelete(s, entity, arrays);
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, arrays}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity, Object id, String name, String value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        //criteria.add(Restrictions.eq(name, value));
        setValue(criteria, name, value);
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> boolean openExistsByNotId(IDataBase d, Class<T> entity, String id, HashMap<String, Object> map) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            DetachedCriteria criteria = DetachedCriteria.forClass(entity);
            criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
            for (Map.Entry<String, Object> entry : map.entrySet())
                //criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
                setValue(criteria, entry.getKey(), entry.getValue());
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> boolean openExists(IDataBase d, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> List<T> openSelect(IDataBase d, Class<T> entity) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        Session s = d.openSession();
        try {
            d.plusSelect();
//            CriteriaBuilder builder = s.getCriteriaBuilder();
//            CriteriaQuery<T> query = builder.createQuery(entity);
//            Root<T> root = query.from(entity);
            return currentSelect(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> List<T> currentSelect(IDataBase d, Class<T> entity) {
        try {
            d.plusSelect();
            return currentSelect(d.openSession(), entity);
        } finally {
            d.reduceSelect();
        }
    }

    public static <T> int currentDelete(IDataBase d, Class<T> entity) {
        try {
            d.plusDelete();
            return currentDelete(d.getCurrentSession(), entity);
        } finally {
            d.reduceDelete();
        }
    }

    public static <T> int openUpdateById(IDataBase d, Class<T> entity, Object id, String[] names, Object[] values) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusUpdate();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
            Root<T> root = query.from(entity);
            int i = 0;
            for (String name : names) {
                if (name.equals(id)) {
                    query.where(builder.equal(root.get(name), id));
                } else {
                    query.set(root.get(name), values[i]);
                }
                i++;
            }
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(query).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, id, names, values}, e);
            }
        } finally {
            updateClose(d, s);
        }
        return result;
    }

    public static <T> int openUpdate(IDataBase d, Class<T> entity, String[] names, Object[] values) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusUpdate();
            String id = getIdName(entity);
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
            Root<T> root = query.from(entity);
            int i = 0;
            for (String name : names) {
                if (name.equals(id)) {
                    query.where(builder.equal(root.get(name), id));
                } else {
                    query.set(root.get(name), values[i]);
                }
                i++;
            }
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(query).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, names, values}, e);
            }
        } finally {
            updateClose(d, s);
        }
        return result;
    }

    public static <T> int currentUpdate(IDataBase d, Session s, CriteriaUpdate<T> criteriaUpdate) {
        try {
            d.plusUpdate();
            return s.createQuery(criteriaUpdate).executeUpdate();
        } finally {
            d.reduceUpdate();
        }
    }

    public static <T> CriteriaDelete<T> createCriteriaDelete(Session s, Class<T> entity, String name, Object id) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(entity);
        Root<T> root = query.from(entity);
        query.where(builder.equal(root.get(name), id));
        return query;
    }

    public static <T> int openUpdate(IDataBase d, Class<T> entity, String name, Object value) throws Exception {
        int result = 0;
        Session s = d.openSession();
        try {
            d.plusUpdate();
            String id = getIdName(entity);
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
            Root<T> root = query.from(entity);
            query.set(root.get(name), value);
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                result = s.createQuery(query).executeUpdate();
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, id, name, value}, e);
            }
        } finally {
            updateClose(d, s);
        }
        return result;
    }

    public static <T> boolean currentExists(IDataSession s, Class<T> entity, String name, Object value) {
        return currentExists(s.getSession(), entity, name, value);
    }

    public static <T> CriteriaUpdate<T> createCriteriaUpdate(Session s, Class<T> entity, Object id, HashMap<String, Object> update) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        query.where(builder.equal(root.get(getIdName(entity)), id));
        for (Map.Entry<String, Object> entry : update.entrySet()) {
            query.set(root.get(entry.getKey()), entry.getValue());
        }
        return query;
    }

    public static long currentCount(Session s, Class<?> entity) {
        return currentCount(s, DetachedCriteria.forClass(entity));
    }

    public static <R> R openUniqueResult(IDataBase d, Class<?> entity, String name, String[] names, Object[] values) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentUniqueResult(s, entity, name, names, values);
        } finally {
            selectClose(d, s);
        }
    }

    private static <R> R currentUniqueResult(Session s, Class<?> entity, String name, String[] names, Object[] values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String n : names) {
            criteria.add(Restrictions.eq(n, values[0]));
            i++;
        }
        criteria.setProjection(Projections.property(name));
        return currentUniqueResult(s, criteria);
    }

    public static <T> List<T> openSelect(IDataBase d, Class<T> entity, int start, int index) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return DetachedCriteria.forClass(entity).getExecutableCriteria(s).setFirstResult(start).setMaxResults(index).list();
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> int openDelete(IDataBase d, Class<T> entity, String[] names, Object[] values) throws Exception {
        int c = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            //执行删除操作。
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                c = currentDelete(s, entity, names, values);
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, names, values}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    public static <T> T openSingleResult(IDataBase d, Class<T> entity, String[] names, Object[] values) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentSingleResult(s, entity, names, values);
        } finally {
            selectClose(d, s);
        }
    }

    private static <T> T currentSingleResult(Session s, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            //criteria.add(Restrictions.eq(name, values[i]));
            setValue(criteria, name, values[i]);
            i++;
        }
        return singleResult(criteria.getExecutableCriteria(s));
    }

//    public static <T> boolean openExistsByNotId(IDataBase d, Class<T> entity, Object id, String name, Object value) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
//        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
//        criteria.add(Restrictions.eq(name, value));
//        Session s = d.openSession();
//        try {
//            d.plusSelect();
//            return currentExists(s, criteria);
//        } finally {
//            selectClose(d, s);
//        }
//    }

    public static <T> boolean currentExistsById(Session s, Class<T> entity, Object id, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        //criteria.add(Restrictions.eq(name, value));
        setValue(criteria, name, value);
        return currentExists(s, criteria);
    }

    public static <T> int currentUpdateById(Session s, Class<T> entity, Object id, String[] names, Object[] values) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        int i = 0;
        query.where(builder.equal(root.get(getIdName(entity)), id));
        for (String name : names) {
            query.set(root.get(name), values[i]);
            i++;
        }
        return s.createQuery(query).executeUpdate();
    }

    public static <T> boolean currentExistsById(Session s, Class<T> entity, Object id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        return currentExists(s, criteria);
    }

    public static <T> int currentUpdateById(Session s, Class<T> entity, Object id, String name, Object value) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        query.where(builder.equal(root.get(getIdName(entity)), id));
        query.set(root.get(name), value);
        return s.createQuery(query).executeUpdate();
    }

    public static <T> boolean openExistsById(IDataBase d, Class<T> entity, Object id, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.eq(getIdName(entity), id));
        //criteria.add(Restrictions.eq(name, value));
        setValue(criteria, name, value);
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentExists(s, criteria);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T, I> int currentDeleteByIds(Session s, Class<T> entity, List<I> list) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(entity);
        Root<T> root = query.from(entity);
        query.where(builder.in(root.get(getIdName(entity)).in(list)));
        return s.createQuery(query).executeUpdate();
    }

    public static <T> T openSingleResult(IDataBase d, Class<T> entity, String name, Object value) {
        Session s = d.openSession();
        try {
            d.plusSelect();
            return currentSingleResult(s, entity, name, value);
        } finally {
            selectClose(d, s);
        }
    }

    public static <T> T currentSingleResult(Session s, Class<T> entity, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        //criteria.add(Restrictions.eq(name, value));
        setValue(criteria, name, value);
        return singleResult(criteria.getExecutableCriteria(s));
    }

    public static <T> boolean currentExistsByNotId(Session s, Class<T> entity, Object id) throws HibernateException {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
        return isExists(criteria.getExecutableCriteria(s));
    }

    /**
     * Sqlite 分页函数
     *
     * @param d
     * @param criteriaCount 分页统计
     * @param criteria
     * @param start
     * @param size
     * @param <T>
     * @return
     */
    public static <T> DataPage<T> openSqlitePage(IDataBase d, DetachedCriteria criteriaCount, DetachedCriteria criteria, int start, int size) {
        //获取统计。
        long c = openCount(d, criteriaCount);
        //计算分页
        long pages = c / size;
        //计算余数
        if (c % size != 0) {
            pages++;
        }
        //if (pages > 0) pages--;
        int pageBegin = (start * size) - size;
        if (pageBegin == 0) pageBegin++;
        int firstResult = (start - 1) * size;
        if (firstResult < 0) firstResult = size;
        if (firstResult > size) {
            return new DataPage<T>(c, pages, pageBegin, openSelect(d, criteria, size, firstResult));
        } else {
            return new DataPage<T>(c, pages, pageBegin, openSelect(d, criteria, firstResult, size));
        }
    }

    public static <T> int openUpdateByNotId(IDataBase d, Class<T> entity, Object id, String name, Object value) throws Exception {
        int n = 0;
        Session s = d.openSession();
        Transaction t = s.beginTransaction();
        try {
            begin(d, t);
            d.plusSelect();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
            Root<T> root = query.from(entity);
            query.where(builder.notEqual(root.get(getIdName(entity)), id));
            query.set(root.get(name), value);
            n = s.createQuery(query).executeUpdate();
            commit(s, t);
        } catch (Exception e) {
            t.rollback();
            ((IDataBaseError) d).error(new Object[]{entity, id, name, value}, e);
        } finally {
            selectClose(d, s);
        }
        return n;
    }

    public static <T> int currentUpdateByNotId(Session s, Class<T> entity, Object id, String name, Object value) {
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaUpdate<T> query = builder.createCriteriaUpdate(entity);
        Root<T> root = query.from(entity);
        query.where(builder.notEqual(root.get(getIdName(entity)), id));
        query.set(root.get(name), value);
        return s.createQuery(query).executeUpdate();
    }

    public static <T, I> int openDeleteByIds(IDataBase d, Class<T> entity, List<I> list) throws Exception {
        int c = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            //执行删除操作。
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                c = currentDeleteByIds(s, entity, list);
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entity, list}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    public static <T> boolean openExistsByNotId(IDataBase d, Class<T> entity, Object id, String[] names, Object[] values) {
        return openExistsByNotId(d, entity, id, names, values, false);
    }

    public static <T> boolean openExistsByNotId(IDataBase d, Class<T> entity, Object id, String[] names, Object[] values, boolean isTransaction) {
        boolean is;
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        criteria.add(Restrictions.not(Restrictions.eq(getIdName(entity), id)));
        int i = 0;
        for (String name : names) {
            criteria.add(Restrictions.eq(name, values[i]));
            i++;
        }
        Session s = d.openSession();
        Transaction t = null;
        if (isTransaction) {
            s.beginTransaction();
        }
        try {
            if (isTransaction) {
                begin(d, t);
            }
            d.plusSelect();
            is = currentExists(s, criteria);
            if (isTransaction) {
                commit(s, t);
            }
        } catch (Exception e) {
            if (isTransaction) t.rollback();
            throw e;
        } finally {
            selectClose(d, s);
        }
        return is;
    }

    /**
     * 删除主键列表
     *
     * @param d       数据库
     * @param entitys 实体列表
     * @param list    主键Id列表
     * @param <I>
     * @return
     * @throws Exception
     */
    public static <I> int openDeleteByIds(IDataBase d, Class<?>[] entitys, List<I> list) throws Exception {
        int c = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                for (Class<?> entity : entitys) {
                    c += currentDeleteByIds(s, entity, list);
                }
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entitys, list}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    /**
     * 删除主键
     *
     * @param d       数据库
     * @param entitys 实体列表
     * @param id      主键Id
     * @return
     * @throws Exception
     */
    public static int openDeleteById(IDataBase d, Class<?>[] entitys, Object id) throws Exception {
        int c = 0;
        Session s = d.openSession();
        try {
            d.plusDelete();
            Transaction t = s.beginTransaction();
            try {
                begin(d, t);
                for (Class<?> entity : entitys) {
                    c += currentDeleteById(s, entity, id);
                }
                commit(s, t);
            } catch (Exception e) {
                t.rollback();
                ((IDataBaseError) d).error(new Object[]{entitys, id}, e);
            }
        } finally {
            deleteClose(d, s);
        }
        return c;
    }

    public static <T> List<T> openSelect(IDataBase d, Class<T> entity, String name, Object value, boolean isTransaction) throws Exception {
        List<T> list = null;
        Session s = d.openSession();
        Transaction t = null;
        try {
            if (isTransaction) {
                t = s.beginTransaction();
                begin(d, t);
            }
            d.plusSelect();

            list = currentSelect(s, entity, name, value);
            if (isTransaction) {
                commit(s, t);
            }
        } catch (Exception e) {
            if (isTransaction) {
                t.rollback();
            }
            ((IDataBaseError) d).error(new Object[]{entity, name, value}, e);
        } finally {
            selectClose(d, s);
        }
        return list;
    }

    public static <T> long currentCount(Session s, Class<T> entity, String[] names, Object[] values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        int i = 0;
        for (String name : names) {
            setValue(criteria, name, values[i]);
            i++;
        }
        return currentCount(s, criteria);
    }

    public static <T> long currentCount(Session s, Class<T> entity, String name, Object value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entity);
        setValue(criteria, name, value);
        return currentCount(s, criteria);
    }
}