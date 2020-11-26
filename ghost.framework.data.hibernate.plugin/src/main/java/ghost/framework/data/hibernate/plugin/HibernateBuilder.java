package ghost.framework.data.hibernate.plugin;

import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.configuration.ConfigurationClassProperties;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.data.commons.DataBaseProperties;
import ghost.framework.data.commons.IDataBaseProperties;
import ghost.framework.data.hibernate.*;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import java.util.ArrayList;
import java.util.List;
/**
 * package: ghost.framework.data.hibernate.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:19:16
 */
@ConfigurationClassProperties(value = DataBaseProperties.class, prefix = "ghost.framework.datasource")
@Configuration()
public class HibernateBuilder implements IHibernateBuilder {
    private BootstrapServiceRegistryBuilder bootstrap;
    //    private ServiceRegistry serviceRegistry;
    private StandardServiceRegistryBuilder serviceRegistryBuilder;
    //    private BootstrapServiceRegistry serviceRegistry;
//    private org.hibernate.cfg.Configuration configuration;
    private SqlSessionFactory sessionFactory;
    /**
     * 注入全局拦截器
     */
    @Autowired
    private HibernateGlobalEmptyInterceptor globalEmptyInterceptor;
    private HibernateDeleteEventListener deleteEventListener = new HibernateDeleteEventListener();
    private HibernatePostDeleteEventListener postDeleteEventListener = new HibernatePostDeleteEventListener();
    private HibernatePreDeleteEventListener preDeleteEventListener = new HibernatePreDeleteEventListener();

    /**
     * 删除拦截器
     * {@link IHibernateInterceptor}
     *
     * @param interceptor
     */
    @Override
    public void removeInterceptor(IHibernateInterceptor interceptor) {
        this.globalEmptyInterceptor.remove(interceptor);
    }

    /**
     * 添加拦截器
     * {@link IHibernateInterceptor}
     *
     * @param interceptor
     */
    @Override
    public void addInterceptor(IHibernateInterceptor interceptor) {
        this.globalEmptyInterceptor.add(interceptor);
    }

    /**
     * 实体类型列表
     */
    private List<Class<?>> entityList = new ArrayList<>();

    /**
     * 删除实体
     *
     * @param entity 实体类型
     */
    @Override
    public void remove(Class<?> entity) {
        synchronized (entityList) {
            entityList.remove(entity);
        }
    }

    /**
     * 删除实体
     *
     * @param entitys 实体类型
     */
    @Override
    public void remove(Class<?>[] entitys) {
        synchronized (entityList) {
            for (Class<?> entity : entitys) {
                entityList.remove(entity);
            }
        }
    }

    /**
     * 添加实体
     *
     * @param entity 实体类型
     */
    @Override
    public void add(Class<?> entity) {
        synchronized (entityList) {
            if (!entityList.contains(entity)) {
                entityList.add(entity);
            }
        }
    }

    /**
     * 添加实体
     *
     * @param entitys 实体类型
     */
    @Override
    public void add(Class<?>[] entitys) {
        synchronized (entityList) {
            for (Class<?> entity : entitys) {
                if (!entityList.contains(entity)) {
                    entityList.add(entity);
                }
            }
        }
    }

    /**
     * 获取实体列表
     *
     * @return
     */
    @Override
    public List<Class<?>> getEntityList() {
        return entityList;
    }

    /**
     * 由DataBaseProperties类型配置完成后注入接口
     */
    @Autowired
    private IDataBaseProperties dataBaseProperties;

    /**
     * 获取数据库配置
     *
     * @return
     */
    @Override
    public IDataBaseProperties getDataBaseProperties() {
        return dataBaseProperties;
    }

    /**
     * 设置数据库配置
     *
     * @param dataBaseProperties
     */
    @Override
    public void setDataBaseProperties(IDataBaseProperties dataBaseProperties) {
        this.dataBaseProperties = dataBaseProperties;
    }

    /**
     * 注入类加载器接口
     */
    @Autowired
    private IClassLoader classLoader;

    /**
     * @return
     */
    @Order
    @Bean
    public BootstrapServiceRegistryBuilder newBootstrapServiceRegistryBuilder() {
        if (bootstrap == null) {
            bootstrap = new BootstrapServiceRegistryBuilder();
        }
        //设置类加载器
        bootstrap.applyClassLoader((ClassLoader) this.classLoader);
//        this.serviceRegistry = bootstrap.build();
        return bootstrap;
    }

//    /**
//     * @return
//     */
//    @Order(2)
//    @Bean
//    public org.hibernate.cfg.Configuration newConfiguration() {
//        if (configuration == null) {
//            configuration = new org.hibernate.cfg.Configuration(serviceRegistryBuilder.getBootstrapServiceRegistry());
//        }
//        return configuration;
//    }

    /**
     * @return
     */
    @Order(1)
    @Bean
    public StandardServiceRegistryBuilder newStandardServiceRegistryBuilder() {
        if (serviceRegistryBuilder == null) {
            this.serviceRegistryBuilder = new StandardServiceRegistryBuilder(this.bootstrap.build());
        }
        return serviceRegistryBuilder;
    }

    /**
     * 初始化会话工厂
     *
     * @return
     */
    @Order(3)
    @Bean
    public ISessionFactory init() {
        try {
            //判断是否未初始化会话工厂
            if (this.sessionFactory == null) {
//            String url = "";
//            this.bootstrap = new BootstrapServiceRegistryBuilder();
//            //设置类加载器
//            this.bootstrap.applyClassLoader((ClassLoader) this.classLoader);
//            this.configuration = new org.hibernate.cfg.Configuration();
//            for (Class<?> c : entityList) {
//                this.configuration.addClass(c);
//            }
//            this.serviceRegistryBuilder = new StandardServiceRegistryBuilder(this.bootstrap.enableAutoClose().build());
                //使用的是本地事务（jdbc事务）
                if (this.dataBaseProperties.getCurrentSessionContextClass() != null && !this.dataBaseProperties.getCurrentSessionContextClass().isEmpty()) {
                    this.serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", this.dataBaseProperties.getCurrentSessionContextClass());
                }
                //使用的是本地事务（jdbc事务）
//        this.serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", "thread");
                //使用的是全局事务（jta事务）
                // this.serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", "jta");
                //
                this.serviceRegistryBuilder.applySetting("hibernate.connection.driver_class", this.dataBaseProperties.getDriver());
                if (this.dataBaseProperties.getUsername() != null)
                    this.serviceRegistryBuilder.applySetting("hibernate.connection.username", this.dataBaseProperties.getUsername());
                if (this.dataBaseProperties.getPassword() != null)
                    this.serviceRegistryBuilder.applySetting("hibernate.connection.password", this.dataBaseProperties.getPassword());
                this.serviceRegistryBuilder.applySetting("hibernate.connection.min_pool_size", 0);
                this.serviceRegistryBuilder.applySetting("hibernate.dialect", this.dataBaseProperties.getDialect());
                this.serviceRegistryBuilder.applySetting("hibernate.hbm2ddl.auto", this.dataBaseProperties.getHbm2ddlAuto());
                this.serviceRegistryBuilder.applySetting("hibernate.show_sql", this.dataBaseProperties.isShowSql());
                this.serviceRegistryBuilder.applySetting("hibernate.connection.url", this.dataBaseProperties.getUrl());
                //
                this.serviceRegistryBuilder.applySetting("hibernate.format_sql", this.dataBaseProperties.isFormatSql());
                this.serviceRegistryBuilder.applySetting("hibernate.use_sql_comments", this.dataBaseProperties.isUseSqlComments());
                //设置缓存。
                //this.serviceRegistryBuilder.applySetting("javax.persistence.sharedCache.mode","ENABLE_SELECTIVE");
                this.serviceRegistryBuilder.applySetting("hibernate.generate_statistics", this.dataBaseProperties.isGenerateStatistics());
                this.serviceRegistryBuilder.applySetting("hibernate.cache.use_second_level_cache", this.dataBaseProperties.isUseSecondLevelCache());
                this.serviceRegistryBuilder.applySetting("hibernate.cache.use_query_cache", this.dataBaseProperties.isUseQueryCache());
                if (this.dataBaseProperties.getProviderClass() != null)
                    this.serviceRegistryBuilder.applySetting("hibernate.cache.provider_class", this.dataBaseProperties.getProviderClass());
                if (this.dataBaseProperties.getFactoryClass() != null)
                    this.serviceRegistryBuilder.applySetting("hibernate.cache.region.factory_class", this.dataBaseProperties.getFactoryClass());
                //
                StandardServiceRegistry serviceRegistry = serviceRegistryBuilder.enableAutoClose().build();
//                EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
//                eventListenerRegistry.appendListeners(EventType.POST_DELETE, new HibernatePostDeleteEventListener());
//                eventListenerRegistry.appendListeners(EventType.DELETE, new HibernateDeleteEventListener());
//                eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new HibernatePreDeleteEventListener());
                //构建设置
                org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
                //设置全局拦截器
//                configuration.setInterceptor(this.globalEmptyInterceptor);
                //从新包装会话
                this.sessionFactory = new SqlSessionFactory(configuration.buildSessionFactory(serviceRegistry));
//                EventListenerRegistry eventListenerRegistry = this.sessionFactory.getSessionFactory().getServiceRegistry().getService(EventListenerRegistry.class);
//                eventListenerRegistry.appendListeners(EventType.POST_DELETE, new HibernatePostDeleteEventListener());
//                eventListenerRegistry.appendListeners(EventType.DELETE, new HibernateDeleteEventListener());
//                eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new HibernatePreDeleteEventListener());

//                this.sessionFactory.getSessionFactory().withOptions().interceptor(this.globalEmptyInterceptor);
                //设置缓冲模式
                configuration.setSharedCacheMode(this.dataBaseProperties.getCacheMode());
            }
            return this.sessionFactory;
        } catch (Exception e) {
            throw new HibernateException(e.getMessage(), e);
        }
    }

    /**
     * 重建数据源
     */
    @Override
    public synchronized void rebuild() {
        try {
            //判断旧的会话工厂是否存在
            if (this.sessionFactory.getOld() != null) {
                //获取连接池
                DriverManagerConnectionProviderImpl.PooledConnections connections = HibernateUtils.getPool(this.sessionFactory.getOld());
                //循环等待旧的连接全部断开才继续
//                while (connections.size() > 0) {
//                    System.out.println(connections.size());
//                    //延迟循环等待连接数全部完成才继续
//                    Thread.sleep(5000);
//                    connections.close();
//                }
                System.out.println(connections.size());
                //关闭资源
                this.sessionFactory.getOld().getSessionFactoryOptions().getServiceRegistry().close();
            }
//            //获取历史实体列表对象
//            final MetadataSources metadataSources = ReflectUtil.findField(configuration, "metadataSources");
//            //清理历史实体列表
//            metadataSources.getAnnotatedClasses().clear();
            //
            StandardServiceRegistry serviceRegistry = serviceRegistryBuilder.enableAutoClose().build();
//            EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
//            eventListenerRegistry.appendListeners(EventType.POST_DELETE, new HibernatePostDeleteEventListener());
//            eventListenerRegistry.appendListeners(EventType.DELETE, new HibernateDeleteEventListener());
//            eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new HibernatePreDeleteEventListener());
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
//            configuration.setInterceptor(this.globalEmptyInterceptor);
            for (Class<?> c : entityList) {
                configuration.addAnnotatedClass(c);
            }
            //从新包装会话
            this.sessionFactory.setCurrent(configuration.buildSessionFactory(serviceRegistry));
//            this.sessionFactory.getSessionFactory().withOptions().interceptor(this.globalEmptyInterceptor);
//            EventListenerRegistry eventListenerRegistry = this.sessionFactory.getSessionFactory().getServiceRegistry().getService(EventListenerRegistry.class);
//            eventListenerRegistry.appendListeners(EventType.POST_DELETE, new HibernatePostDeleteEventListener());
//            eventListenerRegistry.appendListeners(EventType.DELETE, new HibernateDeleteEventListener());
//            eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new HibernatePreDeleteEventListener());
            //设置缓冲模式
            configuration.setSharedCacheMode(this.dataBaseProperties.getCacheMode());
        } catch (Exception e) {
            throw new HibernateException(e.getMessage(), e);
        }
    }
}