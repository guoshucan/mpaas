package ghost.framework.jetty.web.module.test;

import ghost.framework.beans.annotation.Autowired;
import ghost.framework.beans.annotation.Bean;
import ghost.framework.beans.annotation.Value;
import ghost.framework.beans.application.annotation.Application;
import ghost.framework.beans.enums.Action;
import ghost.framework.beans.execute.annotation.BeanAction;
import ghost.framework.beans.annotation.module.annotation.Module;
import ghost.framework.core.application.IApplication;
import ghost.framework.core.module.IModule;

/**
 * package: ghost.framework.jetty.web.module.test
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:11:11
 */
//@Configuration(
//        properties = {@ConfigurationProperties},
//        classProperties = {@ClassProperties(prefix = "ghost.framework.datasource", type = DataSourceProperties.class)}
//        )
@BeanAction(Action.Before)
@Application
public class config {
    @Application
    @Value(value = "ghost.framework.datasource.username")
    private String username;
    @Application
    @Value(value = "ghost.framework.datasource.password")
    private String password;
    @Autowired
    @Module
    private IModule module;

    @Bean
    private Object newObj(@Application @Autowired IApplication app, @Application @Value(value = "ghost.framework.datasource.password") String pass){
        System.out.println(app.toString());
        return new Object();
    }
//    @Bean
//    private HikariDataSource hikariConfig(@Autowired DataSourceProperties properties) {
//        log.info(this.getClass().getName());
//        HikariConfig jdbcConfig = new HikariConfig();
//        jdbcConfig.setPoolName(getClass().getName());
////        jdbcConfig.setDriverClassName(driverClassName);
////        jdbcConfig.setJdbcUrl(url);
////        jdbcConfig.setUsername(username);
////        jdbcConfig.setPassword(password);
////        jdbcConfig.setMaximumPoolSize(maximumPoolSize);
////        jdbcConfig.setMaxLifetime(maxLifetime);
////        jdbcConfig.setConnectionTimeout(connectionTimeout);
////        jdbcConfig.setIdleTimeout(idleTimeout);
//
//        return new HikariDataSource(jdbcConfig);
//    }
     private Log log = LogFactory.getLog(config.class);
}