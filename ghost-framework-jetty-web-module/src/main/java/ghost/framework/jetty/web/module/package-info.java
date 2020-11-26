/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:17 2019/5/24
 */
@ModulePackage(
        type = {
                //模块运行类
                JettyServletWebServerFactory.class
                //配置文件，没有注释@Configuration的配置本类型只做运行不在Bean绑定中
//                JettyWebServerConfiguration.class
        },
        //模块id
        name = "ghost.framework.jetty.web.module"
//        //注释模块配置文件，配置模块本包内资源根目录下的application.properties文件
//        properties = {@ConfigurationProperties}
        //注释依赖包，如果应用未加载此依赖包时加载模块过程会优先加载依赖包列表直到依赖包列表加载完成后才加载模块本身包
//        dependency = {
////                @MavenDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT")
////        },
//        //注释模块依赖模块
//        moduleDependency = {
//                @MavenModuleDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT")
//        }
)//注释模块信息
@MavenModuleDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT")//注释模块依赖包添加到应用中
@MavenApplicationDependency(groupId = "ghost.framework.boot", artifactId = "spring-boot-starter-web", version = "2.2.0.RELEASE")//注释模块依赖包添加到应用中
@Global//注释国际化
@Local//注释本地化
@ConfigurationProperties//注释默认配置
package ghost.framework.jetty.web.module;
import ghost.framework.beans.configuration.annotation.ConfigurationProperties;
import ghost.framework.beans.locale.annotation.Global;
import ghost.framework.beans.locale.annotation.Local;
import ghost.framework.beans.maven.annotation.application.MavenApplicationDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.annotation.module.annotation.ModulePackage;