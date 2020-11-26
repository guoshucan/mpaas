package ghost.framework.data.jdbc.jpa.plugin.test;

import ghost.framework.application.core.Application;
import ghost.framework.beans.maven.annotation.MavenDepository;
import ghost.framework.beans.maven.annotation.MavenDepositorys;
import ghost.framework.beans.annotation.application.ApplicationDependency;
import ghost.framework.beans.annotation.application.ApplicationDependencys;
import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.context.application.IApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.data.jdbc.jpa.plugin.test
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/7:10:03
 */
//1 注释依赖maven仓库
@MavenDepositorys({
//        @MavenDepository(id = "maven-snapshots", type = "default", username = "admin", password = "123456", url = "http://www.xxx.com:8081/repository/maven-snapshots/"),
        @MavenDepository(id = "default", type = "default", username = "admin", password = "123456", url = "http://www.xxx.com:8081/repository/maven-public/"),
//        @MavenDepository(id = "default", url = "https://maven.aliyun.com/repository/central"),
//        @MavenDepository(id = "default", url = "http://maven.aliyun.com/nexus/content/groups/public/")
})
//2 注释依赖包
@ApplicationDependencys(
        {
                @ApplicationDependency(groupId = "mysql", artifactId = "mysql-connector-java", version = "8.0.19"),
        }
)
//注释插件
@PluginDependency(artifactId = "ghost.framework.data.datasource.hikaricp.plugin", version = "1.0-SNAPSHOT")//应用hikaricp数据源插件
@PluginDependency(artifactId = "ghost.framework.data.jdbc.template.plugin", version = "1.0-SNAPSHOT")//数据源模板插件
@PluginDependency(artifactId = "ghost.framework.jsr250.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.jsr303.valid.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.jsr310.converter.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.data.orm.eclipse.jpa.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.data.orm.jpa.plugin", version = "1.0-SNAPSHOT")
@PluginDependency(artifactId = "ghost.framework.data.jdbc.jpa.plugin", version = "1.0-SNAPSHOT")
public class DataJdbJpaPluginTest {
    private static Log log = LogFactory.getLog(DataJdbJpaPluginTest.class);

    public static void main(String[] args) throws Exception {
        log.info("main");
        log.info(System.getProperty("java.class.path"));
        IApplication app = Application.run(DataJdbJpaPluginTest.class, args);
        log.info("main app");
    }
}
