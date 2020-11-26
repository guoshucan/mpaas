package ghost.framework.data;
import ghost.framework.data.core.DataTransactionMode;
import ghost.framework.data.core.IDataBase;
import ghost.framework.data.core.IDataSession;
import ghost.framework.data.core.util.DataUtil;
import ghost.framework.thread.FourRunnable;
import ghost.framework.thread.ThreeRunnable;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.criteria.internal.CriteriaDeleteImpl;
import org.hibernate.query.criteria.internal.CriteriaUpdateImpl;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 会话对象。
 */
public final class DataSession implements IDataSession {
    private IDataBase dataBase;
    private Session session;
    private Transaction transaction;
    /**
     * 事务模式。
     */
    private DataTransactionMode mode = DataTransactionMode.parallel;

    /**
     * 获取事务模式。
     *
     * @return
     */
    public DataTransactionMode getMode() {
        return mode;
    }

    /**
     * 初始化会话对象。
     * 并按照指定事务模式创建会话对象。
     *
     * @param controller 数据库控制器。
     * @param mode       会话事务模式。
     */
    public DataSession(DataBaseController controller, DataTransactionMode mode) {
        this(controller);
        this.mode = mode;
    }

    /**
     * 初始化会话对象。
     * 以并行事务模式创建会话。
     *
     * @param controller 数据库控制器。
     */
    public DataSession(DataBaseController controller) {
        this.controller = controller;
        this.dataBase = controller.openEnableOrWriteDataBase();
        this.session = this.dataBase.openSession();
        this.transaction = this.session.beginTransaction();
    }

    public synchronized TransactionStatus getStatus() {
        return this.transaction.getStatus();
    }

    public synchronized int getTimeout() {
        return this.transaction.getTimeout();
    }

    public synchronized void setTimeout(int seconds) {
        this.transaction.setTimeout(seconds);
    }

    public synchronized boolean getRollbackOnly() {
        return this.transaction.getRollbackOnly();
    }

    /**
     * 获取会话对象。
     *
     * @return
     */
    public synchronized Session getSession() {
        return session;
    }

    @Override
    public synchronized int update(Class<?> entity, Object id, HashMap<String, Object> update) {
        return this.addUpdate(entity, id, update);
    }

    private <T> int addUpdate(Class<T> entity, Object id, HashMap<String, Object> update) {
        CriteriaUpdate<T> query = DataUtil.createCriteriaUpdate(this.session, entity, id, update);
        this.taskList.add(query);
        try {
            this.dataBase.plusUpdate();
            return this.session.createQuery(query).executeUpdate();
        } finally {
            this.dataBase.reduceUpdate();
        }
    }

    @Override
    public synchronized boolean exists(Class<?> entity, String name, Object value) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentExists(this.session, entity, name, value);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public synchronized boolean exists(Class<?> entity, String[] names, Object[] values) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentExists(this.session, entity, names, values);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public synchronized long count(Class<?> entity) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentCount(this.session, entity);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public synchronized Object uniqueResult(DetachedCriteria criteria) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentUniqueResult(this.session, criteria);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public synchronized boolean idNotExists(Class<?> entity, Object id, String[] names, Object[] values) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentExistsByNotId(this.session, entity, id, names, values);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public synchronized boolean idNotExists(Class<?> entity, Object id, String name, Object value) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentExistsByNotId(this.session, entity, id, name, value);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public <T> T get(Class<T> entity) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentGet(this.session, entity);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    @Override
    public synchronized Blob createBlob(byte[] bytes) {
        return Hibernate.getLobCreator(this.session).createBlob(bytes);
    }

    @Override
    public synchronized  <T> boolean exists(Class<T> entity) {
        try {
            this.dataBase.plusSelect();
            return DataUtil.currentExists(this.session, entity);
        } finally {
            this.dataBase.reduceSelect();
        }
    }

    /**
     * 获取会话数据库。
     *
     * @return
     */
    public IDataBase getDataBase() {
        return dataBase;
    }

    /**
     * 数据库控制器。
     */
    private DataBaseController controller;

    /**
     * 获取数据库控制器。
     *
     * @return
     */
    public DataBaseController getController() {
        return controller;
    }

    /**
     * 关闭连接。
     */
    public synchronized void close() throws SQLException {
        if (this.session != null && this.session.isOpen()) this.session.close();
    }

    /**
     * 回滚事务。
     */
    public synchronized void rollback() {
        this.transaction.rollback();
    }

    /**
     * 完成事务。
     *
     * @param mode 指定完成事务模式。
     * @throws InterruptedException
     */
    public synchronized void commit(DataTransactionMode mode) throws InterruptedException {
        this.mode = mode;
        this.commit();
    }
    /**
     * 完成事务。
     */
    public synchronized void commit() throws InterruptedException {
        this.transaction.commit();
        switch (this.mode) {
            case parallel:
                //并行模式。
                CountDownLatch threadSignal = null;
                ExecutorService executor = null;
                try {
                    this.controller.lock();
                    //获取排除会话自身数据库的所有可操作数据库数量。
                    List<IDataBase> dataBaseList = this.controller.getEnableOrWriteDataBase(this.dataBase.getId());
                    if (dataBaseList.size() == 0) return;
                    threadSignal = new CountDownLatch(dataBaseList.size());
                    executor = Executors.newFixedThreadPool(dataBaseList.size());
                    for (IDataBase task : dataBaseList) {
                        executor.execute(new FourRunnable<CountDownLatch, DataSession, IDataBase, List<Object>>(threadSignal, this, task, this.taskList) {
                            @Override
                            public void run() {
                                try {
                                    //遍历任务对象。
                                    for (Object t : this.getD()) {
                                        try {
                                            if (t.getClass() == CriteriaDeleteImpl.class) {
                                                //删除任务。
                                                this.getC().openDelete((CriteriaDelete) t);
                                            } else if (t.getClass() == CriteriaUpdateImpl.class) {
                                                //更新任务。
                                                this.getC().openUpdate((CriteriaUpdate) t);
                                            } else if (t.getClass() == DataSessionTaskUpdate.class) {
                                                //更新实体。
                                                this.getC().openUpdate(((DataSessionTaskUpdate) t).getItem());
                                            } else {
                                                //添加实体。
                                                this.getC().openInsert(((DataSessionTaskInsert) t).getItem());
                                            }
                                        } catch (Exception e) {
                                            this.getB().controller.error(this.getC(), this.getD(), e);
                                        }
                                    }
                                } finally {
                                    this.getA().countDown();
                                }
                            }
                        });
                    }
                } finally {
                    this.controller.unlock();
                }
                executor.shutdown();
                threadSignal.await();
                return;
//            case async:
//                //异步模式。
//                return;
            case synch:
                //同步模式。
                try {
                    this.controller.lock();
                    //获取排除会话自身数据库的所有可操作数据库数量。
                    for (IDataBase task : this.controller.getEnableOrWriteDataBase(this.dataBase.getId())) {
                        new Thread(new ThreeRunnable<DataSession, IDataBase, List<Object>>(this, task, this.taskList) {
                            @Override
                            public void run() {
                                //遍历任务对象。
                                for (Object t : this.getC()) {
                                    try {
                                        if (t.getClass() == CriteriaDelete.class) {
                                            //删除任务。
                                            this.getB().openDelete((CriteriaDelete) t);
                                        } else if (t.getClass() == CriteriaUpdate.class) {
                                            //更新任务。
                                            this.getB().openUpdate((CriteriaUpdate) t);
                                        } else if (t.getClass() == DataSessionTaskUpdate.class) {
                                            //更新实体。
                                            this.getB().openUpdate(((DataSessionTaskUpdate) t).getItem());
                                        } else {
                                            //添加实体。
                                            this.getB().openInsert(((DataSessionTaskInsert) t).getItem());
                                        }
                                    } catch (Exception e) {
                                        this.getA().controller.error(this.getB(), this.getC(), e);
                                    }
                                }
                            }
                        }).start();
                    }
                } finally {
                    this.controller.unlock();
                }
                return;
        }
    }

    /**
     * 插入表数据。
     *
     * @param entity
     */
    public synchronized void insert(Object entity) {
        try {
            this.dataBase.plusInsert();
            this.session.save(entity);
        } finally {
            this.dataBase.reduceInsert();
        }
        this.taskList.add(new DataSessionTaskInsert(entity));
    }

    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    public synchronized void update(Object entity) {
        try {
            this.dataBase.plusInsert();
            this.session.saveOrUpdate(entity);
        } finally {
            this.dataBase.reduceInsert();
        }
        this.taskList.add(new DataSessionTaskUpdate(entity));
    }

    /**
     * 删除表数据。
     *
     * @param entity 指定实体。
     * @param name   指定名称。
     * @param id     指定键值。
     * @param <T>
     * @return
     */
    public synchronized <T> int delete(Class<T> entity, String name, Object id) {
        return this.addDelete(entity, name, id);
    }

    public synchronized <T> int delete(Class<T>[] entitys, String name, Object id) {
        int count = 0;
        for (Class<T> entity : entitys) {
            count += this.addDelete(entity, name, id);
        }
        return count;
    }

    private synchronized <T> int addDelete(Class<T> entity, String name, Object id) {
        CriteriaDelete<T> query = DataUtil.createCriteriaDelete(this.session, entity, name, id);
        this.taskList.add(query);
        try {
            this.dataBase.plusDelete();
            return session.createQuery(query).executeUpdate();
        } finally {
            this.dataBase.reduceDelete();
        }
    }

    private synchronized <T> int addDelete(Class<T> entity, Object id) {
        CriteriaDelete<T> query = DataUtil.createCriteriaDelete(this.session, entity, id);
        this.taskList.add(query);
        try {
            this.dataBase.plusDelete();
            return session.createQuery(query).executeUpdate();
        } finally {
            this.dataBase.reduceDelete();
        }
    }

    /**
     * 删除表数据。
     *
     * @param entity 指定实体。
     * @param id     指定键值。
     * @param <T>
     * @return
     */
    public synchronized <T> int delete(Class<T> entity, Object id) {
        return this.addDelete(entity, id);
    }

    public synchronized <T> int delete(Class<T>[] entitys, Object id) {
        int count = 0;
        for (Class<T> entity : entitys) {
            count += this.addDelete(entity, id);
        }
        return count;
    }

    /**
     * 任务列表。
     */
    private List<Object> taskList = new ArrayList<>();
}