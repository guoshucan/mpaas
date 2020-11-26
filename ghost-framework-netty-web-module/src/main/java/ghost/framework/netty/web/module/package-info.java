/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:28 2019/11/25
 */
@ModulePackage(
        type = {
                //模块运行类
                NettyReactiveWebServerFactory.class
                //配置文件，没有注释@Configuration的配置本类型只做运行不在Bean绑定中
//                JettyWebServerConfiguration.class
        },
        //模块id
        name = "ghost.framework.netty.web.module"
)//注释模块信息
@MavenModuleDependency(artifactId = "ghost.framework.web.module", version = "1.0-SNAPSHOT")//注释模块依赖包添加到应用中
@Global//注释国际化
@Local//注释本地化
@ConfigurationProperties//注释默认配置
package ghost.framework.netty.web.module;

import ghost.framework.beans.configuration.annotation.ConfigurationProperties;
import ghost.framework.beans.locale.annotation.Global;
import ghost.framework.beans.locale.annotation.Local;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.annotation.module.annotation.ModulePackage;