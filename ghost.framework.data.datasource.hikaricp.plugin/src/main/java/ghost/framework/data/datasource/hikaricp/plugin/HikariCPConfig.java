package ghost.framework.data.datasource.hikaricp.plugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ghost.framework.beans.annotation.Primary;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.application.IApplication;
/**
 * package: ghost.framework.data.datasource.hikaricp.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:HikariCP连接配置
 * @Date: 2020/3/12:21:19
 */
@Configuration
public class HikariCPConfig {
    /**
     * 注入应用
     */
//    @Application
    @Autowired
    private IApplication app;
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
    private HikariDataSource dataSource;

    /**
     * 获取数据源
     *
     * @return
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }

    /**
     * 绑定数据源
     *
     * @return
     */
//    @Application//绑定入应用容器
    @Primary
    @Bean
    public synchronized HikariDataSource dataSource() {
        if (this.dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName(driver);
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.dataSource = new HikariDataSource(config);
        }
        return this.dataSource;
    }
}