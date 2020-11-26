package ghost.framework.data.mybatis.plugin;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.application.IApplicationEnvironment;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
/**
 * package: ghost.framework.data.mybatis.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:19:06
 */
@Configuration
public class MybatisConfig {
    @Application
    @Autowired
    private IApplicationEnvironment environment;
    /**
     * 注入应用数据源地址
     */
//    @Application//注入应用配置
    @Value("ghost.framework.datasource.url")
    private String url;
    /**
     * 注入应用数据源账号
     */
//    @Application//注入应用配置
    @Value("ghost.framework.datasource.username")
    private String username;
    /**
     * 注入应用数据源密码
     */
//    @Application//注入应用配置
    @Value("ghost.framework.datasource.password")
    private String password;
    /**
     * 数据源驱动
     */
    @Value("ghost.framework.datasource.driver-class-name")
    private String driver;
    /**
     * 本地声明数据源，在插件卸载时可以使用比对数据源
     */
    private PooledDataSource dataSource;
    private SqlSessionFactory sessionFactory;

    @Bean
    public SqlSessionFactory sessionFactory() {
        if (sessionFactory == null) {
            dataSource = new PooledDataSource(driver, url, username, password);
            //创建事务
            /*
             * <transactionManager type="JDBC" />
             */
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSource);
            org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(environment);
            //加入资源
            /*
             * <mapper resource="ssm/BlogMapper.xml"/>
             */
//            configuration.addMapper(UserMapper.class);
            sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            System.out.println(sessionFactory);
        }
        return sessionFactory;
    }
//    private SqlSession createSession(DataSource dataSource) {
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(environment);
//        configuration.addMapper(PersonMapper.class);
//        configuration.addMapper(TestMapper.class);
//        configuration.addMapper(OrderMapper.class);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
//        return sqlSessionFactory.openSession();
//    }
//    public static SqlSessionFactory getSqlSessionFactory(ConnectionParam connParam) {
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, connParam.dataSource);
//        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(DbObjectMapper.class);
//        SqlSessionFactory sqlSession = new SqlSessionFactoryBuilder().build(configuration);
//        return sqlSession;
//    }
//    public void setup() throws Exception
//    {
//        try ( InputStream stream = Resources.getResource("test-mybatis.xml").openStream() )
//        {
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
//            Configuration mybatisConfiguration = sqlSessionFactory.getConfiguration();
//            mybatisConfiguration.addMapper(AttributeEntityMapper.class);
//            session = sqlSessionFactory.openSession(true);
//            AttributeEntityMapper mapper = session.getMapper(AttributeEntityMapper.class);
//            mapper.createTable();
//            dynamicAttributes = new SqlDynamicAttributes(session, Collections.singletonList("test"));
//            dynamicAttributes.start();
//        }
//    }
//
//    public void run(T configuration, Environment environment) throws Exception
//    {
//        SqlConfiguration sqlConfiguration = ComposedConfigurationAccessor.access(configuration, environment, SqlConfiguration.class);
//        try
//        {
//            try ( InputStream stream = Resources.getResource(sqlConfiguration.getMybatisConfigUrl()).openStream() )
//            {
//                SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
//                Configuration mybatisConfiguration = sqlSessionFactory.getConfiguration();
//                mybatisConfiguration.addMapper(AttributeEntityMapper.class);
//                final SqlSession session = sqlSessionFactory.openSession(true);
//                SoaBundle.getFeatures(environment).putNamed(session, SqlSession.class, sqlConfiguration.getName());
//                Managed managed = new Managed()
//                {
//                    @Override
//                    public void start() throws Exception
//                    {
//                    }
//                    @Override
//                    public void stop() throws Exception
//                    {
//                        session.close();
//                    }
//                };
//                environment.lifecycle().manage(managed);
//            }
//        }
//        catch ( Exception e )
//        {
//            log.error("Could not initialize MyBatis", e);
//            throw new RuntimeException(e);
//        }
//    }
}