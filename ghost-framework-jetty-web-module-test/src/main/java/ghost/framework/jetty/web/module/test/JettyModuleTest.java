package ghost.framework.jetty.web.module.test;

import com.google.gson.internal.JavaVersion;
import ghost.framework.app.core.Application;
import ghost.framework.beans.execute.annotation.Boot;
import ghost.framework.beans.configuration.annotation.ConfigurationProperties;
import ghost.framework.beans.configuration.annotation.ConfigurationPropertiess;
import ghost.framework.beans.execute.LoadingMode;
import ghost.framework.beans.locale.annotation.Global;
import ghost.framework.beans.locale.annotation.Local;
import ghost.framework.beans.maven.annotation.*;
import ghost.framework.beans.maven.annotation.application.MavenApplicationDependency;
import ghost.framework.beans.maven.annotation.application.MavenApplicationDependencys;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependencys;
import ghost.framework.core.application.IApplication;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 17:04 2019/5/24
 */
// 0
@ConfigurationPropertiess({@ConfigurationProperties, @ConfigurationProperties(path = "HikariDataSource.yml")})
//1 注释依赖maven仓库
@MavenDepositorys({@MavenDepository(id = "central", url = "https://maven.aliyun.com/repository/central")})
@MavenApplicationDependencys({@MavenApplicationDependency(groupId = "com.zaxxer", artifactId = "HikariCP", version = "3.4.2")})
//@ApplicationBeanMavenDependencys(
//        {
//                @ApplicationBeanMavenDependency(
//                        beanDependency = {
//                                @BeanMavenDependency(
//                                        //注释依赖绑定包
//                                        dependency = @MavenDependency(groupId = "com.zaxxer", artifactId = "HikariCP", version = "3.4.1"),
//                                        //注释依赖绑定的类
//                                        value = {"com.zaxxer.hikari.HikariDataSource"}
//                                )
//                        }
//                )
//        }
//        )
//2 注释依赖包
//@MavenDependencys(
//        {
//                @MavenDependency(groupId = "ghost.framework.boot", artifactId = "spring-boot-starter-web", version = "2.1.6.RELEASE"),
//        }
//)
//3 注释maven绑定数据源
//@DataSourceMavenDependencys({
//        //绑定包
//        @DataSourceMavenDependency(
//                //绑定包信息
//                dependencys = {@MavenDependency(groupId = "com.zaxxer", artifactId = "HikariCP", version = "3.4.1")},
//                //绑定包的类型
//                beanClass = {"com.zaxxer.hikari.HikariDataSource"}
////                beanPropertiesClass = {""}
//                //绑定包类型的配置
//                //beanPropertiesClass = {@SelectProperties(path = "HikariDataSource.yml")}
//        ),
//})
//4 引用模块
//@MavenModuleDependencys({@MavenModuleDependency(artifactId = "ghost.framework.jetty.web.module", version = "1.0-SNAPSHOT")})
@MavenModuleDependencys({@MavenModuleDependency(artifactId = "ghost.framework.undertow.web.module", version = "1.0-SNAPSHOT")})
//@MavenModuleDependencys({@MavenModuleDependency(artifactId = "ghost.framework.netty.web.module", version = "1.0-SNAPSHOT")})
//@MavenModuleDependencys({@MavenModuleDependency(artifactId = "ghost.framework.tomcat.web.module", version = "1.0-SNAPSHOT")})
//启动类注释
@Boot(
        //配置基础配
        baseClass = {config.class},
        //配置基础包
        basePackage = {},
        //配置加载模式
        //annotation为使用指定注释加载，不使用扫描包模式加载
        mode = LoadingMode.annotation
        )
@Global//注释国际化
@Local//注释本地化
@ghost.framework.beans.application.annotation.Application
public class JettyModuleTest {
    private static Logger log = Logger.getLogger(JettyModuleTest.class);
    public static void main(String[] args) throws Exception {
        log.info(String.valueOf(JavaVersion.getMajorJavaVersion()));
        log.info("main");
        IApplication app = Application.run(JettyModuleTest.class, args);
        log.info("main app");
    }
}
//@DefaultDataSource(dependency = @Dependency(groupId = "hikari-cp", artifactId = "hikari-cp", version = "2.9.0"))