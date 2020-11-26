/**
 * package: ghost.framework.web.login.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/15:0:40
 */
@PluginPackage(loadClass = {LoginConfig.class, OpenLoginRestController.class})
//注释依赖插件
@PluginDependencys(
        {
                @PluginDependency(
                        groupId = "ghost.framework",
                        artifactId = "ghost.framework.web.image.code.plugin",
                        version = "1.0-SNAPSHOT"
                )
        }
        )
//@I18n
//@L10n
package ghost.framework.web.login.plugin;
import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.beans.annotation.plugin.PluginDependencys;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;