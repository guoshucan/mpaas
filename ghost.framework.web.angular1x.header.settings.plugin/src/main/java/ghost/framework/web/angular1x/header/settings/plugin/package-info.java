/**
 * package: ghost.framework.web.angular1x.header.settings.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:头部设置插件包
 * @Date: 2020/6/8:12:29
 */
@PluginPackage(loadClass = {HeaderSettingsRestController.class})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "ab5702ef/headerSettings.html",
        position = WebHeaderMenu.Position.right,
        elementName = "li",
        elementClass = "hidden-md"
)
package ghost.framework.web.angular1x.header.settings.plugin;

import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;