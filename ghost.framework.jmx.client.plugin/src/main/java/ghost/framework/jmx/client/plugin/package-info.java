/**
 * package: ghost.framework.jmx.client.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/26:16:09
 */
@ConfigurationProperties
@PluginPackage(loadClass = MBeanClientInterceptor.class)
package ghost.framework.jmx.client.plugin;

import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;