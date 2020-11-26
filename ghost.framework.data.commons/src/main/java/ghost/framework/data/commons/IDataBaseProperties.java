package ghost.framework.data.commons;

import javax.persistence.SharedCacheMode;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库配置接口。
 * @Date: 22:51 2018-08-31
 */
public interface IDataBaseProperties extends IGetDataBaseCacheProperties {
    SharedCacheMode getCacheMode();

    void setCacheMode(SharedCacheMode cacheMode);

    String getUrl();

    void setUrl(String url);

    /**
     * 获取数据库Id。
     *
     * @return
     */
    String getId();

    /**
     * 设置数据库Id。
     *
     * @param id
     */
    void setId(String id);

    /**
     * 获取数据库名称。
     *
     * @return
     */
    String getDataBaseName();

    /**
     * 设置数据库名称。
     *
     * @param dataBaseName
     */
    void setDataBaseName(String dataBaseName);

    String getProviderClass();

    /**
     * 获取开启查询缓存。
     *
     * @return
     */
    boolean isUseQueryCache();

    /**
     * 获取开启缓存。
     *
     * @return
     */
    boolean isUseSecondLevelCache();

    void setProviderClass(String providerClass);

    /**
     * 设置开启查询缓存。
     *
     * @param useQueryCache
     */
    void setUseQueryCache(boolean useQueryCache);

    /**
     * 设置开启缓存。
     *
     * @param useSecondLevelCache
     */
    void setUseSecondLevelCache(boolean useSecondLevelCache);

    String getFactoryClass();

    void setFactoryClass(String factoryClass);

    /**
     * 获取管理密码。
     *
     * @return
     */
    String getUsername();

    /**
     * 设置管理密码。
     *
     * @param username
     */
    void setUsername(String username);

    /**
     * 获取管理密码。
     *
     * @return
     */
    String getPassword();

    /**
     * 设置管理密码。
     *
     * @param password
     */
    void setPassword(String password);

    /**
     * 获取数据库使用的驱动类。
     *
     * @return
     */
    String getDriver();

    /**
     * 设置数据库使用的驱动类。
     *
     * @param driver
     */
    void setDriver(String driver);

    /**
     * 设置数据库使用的方言。
     *
     * @param dialect
     */
    void setDialect(String dialect);

    /**
     * 获取数据库使用的方言。
     *
     * @return
     */
    String getDialect();

    /**
     * 获取IP地址。
     *
     * @return
     */
    String getIp();

    /**
     * 设置IP地址。
     *
     * @param ip
     */
    void setIp(String ip);


    /**
     * 获取端口。
     *
     * @return
     */
    int getPort();

    /**
     * 设置端口。
     *
     * @param port
     */
    void setPort(int port);

    /**
     * 获取是否打印SQL语句。
     *
     * @return
     */
    boolean isShowSql();

    /**
     * 设置是否打印SQL语句。
     *
     * @param showSql
     */
    void setShowSql(boolean showSql);

    /**
     * 获取连接池大小。
     *
     * @return
     */
    int getPoolSize();

    /**
     * 设置连接池大小。
     *
     * @param poolSize
     */
    void setPoolSize(int poolSize);


    /**
     * 获取在 log 和 console 中打印出更漂亮的 SQL。
     *
     * @return
     */
    boolean isFormatSql();

    /**
     * 设置在 log 和 console 中打印出更漂亮的 SQL。
     *
     * @param formatSql
     */
    void setFormatSql(boolean formatSql);

    /**
     * 获取如果开启，Hibernate 将收集有助于性能调节的统计数据。
     *
     * @return
     */
    boolean isGenerateStatistics();

    /**
     * 设置如果开启，Hibernate 将收集有助于性能调节的统计数据。
     *
     * @param generateStatistics
     */
    void setGenerateStatistics(boolean generateStatistics);

    /**
     * 获取如果开启，在对象被删除时生成的标识属性将被重设为默认值。
     *
     * @return
     */
    boolean isUseIdentifierRollback();

    /**
     * 设置如果开启，在对象被删除时生成的标识属性将被重设为默认值。
     *
     * @param useIdentifierRollback
     */
    void setUseIdentifierRollback(boolean useIdentifierRollback);

    /**
     * 获取如果开启，Hibernate 将在 SQL 中生成有助于调试的注释信息，默认值为 false。
     *
     * @return
     */
    boolean isUseSqlComments();

    /**
     * 设置如果开启，Hibernate 将在 SQL 中生成有助于调试的注释信息，默认值为 false。
     *
     * @param useSqlComments
     */
    void setUseSqlComments(boolean useSqlComments);

    /**
     * 获取强制 Hibernate 按照被更新数据的主键，为SQL 更新排序。这么做将减少在高并发系统中事务的死锁。
     *
     * @return
     */
    boolean isOrderUpdates();

    /**
     * 设置强制 Hibernate 按照被更新数据的主键，为SQL 更新排序。这么做将减少在高并发系统中事务的死锁。
     *
     * @param orderUpdates
     */
    void setOrderUpdates(boolean orderUpdates);

    /**
     * 获取为由这个 SessionFactory 打开的所有 Session指定默认的实体表现模式。
     *
     * @return
     */
    String getDefaultEntityMode();

    /**
     * 设置为由这个 SessionFactory 打开的所有 Session指定默认的实体表现模式。
     *
     * @param defaultEntityMode
     */
    void setDefaultEntityMode(String defaultEntityMode);

    /**
     * 获取为 Hibernate 关联的批量抓取设置默认数量。
     *
     * @return
     */
    int getDefaultBatchFetchSize();

    /**
     * 设置为 Hibernate 关联的批量抓取设置默认数量。
     *
     * @param defaultBatchFetchSize
     */
    void setDefaultBatchFetchSize(int defaultBatchFetchSize);

    /**
     * 获取为单向关联（一对一，多对一）的外连接抓取（outer join fetch）树设置最大深度。
     *
     * @return
     */
    int getMaxFetchDepth();

    /**
     * 设置为单向关联（一对一，多对一）的外连接抓取（outer join fetch）树设置最大深度。
     *
     * @param maxFetchDepth
     */
    void setMaxFetchDepth(int maxFetchDepth);

    /**
     * 获取org.hibernate.SessionFactory 创建后，将自动使用这个名字绑定到 JNDI 中。
     *
     * @return
     */
    String getSessionFactoryName();

    /**
     * 设置org.hibernate.SessionFactory 创建后，将自动使用这个名字绑定到 JNDI 中。
     *
     * @param sessionFactoryName
     */
    void setSessionFactoryName(String sessionFactoryName);

    /**
     * 获取在生成的 SQL 中，将给定的 catalog 附加于非全限定名的表名上。
     *
     * @return
     */
    String getDefaultCatalog();

    /**
     * 设置在生成的 SQL 中，将给定的 catalog 附加于非全限定名的表名上。
     *
     * @param defaultCatalog
     */
    void setDefaultCatalog(String defaultCatalog);

    /**
     * 获取在启动和停止时自动地创建，更新或删除数据库模式。
     *
     * @return
     */
    String getHbm2ddlAuto();

    /**
     * 设置在启动和停止时自动地创建，更新或删除数据库模式。
     *
     * @param hbm2ddlAuto
     */
    void setHbm2ddlAuto(String hbm2ddlAuto);

    /**
     * 获取数据库编码。
     *
     * @return
     */
    default String getCharacterEncoding() {
        return "UTF-8";
    }

    /**
     * 设置数据库编码。
     *
     * @param characterEncoding
     */
    void setCharacterEncoding(String characterEncoding);

    boolean isUseSSL();

    void setUseSSL(boolean useSSL);

    boolean isUseUnicode();

    void setUseUnicode(boolean useUnicode);


    String getCurrentSessionContextClass();

    void setCurrentSessionContextClass(String currentSessionContextClass);
}