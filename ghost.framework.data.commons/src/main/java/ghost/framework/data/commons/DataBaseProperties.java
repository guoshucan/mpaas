package ghost.framework.data.commons;
import ghost.framework.util.UniqueKeyUtil;
import org.apache.commons.beanutils.BeanUtils;
import javax.persistence.SharedCacheMode;
import java.lang.reflect.InvocationTargetException;
/**
 * 数据库配置。
 */
public class DataBaseProperties implements IDataBaseProperties {
    private String url;
    @Override
    public String getUrl() {
        return url;
    }
    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 事务超时
     */
    private int transactionTimeout;

    /**
     * 获取事务超时
     *
     * @return
     */
    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    /**
     * 设置事务超时
     *
     * @param transactionTimeout
     */
    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }
//    /**
//     * 数据库状态。
//     */
//    private DataBaseStatus status = DataBaseStatus.disabled;
//
//    /**
//     * 获取数据库状态。
//     *
//     * @return
//     */
//    public DataBaseStatus getStatus() {
//        return status;
//    }
//
//    /**
//     * 设置数据库状态。
//     *
//     * @param status
//     */
//    public void setStatus(DataBaseStatus status) {
//        this.status = status;
//    }

    /**
     * 初始化数据库配置。
     */
    public DataBaseProperties() {
    }

    /**
     * 初始化数据库配置。
     *
     * @param propertie
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected DataBaseProperties(IDataBaseProperties propertie) throws SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BeanUtils.copyProperties(this, propertie);
        if (this.id == null || this.id.isEmpty()) this.id = UniqueKeyUtil.createUuid();
    }

    /**
     * 数据库Id。
     */
    private String id;

    /**
     * 获取数据库Id。
     *
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 设置数据库Id。
     *
     * @param id
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 数据库名称。
     */
    private String dataBaseName;

    /**
     * 获取数据库名称。
     *
     * @return
     */
    @Override
    public String getDataBaseName() {
        return dataBaseName;
    }

    /**
     * 设置数据库名称。
     *
     * @param dataBaseName
     */
    @Override
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    /**
     * 开启缓存。
     */
    private boolean useSecondLevelCache;

    @Override
    public String getProviderClass() {
        return providerClass;
    }

    /**
     * 获取开启查询缓存。
     *
     * @return
     */
    @Override
    public boolean isUseQueryCache() {
        return useQueryCache;
    }

    /**
     * 获取开启缓存。
     *
     * @return
     */
    @Override
    public boolean isUseSecondLevelCache() {
        return useSecondLevelCache;
    }

    @Override
    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    /**
     * 设置开启查询缓存。
     *
     * @param useQueryCache
     */
    @Override
    public void setUseQueryCache(boolean useQueryCache) {
        this.useQueryCache = useQueryCache;
    }

    /**
     * 设置开启缓存。
     *
     * @param useSecondLevelCache
     */
    @Override
    public void setUseSecondLevelCache(boolean useSecondLevelCache) {
        this.useSecondLevelCache = useSecondLevelCache;
    }

    @Override
    public String getFactoryClass() {
        return factoryClass;
    }

    @Override
    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    /**
     * 开启查询缓存。
     */
    private boolean useQueryCache;
    private String providerClass = "org.hibernate.cache.EhCacheProvider";
    private String factoryClass = "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory";
    /**
     * 管理帐号。
     */
    private String username;

    /**
     * 获取管理密码。
     *
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 设置管理密码。
     *
     * @param username
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 管理密码。
     */
    private String password;

    /**
     * 获取管理密码。
     *
     * @return
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 设置管理密码。
     *
     * @param password
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 数据库使用的驱动类。
     */
    private String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * 获取数据库使用的驱动类。
     *
     * @return
     */
    @Override
    public String getDriver() {
        return driver;
    }

    /**
     * 设置数据库使用的驱动类。
     *
     * @param driver
     */
    @Override
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * 数据库使用的方言。
     * 允许 Hibernate 针对特定的关系数据库生成优化的 SQL 的 org.hibernate.dialect.Dialect 的类名。
     * 例如：org.hibernate.dialect.MySQL57Dialect
     */
    private String dialect = "org.hibernate.dialect.MySQL57Dialect";

    /**
     * 设置数据库使用的方言。
     *
     * @param dialect
     */
    @Override
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    /**
     * 获取数据库使用的方言。
     *
     * @return
     */
    @Override
    public String getDialect() {
        return dialect;
    }

    /**
     * IP地址。
     */
    private String ip;

    /**
     * 获取IP地址。
     *
     * @return
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置IP地址。
     *
     * @param ip
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 端口。
     */
    private int port;

    /**
     * 获取端口。
     *
     * @return
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * 设置端口。
     *
     * @param port
     */
    @Override
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 是否打印SQL语句。
     */
    private boolean showSql;

    /**
     * 获取是否打印SQL语句。
     *
     * @return
     */
    @Override
    public boolean isShowSql() {
        return showSql;
    }

    /**
     * 设置是否打印SQL语句。
     *
     * @param showSql
     */
    @Override
    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    /**
     * 连接池大小。
     */
    private int poolSize;

    /**
     * 获取连接池大小。
     *
     * @return
     */
    @Override
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * 设置连接池大小。
     *
     * @param poolSize
     */
    @Override
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
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
    @Override
    public boolean isFormatSql() {
        return formatSql;
    }

    /**
     * 设置在 log 和 console 中打印出更漂亮的 SQL。
     *
     * @param formatSql
     */
    @Override
    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    /**
     * 如果开启，Hibernate 将收集有助于性能调节的统计数据。
     * hibernate.generate_statistics
     */
    private boolean generateStatistics;

    /**
     * 获取如果开启，Hibernate 将收集有助于性能调节的统计数据。
     *
     * @return
     */
    @Override
    public boolean isGenerateStatistics() {
        return generateStatistics;
    }

    /**
     * 设置如果开启，Hibernate 将收集有助于性能调节的统计数据。
     *
     * @param generateStatistics
     */
    @Override
    public void setGenerateStatistics(boolean generateStatistics) {
        this.generateStatistics = generateStatistics;
    }

    /**
     * 如果开启，在对象被删除时生成的标识属性将被重设为默认值。
     * hibernate.use_identifier_rollback
     */
    private boolean useIdentifierRollback;

    /**
     * 获取如果开启，在对象被删除时生成的标识属性将被重设为默认值。
     *
     * @return
     */
    @Override
    public boolean isUseIdentifierRollback() {
        return useIdentifierRollback;
    }

    /**
     * 设置如果开启，在对象被删除时生成的标识属性将被重设为默认值。
     *
     * @param useIdentifierRollback
     */
    @Override
    public void setUseIdentifierRollback(boolean useIdentifierRollback) {
        this.useIdentifierRollback = useIdentifierRollback;
    }

    /**
     * 如果开启，Hibernate 将在 SQL 中生成有助于调试的注释信息，默认值为 false。
     * hibernate.use_sql_comments
     */
    private boolean useSqlComments;

    /**
     * 获取如果开启，Hibernate 将在 SQL 中生成有助于调试的注释信息，默认值为 false。
     *
     * @return
     */
    @Override
    public boolean isUseSqlComments() {
        return useSqlComments;
    }

    /**
     * 设置如果开启，Hibernate 将在 SQL 中生成有助于调试的注释信息，默认值为 false。
     *
     * @param useSqlComments
     */
    @Override
    public void setUseSqlComments(boolean useSqlComments) {
        this.useSqlComments = useSqlComments;
    }

    /**
     * 强制 Hibernate 按照被更新数据的主键，为SQL 更新排序。这么做将减少在高并发系统中事务的死锁。
     * hibernate.order_updates
     */
    private boolean orderUpdates;

    /**
     * 获取强制 Hibernate 按照被更新数据的主键，为SQL 更新排序。这么做将减少在高并发系统中事务的死锁。
     *
     * @return
     */
    @Override
    public boolean isOrderUpdates() {
        return orderUpdates;
    }

    /**
     * 设置强制 Hibernate 按照被更新数据的主键，为SQL 更新排序。这么做将减少在高并发系统中事务的死锁。
     *
     * @param orderUpdates
     */
    @Override
    public void setOrderUpdates(boolean orderUpdates) {
        this.orderUpdates = orderUpdates;
    }

    /**
     * 为由这个 SessionFactory 打开的所有 Session指定默认的实体表现模式。
     * hibernate.default_entity_mode = dynamic-map，dom4j，pojo
     */
    private String defaultEntityMode;

    /**
     * 获取为由这个 SessionFactory 打开的所有 Session指定默认的实体表现模式。
     *
     * @return
     */
    @Override
    public String getDefaultEntityMode() {
        return defaultEntityMode;
    }

    /**
     * 设置为由这个 SessionFactory 打开的所有 Session指定默认的实体表现模式。
     *
     * @param defaultEntityMode
     */
    @Override
    public void setDefaultEntityMode(String defaultEntityMode) {
        this.defaultEntityMode = defaultEntityMode;
    }

    /**
     * 为 Hibernate 关联的批量抓取设置默认数量。
     * hibernate.default_batch_fetch_size = 4、8、16
     */
    private int defaultBatchFetchSize;

    /**
     * 获取为 Hibernate 关联的批量抓取设置默认数量。
     *
     * @return
     */
    @Override
    public int getDefaultBatchFetchSize() {
        return defaultBatchFetchSize;
    }

    /**
     * 设置为 Hibernate 关联的批量抓取设置默认数量。
     *
     * @param defaultBatchFetchSize
     */
    @Override
    public void setDefaultBatchFetchSize(int defaultBatchFetchSize) {
        this.defaultBatchFetchSize = defaultBatchFetchSize;
    }

    /**
     * 为单向关联（一对一，多对一）的外连接抓取（outer join fetch）树设置最大深度。
     * hibernate.max_fetch_depth = 0到3
     */
    private int maxFetchDepth;

    /**
     * 获取为单向关联（一对一，多对一）的外连接抓取（outer join fetch）树设置最大深度。
     *
     * @return
     */
    @Override
    public int getMaxFetchDepth() {
        return maxFetchDepth;
    }

    /**
     * 设置为单向关联（一对一，多对一）的外连接抓取（outer join fetch）树设置最大深度。
     *
     * @param maxFetchDepth
     */
    @Override
    public void setMaxFetchDepth(int maxFetchDepth) {
        this.maxFetchDepth = maxFetchDepth;
    }

    /**
     * org.hibernate.SessionFactory 创建后，将自动使用这个名字绑定到 JNDI 中。
     * hibernate.session_factory_name
     */
    private String sessionFactoryName;

    /**
     * 获取org.hibernate.SessionFactory 创建后，将自动使用这个名字绑定到 JNDI 中。
     *
     * @return
     */
    @Override
    public String getSessionFactoryName() {
        return sessionFactoryName;
    }

    /**
     * 设置org.hibernate.SessionFactory 创建后，将自动使用这个名字绑定到 JNDI 中。
     *
     * @param sessionFactoryName
     */
    @Override
    public void setSessionFactoryName(String sessionFactoryName) {
        this.sessionFactoryName = sessionFactoryName;
    }

    /**
     * 在生成的 SQL 中，将给定的 catalog 附加于非全限定名的表名上。
     * hibernate.default_catalog
     */
    private String defaultCatalog;

    /**
     * 获取在生成的 SQL 中，将给定的 catalog 附加于非全限定名的表名上。
     *
     * @return
     */
    @Override
    public String getDefaultCatalog() {
        return defaultCatalog;
    }

    /**
     * 设置在生成的 SQL 中，将给定的 catalog 附加于非全限定名的表名上。
     *
     * @param defaultCatalog
     */
    @Override
    public void setDefaultCatalog(String defaultCatalog) {
        this.defaultCatalog = defaultCatalog;
    }

    /**
     * 在启动和停止时自动地创建，更新或删除数据库模式。取值 create | update| create-drop | validate
     * 昨天自己创建了表，通过hibernate进行映射，添加了unique的限制，运行JUnit测试的时候发现，hibernate没有对配置的字段没有进行唯一性检查，而是直接通过，存入数据库。让人百思不得其解！
     * 后来查阅hibernate文档和网上核对，发现unique只在表是由hibernate自己创建的时候才有效，也就是说
     *
     * validate                加载hibernate时，验证创建数据库表结构
     * create                  每次加载hibernate，重新创建数据库表结构
     * create-drop        加载hibernate时创建，退出是删除表结构
     * update                 加载hibernate自动更新数据库结构
     * hibernate.hbm2ddl.auto=update或create或create-drop时 如果数据库中没有表，那么hibernate创建表的时候就会在配置的字段上加上唯一约束。
     * hibernate.hbm2ddl.auto=update时 如果数据库中有表，hibernate并不会检测配置的字段是否有唯一约束，只要其他配置相同，就不会更新，所以此时的表没有唯一约束。
     * 总之：如果要使用unique约束，那么表需要由hibernate生成 或者自己在表中添加约束，所以对于字段的唯一性约束，都是在数据库端做检查的
     */
    private String hbm2ddlAuto = "update";

    /**
     * 获取在启动和停止时自动地创建，更新或删除数据库模式。
     *
     * @return
     */
    @Override
    public String getHbm2ddlAuto() {
        return hbm2ddlAuto;
    }

    /**
     * 设置在启动和停止时自动地创建，更新或删除数据库模式。
     *
     * @param hbm2ddlAuto
     */
    @Override
    public void setHbm2ddlAuto(String hbm2ddlAuto) {
        this.hbm2ddlAuto = hbm2ddlAuto;
    }

    /**
     * 数据库编码，默认是utf-8.
     */
    private String characterEncoding = "utf-8";

    /**
     * 获取数据库编码。
     *
     * @return
     */
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * 设置数据库编码。
     *
     * @param characterEncoding
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * 数据库是否使用ssl连接。
     */
    private boolean useSSL;

    @Override
    public boolean isUseSSL() {
        return useSSL;
    }

    @Override
    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    /**
     *
     */
    private boolean useUnicode = true;

    @Override
    public boolean isUseUnicode() {
        return useUnicode;
    }

    @Override
    public void setUseUnicode(boolean useUnicode) {
        this.useUnicode = useUnicode;
    }

    /**
     * 会话事务。
     */
    private String currentSessionContextClass = "jta";

    @Override
    public String getCurrentSessionContextClass() {
        return currentSessionContextClass;
    }

    @Override
    public void setCurrentSessionContextClass(String currentSessionContextClass) {
        this.currentSessionContextClass = currentSessionContextClass;
    }

    /**
     * 缓存模式
     */
    private SharedCacheMode cacheMode = SharedCacheMode.NONE;

    @Override
    public SharedCacheMode getCacheMode() {
        return cacheMode;
    }

    @Override
    public void setCacheMode(SharedCacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }
}
