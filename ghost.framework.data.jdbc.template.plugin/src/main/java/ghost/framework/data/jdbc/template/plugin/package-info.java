/**
 * package: ghost.framework.data.jdbc.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/22:18:40
 */
@PluginDependency(artifactId = "ghost.framework.data.datasource.hikaricp.plugin", version = "1.0-SNAPSHOT")//依赖数据源插件
@PluginPackage(loadClass = JdbcTemplateConfig.class)
package ghost.framework.data.jdbc.template.plugin;

import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;