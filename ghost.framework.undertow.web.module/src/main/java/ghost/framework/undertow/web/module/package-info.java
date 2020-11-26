/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:22 2019/11/25
 */
@ModulePackage(
        depend = {
                //模块运行类
                UndertowServletWebServerFactory.class
        }
)//注释模块信息
@MavenModuleDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT")//注释模块依赖包添加到应用中
@I18n//注释国际化
@L10n//注释本地化
@ConfigurationProperties//注释默认配置
package ghost.framework.undertow.web.module;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.annotation.module.ModulePackage;
