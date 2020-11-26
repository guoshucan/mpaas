/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 4:18 2019/11/8
 */
@ModulePackage//(type = JettyWsApplication.class)//(name = "ghost.framework.jetty.ws.module")
@Global//注释国际化
@Local//注释本地化
@ConfigurationProperties//注释默认配置
package ghost.framework.jetty.ws.module;
import ghost.framework.beans.configuration.annotation.ConfigurationProperties;
import ghost.framework.beans.locale.annotation.Global;
import ghost.framework.beans.locale.annotation.Local;
import ghost.framework.beans.annotation.module.annotation.ModulePackage;