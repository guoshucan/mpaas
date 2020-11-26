package ghost.framework.platform;

import ghost.framework.application.core.Application;
import ghost.framework.beans.annotation.application.ApplicationDependency;
import ghost.framework.beans.annotation.application.ApplicationDependencys;
import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.beans.annotation.stereotype.ApplicationBoot;
import ghost.framework.beans.execute.LoadingMode;
import ghost.framework.beans.maven.annotation.MavenDepository;
import ghost.framework.beans.maven.annotation.MavenDepositorys;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependencys;
import ghost.framework.context.application.IApplication;
import org.apache.log4j.Logger;

/**
 * package: ghost.framework.platform
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 12:46 2020/1/31
 */
// 0
//@ConfigurationPropertiess({@ConfigurationProperties, @ConfigurationProperties(path = "HikariDataSource.yml")})
//1 注释依赖maven仓库
@MavenDepositorys({
//        @MavenDepository(id = "maven-snapshots", type = "default", username = "admin", password = "123456", url = "http://www.xxx.com:8081/repository/maven-snapshots/"),
        @MavenDepository(id = "default", type = "default", username = "admin", password = "123456", url = "http://www.xxx.com:8081/repository/maven-public/"),
//        @MavenDepository(id = "default", url = "https://maven.aliyun.com/repository/central"),
//        @MavenDepository(id = "default", url = "http://maven.aliyun.com/nexus/content/groups/public/")
})
//@ApplicationDependencys({@ApplicationDependency(groupId = "com.zaxxer", artifactId = "HikariCP", version = "3.4.2")})
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
@ApplicationDependencys(
        {
                @ApplicationDependency(groupId = "mysql", artifactId = "mysql-connector-java", version = "8.0.19"),
                @ApplicationDependency(groupId = "com.jcraft", artifactId = "jsch", version = "0.1.55"),
        }
)
@I18n//注释国际化
@L10n//注释本地化
//@PluginDependency(artifactId = "ghost.framework.packageclassresolve.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.maven.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.data.datasource.hikaricp.plugin", version = "1.0-SNAPSHOT")//应用hikaricp数据源插件
@PluginDependency(artifactId = "ghost.framework.data.jdbc.template.plugin", version = "1.0-SNAPSHOT")//数据源模板插件
@PluginDependency(artifactId = "ghost.framework.jsr250.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.jsr303.valid.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.jsr310.converter.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.data.hibernate.plugin", version = "1.0-SNAPSHOT"/*,
        classProperties = {@ConfigurationClassProperties(value = DataBaseProperties.class, prefix = "ghost.framework.datasource")}*/)
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
@MavenModuleDependencys({
        @MavenModuleDependency(
                artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT",
                plugin = {
                        @PluginDependency(artifactId = "ghost.framework.web.webssh.resources.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.socket.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.jquery.resources.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.session.data.jdbc.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.session.data.redis.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.container.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.codemirror.resources.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.search.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.test1.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.test2.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.settings.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.locale.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.logo.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.user.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.message.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.header.fullscreen.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.picture.drag.verify.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.test.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.error.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.i18n.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.setting.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.admin.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.login.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.module.manage.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.nginx.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.maintenance.manage.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.resource.container.manage.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.range.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.ssh.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.script.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.top.message.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.deploy.manage.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.cerebro.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.angular1x.monitor.manage.plugin", version = "1.0-SNAPSHOT"),
                        @PluginDependency(artifactId = "ghost.framework.web.socket.test.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.mvc.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.mvc.thymeleaf.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.mvc.thymeleaf.layout.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.mvc.freemarker.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.mvc.nginx.ui.plugin", version = "1.0-SNAPSHOT"),
//                        @PluginDependency(artifactId = "ghost.framework.web.mvc.test.plugin", version = "1.0-SNAPSHOT"),
                }
        ),
//        @MavenModuleDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT"),
        @MavenModuleDependency(
                artifactId = "ghost.framework.undertow.web.module", version = "1.0-SNAPSHOT",
                plugin = {}
        )
})
//@MavenModuleDependencys({@MavenModuleDependency(artifactId = "ghost.framework.netty.web.module", version = "1.0-SNAPSHOT")})
//@MavenModuleDependencys({@MavenModuleDependency(artifactId = "ghost.framework.tomcat.web.module", version = "1.0-SNAPSHOT")})
//引用包
//@MavenDependencys(
//        {@MavenDependency(groupId = "com.zaxxer", artifactId = "HikariCP", version = "3.4.1")}
//        )
//启动类注释
@ApplicationBoot(
        //配置基础配
        baseClass = {},
        //配置基础包
        basePackage = {},
        //配置加载模式
        //annotation为使用指定注释加载，不使用扫描包模式加载
        mode = LoadingMode.annotation
)
//@ConfigurationProperties
//@ghost.framework.beans.annotation.application.Application
public class PlatformApplication {
    private static Logger log = Logger.getLogger(PlatformApplication.class);
    public static void main(String[] args) throws Exception {
        log.info("main");
        log.info(System.getProperty("java.class.path"));
        IApplication app = Application.run(PlatformApplication.class, args);
        log.info("main app");
    }
}
