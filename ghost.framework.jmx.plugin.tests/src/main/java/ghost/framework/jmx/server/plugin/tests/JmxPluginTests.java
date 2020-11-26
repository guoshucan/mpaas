package ghost.framework.jmx.server.plugin.tests;
import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.beans.maven.annotation.plugin.MavenPluginDependency;
import ghost.framework.context.application.IApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.jmx.plugin.tests
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/26:15:25
 */
@I18n//注释国际化
@L10n//注释本地化
@MavenPluginDependency(artifactId = "ghost.framework.jmx.server.plugin", version = "1.0-SNAPSHOT")
@MavenPluginDependency(artifactId = "ghost.framework.jmx.client.plugin", version = "1.0-SNAPSHOT")
public class JmxPluginTests {
    private static Log log = LogFactory.getLog(JmxPluginTests.class);

    public static void main(String[] args) throws Exception {
        log.info("JmxPluginTests main");
        log.info(System.getProperty("java.class.path"));
        IApplication app = ghost.framework.application.core.Application.run(JmxPluginTests.class, args);
        log.info("JmxPluginTests main app");
    }
}