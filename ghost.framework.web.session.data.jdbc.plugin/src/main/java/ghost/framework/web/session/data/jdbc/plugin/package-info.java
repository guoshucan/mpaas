/**
 * package: ghost.framework.web.session.data.jdbc.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/22:18:28
 */
@PluginPackage(loadClass = {SessionJdbcConfiguration.class})
@ConfigurationProperties
package ghost.framework.web.session.data.jdbc.plugin;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;