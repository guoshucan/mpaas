package ghost.framework.data;
import ghost.framework.data.core.util.DataConstant;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.DataException;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.List;

/**
 * Hibernate数据模块。
 */
public final class DataHibernateModule {
    private DataBaseController controller = null;
    private DataBase dataBase;
    private BootstrapServiceRegistryBuilder bootstrap;
    private List<Class> entityList;

    /**
     * 使用单独数据库配置初始化
     *
     * @param entityList 实体列表
     * @param dataBase   数据库
     * @throws DataException
     */
    public DataHibernateModule(
            List<Class> entityList,
            DataBase dataBase) throws DataException {
        this.entityList = entityList;
        this.dataBase = dataBase;
        this.bootstrap = new BootstrapServiceRegistryBuilder();
        this.init();
    }

    /**
     * 使用数据库控制器初始化
     *
     * @param controller 数据库控制器
     * @param dataBase   数据库
     * @throws DataException
     */
    public DataHibernateModule(
            DataBaseController controller,
            DataBase dataBase) throws DataException {
        this.controller = controller;
        this.bootstrap = this.controller.bootstrap;
        this.entityList = this.controller.getEntityList();
        this.dataBase = dataBase;
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        String url;
        if (this.dataBase.getDialect().indexOf(DataConstant.MySQL) > -1) {
            url = "jdbc:mysql://" + this.dataBase.getIp() + ":" + this.dataBase.getPort() + "/" + this.dataBase.getDataBaseName() + "?createDatabaseIfNotExist=true&useUnicode=" + this.dataBase.isUseUnicode() + "&characterEncoding=" + this.dataBase.getCharacterEncoding() + "&useSSL=" + this.dataBase.isUseSSL();
        } else if (this.dataBase.getDialect().indexOf(DataConstant.SQLServer) > -1) {
            url = "jdbc:jtds:sqlserver://" + this.dataBase.getIp() + ":" + this.dataBase.getPort() + "/dbname;DatabaseName=" + this.dataBase.getDataBaseName();
        } else if (this.dataBase.getDialect().indexOf(DataConstant.Oracle) > -1) {
            url = "jdbc:oracle:thin:@" + this.dataBase.getIp() + ":" + this.dataBase.getPort() + ":" + this.dataBase.getDataBaseName();
        } else if (this.dataBase.getDialect().indexOf(DataConstant.SQLite) > -1) {
            url = this.dataBase.getDataBaseName();
            //jdbc:sqlite:连接次数据库必须在数据库名称加上前缀。
        } else if (this.dataBase.getDialect().indexOf(DataConstant.DB2) > -1) {
            url = "jdbc:db2://" + this.dataBase.getIp() + ":" + this.dataBase.getPort() + "/" + this.dataBase.getDataBaseName();
        } else {
            throw new DataException("The dialect was not allowed.==fd==" + this.dataBase.getDialect(), new SQLException());
        }
        this.configuration = new Configuration();
        //添加实体所在包。
        //添加实体类。
        for (Class c : this.entityList)
            this.configuration.addAnnotatedClass(c);
        this.serviceRegistryBuilder = new StandardServiceRegistryBuilder(this.bootstrap.enableAutoClose().build());
        //使用的是本地事务（jdbc事务）
        if (this.dataBase.getCurrentSessionContextClass() != null && !this.dataBase.getCurrentSessionContextClass().isEmpty())
            this.serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", this.dataBase.getCurrentSessionContextClass());
        //使用的是本地事务（jdbc事务）
//        this.serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", "thread");
        //使用的是全局事务（jta事务）
        // this.serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", "jta");
        //
        this.serviceRegistryBuilder.applySetting("hibernate.connection.driver_class", this.dataBase.getDriver());
        if (this.dataBase.getUsername() != null)
            this.serviceRegistryBuilder.applySetting("hibernate.connection.username", this.dataBase.getUsername());
        if (this.dataBase.getPassword() != null)
            this.serviceRegistryBuilder.applySetting("hibernate.connection.password", this.dataBase.getPassword());
        this.serviceRegistryBuilder.applySetting("hibernate.dialect", this.dataBase.getDialect());
        this.serviceRegistryBuilder.applySetting("hibernate.hbm2ddl.auto", this.controller == null ? this.dataBase.getHbm2ddlAuto() : this.controller.getHbm2ddlAuto());
        this.serviceRegistryBuilder.applySetting("hibernate.show_sql", this.controller == null ? this.dataBase.isShowSql() : this.controller.isShowSql());
        this.serviceRegistryBuilder.applySetting("hibernate.connection.url", url);
        //
        this.serviceRegistryBuilder.applySetting("hibernate.format_sql", this.dataBase.isFormatSql());
        this.serviceRegistryBuilder.applySetting("hibernate.use_sql_comments", this.dataBase.isUseSqlComments());
        //设置缓存。
        //this.serviceRegistryBuilder.applySetting("javax.persistence.sharedCache.mode","ENABLE_SELECTIVE");
        this.serviceRegistryBuilder.applySetting("hibernate.generate_statistics", this.dataBase.isGenerateStatistics());
        this.serviceRegistryBuilder.applySetting("hibernate.cache.use_second_level_cache", this.dataBase.isUseSecondLevelCache());
        this.serviceRegistryBuilder.applySetting("hibernate.cache.use_query_cache", this.dataBase.isUseQueryCache());
        if (this.dataBase.getProviderClass() != null)
            this.serviceRegistryBuilder.applySetting("hibernate.cache.provider_class", this.dataBase.getProviderClass());
        if (this.dataBase.getFactoryClass() != null)
            this.serviceRegistryBuilder.applySetting("hibernate.cache.region.factory_class", this.dataBase.getFactoryClass());
        //
        this.serviceRegistry = serviceRegistryBuilder.build();
        this.sessionFactory = this.configuration.buildSessionFactory(this.serviceRegistry);
        this.configuration.setSharedCacheMode(this.controller == null ? this.dataBase.getCacheMode() : this.controller.getCacheMode());
    }

    //
    private ServiceRegistry serviceRegistry;
    private StandardServiceRegistryBuilder serviceRegistryBuilder;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public Configuration getConfiguration() {
        return configuration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}