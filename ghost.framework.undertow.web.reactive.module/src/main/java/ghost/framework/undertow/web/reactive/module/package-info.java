/**
 * package: ghost.framework.undertow.web.reactive.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/13:18:16
 */
@ModulePackage(
        type = {
//                DefaultClassWebFilterAnnotationEventFactory.class,
                //模块运行类
                UndertowReactiveWebServerFactory.class
                //配置文件，没有注释@Configuration的配置本类型只做运行不在Bean绑定中
//                JettyWebServerConfiguration.class
        }
)//注释模块信息
@MavenModuleDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT")//注释模块依赖包添加到应用中
@Global//注释国际化
@Local//注释本地化
@ConfigurationProperties//注释默认配置
package ghost.framework.undertow.web.reactive.module;

import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.locale.annotation.Global;
import ghost.framework.beans.locale.annotation.Local;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;