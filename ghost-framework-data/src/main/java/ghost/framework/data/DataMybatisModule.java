package ghost.framework.data;
//
//import org.apache.ibatis.datasource.pooled.PooledDataSource;
//import org.apache.ibatis.mapping.Environment;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.apache.ibatis.transaction.TransactionFactory;
//import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
//
//import javax.sql.DataSource;
//import java.util.List;
//
///**
// * Mybatis模块。
// */
//public class DataMybatisModule extends DataBaseProperties {
//    //
//    private SqlSessionFactory sqlSessionFactory;
//
//    public SqlSessionFactory getSqlSessionFactory() {
//        return sqlSessionFactory;
//    }
//
//    private TransactionFactory transactionFactory;
//
//    public TransactionFactory getTransactionFactory() {
//        return transactionFactory;
//    }
//
//    private Configuration configuration;
//
//    public Configuration getConfiguration() {
//        return configuration;
//    }
//
//    private Environment environment;
//
//    public Environment getEnvironment() {
//        return environment;
//    }
//
//    /**
//     * 初始化Mybatis模块。
//     * @param properties
//     * @param annotatedClasss
//     * @throws DataException
//     * @throws SecurityException
//     * @throws NoSuchFieldException
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public DataMybatisModule(
//            DataBaseProperties properties,
//            List<Class> annotatedClasss) throws DataException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//        super(properties);
//        String url;
//        if (this.getDialect().indexOf("MySQL") > -1) {
//            url = "jdbc:mysql://" + this.getIp() + ":" + this.getPort() + "/" + this.getDataBaseName();
//            //dataSource = new PooledDataSource("com.mysql.jdbc.Driver", url, username, password);
//        } else if (this.getDialect().indexOf("SQLServer") > -1) {
//            url = "jdbc:sqlserver://" + this.getIp() + ":" + this.getPort() + ";DataBaseName=" + this.getDataBaseName();
//            //dataSource = new PooledDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver", url, username, password);
//        } else if (this.getDialect().indexOf("Oracle") > -1) {
//            url = "jdbc:oracle:thin:@" + this.getIp() + ":" + this.getPort() + ":" + this.getDataBaseName();
//            //dataSource = new PooledDataSource("oracle.jdbc.OracleDriver", url, username, password);
//        } else {
//            throw new DataException("The dialect was not allowed.==fd==" + this.getDialect());
//        }
//        DataSource dataSource = new PooledDataSource(this.getDriverClass(), url, this.getUsername(), this.getPassword());
//        this.transactionFactory = new JdbcTransactionFactory();
//        this.environment = new Environment(this.getId(), transactionFactory, dataSource);
//        this.configuration = new Configuration(environment);
//        for (Class c : annotatedClasss)
//            this.configuration.addMapper(c);
//        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(this.configuration);
//    }
//}