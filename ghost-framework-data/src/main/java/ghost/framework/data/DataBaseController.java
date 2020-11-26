package ghost.framework.data;
import ghost.framework.data.core.*;
import ghost.framework.data.core.util.DataUtil;
import ghost.framework.generic.Five;
import ghost.framework.generic.Two;
import ghost.framework.module.reflect.IGetObjectId;
import ghost.framework.thread.*;
import ghost.framework.util.ExceptionUtil;
import ghost.framework.util.UniqueKeyUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.criterion.DetachedCriteria;

import javax.persistence.SharedCacheMode;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 数据库控制器。
 */
public class DataBaseController implements IDataBaseController, AutoCloseable, IGetObjectId {
    /**
     * 初始化日记。
     */
    private Log logger = LogFactory.getLog(DataBaseController.class);
    @Override
    public void setLogger(Log logger) {
        this.logger = logger;
    }
    @Override
    public Log getLogger() {
        return logger;
    }
    protected BootstrapServiceRegistryBuilder bootstrap = new BootstrapServiceRegistryBuilder();
    private Field providedClassLoadersField = null;

    /**
     * 删除表
     * 把表从数据库中删除
     * 包括数据一起删除
     * @param tables 要删除表列表
     * @return
     */
    @Override
    public boolean deleteTables(Object[] tables) {

        return false;
    }

    /**
     * 添加包加载器。
     * @param loader
     */
    @Override
    public void addClassLoader(ClassLoader loader){
        this.bootstrap.applyClassLoader(loader);
    }

    /**
     * 删除包加载器。
     * @param loader
     */
    @Override
    public void removeClassLoader(ClassLoader loader) {
        //获取hibernate的BootstrapServiceRegistryBuilder包列表例。
        if (this.providedClassLoadersField == null) {
            try {
                this.providedClassLoadersField = BootstrapServiceRegistryBuilder.class.getDeclaredField("providedClassLoaders");
                this.providedClassLoadersField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                if (this.getLogger().isDebugEnabled()) {
                    logger.debug(ExceptionUtil.outStackTrace(e));
                } else {
                    logger.error(ExceptionUtil.outStackTrace(e));
                }
            } catch (SecurityException e) {
                if (this.getLogger().isDebugEnabled()) {
                    logger.debug(ExceptionUtil.outStackTrace(e));
                } else {
                    logger.error(ExceptionUtil.outStackTrace(e));
                }
            }
        }
        //删除包。
        try {
            ((List<ClassLoader>) this.providedClassLoadersField.get(this.bootstrap)).remove(loader);
        } catch (IllegalArgumentException e) {
            if (this.getLogger().isDebugEnabled()) {
                logger.debug(ExceptionUtil.outStackTrace(e));
            } else {
                logger.error(ExceptionUtil.outStackTrace(e));
            }
        } catch (IllegalAccessException e) {
            if (this.getLogger().isDebugEnabled()) {
                logger.debug(ExceptionUtil.outStackTrace(e));
            } else {
                logger.error(ExceptionUtil.outStackTrace(e));
            }
        }
    }
    /**
     * 重置数据库控制器。
     */
    @Override
    public synchronized void reset() throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        //数据库配置列表。
        List<IDataBaseProperties> propertiesList = new ArrayList<>();
        //遍历获取数据库配置。
        for (IDataBase dataBase : this.map.values()) {
            //获取数据库配置。
            propertiesList.add((IDataBaseProperties)dataBase);
            //关闭数据库资源。
            dataBase.close();
        }
        //清除数据库列表。
        this.map.clear();
        //重新创建数据库列表。
        for (IDataBaseProperties properties : propertiesList) {
            this.create(properties);
        }
    }

    /**
     * 数据库控制器并发模式。
     */
    private DataConcurrencyMode concurrencyMode = DataConcurrencyMode.parallel;

    /**
     * 获取数据库控制器并发模式。
     * @return
     */
    public DataConcurrencyMode getConcurrencyMode() {
        return concurrencyMode;
    }

    /**
     * 设置数据库控制器并发模式。
     * @param concurrencyMode
     */
    public void setConcurrencyMode(DataConcurrencyMode concurrencyMode) {
        this.concurrencyMode = concurrencyMode;
    }

    /**
     * 缓存模式。
     * 默认SharedCacheMode.ENABLE_SELECTIVE。
     */
    private SharedCacheMode cacheMode = SharedCacheMode.ENABLE_SELECTIVE;

    /**
     * 获取缓存模式。
     * @return
     */
    public SharedCacheMode getCacheMode() {
        return cacheMode;
    }

    /**
     * 设置缓存模式。
     * @param cacheMode
     */
    public void setCacheMode(SharedCacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }
    private String id;

    public void setId(String id) {
        this.id = id;
    }
    public void lock(){
        this.lock.lock();
    }

    public  void unlock(){
        this.lock.unlock();
    }
    /**
     * 获取控制器Id。
     * @return
     */
    public String getId(){
        return null;
    }
    public boolean isDebug() {
        return this.logger.isDebugEnabled();
    }
    /**
     * 错误处理。
     *
     * @param source
     * @param e
     */
    public void error(Object source, Exception e) {

    }
    /**
     * 获取第一个数据库。
     *
     * @return
     */
    public IDataBase getFirst() {
        try {
            this.lock.lock();
            return this.map.values().iterator().next();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 按照数据库Id获取数据库对象。
     *
     * @param id 数据库Id。
     * @return
     */
    public IDataBase get(String id) {
        try {
            this.lock.lock();
            for (IDataBase dataBase : this.map.values()) {
                if (id.equals(dataBase.getId())) return dataBase;
            }
        } finally {
            this.lock.unlock();
        }
        if (this.isDebug() || this.getLogger().isDebugEnabled()) this.getLogger().debug(id);
        return null;
    }

//    @Override
//    public DataBaseUpdateResults openUpdate(Class<?> entity, Object id, String name, Object value) throws InterruptedException {
//        return this.openUpdate(entity, new String[]{JpaUtil.getIdName(entity), name}, new Object[]{id, value});
//    }

//    @Override
//    public DataBaseUpdateResults openUpdate(Class<?> entity, HashMap<String, Object> map) throws InterruptedException {
//        String[] strings = new String[map.size()];
//        Object[] values = new Object[map.size()];
//        int i = 0;
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            strings[i] = entry.getKey();
//            values[i] = entry.getValue();
//            i++;
//        }
//        return this.openUpdate(entity, strings, values);
//    }

    /**
     * 按照数据库位置获取数据库对象。
     *
     * @param index
     * @return
     */
    public IDataBase get(int index) {
        int i = 0;
        try {
            this.lock.lock();
            for (IDataBase dataBase : this.map.values()) {
                if (index == i) return dataBase;
                i++;
            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }

    public void clear() {
        try {
            this.lock.lock();
            for (IDataBase dataBase : this.map.values()) {
                dataBase.close();
            }
            this.map.clear();
        } finally {
            this.lock.unlock();
        }
    }

    public int size() {
        try {
            this.lock.lock();
            return this.map.size();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean contains(String id) {
        try {
            this.lock.lock();
            for (IDataBase m : this.map.values()) {
                if (id.equals(m.getId())) return true;
            }
        } finally {
            this.lock.unlock();
        }
        return false;
    }
    public boolean contains(DataBase dataBase) {
        try {
            this.lock.lock();
            for (IDataBase m : this.map.values()) {
                if (dataBase.getId().equals(m.getId())) return true;
            }
        } finally {
            this.lock.unlock();
        }
        return false;
    }

    public boolean removeAll(Collection<IDataBase> c) {
        boolean isRemoveAll = false;
        try {
            this.lock.lock();
            for (IDataBase m : c) {
                IDataBase dataBase = this.map.remove(m.getId());
                if (dataBase != null) {
                    dataBase.close();
                    isRemoveAll = true;
                }
            }
        } finally {
            this.lock.unlock();
        }
        return isRemoveAll;
    }

    public void remove(int index) {
        int i = 0;
        IDataBase d = null;
        try {
            this.lock.lock();
            for (IDataBase dataBase : this.map.values()) {
                if (index == i) {
                    d = dataBase;
                    break;
                }
                i++;
            }
            if (d != null) {
                this.map.remove(d.getId());
                d.close();
            }
        } finally {
            this.lock.unlock();
        }
    }
    public boolean isEmpty() {
        try {
            this.lock.lock();
            return this.map.isEmpty();
        } finally {
            this.lock.unlock();
        }
    }

    public IDataBase remove(IDataBase module) {
        boolean isRemove = false;
        try {
            this.lock.lock();
            IDataBase dataBase = this.map.remove(module.getId());
            if (dataBase != null) dataBase.close();
            return dataBase;
        } finally {
            this.lock.unlock();
        }
    }

    public void addAll(Collection<? extends IDataBase> c) {
        try {
            this.lock.lock();
            for (IDataBase module : c) {
                this.map.put(module.getId(), module);
            }
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 实体类型列表。
     */
    private List<Class> entityList;

    /**
     * 获取实体类型列表。
     *
     * @return
     */
    public List<Class> getEntityList() {
        return entityList;
    }

    /**
     * 设置实体类型列表。
     *
     * @param entityList
     */
    public void setEntityList(List<Class> entityList) {
        this.entityList = entityList;
    }
    /**
     * 初始化数据库控制器。
     */
    public DataBaseController(List<Class> entityList) {
        this.entityList = entityList;
        this.id = UniqueKeyUtil.createUuid();
    }
    /**
     * 初始化数据库控制器。
     */
    public DataBaseController() {
        this.id = UniqueKeyUtil.createUuid();
    }

    /**
     * 初始化数据库控制器。
     *
     * @param showSql 输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。 eg. true | false
     * @param formatSql 在log和console中打印出更漂亮的SQL。 取值 true | false
     */
    public DataBaseController(boolean showSql, boolean formatSql) {
        this.id = UniqueKeyUtil.createUuid();
        this.showSql = showSql;
        this.formatSql = formatSql;
    }

    /**
     * 初始化初始化数据库控制器。
     *
     * @param showSql 输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。 eg. true | false
     * @param formatSql 在log和console中打印出更漂亮的SQL。 取值 true | false
     * @param generateStatistics 如果开启, Hibernate将收集有助于性能调节的统计数据. 取值 true | false
     */
    public DataBaseController(boolean showSql, boolean formatSql, boolean generateStatistics) {
        this.id = UniqueKeyUtil.createUuid();
        this.showSql = showSql;
        this.formatSql = formatSql;
        this.generateStatistics = generateStatistics;
    }

    /**
     * 初始化初始化数据库控制器。
     *
     * @param showSql 输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。 eg. true | false
     * @param formatSql 在log和console中打印出更漂亮的SQL。 取值 true | false
     * @param generateStatistics 如果开启, Hibernate将收集有助于性能调节的统计数据. 取值 true | false
     * @param hbm2ddlAuto 在SessionFactory创建时，自动检查数据库结构，或者将数据库schema的DDL导出到数据库. 使用 create-drop时,在显式关闭SessionFactory时，将drop掉数据库schema. 取值 validate | update | create | create-drop
     * @param useSecondLevelCache 能用来完全禁止使用二级缓存. 对那些在类的映射定义中指定<cache>的类，会默认开启二级缓存. 取值 true|false
     * @param useQueryCache 允许查询缓存, 个别查询仍然需要被设置为可缓存的. 取值 true|false
     * @param providerClass 自定义的CacheProvider的类名. 取值 classname.of.CacheProvider
     * @param factoryClass 自定义实现QueryCache接口的类名, 默认为内建的StandardQueryCache. 取值 classname.of.QueryCache
     */
    public DataBaseController(boolean showSql,
                              boolean formatSql,
                              boolean generateStatistics,
                              String hbm2ddlAuto,
                              boolean useSecondLevelCache,
                              boolean useQueryCache,
                              String providerClass,
                              String factoryClass) {
        this.id = UniqueKeyUtil.createUuid();
        this.showSql = showSql;
        this.formatSql = formatSql;
        this.generateStatistics = generateStatistics;
        this.hbm2ddlAuto = hbm2ddlAuto;
        this.useSecondLevelCache = useSecondLevelCache;
        this.useQueryCache = useQueryCache;
        this.providerClass = providerClass;
        this.factoryClass = factoryClass;
    }

    /**
     * 能用来完全禁止使用二级缓存. 对那些在类的映射定义中指定<cache>的类，会默认开启二级缓存. 取值 true|false
     */
    private boolean useSecondLevelCache;

    /**
     * 获取自定义的CacheProvider的类名. 取值 classname.of.CacheProvider
     * @return
     */
    public String getProviderClass() {
        return providerClass;
    }

    /**
     * 获取允许查询缓存, 个别查询仍然需要被设置为可缓存的. 取值 true|false
     * @return
     */
    public boolean isUseQueryCache() {
        return useQueryCache;
    }

    /**
     * 获取能用来完全禁止使用二级缓存. 对那些在类的映射定义中指定<cache>的类，会默认开启二级缓存. 取值 true|false
     * @return
     */
    public boolean isUseSecondLevelCache() {
        return useSecondLevelCache;
    }

    /**
     * 设置自定义的CacheProvider的类名. 取值 classname.of.CacheProvider
     * @param providerClass
     */
    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    /**
     * 设置允许查询缓存, 个别查询仍然需要被设置为可缓存的. 取值 true|false
     * @param useQueryCache
     */
    public void setUseQueryCache(boolean useQueryCache) {
        this.useQueryCache = useQueryCache;
    }

    /**
     * 设置能用来完全禁止使用二级缓存. 对那些在类的映射定义中指定<cache>的类，会默认开启二级缓存. 取值 true|false
     * @param useSecondLevelCache
     */
    public void setUseSecondLevelCache(boolean useSecondLevelCache) {
        this.useSecondLevelCache = useSecondLevelCache;
    }

    /**
     * 获取自定义实现QueryCache接口的类名, 默认为内建的StandardQueryCache. 取值 classname.of.QueryCache
     * @return
     */
    public String getFactoryClass() {
        return factoryClass;
    }

    /**
     * 设置自定义实现QueryCache接口的类名, 默认为内建的StandardQueryCache. 取值 classname.of.QueryCache
     * @param factoryClass
     */
    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    /**
     * 允许查询缓存, 个别查询仍然需要被设置为可缓存的. 取值 true|false
     */
    private boolean useQueryCache;
    /**
     * 自定义的CacheProvider的类名. 取值 classname.of.CacheProvider
     */
    private String providerClass = "org.hibernate.cache.EhCacheProvider";
    /**
     * 自定义实现QueryCache接口的类名, 默认为内建的StandardQueryCache. 取值 classname.of.QueryCache
     */
    private String factoryClass = "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory";
    /**
     * 如果开启, Hibernate将收集有助于性能调节的统计数据. 取值 true | false
     */
    private boolean generateStatistics = false;

    /**
     * 获取如果开启, Hibernate将收集有助于性能调节的统计数据. 取值 true | false
     * @return
     */
    public boolean isGenerateStatistics() {
        return generateStatistics;
    }

    /**
     * 设置如果开启, Hibernate将收集有助于性能调节的统计数据. 取值 true | false
     * @param generateStatistics
     */
    public void setGenerateStatistics(boolean generateStatistics) {
        this.generateStatistics = generateStatistics;
    }

    /**
     * SessionFactory创建时，自动检查数据库结构，或者将数据库schema的DDL导出到数据库. 使用 create-drop时,在显式关闭SessionFactory时，将drop掉数据库schema. 取值 validate | update | create | create-drop
     */
    private String hbm2ddlAuto = "update";

    /**
     * 获取SessionFactory创建时，自动检查数据库结构，或者将数据库schema的DDL导出到数据库. 使用 create-drop时,在显式关闭SessionFactory时，将drop掉数据库schema. 取值 validate | update | create | create-drop
     * @return
     */
    public String getHbm2ddlAuto() {
        return hbm2ddlAuto;
    }

    /**
     * 设置SessionFactory创建时，自动检查数据库结构，或者将数据库schema的DDL导出到数据库. 使用 create-drop时,在显式关闭SessionFactory时，将drop掉数据库schema. 取值 validate | update | create | create-drop
     * @param hbm2ddlAuto
     */
    public void setHbm2ddlAuto(String hbm2ddlAuto) {
        this.hbm2ddlAuto = hbm2ddlAuto;
    }

    /**
     * 输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。 eg. true | false
     */
    private boolean showSql = true;

    /**
     * 获取输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。 eg. true | false
     * @return
     */
    public boolean isShowSql() {
        return showSql;
    }

    /**
     * 设置输出所有SQL语句到控制台. 有一个另外的选择是把org.hibernate.SQL这个log category设为debug。 eg. true | false
     * @param showSql
     */
    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    /**
     * 在 log 和 console 中打印出更漂亮的 SQL。
     * hibernate.format_sql
     */
    private boolean formatSql;

    /**
     * 获取在 log 和 console 中打印出更漂亮的 SQL。
     *
     * @return
     */
    public boolean isFormatSql() {
        return formatSql;
    }

    /**
     * 设置在 log 和 console 中打印出更漂亮的 SQL。
     *
     * @param formatSql
     */
    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    /**
     * 同步锁。
     */
    private Lock lock = new ReentrantLock();
    /**
     * 数据库列表。
     */
    private HashMap<String, IDataBase> map = new HashMap();

    /**
     * 获取数据库列表。
     *
     * @return
     */
    @Override
    public HashMap<String, IDataBase> getMap() {
        //锁住数据库。
        try {
            this.lock.lock();
            return this.map;
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 添加模块。
     * {@link DataBase}
     *
     * @param module
     */
    public void add(IDataBase module) {
        try {
            this.lock.lock();
            this.map.put(module.getId(), module);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 删除模块。
     *
     * @param id
     */
    public void remove(String id) {
        IDataBase dataBase = null;
        try {
            this.lock.lock();
            dataBase = this.map.remove(id);
        } finally {
            this.lock.unlock();
        }
        if (dataBase != null) dataBase.close();
    }
    /**
     * 创建数据库。
     *
     * @param dialect
     * @param driverClass
     * @param ip
     * @param port
     * @param dataBaseName
     * @param username
     * @param password
     * @return
     * @throws DataException
     */
    public IDataBase create(String dialect,
                           String driverClass,
                           String ip,
                           int port,
                           String dataBaseName,
                           String username,
                           String password,
                           String hbm2ddlAuto,
                           boolean useSecondLevelCache,
                           boolean useQueryCache,
                           String providerClass,
                           String factoryClass) throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        DataBaseProperties properties = new DataBaseProperties();
        properties.setDialect(dialect);
        properties.setDriver(driverClass);
        properties.setIp(ip);
        properties.setPort(port);
        properties.setDataBaseName(dataBaseName);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setUseSecondLevelCache(useSecondLevelCache);
        properties.setUseQueryCache(useQueryCache);
        properties.setProviderClass(providerClass);
        properties.setFactoryClass(factoryClass);
        if (hbm2ddlAuto == null || hbm2ddlAuto.isEmpty())
            properties.setHbm2ddlAuto(this.hbm2ddlAuto);
        else
            properties.setHbm2ddlAuto(hbm2ddlAuto);
        if (this.showSql)
            properties.setShowSql(this.showSql);
        if (this.formatSql)
            properties.setFormatSql(this.formatSql);
        if (this.generateStatistics)
            properties.setGenerateStatistics(this.generateStatistics);
        IDataBase dataBase = new DataBase(this, properties);
        this.put(dataBase);
        return dataBase;
    }
    @Override
    public IDataBase create(IDataBaseProperties properties) throws DataException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        IDataBase dataBase = new DataBase(this, properties);
         this.put(dataBase);
         return dataBase;
    }
    private void put(IDataBase dataBase) {
        this.lock.lock();
        try {
             this.map.put(dataBase.getId(), dataBase);
        } finally {
            this.lock.unlock();
        }
    }
    /**
     * 获取数据库配置。
     *
     * @param id
     * @return
     */
    public DataBaseProperties getPropertie(String id) {
        return (DataBaseProperties)this.map.get(id);
    }

    /**
     * 获取启用与写入数据库数量。
     * @param excludeId 排除要统计数量的数据库Id。
     * @return
     */
    protected int getEnableOrWriteCount(String excludeId) {
        int c = 0;
        for (IDataBase dataBase : this.map.values()) {
            if (!excludeId.equals(dataBase.getId()) && dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                c++;
            }
        }
        return c;
    }
    /**
     * 获取启用与写入数据库数量。
     * @return
     */
    protected int getEnableOrWriteCount() {
        int c = 0;
        for (IDataBase dataBase : this.map.values()) {
            if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                c++;
            }
        }
        return c;
    }
    /**
     * 并行插入数据。
     * @param entity
     * @return
     * @throws InterruptedException
     */
    private DataBaseInsertResults parallelInsert(Object entity) throws InterruptedException {
        DataBaseInsertResults results = new DataBaseInsertResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        int threadNum = 0;
        try {
            this.lock.lock();
            //获取可写入数据库数量。
            threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new FourRunnable<CountDownLatch, IDataBase, DataBaseInsertResults, Object>(threadSignal, dataBase, results, entity) {
                        @Override
                        public void run() {
                            try {
                                this.getB().openInsert(this.getD());
                                this.getC().getResults().add(new DataBaseInsertResult(this.getB(), 0));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseInsertResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //验证是否正常完成。
        for (DataBaseInsertResult insertResult : results.getResults())
            if (insertResult.getInsert() == -1 || insertResult.getError() != null) {
                results.setComplete(false);
                break;
            }
        //验证是否全部数据库都完成插入。
        if (!results.isComplete()) {
            //添加失败删除。
            try {
                List<DataBaseInsertResult> completeResults = results.getCompleteResult();
                this.lock.lock();
                threadNum = completeResults.size();
                threadSignal = new CountDownLatch(threadNum);
                executor = Executors.newFixedThreadPool(threadNum);
                for (DataBaseInsertResult result : completeResults) {
                    executor.execute(new ThreeRunnable<CountDownLatch, DataBaseInsertResult, Object>(threadSignal, result, entity) {
                        @Override
                        public void run() {
                            try {
                                this.getB().getDataBase().openDelete(this.getC());
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getB().setInsert(-1, e);
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            } finally {
                this.lock.unlock();
            }
            //固定线程池执行完成后 将释放掉资源 退出主进程
            executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
            threadSignal.await(); // 等待所有子线程执行完
        }
        //返回状态。
        return results;
    }
    /**
     * 插入实体。
     *
     * @param entity 实体对象。
     */
    public DataBaseInsertResults openInsert(Object entity) throws InterruptedException, IllegalArgumentException {
        switch (this.concurrencyMode){
            case async:
                return this.asyncInsert(entity);
            case parallel:
                return this.parallelInsert(entity);
            case order:
                return this.orderInsert(entity);
            case idleOrder:
                return this.idleOrderInsert(entity);
        }
        return null;
    }

    private DataBaseInsertResults asyncInsert(Object entity) throws InterruptedException{
        return null;
    }

    private DataBaseInsertResults idleOrderInsert(Object entity) {
        return null;
    }

    private DataBaseInsertResults orderInsert(Object entity) {
        return null;
    }

    /**
     * 插入实体。
     * @param entity 实体对象。,
     * @return
     */
    public DataBaseInsertResults currentInsert(Object entity)  throws InterruptedException{
        switch (this.concurrencyMode) {
            case async:
                return this.currentAsyncInsert(this.getCurrentDataBase(), this.getCurrentSession(), entity);
            case parallel:
                return this.currentParallelInsert(this.getCurrentDataBase(), this.getCurrentSession(), entity);
            case order:
                return this.currentOrderInsert(this.getCurrentDataBase(), this.getCurrentSession(), entity);
            case idleOrder:
                return this.currentIdleOrderInsert(this.getCurrentDataBase(), this.getCurrentSession(), entity);
        }
        return null;
    }

    private DataBaseInsertResults currentIdleOrderInsert(IDataBase dataBase, Session session, Object entity) {
        return null;
    }

    private DataBaseInsertResults currentOrderInsert(IDataBase dataBase, Session session, Object entity) {
        return null;
    }

    private DataBaseInsertResults currentParallelInsert(IDataBase currentDataBase, Session currentSession, Object entity) throws InterruptedException{
        DataBaseInsertResults results = new DataBaseInsertResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        int threadNum = 0;
        try {
            this.lock.lock();
            //获取可写入数据库数量。
            threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseInsertResults, Object, IDataBase, Session>(threadSignal, dataBase, results, entity, currentDataBase, currentSession) {
                        @Override
                        public void run() {
                            try {
                                if(this.getB().getId().equals(this.getE().getId())){
                                    DataUtil.currentInsert(this.getE(), this.getF(), this.getD());
                                }else{
                                    this.getB().openInsert(this.getD());
                                }
                                this.getC().getResults().add(new DataBaseInsertResult(this.getB(), 0));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseInsertResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //验证是否正常完成。
        for (DataBaseInsertResult insertResult : results.getResults())
            if (insertResult.getInsert() == -1 || insertResult.getError() != null) {
                results.setComplete(false);
                break;
            }
        //验证是否全部数据库都完成插入。
        if (!results.isComplete()) {
            //添加失败删除。
            try {
                List<DataBaseInsertResult> completeResults = results.getCompleteResult();
                this.lock.lock();
                threadNum = completeResults.size();
                threadSignal = new CountDownLatch(threadNum);
                executor = Executors.newFixedThreadPool(threadNum);
                for (DataBaseInsertResult result : completeResults) {
                    executor.execute(new ThreeRunnable<CountDownLatch, DataBaseInsertResult, Object>(threadSignal, result, entity) {
                        @Override
                        public void run() {
                            try {
                                this.getB().getDataBase().openDelete(this.getC());
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getB().setInsert(-1, e);
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            } finally {
                this.lock.unlock();
            }
            //固定线程池执行完成后 将释放掉资源 退出主进程
            executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
            threadSignal.await(); // 等待所有子线程执行完
        }
        //返回状态。
        return results;
    }

    private DataBaseInsertResults currentAsyncInsert(IDataBase dataBase, Session session, Object entity) {
        return null;
    }

    /**
     * 打开当前线程会话查询数据
     * @param criteriaQuery
     * @param start
     * @param index
     * @param <T>
     * @return
     */
    public <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery, int start, int index) {
        return this.getCurrentEnableOrReadDataBase().currentSelect(criteriaQuery, start, index);
    }
    /**
     * 打开新会话查询数据。
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> List<T> openSelect(CriteriaQuery<T> criteriaQuery, int start, int index) {
        return this.openEnableOrReadDataBase().openSelect(criteriaQuery, start, index);
    }
    /**
     * 打开当前线程会话查询数据。
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> List<T> currentSelect(CriteriaQuery<T> criteriaQuery) {
        return this.getCurrentEnableOrReadDataBase().currentSelect(criteriaQuery);
    }
    /**
     * 打开新会话查询数据。
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> List<T> openSelect(CriteriaQuery<T> criteriaQuery) {
        return this.openEnableOrReadDataBase().openSelect(criteriaQuery);
    }
    /**
     * 当前线程会话查询一行数据。
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> T currentSingleResult(CriteriaQuery<T> criteriaQuery) {
        return this.getCurrentEnableOrReadDataBase().currentSingleResult(criteriaQuery);
    }
    /**
     * 打开新会话查询一行数据。
     * @param criteriaQuery
     * @param <T>
     * @return
     */
    public <T> T openSingleResult(CriteriaQuery<T> criteriaQuery) {
        return this.openEnableOrReadDataBase().openSingleResult(criteriaQuery);
    }
    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    public DataBaseUpdateResults openUpdate(Object entity) throws InterruptedException {
        switch (this.concurrencyMode){
            case async:
                return this.asyncUpdate(entity);
            case parallel:
                return this.parallelUpdate(entity);
            case order:
                return this.orderUpdate(entity);
            case idleOrder:
                return this.idleOrderUpdate(entity);
        }
        return null;
    }

    private DataBaseUpdateResults idleOrderUpdate(Object entity) {
        return null;
    }

    private DataBaseUpdateResults orderUpdate(Object entity) {
        return null;
    }

    /**
     * 并行更新。
     * @param entity
     * @return
     * @throws InterruptedException
     */
    private DataBaseUpdateResults parallelUpdate(Object entity) throws InterruptedException {
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new FourRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Object>(threadSignal, dataBase, results, entity) {
                        @Override
                        public void run() {
                            try {
                                this.getB().openUpdate(this.getD());
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), 0));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        return results;
    }

    private DataBaseUpdateResults asyncUpdate(Object entity) {
        return null;
    }
    private <T> DataBaseUpdateResults asyncCriteriaUpdate(CriteriaUpdate<T> criteriaUpdate){
        return null;
    }
    private <T> DataBaseUpdateResults parallelCriteriaUpdate(CriteriaUpdate<T> criteriaUpdate) throws InterruptedException{
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new FourRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, CriteriaUpdate<T>>(threadSignal, dataBase, results, criteriaUpdate) {
                        @Override
                        public void run() {
                            try {
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(),  this.getB().openUpdate(this.getD())));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        return results;
    }
    private <T> DataBaseUpdateResults orderCriteriaUpdate(CriteriaUpdate<T> criteriaUpdate){
        return null;
    }
    private <T> DataBaseUpdateResults idleOrderCriteriaUpdate(CriteriaUpdate<T> criteriaUpdate){
        return null;
    }
    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    public <T> DataBaseUpdateResults openUpdate(CriteriaUpdate<T> criteriaUpdate) throws InterruptedException {
        switch (this.concurrencyMode){
            case async:
                return this.asyncCriteriaUpdate(criteriaUpdate);
            case parallel:
                return this.parallelCriteriaUpdate(criteriaUpdate);
            case order:
                return this.orderCriteriaUpdate(criteriaUpdate);
            case idleOrder:
                return this.idleOrderCriteriaUpdate(criteriaUpdate);
        }
        return null;
    }
    /**
     * 更新。
     *
     * @param criteriaUpdate
     */
    public <T> DataBaseUpdateResults currentUpdate(CriteriaUpdate<T> criteriaUpdate) throws InterruptedException {
        switch (this.concurrencyMode) {
            case async:
                return this.asyncCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(), criteriaUpdate);
            case parallel:
                return this.parallelCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(), criteriaUpdate);
            case order:
                return this.orderCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(), criteriaUpdate);
            case idleOrder:
                return this.idleOrderCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(), criteriaUpdate);
        }
        return null;
    }

    private <T> DataBaseUpdateResults asyncCriteriaUpdate(IDataBase currentDataBase, Session currentSession, CriteriaUpdate<T> criteriaUpdate) {
        return null;
    }

    private <T> DataBaseUpdateResults idleOrderCriteriaUpdate(IDataBase currentDataBase, Session currentSession, CriteriaUpdate<T> criteriaUpdate) {
        return null;
    }

    private <T> DataBaseUpdateResults orderCriteriaUpdate(IDataBase currentDataBase, Session currentSession, CriteriaUpdate<T> criteriaUpdate) {
        return null;
    }

    private <T> DataBaseUpdateResults parallelCriteriaUpdate(IDataBase currentDataBase, Session currentSession, CriteriaUpdate<T> criteriaUpdate) throws InterruptedException {
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, CriteriaUpdate<T>, IDataBase, Session>(threadSignal, dataBase, results, criteriaUpdate, currentDataBase, currentSession) {
                        @Override
                        public void run() {
                            try {
                                if (this.getB().getId().equals(this.getE().getId())) {
                                    this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), DataUtil.currentUpdate(this.getE(), this.getF(), this.getD())));
                                } else {
                                    this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), this.getB().openUpdate(this.getD())));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        return results;
    }

    private <T> DataBaseUpdateResults asyncCriteriaUpdate(IDataBase currentDataBase, CriteriaUpdate<T> criteriaUpdate) {
        return null;
    }
    /**
     * 当前线程会话更新实体属性列表。
     * @param entity 实体。
     * @param update 实体属性列表，如果字段包含主键侧自动指定主键条件。
     * @param <T>
     * @return
     */
    public <T> DataBaseUpdateResults currentUpdate(Class<T> entity , HashMap<String, Object> update) throws InterruptedException {
        switch (this.concurrencyMode) {
            case async:
                return this.asyncCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(), entity, update);
            case parallel:
                return this.parallelCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(),entity, update);
            case order:
                return this.orderCriteriaUpdate(this.getCurrentDataBase(),this.getCurrentSession(), entity, update);
            case idleOrder:
                return this.idleOrderCriteriaUpdate(this.getCurrentDataBase(), this.getCurrentSession(), entity, update);
        }
        return null;
    }

    private <T> DataBaseUpdateResults idleOrderCriteriaUpdate(IDataBase currentDataBase, Session currentSession, Class<T> entity, HashMap<String, Object> update) {
        return null;
    }

    private <T> DataBaseUpdateResults orderCriteriaUpdate(IDataBase currentDataBase, Session currentSession, Class<T> entity, HashMap<String, Object> update) {
        return null;
    }

    private <T> DataBaseUpdateResults parallelCriteriaUpdate(IDataBase currentDataBase, Session currentSession, Class<T> entity, HashMap<String, Object> update)throws InterruptedException  {
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Two<Class<T>, HashMap<String, Object>>, IDataBase, Session>(threadSignal, dataBase, results, new Two<>(entity, update), currentDataBase, currentSession) {
                        @Override
                        public void run() {
                            try {
                                if (this.getB().getId().equals(this.getE().getId())) {
                                    this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), DataUtil.currentUpdate(this.getE(),this.getF() , this.getD().getA(), this.getD().getB())));
                                } else {
                                    this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), this.getB().openUpdate(this.getD().getA(), this.getD().getB())));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        return results;
    }

    private <T> DataBaseUpdateResults asyncCriteriaUpdate(IDataBase currentDataBase, Session currentSession, Class<T> entity, HashMap<String, Object> update) {
        return null;
    }

    /**
     * 更新实体。
     *
     * @param entity 实体对象。
     */
    public DataBaseUpdateResults currentUpdate(Object entity) throws InterruptedException{
        switch (this.concurrencyMode) {
            case async:
                return this.currentAsyncUpdate(this.getCurrentDataBase(), this.getCurrentSession(), entity);
            case parallel:
                return this.currentParallelUpdate(this.getCurrentDataBase(), this.getCurrentSession(),entity);
            case order:
                return this.currentOrderUpdate(this.getCurrentDataBase(),this.getCurrentSession(), entity);
            case idleOrder:
                return this.currentIdleOrderUpdate(this.getCurrentDataBase(), this.getCurrentSession(), entity);
        }
        return null;
    }

    private DataBaseUpdateResults currentIdleOrderUpdate(IDataBase currentDataBase, Session currentSession, Object entity) {
        return null;
    }

    private DataBaseUpdateResults currentOrderUpdate(IDataBase currentDataBase, Session currentSession, Object entity) {
        return null;
    }

    private DataBaseUpdateResults currentParallelUpdate(IDataBase currentDataBase, Session currentSession, Object entity) throws InterruptedException{
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Object, IDataBase, Session>(threadSignal, dataBase, results, entity, currentDataBase, currentSession) {
                        @Override
                        public void run() {
                            try {
                                if (this.getB().getId().equals(this.getE().getId())) {
                                    DataUtil.currentUpdate(this.getE(),this.getF() , this.getD());
                                } else {
                                    this.getB().openUpdate(this.getD());
                                }
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), 0));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        return results;
    }

    private DataBaseUpdateResults currentAsyncUpdate(IDataBase currentDataBase, Session currentSession, Object entity) {
        return null;
    }

    /**
     * 验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    public boolean openExists(DetachedCriteria criteria) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), criteria);
    }
    /**
     * 当前线程会话验证是否存在。
     *
     * @param criteria 查询对象。
     * @return
     */
    public boolean currentExists(DetachedCriteria criteria) {
        return DataUtil.currentExists(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), criteria);
    }

    /**
     * 当前线程会话统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public long currentCount(DetachedCriteria criteria) {
        return DataUtil.currentCount(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), criteria);
    }

    /**
     * 获取实体存在行数。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> long openCount(Class<T> entity) {
        return DataUtil.openCount(this.openEnableOrReadDataBase(), DetachedCriteria.forClass(entity));
    }
    /**
     * 统计行数。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public long openCount(DetachedCriteria criteria) {
        return DataUtil.openCount(this.openEnableOrReadDataBase(), criteria);
    }

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public Object currentUniqueResult(DetachedCriteria criteria) {
        return DataUtil.currentUniqueResult(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), criteria);
    }

    /**
     * 返回唯一例。
     *
     * @param criteria 查询对象。
     * @return 返回行数。
     */
    public <R> R openUniqueResult(DetachedCriteria criteria) {
        return DataUtil.openUniqueResult(this.openEnableOrReadDataBase(), criteria);
    }

    /**
     * 删除实体。
     * @param entity 删除实体类对象。
     * @param id 实体主键Id。
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    @Override
    public <T> DataBaseDeleteResults openDeleteById(Class<T> entity, Object id) throws InterruptedException {
        switch (this.concurrencyMode) {
            case async:
                return this.openAsyncDelete( entity, id);
            case parallel:
                return this.openParallelDelete(entity, id);
            case order:
                return this.openOrderDelete(entity, id);
            case idleOrder:
                return this.openIdleOrderDelete(entity, id);
        }
        return null;
    }

    private <T> DataBaseDeleteResults openIdleOrderDelete(Class<T> entity, Object id) {
        return null;
    }

    private <T> DataBaseDeleteResults openOrderDelete(Class<T> entity, Object id) {
        return null;
    }

    private <T> DataBaseDeleteResults openParallelDelete(Class<T> entity, Object id) throws InterruptedException {
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new FiveRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, Class<T>, Object>(threadSignal, dataBase, results, entity, id) {
                        @Override
                        public void run() {
                            try {
                                this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), DataUtil.openDeleteById(this.getB(), this.getD(), this.getE())));
                            } catch (Exception e) {
                                e.printStackTrace();
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseDeleteResults openAsyncDelete(Class<T> entity, Object id) {
        return null;
    }

    /**
     * 当前线程会话删除指定实体名称指定的Id。
     *
     * @param entity   实体类型。
     * @param id 实体主键Id。
     * @param <T>      泛型实体。
     * @return 返回删除行数。
     * @throws IllegalAccessException
     */
    public <T> DataBaseDeleteResults currentDeleteById(Class<T> entity, Object id) throws InterruptedException{
        switch (this.concurrencyMode) {
            case async:
                return this.currentAsyncDelete(this.getCurrentDataBase(), this.getCurrentSession(), entity, id);
            case parallel:
                return this.currentParallelDelete(this.getCurrentDataBase(), this.getCurrentSession(),entity, id);
            case order:
                return this.currentOrderDelete(this.getCurrentDataBase(), this.getCurrentSession(),entity, id);
            case idleOrder:
                return this.currentIdleOrderDelete(this.getCurrentDataBase(), this.getCurrentSession(),entity, id);
        }
        return null;
    }

    private <T> DataBaseDeleteResults currentIdleOrderDelete(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) {
        return null;
    }

    private <T> DataBaseDeleteResults currentOrderDelete(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) {
        return null;
    }

    private <T> DataBaseDeleteResults currentParallelDelete(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) throws InterruptedException{
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SevenRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, Class<T>, Object, IDataBase, Session>(threadSignal, dataBase, results, entity, id, currentDataBase, currentSession) {
                        @Override
                        public void run() {
                            try {
                                if(this.getB().getId().equals(this.getF().getId())){
                                    this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), DataUtil.currentDeleteById(this.getG(), this.getD(), this.getE())));
                                }else {
                                    this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), DataUtil.openDeleteById(this.getB(), this.getD(), this.getE())));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseDeleteResults currentAsyncDelete(IDataBase currentDataBase, Session currentSession, Class<T> entity, Object id) {
        return null;
    }
    /**
     * 当前线程会话删除实体。
     *
     * @param entity 实体。
     */
    public void currentDelete(Object entity) {
        DataUtil.currentDelete(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity);
    }

    /**
     * 删除实体。
     *
     * @param entity 实体。
     */
    public DataBaseDeleteResults openDelete(Object entity) throws InterruptedException {
        switch (this.concurrencyMode) {
            case async:
                return this.openAsyncDelete(entity);
            case parallel:
                return this.openParallelDelete(entity);
            case order:
                return this.openOrderDelete(entity);
            case idleOrder:
                return this.openIdleOrderDelete(entity);
        }
        return null;
    }

    private DataBaseDeleteResults openIdleOrderDelete(Object entity) {
        return null;
    }

    private DataBaseDeleteResults openOrderDelete(Object entity) {
        return null;
    }

    private DataBaseDeleteResults openParallelDelete(Object entity) throws InterruptedException {
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new FourRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, Object>(threadSignal, dataBase, results, entity) {
                        @Override
                        public void run() {
                            try {
                                this.getB().openDelete(this.getD());
                                this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), 0));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), 1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执
        return results;
    }

    private DataBaseDeleteResults openAsyncDelete(Object entity) {
        return null;
    }

    /**
     * 删除。
     *
     * @param criteria
     */
    public <T> DataBaseDeleteResults openDelete(CriteriaDelete<T> criteria) throws InterruptedException{
        switch (this.concurrencyMode) {
            case async:
                return this.openAsyncDelete(criteria);
            case parallel:
                return this.openParallelDelete(criteria);
            case order:
                return this.openOrderDelete(criteria);
            case idleOrder:
                return this.openIdleOrderDelete(criteria);
        }
        return null;
    }
private  <T>  DataBaseDeleteResults openAsyncDelete(CriteriaDelete<T> criteria){
       return null;
}
    private  <T>  DataBaseDeleteResults openParallelDelete(CriteriaDelete<T> criteria) throws InterruptedException {
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new FourRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, CriteriaDelete<T>>(threadSignal, dataBase, results, criteria) {
                        @Override
                        public void run() {
                            try {
                                this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), this.getB().openDelete(this.getD())));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), 1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执
        return results;
    }
    private  <T>  DataBaseDeleteResults openOrderDelete(CriteriaDelete<T> criteria){
        return null;
    }
    private  <T>  DataBaseDeleteResults openIdleOrderDelete(CriteriaDelete<T> criteria){
        return null;
    }
    /**
     * 当前线程会话删除。
     *
     * @param criteria
     * @param <T>
     * @return
     */
    public <T> int currentDelete(CriteriaDelete<T> criteria) {
        return DataUtil.currentDelete(this.getCurrentDataBase(), this.getCurrentSession(), criteria);
    }
    /**
     *
     * @return
     */
    public CriteriaBuilder currentCriteriaBuilder() {
        return this.getCurrentSession().getCriteriaBuilder();
    }
    /**
     * 错误。
     * @param dataBase 错误发生数据库。
     * @param source 错误源。
     * @param e 错误信息。
     */
    public void error(IDataBase dataBase, Object source, Exception e){

    }
    /**
     * 打开指定数据库并发模式与并发类型会话。
     * 在相同线程打开同一个会话对象。
     *
     * @return
     */
    public synchronized Session getCurrentSession() {
        //返回当前线程会话对象。
        return  this. getCurrentDataBase().getCurrentSession();
    }
    /**
     * 打开当前线程数据库。
     * @return
     */
    public synchronized IDataBase getCurrentDataBase() {
        if (this.currentDataBase.get() == null) {
            this.currentDataBase.set(this.openDataBase());
        }
        return this.currentDataBase.get();
    }
    /**
     * 开打数据位置。
     */
    private int position = 0;

    protected synchronized void setPosition(int position) {
        this.position = position;
    }

    /**
     * 获取当前打开数据库位置。
     * @return
     */
    protected synchronized int getPosition() {
        return this.position;
    }

    /**
     * 打开数据库会话。
     * @return
     */
    public synchronized IDataSession openDataSession() {
        return new DataSession(this);
    }
    /**
     * 返回包装对象。
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public <T> T openSingleResultTransformer(Class<?> result, DetachedCriteria criteria) throws Exception {
        return (T)DataUtil.openSingleResultTransformer(this.openEnableOrReadDataBase(), result, criteria);
    }


    @Override
    public <T> DataBaseUpdateResults openUpdate(Class<T> entity, String name, Object value) throws InterruptedException{
        switch (this.concurrencyMode){
            case async:
                return this.asyncUpdate(entity, name, value);
            case parallel:
                return this.parallelUpdate(entity, name, value);
            case order:
                return this.orderUpdate(entity, name, value);
            case idleOrder:
                return this.idleOrderUpdate(entity, name, value);
        }
        return null;
    }

    private DataBaseUpdateResults idleOrderUpdate(Class<?> entity, String name, Object value) {
        return null;
    }

    private DataBaseUpdateResults orderUpdate(Class<?> entity, String name, Object value) {
        return null;
    }

    private DataBaseUpdateResults parallelUpdate(Class<?> entity, String name, Object value) throws InterruptedException{
        //锁定数据库控制器。
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (!dataBase.isClose()) {
                    executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Class<?>, String, Object>(threadSignal, dataBase, results, entity, name, value) {
                        @Override
                        public void run() {
                            try {
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), this.getB().openUpdate(this.getD(), this.getE(), this.getF())));
                            } catch (Exception e) {
                                e.printStackTrace();
                                //返回错误信息。
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                //处理错误。
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }

    private DataBaseUpdateResults asyncUpdate(Class<?> entity, String name, Object value) {
        return null;
    }

    /**
     * 获取当前线程正常与只读数据库。
     * @return
     */
    public IDataBase getCurrentEnableOrReadDataBase() {
        this.lock.lock();
        try {
            //获取线程数据库。
            IDataBase dataBase = currentDataBase.get();
            //声明选择数据库。
            IDataBase selectDataBase = null;
            //遍历数据库。
            for (Map.Entry<String, IDataBase> entry : this.map.entrySet()) {
                //验证正常与只读状态数据库。
                if (entry.getValue().getStatus() == DataBaseStatus.enable || entry.getValue().getStatus() == DataBaseStatus.read && !entry.getValue().isClose()) {
                    //返回找到的数据库。
                    selectDataBase = entry.getValue();
                }
            }
            //验证线程数据库与选中数据库是否同等。
            if (!dataBase.getId().equals(selectDataBase.getId())) {
                //数据不同等重置线程数据库。
                this.currentDataBase.remove();
                this.currentDataBase.set(selectDataBase);
            }
            //返回线程数据库。
            return this.currentDataBase.get();
        } finally {
            this.lock.unlock();
        }
    }
    /**
     * 打开正常与只写数据库。
     * @return
     */
    public IDataBase openEnableOrWriteDataBase() {
        this.lock.lock();
        try {
            for (Map.Entry<String, IDataBase> entry : this.map.entrySet()) {
                if (entry.getValue().getStatus() == DataBaseStatus.enable || entry.getValue().getStatus() == DataBaseStatus.write && !entry.getValue().isClose()) {
                    return entry.getValue();
                }
            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }
    public IDataBase openEnableOrReadDataBase() {
        this.lock.lock();
        try {
            for (Map.Entry<String, IDataBase> entry : this.map.entrySet()) {
                if (entry.getValue().getStatus() == DataBaseStatus.enable || entry.getValue().getStatus() == DataBaseStatus.read && !entry.getValue().isClose()) {
                    return entry.getValue();
                }
            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }
    public IDataBase openDataBase(DataBaseStatus[] statuses) {
        this.lock.lock();
        try {
            List<Map.Entry<String, IDataBase>> list = new ArrayList<>();
            for (DataBaseStatus status : statuses) {
                for (Map.Entry<String, IDataBase> entry : this.map.entrySet()) {
                    if (entry.getValue().getStatus() == status && !entry.getValue().isClose()) {
                        list.add(entry);
                    }
                }
            }
            //获取指定操作类型的数据库列表。
            for (Map.Entry<String, IDataBase> entry : list) {

            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }
    /**
     * 打开最空闲数据库。
     * @return
     */
    public IDataBase openDataBase() {
        this.lock.lock();
        try {
            for (Map.Entry<String, IDataBase> entry : this.map.entrySet()) {
                if(entry.getValue().getStatus() == DataBaseStatus.enable && !entry.getValue().isClose()){
                    return entry.getValue();
                }
            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }
    /**
     * 当前线程数据库。
     */
    private ThreadLocal<IDataBase> currentDataBase = new ThreadLocal<>();
    /**
     * 当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> currentSelect(DetachedCriteria criteria) {
            return DataUtil.currentSelect(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), criteria);
    }
    /**
     * 当前线程的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param index 选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> currentSelect(DetachedCriteria criteria, int start, int index) {
        return DataUtil.currentSelect(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), criteria, start, index);
    }
    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> openSelect(DetachedCriteria criteria) {
        return DataUtil.openSelect(this.openEnableOrReadDataBase(), criteria);
    }

    @Override
    public <T> DataBaseDeleteResults openDelete(Class<T> entity, String[] names, Object[] values) throws InterruptedException{
        switch (this.concurrencyMode){
            case async:
                return this.asyncDelete(entity, names, values);
            case parallel:
                return this.parallelDelete(entity, names, values);
            case order:
                return this.orderDelete(entity, names, values);
            case idleOrder:
                return this.idleOrderDelete(entity, names, values);
        }
        return null;
    }

    private <T> DataBaseDeleteResults idleOrderDelete(Class<T> entity, String[] names, Object[] values) {
        return null;
    }

    private <T> DataBaseDeleteResults orderDelete(Class<T> entity, String[] names, Object[] values) {
        return null;
    }

    private <T> DataBaseDeleteResults parallelDelete(Class<T> entity, String[] names, Object[] values) throws InterruptedException{
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, Class<T>, String[], Object[]>(threadSignal, dataBase, results, entity, names, values) {
                    @Override
                    public void run() {
                        try {
                            this.getC().getResults().add(new DataBaseDeleteResult(this.getB(),  this.getB().openDelete(this.getD(), this.getE(), this.getF())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), -1, e));
                            error(this, e);
                        } finally {
                            this.getA().countDown();
                        }
                    }
                });
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseDeleteResults asyncDelete(Class<T> entity, String[] names, Object[] values) {
        return null;
    }

    /**
     * 打开新的会话选择数据。
     *
     * @param criteria 数据选择器。
     * @param start    开始位置。
     * @param index 选择数量。
     * @param <T>
     * @return 返回选择数据行。
     */
    public <T> List<T> openSelect(DetachedCriteria criteria, int start, int index) {
        return DataUtil.openSelect(this.openEnableOrReadDataBase(), criteria, start, index);
    }

    /**
     * 当前线程会话获取实体数据。
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> T currentSingleResult(Class<T> entity, Object id) {
        return DataUtil.currentSingleResult(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, id);
    }

    /**
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> T openSingleResult(Class<T> entity, Object id) {
        return DataUtil.openSingleResult(this.openEnableOrReadDataBase(), entity, id);
    }
    public <T> T openSingleResult(Class<T> entity, String name, Serializable id) {
        return DataUtil.openSingleResult(this.openEnableOrReadDataBase(), entity, name, id);
    }
    /**
     * 当前线程会话验证实体Id的键值是否存在。
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> boolean currentExistsById(Class<T> entity, Object id) {
        return DataUtil.currentExists(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, id);
    }
     /**
     * 当前线程会话验证实体条件是否存在。
     * @param entity 实体对象。
     * @param name 例名称。
     * @param id 例值。
     * @param <T>
     * @return
     */
    public <T> boolean currentExists(Class<T> entity, String name, Object id) {
        return DataUtil.currentExists(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, name, id);
    }
    /**
     * 当前线程会话验证实体条件是否存在。
     * @param entity 实体对象。
     * @param names 例名称。
     * @param values 例值。
     * @param <T>
     * @return
     */
    public <T> boolean currentExists(Class<T> entity, String[] names, Object[] values) {
        return DataUtil.currentExists(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, names, values);
    }
    /**
     * 当前线程会话验证实体条件是否存在。
     * @param entity 实体对象。
     * @param map 例列表。
     * @param <T>
     * @return
     */
    public <T> boolean currentExists(Class<T> entity, HashMap<String, Object> map) {
        return DataUtil.currentExists(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, map);
    }
    /**
     * 当前线程会话验证实体参数的键值是否存在。
     * @param entity
     * @param map
     * @param <T>
     * @return
     */
    public <T> boolean currentExistsByNotId(Class<T> entity, Object id, HashMap<String, Object> map) {
        return DataUtil.currentExistsByNotId(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, id, map);
    }
    /**
     * 验证实体参数的键值是否存在。
     * @param entity
     * @param id
     * @param map
     * @param <T>
     * @return
     */
    public <T> boolean openExistsById(Class<T> entity, Object id, HashMap<String, Object> map) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity, id, map);
    }

    /**
     * 验证实体参数的键值是否存在。
     * @param entity
     * @param map
     * @param <T>
     * @return
     */
    public <T> boolean openExists(Class<T> entity,  HashMap<String, Object> map) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity, map);
    }
    /**
     * 当前线程会话查询单行。
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    public <T> List<T> currentSelect(Class<T> entity, Object id) {
        return DataUtil.currentSelect(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity, id);
    }
    /**
     * 当前线程会话 验证实体是否已经存在数据。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> boolean currentExists(Class<T> entity) {
        return DataUtil.currentExists(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity);
    }

    /**
     * 判断指定实体是否存在数据行。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> boolean openExists(Class<T> entity) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity);
    }

    /**
     * 获取实体一行数据。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T openSingleResult(Class<T> entity) {
        return DataUtil.openSingleResult(this.openEnableOrReadDataBase(), entity);
    }
    /**
     * 当前线程会话获取实体一行数据。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T currentSingleResult(Class<T> entity) {
        return DataUtil.currentSingleResult(this.getCurrentEnableOrReadDataBase(), this.getCurrentSession(), entity);
    }
    /**
     * 关闭当前线程的数据库与会话对象。
     * @throws Exception
     */
    public synchronized void close() throws Exception {
        //清空当前线程数据库对象。
        this.currentDataBase.remove();
    }

    /**
     * 更新实体字典。
     * @param entity
     * @param update
     * @param <T>
     * @return 返回更新状态。
     * @throws InterruptedException
     */
    public <T> DataBaseUpdateResults openUpdate(Class<T> entity, HashMap<String, Object> update) throws InterruptedException {
        //锁定数据库控制器。
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.map.size();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (!dataBase.isClose()) {
                    executor.execute(new OneRunnable<Five<CountDownLatch, IDataBase, DataBaseUpdateResults, Class<T>, HashMap<String, Object>>>(new Five<>(threadSignal, dataBase, results, entity, update)) {
                        @Override
                        public void run() {
                            try {
                                Session session = this.getA().getB().openSession();
                                try {
                                    Transaction transaction = session.beginTransaction();
                                    try {
                                        CriteriaBuilder builder = session.getCriteriaBuilder();
                                        CriteriaUpdate<T> query = builder.createCriteriaUpdate(this.getA().getD());
                                        Root<T> root = query.from(this.getA().getD());
                                        //获取主键Id名称。
                                        String idName = DataUtil.getIdName(this.getA().getD());
                                        for (Map.Entry<String, Object> entry : this.getA().getE().entrySet()) {
                                            //验证是否为主键。
                                            if (entry.getKey().equals(idName)) {
                                                query.where(builder.equal(root.get(idName), entry.getValue()));
                                            } else {
                                                query.set(root.get(entry.getKey()), entry.getValue());
                                            }
                                        }
                                        //返回更新状态。
                                        this.getA().getC().getResults().add(new DataBaseUpdateResult(this.getA().getB(), session.createQuery(query).executeUpdate()));
                                        transaction.commit();
                                    } catch (Exception e) {
                                        transaction.rollback();
                                        throw e;
                                    }
                                } finally {
                                    if (session != null && session.isOpen()) session.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //返回错误信息。
                                this.getA().getC().getResults().add(new DataBaseUpdateResult(this.getA().getB(), -1, e));
                                //处理错误。
                                error(this, e);
                            } finally {
                                this.getA().getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        threadSignal.await(); // 等待所有子线程执行完
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        //返回更新数据库总行数。
        return results;
    }

    @Override
    public <T> List<T> openSelect(Class<T> entity, int start, int size) {
        return DataUtil.openSelect(this.openEnableOrReadDataBase(), entity, start, size);
    }

    /**
     * 验证实体主键Id是否存在。
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean openExists(Class<T> entity, Object id) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity, id);
    }

    /**
     * 获取该实体数据库所有数据行。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> List<T> openSelect(Class<T> entity) {
        return DataUtil.openSelect(this.openEnableOrReadDataBase(), entity);
    }
    /**
     * 当前线程会话获取该实体数据库所有数据行。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> List<T> currentSelect(Class<T> entity) {
        return DataUtil.currentSelect(this.getCurrentEnableOrReadDataBase(), entity);
    }
    /**
     * 当前线程会话删除实体全部数据。
     * @param entity
     * @param <T>
     * @return
     */
    public <T> int currentDelete(Class<T> entity) {
        return DataUtil.currentDelete(this.getCurrentEnableOrReadDataBase(), entity);
    }
    /**
     * 验证例值是否存在。
     * @param entity
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean openExists(Class<T> entity, String name, Object value) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity, name, value);
    }
    /**
     * 将原版实体的所有属性数据插入指定实体。
     * @param originalEntity 原版实体数据对象。
     * @param insertEntity 要插入数据的实体。
     */
    public void openOriginalInsert(Object originalEntity, Class<?> insertEntity) throws InterruptedException, InstantiationException, IllegalAccessException, InvocationTargetException{
        Object entity = insertEntity.newInstance();
        BeanUtils.copyProperties(entity, originalEntity);
        this.openInsert(entity);
    }

    /**
     * 将原版实体更新到指定实体对象。
     * @param originalEntity 原版实体数据对象。
     * @param updateEntity 要插更新据的实体。
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void openOriginalUpdate(Object originalEntity, Object updateEntity) throws IllegalAccessException, InvocationTargetException, InterruptedException{
        BeanUtils.copyProperties(updateEntity, originalEntity);
        this.openUpdate(updateEntity);
    }
    /**
     * 删除实体表全部行数据。
     * @param entity
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> DataBaseDeleteResults openDelete(Class<T> entity) throws InterruptedException {
        switch (this.concurrencyMode){
            case async:
                return this.asyncDelete(entity);
            case parallel:
                return this.parallelDelete(entity);
            case order:
                return this.orderDelete(entity);
            case idleOrder:
                return this.idleOrderDelete(entity);
        }
        return null;
    }

    private <T> DataBaseDeleteResults idleOrderDelete(Class<T> entity) {
        return null;
    }

    private <T> DataBaseDeleteResults orderDelete(Class<T> entity) {
        return null;
    }

    private <T> DataBaseDeleteResults parallelDelete(Class<T> entity) throws InterruptedException {
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                executor.execute(new FourRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, Class<T>>(threadSignal, dataBase, results, entity) {
                    @Override
                    public void run() {
                        try {
                            this.getC().getResults().add(new DataBaseDeleteResult(this.getB(),  this.getB().openDelete(this.getD())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), -1, e));
                            error(this, e);
                        } finally {
                            this.getA().countDown();
                        }
                    }
                });
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseDeleteResults asyncDelete(Class<T> entity) {
        return null;
    }

    /**
     * 验证不是此Id的重复是否存在。
     * @param entity
     * @param id
     * @param map
     * @param <T>
     * @return
     */
    public <T> boolean openExistsByNotId(Class<T> entity, String id, HashMap<String, Object> map) {
        return DataUtil.openExistsByNotId(this.openEnableOrReadDataBase(), entity, id, map);
    }

    /**
     * 更新列表。
     * @param entity 更新实体。
     * @param names 更新例名称，包括主键。
     * @param values 更新值，包括例主键值。
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> DataBaseUpdateResults openUpdate(Class<T> entity, String[] names, Object[] values) throws InterruptedException {
        switch (this.concurrencyMode){
            case async:
                return this.asyncUpdate(entity, names, values);
            case parallel:
                return this.parallelUpdate(entity, names, values);
            case order:
                return this.orderUpdate(entity, names, values);
            case idleOrder:
                return this.idleOrderUpdate(entity, names, values);
        }
        return null;
    }

    private <T> DataBaseUpdateResults idleOrderUpdate(Class<T> entity, String[] names, Object[] values) {
        return null;
    }

    private <T> DataBaseUpdateResults orderUpdate(Class<T> entity, String[] names, Object[] values) {
        return null;
    }

    private <T> DataBaseUpdateResults parallelUpdate(Class<T> entity, String[] names, Object[] values) throws InterruptedException {
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                executor.execute(new SixRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Class<T>, String[], Object[]>(threadSignal, dataBase, results, entity, names, values) {
                    @Override
                    public void run() {
                        try {
                            this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), this.getB().openUpdate(this.getD(), this.getE(), this.getF())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                            error(this, e);
                        } finally {
                            this.getA().countDown();
                        }
                    }
                });
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任
        threadSignal.await(); // 等待所有子线程执行完务
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseUpdateResults asyncUpdate(Class<T> entity, String[] names, Object[] values) {
        return null;
    }

    /**
     * 更新实体。
     * @param entity 是定实体。
     * @param id 实体主键Id。
     * @param names 更新例名称列表。
     * @param values 更新例值列表。
     * @param <T> 实体类型。
     * @return 返回更新状态列表。
     * @throws InterruptedException
     */
    public <T> DataBaseUpdateResults openUpdateById(Class<T> entity, Object id, String[] names, Object[] values) throws InterruptedException {
        switch (this.concurrencyMode){
            case async:
                return this.asyncUpdate(entity, id, names, values);
            case parallel:
                return this.parallelUpdate(entity, id, names, values);
            case order:
                return this.orderUpdate(entity, id, names, values);
            case idleOrder:
                return this.idleOrderUpdate(entity, id, names, values);
        }
        return null;
    }

    private <T> DataBaseUpdateResults idleOrderUpdate(Class<T> entity, Object id, String[] names, Object[] values) {
        return null;
    }

    private <T> DataBaseUpdateResults orderUpdate(Class<T> entity, Object id, String[] names, Object[] values) {
        return null;
    }

    private <T> DataBaseUpdateResults parallelUpdate(Class<T> entity, Object id, String[] names, Object[] values)throws InterruptedException {
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        CountDownLatch threadSignal =  null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
             threadSignal = new CountDownLatch(threadNum);
             executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SevenRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Class<T>, Object, String[], Object[]>(threadSignal, dataBase, results, entity, id, names, values) {
                        @Override
                        public void run() {
                            try {
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), this.getB().openUpdateById(this.getD(), this.getE(), this.getF(), this.getG())));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1, e));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseUpdateResults asyncUpdate(Class<T> entity, Object id, String[] names, Object[] values) {
        return null;
    }

    /**
     *
     * @param entity
     * @param id
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean openExists(Class<T> entity, Object id, String name, String value) {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity, id, name, value);
    }

    /**
     *
     * @param entity
     * @param id
     * @param name
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean openExistsByNotId(Class<T> entity, Object id, String name, Object value) {
        return DataUtil.openExistsByNotId(this.openEnableOrReadDataBase(), entity, id, name, value);
    }
    private <T> DataBaseDeleteResults openAsyncDelete(Class<T> entity, List<Object> arrays){
        return null;
    }
    private <T> DataBaseDeleteResults openParallelDelete(Class<T> entity, List<Object> arrays) throws InterruptedException{
        DataBaseDeleteResults results = new DataBaseDeleteResults();
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
             threadSignal = new CountDownLatch(threadNum);
             executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                executor.execute(new FiveRunnable<CountDownLatch, IDataBase, DataBaseDeleteResults, Class<T>, List<Object>>(threadSignal, dataBase, results, entity, arrays) {
                    @Override
                    public void run() {
                        try {
                            this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), this.getB().openDeleteByIds(this.getD(),this.getE())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            this.getC().getResults().add(new DataBaseDeleteResult(this.getB(), -1, e));
                            error(this, e);
                        } finally {
                            this.getA().countDown();
                        }
                    }
                });
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        threadSignal.await(); // 等待所有子线程执行完
        //返回更新数据库总行数。
        return results;
    }
    private <T> DataBaseDeleteResults openOrderDelete(Class<T> entity, List<Object> arrays){
        return null;
    }
    private <T> DataBaseDeleteResults openIdleOrderDelete(Class<T> entity, List<Object> arrays){
        return null;
    }
    /**
     * 删除列表。
     * @param entity 删除指定实体。
     * @param arrays 删除实体指定数组列表。
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> DataBaseDeleteResults openDelete(Class<T> entity, List<Object> arrays) throws InterruptedException {
        switch (this.concurrencyMode) {
            case async:
                return this.openAsyncDelete(entity, arrays);
            case parallel:
                return this.openParallelDelete(entity, arrays);
            case order:
                return this.openOrderDelete(entity, arrays);
            case idleOrder:
                return this.openIdleOrderDelete(entity, arrays);
        }
        return null;
    }

    /**
     * 更新指定实体主键条件的例值。
     * @param entity 更新实体。
     * @param id 更新实体主键Id。
     * @param name 更新实体例名称。
     * @param value 更新实体例值。
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> DataBaseUpdateResults openUpdateById(Class<T> entity, Object id, String name, Object value) throws InterruptedException {
        switch (this.concurrencyMode) {
            case async:
                return this.openAsyncUpdate(entity, id, name, value);
            case parallel:
                return this.openParallelUpdate(entity, id, name, value);
            case order:
                return this.openOrderUpdate(entity, id, name, value);
            case idleOrder:
                return this.openIdleOrderUpdate(entity, id, name, value);
        }
        return null;
    }

    private <T> DataBaseUpdateResults openIdleOrderUpdate(Class<T> entity, Object id, String name, Object value) {
        return null;
    }

    private <T> DataBaseUpdateResults openOrderUpdate(Class<T> entity, Object id, String name, Object value) {
        return null;
    }

    private <T> DataBaseUpdateResults openParallelUpdate(Class<T> entity, Object id, String name, Object value) throws InterruptedException {
        CountDownLatch threadSignal = null;
        ExecutorService executor = null;
        DataBaseUpdateResults results = new DataBaseUpdateResults();
        try {
            this.lock.lock();
            int threadNum = this.getEnableOrWriteCount();
            threadSignal = new CountDownLatch(threadNum);
            executor = Executors.newFixedThreadPool(threadNum);
            for (IDataBase dataBase : this.map.values()) {
                if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                    executor.execute(new SevenRunnable<CountDownLatch, IDataBase, DataBaseUpdateResults, Class<T>, Object, String, Object>(threadSignal, dataBase, results, entity, id, name, value) {
                        @Override
                        public void run() {
                            DataBaseUpdateResult results = null;
                            try {
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), this.getB().openUpdateById(this.getD(), this.getE(), this.getF(), this.getG())));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.getC().getResults().add(new DataBaseUpdateResult(this.getB(), -1));
                                error(this, e);
                            } finally {
                                this.getA().countDown();
                            }
                        }
                    });
                }
            }
        } finally {
            //解锁数据库控制器。
            this.lock.unlock();
        }
        threadSignal.await(); // 等待所有子线程执行完
        //固定线程池执行完成后 将释放掉资源 退出主进程
        executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        //返回更新数据库总行数。
        return results;
    }

    private <T> DataBaseUpdateResults openAsyncUpdate(Class<T> entity, Object id, String name, Object value) {
        return null;
    }

    /**
     * 获取实体第一行数据。
     * @param entity
     * @param <T>
     * @return
     */
//    public <T> T openGet(Class<T> entity) {
//        return DataUtil.openGet(this.openEnableOrReadDataBase(), entity);
//    }

    /**
     * 验证实体条件是否存在。
     * @param entity 实体对象。
     * @param names 例名称。
     * @param values 例值。
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> boolean openExists(Class<T> entity, String[] names, Object[] values)  {
        return DataUtil.openExists(this.openEnableOrReadDataBase(), entity, names, values, false);
    }

    /**
     * 返回包装对象。
     * @param result
     * @param criteria
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public <T> T openTransactionSingleResultTransformer(Class<T> result, DetachedCriteria criteria) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return DataUtil.openTransactionSingleResultTransformer(this.openEnableOrReadDataBase(), result, criteria);
    }
    /**
     * 当前线程会话获取实体指定字段唯一键。
     * @param entity
     * @param id
     * @param name
     * @param <T>
     * @return
     */
    public <T> Object openByIdNameUniqueResult(Class<T> entity, Object id, String name) {
        return DataUtil.openByIdNameUniqueResult(this.openEnableOrReadDataBase(), entity, id, name);
    }

//    @Override
//    public DataBaseDeleteResults openDelete(Class<?> entity, Object value) throws InterruptedException {
//        return this.openDelete(entity, new String[]{}, new Object[]{value});
//    }

//    @Override
//    public int openDelete(Class<?> entity, Object value) {
//        return DataUtil.openDelete(this.openEnableOrReadDataBase(), entity, value);
//    }

    /**
     * 当前线程会话获取实体指定字段唯一键。
     * @param entity
     * @param name
     * @param id
     * @param <T>
     * @return
     */
    public <T> Object openTransactionUniqueResult(Class<T> entity, String name, Object id) {
        return DataUtil.openTransactionUniqueResult(this.openEnableOrReadDataBase(), entity, name, id);
    }
    /**
     * 获取实体分页。
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public <T> List<T> openSelect(Class<T> entity, String[] names, Object[] values) {
        return DataUtil.openSelect(this.openEnableOrReadDataBase(), entity, names, values);
    }
    /**
     * 使用事务读取数据。
     * @param entity
     * @param names
     * @param values
     * @param <T>
     * @return
     */
    public <T> List<T> openTransactionSelect(Class<T> entity, String[] names, Object[] values) {
        return DataUtil.openTransactionSelect(this.openEnableOrReadDataBase(), entity, names, values);
    }

    /**
     *
     * @param entity
     * @param name
     * @param names
     * @param values
     * @param <T>
     * @param <R>
     * @return
     */
    public <T, R> R openUniqueResult(Class<T> entity, String name, String[] names, Object[] values){
        return DataUtil.openUniqueResult(this.openEnableOrReadDataBase(), entity, name, names, values);
    }

    /**
     * 按照数据库开始位置的作为数据源复制到指定数据库。
     * @param fillDataBase 指定要填充数据的数据库。
     * @throws Exception
     */
    @Override
    public  void rootToCopy(IDataBase fillDataBase) throws Exception{
        this.toCopy(this.getRoot(), fillDataBase);
    }
    /**
     * 指定源数据库位置复制到指定数据库。
     * @param sourceId 源数据库Id。
     * @param fillDataBase 要填充的数据库。
     * @throws Exception
     */
    @Override
    public void toCopy(String sourceId, IDataBase fillDataBase) throws Exception {
        this.toCopy(this.get(sourceId), fillDataBase);
    }
    /**
     * 指定源数据库复制到指定数据库。
     * @param sourceDataBase 源数据库位置。
     * @param fillDataBase 要填充的数据库。
     * @throws Exception
     */
    @Override
    public void toCopy(IDataBase sourceDataBase, IDataBase fillDataBase) throws Exception {
        if (sourceDataBase.getId().equals(fillDataBase.getId())) {
            throw new Exception("数据库位置" + sourceDataBase.getId() + "与填充数据库位置相同错误！");
        }
        //按照表实体类逐个复制数据。
        for (Class<?> c : this.entityList) {
            //获取行数。
            long count = sourceDataBase.openCount(c);
            //位置。
            int index = 0;
            //每次100行。
            int size = 100;
            while (count > index) {
                List list = sourceDataBase.openSelect(c, index, size);
                for (Object o : list) {
                    fillDataBase.openInsert(o);
                }
                index += size;
            }
        }
    }
    /**
     * 清除数据库数据。
     * @param dataBase
     */
    @Override
    public void clearData(IDataBase dataBase) throws Exception{
        for (Class<?> c : this.entityList) {
            dataBase.openDelete(c);
        }
    }

    /**
     * 获取root数据库。
     * @return
     */
    @Override
    public IDataBase getRoot() {
        for (IDataBase dataBase : this.map.values()){
            if(dataBase.isRoot()){
                return dataBase;
            }
        }
        return null;
    }
    /**
     * 获取正常与写入数据库列表。
     * @return
     */
    @Override
    public List<IDataBase> getEnableOrWriteDataBase() {
        List<IDataBase> list = new ArrayList<>();
        for (IDataBase dataBase : this.map.values()) {
            if (dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                list.add(dataBase);
            }
        }
        return list;
    }
    /**
     * 获取正常与写入数据库列表。
     * @param excludeId 指定不纳入列表的数据库Id。
     * @return
     */
    @Override
    public List<IDataBase> getEnableOrWriteDataBase(String excludeId) {
        List<IDataBase> list = new ArrayList<>();
        for (IDataBase dataBase : this.map.values()) {
            if (!excludeId.equals(dataBase.getId()) && dataBase.getStatus() == DataBaseStatus.enable || dataBase.getStatus() == DataBaseStatus.write && !dataBase.isClose()) {
                list.add(dataBase);
            }
        }
        return list;
    }
    /**
     * 获取实体指定例值的数据行。
     * @param entity 指定实体。
     * @param name 指定例名称。
     * @param value 指定例值。
     * @param <T>
     * @return 返回指定实体数据行。
     */
    @Override
    public <T> T openSingleResult(Class<T> entity, String name, Object value) {
        return DataUtil.openSingleResult(this.openEnableOrReadDataBase(), entity, name, value);
    }
}
