package ghost.framework.data;
import ghost.framework.data.core.*;
import ghost.framework.data.core.util.DataUtil;
import ghost.framework.module.reflect.IGetObjectId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.exception.DataException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * 数据库。
 */
public class DataBase extends DataBaseProperties implements IDataBase, IDataBaseError, IGetObjectId {
    /**
     * 初始化日记。
     */
    private Log logger = LogFactory.getLog(DataBase.class);

    @Override
    public void close() {
        this.hibernateModule.getSessionFactory().close();
        this.hibernateModule = null;
    }

    /**
     * 删除动作时间。
     */
    private volatile Date deleteActionTime = new Date();

    /**
     * 清理数据库全部外键关联。
     */
//    public void cleanForeignKeyAssociations() {
//        Session session = this.openSession();
//        try {
//            Transaction transaction = session.beginTransaction();
//            try {
//                //清理mysql数据库全部外键关联。
//                if (this.getDriver().equals("com.mysql.jdbc.Driver")) {
//                    NativeQuery query = session.createNativeQuery("select INFORMATION_SCHEMA.KEY_COLUMN_USAGE.CONSTRAINT_NAME,information_schema.KEY_COLUMN_USAGE.TABLE_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE information_schema.KEY_COLUMN_USAGE.TABLE_SCHEMA = '" + this.getDataBaseName() + "'");
//                    List<Object[]> list = query.list();
//                    for (Object[] fk : list) {
//                        String str = (String) fk[0];
//                        if (str.startsWith("FK")) {
//                            session.createNativeQuery("ALTER TABLE `" + (String) fk[1] + "` DROP FOREIGN KEY `" + (String) fk[0] + "`;").executeUpdate();
//                        }
//                    }
//                }
//                transaction.commit();
//            } catch (Exception e) {
//                transaction.rollback();
//                throw e;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            this.getLog().error(ExceptionUtil.outStackTrace(e));
//        } finally {
//            if (session != null && session.isOpen()) session.close();
//        }
//    }

    /**
     * 数据库是否关闭。
     */
    private boolean close;

    /**
     * 获取数据库是否关闭。
     *
     * @return
     */
    public boolean isClose() {
        return close;
    }

    /**
     * 设置数据库是否关闭。
     *
     * @param close
     */
    public void setClose(boolean close) {
        this.close = close;
    }

    /**
     * 数据库最近一次更新数据时间。
     */
    private volatile Date updateActionTime = new Date();

    /**
     * 获取数据库最近一次更新数据时间。
     *
     * @return
     */
    public Date getUpdateActionTime() {
        return updateActionTime;
    }

    /**
     * 数据库最近一次数据插入时间。
     */
    private volatile Date insertActionTime = new Date();

    /**
     * 获取数据库最近一次数据插入时间。
     *
     * @param insertActionTime
     */
    public void setInsertActionTime(Date insertActionTime) {
        this.insertActionTime = insertActionTime;
    }

    public Date getInsertActionTime() {
        return insertActionTime;
    }

    /**
     * 数据库最近一次读取数据时间。
     */
    private volatile Date selectActionTime = new Date();

    /**
     * 获取数据库最近一次读取数据时间。
     *
     * @return
     */
    public Date getSelectActionTime() {
        return this.selectActionTime;
    }

    /**
     * 更新选择动作时间。
     */
    private synchronized void updatesSlectActionTime() {
        this.selectActionTime = new Date();
    }

    /**
     * 获取数据库最近一次动作时间。
     *
     * @return
     */
    public Date getActionTime() {
        return actionTime;
    }

    /**
     * 数据库最近一次动作时间。
     */
    private volatile Date actionTime = new Date();
    /**
     * 计数器。
     */
    private volatile int counter = 0;

    /**
     * 获取计数器数量。
     *
     * @return
     */
    public synchronized int getCounter() {
        return counter;
    }

    /**
     * 计数器加。
     */
    public synchronized void plus() {
        this.counter++;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[" + this.getId() + "]累加计数器[" + this.counter + "]");
        }
    }

    /**
     * 减计数器。
     */
    public synchronized void reduce() {
        this.counter--;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[" + this.getId() + "]减计数器[" + this.counter + "]");
        }
        if (this.counter < 0) throw new ArithmeticException("数据库计数器小于0错误！");
    }

    /**
     * 数据库控制器。
     */
    private IDataBaseController controller = null;

    /**
     * 获取数据库所在控制器。
     *
     * @return
     */
    public IDataBaseController getController() {
        return this.controller;
    }

    /**
     * Hibernate数据库模块。
     * {@link DataHibernateModule}
     */
    private DataHibernateModule hibernateModule;

    /**
     * 错误处理。
     *
     * @param source
     * @param e
     */
    public void error(Object source, Exception e) {
        this.controller.error(this, source, e);
    }

    /**
     * 打开新的会话对象。
     *
     * @return
     */
    public Session openSession() {
        //没有锁住数据库打开会话对象。
        return this.hibernateModule.getSessionFactory().openSession();
    }

    /**
     * 打开当前线程会话对象。
     *
     * @return
     */
    public Session getCurrentSession() {
        //没有锁住数据库打开当前线程会话对象。
        return this.hibernateModule.getSessionFactory().getCurrentSession();
    }

    /**
     * 获取会话工厂。
     *
     * @return
     */
    @Override
    public SessionFactory getSessionFactory() {
        return this.hibernateModule.getSessionFactory();
    }

    /**
     * 初始化数据库。
     *
     * @param controller
     * @param properties
     * @throws DataException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DataBase(
            DataBaseController controller,
            IDataBaseProperties properties
    ) throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        super(properties);
        this.controller = controller;
        //配置hibernate。
        try {
            this.hibernateModule = new DataHibernateModule(controller, this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataException(e.getMessage(), new SQLException(e));
        }
    }

    /**
     * @param entityList
     * @param properties
     * @throws DataException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DataBase(
            List<Class> entityList,
            IDataBaseProperties properties
    ) throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        super(properties);
        //配置hibernate。
        try {
            this.hibernateModule = new DataHibernateModule(entityList, this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataException(e.getMessage(), new SQLException(e));
        }
    }

    /**
     * 选择计数器。
     */
    private volatile int counterSelect = 0;

    /**
     * 获取选择计数器。
     *
     * @return
     */
    public int getCounterSelect() {
        return counterSelect;
    }

    /**
     * 累加选择计数器。
     */
    public synchronized void plusSelect() {
        this.updatesSlectActionTime();
        this.counterSelect++;
        this.plus();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]总选择计数器[" + this.counter + "]");
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]累加选择计数器[" + this.counterSelect + "]");
        }
    }

    /**
     * 减选择计数器。
     */
    public synchronized void reduceSelect() {
        this.counterSelect--;
        this.reduce();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]减选择计数器[" + this.counterSelect + "]");
        }
        if (this.counterSelect < 0) throw new ArithmeticException("数据库减选择计数器小于0错误！");
    }

    /**
     * 删除计数器。
     */
    private volatile int counterDelete = 0;

    /**
     * 获取删除计数器。
     *
     * @return
     */
    public int getCounterDelete() {
        return counterDelete;
    }

    /**
     * 累加删除计数器。
     */
    public synchronized void plusDelete() {
        this.updatesDeleteActionTime();
        this.counterDelete++;
        this.plus();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]累加删除计数器[" + this.counterDelete + "]");
        }
    }

    /**
     * 更新删除动作时间。
     */
    private synchronized void updatesDeleteActionTime() {
        this.deleteActionTime = new Date();
    }

    /**
     * 减删除计数器。
     */
    public synchronized void reduceDelete() {
        this.counterDelete--;
        this.reduce();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]减删除计数器[" + this.counterDelete + "]");
        }
        if (this.counterDelete < 0) throw new ArithmeticException("数据库减删除计数器小于0错误！");
    }

    /**
     * 插入计数器。
     */
    private volatile int counterInsert = 0;

    /**
     * 累加插入计数器。
     */
    public synchronized void plusInsert() {
        this.insertActionTime = new Date();
        this.counterInsert++;
        this.plus();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]累加插入计数器[" + this.counterInsert + "]");
        }
    }

    /**
     * 减插入计数器。
     */
    public synchronized void reduceInsert() {
        this.counterInsert--;
        this.reduce();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]减插入计数器[" + this.counterInsert + "]");
        }
        if (this.counterInsert < 0) throw new ArithmeticException("数据库减插入计数器小于0错误！");
    }

    /**
     * 更新计数器。
     */
    private volatile Integer counterUpdate = 0;

    /**
     * 累加更新计数器。
     */
    public synchronized void plusUpdate() {
        this.counterUpdate++;
        this.plus();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]累加更新计数器[" + this.counterUpdate.toString() + "]");
        }
    }

    /**
     * 减更新计数器。
     */
    public synchronized void reduceUpdate() {
        this.counterUpdate--;
        this.reduce();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("数据库[Id:" + this.getId() + ",Name:" + this.getDataBaseName() + "]减更新计数器[" + this.counterUpdate.toString() + "]");
        }
        if (this.counterUpdate < 0) throw new ArithmeticException("数据库减更新计数器小于0错误！");
    }

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T currentSingleResult(Class<T> entity) {
        return DataUtil.currentSingleResult(this, entity);
    }

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T openTransactionSingleResult(Class<T> entity) throws Exception {
        return DataUtil.openTransactionSingleResult(this, entity);
    }

    /**
     * 获取实体一行数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T openSingleResult(Class<T> entity) {
        return DataUtil.openSingleResult(this, entity);
    }

    /**
     * 验证实体是否已经存在数据。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> boolean currentExists(Class<T> entity) {
        return DataUtil.currentExists(this, entity);
    }

    /**
     * 验证实体是否存在数据行。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> boolean openTransactionExists(Class<T> entity) throws Exception {
        return DataUtil.openTransactionExists(this, entity);
    }

    /**
     * 验证实体是否存在数据行。
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> boolean openExists(Class<T> entity) {
        return DataUtil.openExists(this, entity);
    }

    @Override
    public <T> boolean openExistsByNotId(Class<T> entity, Object id, String name, Object value) {
        return DataUtil.openExistsByNotId(this, entity, id, name, value);
    }

    @Override
    public <T> boolean openExistsById(Class<T> entity, Object id, String name, Object value) {
        return DataUtil.openExistsById(this, entity, id, name, value);
    }

    @Override
    public <T> boolean currentExistsById(Session session, Class<T> entity, Object id, String name, Object value) {
        return DataUtil.currentExistsById(session, entity, id, name, value);
    }

    @Override
    public <T> boolean currentExistsByNotId(Session session, Class<T> entity, Object id, String name, Object value) {
        return DataUtil.currentExistsByNotId(session, entity, id, name, value);
    }

    @Override
    public <T> boolean currentExistsById(Session session, Class<T> entity, Object id) {
        return DataUtil.currentExistsById(session, entity, id);
    }

    @Override
    public <T> int currentUpdateById(Session session, Class<T> entity, Object id, String name, Object value) {
        return DataUtil.currentUpdateById(session, entity, id, name, value);
    }

    /**
     * 验证实体Id的键值是否存在。
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> boolean currentExists(Class<T> entity, Object id) {
        return DataUtil.currentExists(this, entity, id);
    }

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> T openTransactionSingleResult(Class<T> entity, Object id) throws Exception {
        return DataUtil.openTransactionSingleResult(this, entity, id);
    }

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> T openSingleResult(Class<T> entity, Object id) {
        return DataUtil.openSingleResult(this, entity, id);
    }

    @Override
    public <T> T openSingleResult(Class<T> entity, String[] names, Object[] values) {
        return DataUtil.openSingleResult(this, entity, names, values);
    }

    /**
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> T currentSingleResult(Class<T> entity, Object id) {
        return DataUtil.currentSingleResult(this, entity, id);
    }

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param size     选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> openTransactionSelect(DetachedCriteria criteria, int start, int size) throws Exception {
        return DataUtil.openTransactionSelect(this, criteria, start, size);
    }

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param size     选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> openSelect(DetachedCriteria criteria, int start, int size) {
        return DataUtil.openSelect(this, criteria, start, size);
    }

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> openTransactionSelect(DetachedCriteria criteria) throws Exception {
        return DataUtil.openTransactionSelect(this, criteria);
    }

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> openSelect(DetachedCriteria criteria) {
        return DataUtil.openSelect(this, criteria);
    }

    /**
     * 打开当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param index    选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> currentSelect(DetachedCriteria criteria, int start, int index) {
        return DataUtil.currentSelect(this, criteria, start, index);
    }

    /**
     * 打开当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> currentSelect(DetachedCriteria criteria) {
        return DataUtil.currentSelect(this, criteria);
    }

    /**
     * 删除。
     *
     * @param criteria
     * @param <T>
     * @return
     */
    public <T> int currentDelete(CriteriaDelete<T> criteria) {
        return DataUtil.currentDelete(this, criteria);
    }

    /**
     * @return
     */
    public CriteriaBuilder currentCriteriaBuilder() {
        return this.getCurrentSession().getCriteriaBuilder();
    }

    /**
     * 删除。
     *
     * @param criteria
     */
    public <T> int openDelete(CriteriaDelete<T> criteria) throws Exception {
        return DataUtil.openDelete(this, criteria);
    }

    /**
     * 删除实例。
     *
     * @param entity 实例。
     */
    public int openDelete(Object entity) throws Exception {
        return DataUtil.openDelete(this, entity);
    }

    /**
     * 删除实体。
     *
     * @param entity 实体。
     */
    public int openDelete(Class<?> entity) throws Exception{
        return DataUtil.openDelete(this, entity);
    }

    /**
     * 删除实体。
     *
     * @param entity 实体。
     */
    public void currentDelete(Object entity) {
        DataUtil.currentDelete(this, entity);
    }

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entityName 实体名称。
     * @param id         实体主键Id。
     */
    public void currentDeleteById(String entityName, Object id) {
        DataUtil.currentDeleteById(this, entityName, id);
    }

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entityName 实体名称。
     * @param id         实体主键Id。
     */
    public void openDeleteById(String entityName, Object id)throws Exception {
        DataUtil.openDeleteById(this, entityName, id);
    }

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entity 实体类型。
     * @param id     实体主键Id。
     * @param <T>    泛型实体。
     * @return 返回删除行数。
     */
    public <T> int currentDeleteById(Class<T> entity, Object id) throws Exception {
        return DataUtil.currentDeleteById(this, entity, id);
    }

    /**
     * 删除指定实体名称指定的Id。
     *
     * @param entity 实体类型。
     * @param id     实体主键Id。
     * @param <T>    泛型实体。
     * @return 返回删除行数。
     */
    public <T> int openDeleteById(Class<T> entity, Object id) {
        return DataUtil.openDeleteById(this, entity, id);
    }

    @Override
    public <T> int currentDeleteById(Session session, Class<T> entity, Object id) {
        return DataUtil.currentDeleteById(session, entity, id);
    }

    public <T> int openDelete(Class<T> entity, List<Object> arrays) throws Exception {
        return DataUtil.openDelete(this, entity, arrays);
    }

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public Object openTransactionUniqueResult(DetachedCriteria criteria) throws Exception {
        return DataUtil.openTransactionUniqueResult(this, criteria);
    }

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public Object openUniqueResult(DetachedCriteria criteria) {
        return DataUtil.openUniqueResult(this, criteria);
    }

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public Object currentUniqueResult(DetachedCriteria criteria) {
        return DataUtil.currentUniqueResult(this, criteria);
    }

    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public long openTransactionCount(DetachedCriteria criteria) throws Exception {
        return DataUtil.openTransactionCount(this, criteria);
    }

    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public long openCount(DetachedCriteria criteria) {
        return DataUtil.openCount(this, criteria);
    }

    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public long currentCount(DetachedCriteria criteria) {
        return DataUtil.currentCount(this, criteria);
    }

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    public boolean currentExists(DetachedCriteria criteria) {
        return DataUtil.currentExists(this, criteria);
    }

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    public boolean openTransactionExists(DetachedCriteria criteria) throws Exception {
        return DataUtil.openTransactionExists(this, criteria);
    }

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    public boolean openExists(DetachedCriteria criteria) {
        return DataUtil.openExists(this, criteria);
    }

    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    public void currentUpdate(Object entity) {
        DataUtil.currentUpdate(this, entity);
    }

    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    public <T> int currentUpdate(CriteriaUpdate<T> criteriaUpdate) {
        return DataUtil.currentUpdate(this, criteriaUpdate);
    }

    @Override
    public <T> int currentUpdateById(Session session, Class<T> entity, Object id, String[] names, Object[] values) {
        return DataUtil.currentUpdateById(session, entity, id, names, values);
    }

    public <T> int openUpdate(Class<T> entity, HashMap<String, Object> update) throws Exception {
        return DataUtil.openUpdate(this, entity, update);
    }

    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    public <T> int openUpdate(CriteriaUpdate<T> criteriaUpdate) throws Exception {
        return DataUtil.openUpdate(this, criteriaUpdate);
    }

    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    public void openUpdate(Object entity) throws Exception {
        DataUtil.openUpdate(this, entity);
    }

    /**
     * 打开新会话查询一行数据。
     *
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> T openSingleResult(CriteriaQuery<T> criteriaQuery) {
        return DataUtil.openSingleResult(this, criteriaQuery);
    }

    /**
     * 打开当前线程会话查询一行数据。
     *
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> T currentSingleResult(CriteriaQuery<T> criteriaQuery) {
        try {
            this.plusSelect();
            return this.getCurrentSession().createQuery(criteriaQuery).getSingleResult();
        } finally {
            this.reduceSelect();
        }
    }

    /**
     * 打开新会话查询数据。
     *
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> List<T> openSelect(CriteriaQuery<T> criteriaQuery) {
        return DataUtil.openSelect(this, criteriaQuery);
    }


    /**
     * 插入实体。
     *
     * @param entity 实体对象。,
     */
    public void currentInsert(Object entity) {
        try {
            this.plusInsert();
            this.getCurrentSession().save(entity);
        } finally {
            this.reduceInsert();
        }
    }

    /**
     * 插入实体。
     *
     * @param entity 实体对象。
     */
    public void openInsert(Object entity) {
        DataUtil.openInsert(this, entity);
    }

    @Override
    public int openUpdate(Class<?> entity, String name, Object value) throws Exception {
        return DataUtil.openUpdate(this, entity, name, value);
    }

    public <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery, int start, int size) {
        try {
            this.plusSelect();
            return DataUtil.currentSelect(this.getCurrentSession(), criteriaQuery, start, size);
        } finally {
            this.reduceSelect();
        }
    }

    public <T> List<T> openSelect(CriteriaQuery<T> criteriaQuery, int start, int size) {
        return DataUtil.openSelect(this, criteriaQuery, start, size);
    }

    public <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery) {
        try {
            this.plusSelect();
            return DataUtil.currentSelect(this.getCurrentSession(), criteriaQuery);
        } finally {
            this.reduceSelect();
        }
    }

    /**
     * 验证实体指定例名称与值是否存在。
     *
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean openExists(Class<T> entity, String name, Object value) {
        return DataUtil.openExists(this, entity, name, value);
    }

    @Override
    public <T> boolean openExists(Class<T> entity, String[] names, Object[] values) {
        return DataUtil.openExists(this, entity, names, values, false);
    }
    @Override
    public <T> boolean openExistsByNotId(Class<T> entity, Object id, String[] names, Object[] values, boolean isTransaction) {
        return false;
    }

    @Override
    public <T> boolean openExists(Class<T> entity, String[] names, Object[] values, boolean isTransaction) {
        return DataUtil.openExists(this, entity, names, values, isTransaction);
    }
    @Override
    public <T> boolean openExistsByNotId(Class<T> entity, Object id, String[] names, Object[] values) {
        return DataUtil.openExistsByNotId(this, entity, id, names, values);
    }

    @Override
    public void setRoot(boolean root) {
        this.root = root;
    }

    private boolean root;

    @Override
    public boolean isRoot() {
        return this.root;
    }

//    @Override
//    public <T> T openById(Class<T> entity) {
//        return DataUtil.openById(this, entity);
//    }

    @Override
    public <T> List<T> openSelect(Class<T> entity) {
        return DataUtil.openSelect(this, entity);
    }

    @Override
    public <T> int openDelete(Class<T> entity, String[] names, Object[] values) throws Exception {
        return DataUtil.openDelete(this, entity, names, values);
    }

    @Override
    public void reset() {

    }

    /**
     * 返回包装对象。
     *
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T currentSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        try {
            this.plusSelect();
            return DataUtil.currentSingleResultTransformer(this.getCurrentSession(), result, criteria);
        } finally {
            this.reduceSelect();
        }
    }

    @Override
    public Log getLogger() {
        return this.controller.getLogger();
    }

    /**
     * 返回包装对象。
     *
     * @param result
     * @param criteria
     * @param <T>
     * @return
     */
    public <T> T openSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws Exception {
        return DataUtil.openSingleResultTransformer(this, result, criteria);
    }

    /**
     * 返回包装对象。
     *
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T openTransactionSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return DataUtil.openTransactionSingleResultTransformer(this, result, criteria);
    }

    /**
     * @param criteria
     * @return
     */
    public HashMap<String, Object> openSingleResultMap(DetachedCriteria criteria) {
        return DataUtil.openSingleResultMap(this, criteria);
    }

    /**
     * @param criteria
     * @return
     */
    public HashMap<String, Object> openTransactionSingleResultMap(DetachedCriteria criteria) throws Exception {
        return DataUtil.openTransactionSingleResultMap(this, criteria);
    }

    public <T> int openUpdateById(Class<T> entity, Object id, String name, Object value) throws Exception {
        return DataUtil.openUpdateById(this, entity, id, name, value);
    }

    public <T> int openUpdateById(Class<T> entity, Object id, String[] names, Object[] values) throws Exception {
        return DataUtil.openUpdateById(this, entity, id, names, values);
    }

    @Override
    public long openCount(Class<?> entity) {
        return DataUtil.openCount(this, entity);
    }

    @Override
    public <T> List<T> openSelect(Class<T> entity, int index, int size) {
        return DataUtil.openSelect(this, entity, index, size);
    }

    public <T> int openUpdate(Class<T> entity, String[] names, Object[] values) throws Exception {
        return DataUtil.openUpdate(this, entity, names, values);
    }

    @Override
    public <T, I> int currentDeleteByIds(Session session, Class<T> entity, List<I> list) {
        return DataUtil.currentDeleteByIds(session, entity, list);
    }

    @Override
    public <T> DataPage<T> openSqlitePage(DetachedCriteria criteriaCount, DetachedCriteria criteria, int start, int size) {
        return DataUtil.openSqlitePage(this, criteriaCount, criteria, start, size);
    }

    @Override
    public <T> int openUpdateByNotId(Class<T> entity, Object id, String name, Object value) throws Exception{
        return DataUtil.openUpdateByNotId(this, entity, id, name, value);
    }

    @Override
    public <T, I> int openDeleteByIds(Class<T> entity, List<I> list) throws Exception{
       return DataUtil.openDeleteByIds(this, entity, list);
    }
    /**
     * 删除主键列表
     * @param entitys 实体列表
     * @param list 主键Id列表
     * @param <I>
     * @return
     * @throws Exception
     */
    @Override
    public <I> int openDeleteByIds(Class<?>[] entitys, List<I> list) throws Exception{
        return DataUtil.openDeleteByIds(this, entitys, list);
    }
    /**
     * 删除主键
     * @param entitys 实体列表
     * @param id 主键Id
     * @return
     * @throws Exception
     */
    @Override
    public int openDeleteById(Class<?>[] entitys, Object id) throws Exception{
        return DataUtil.openDeleteById(this, entitys, id);
    }

    @Override
    public <T> List<T> openSelect(Class<T> entity, String name, Object value) throws Exception{
        return DataUtil.openSelect(this, entity, name, value, false);
    }

    @Override
    public <T> List<T> openSelect(Class<T> entity, String name, Object value, boolean isTransaction) throws Exception{
        return DataUtil.openSelect(this, entity, name, value, isTransaction);
    }

    /**
     * 处理数据库错误
     *
     * @param objects
     * @param e
     * @throws Exception
     */
    @Override
    public void error(Object[] objects, Exception e) throws Exception {
        if (this.controller == null) {
            throw e;
        }
        this.controller.error(this, objects, e);
    }
}
