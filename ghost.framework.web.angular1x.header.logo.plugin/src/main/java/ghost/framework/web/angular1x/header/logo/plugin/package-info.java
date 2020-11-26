/**
 * package: ghost.framework.web.angular1x.header.logo.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/18:14:08
 */
@PluginPackage(loadClass = {})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "b473d068/headerLogo.html",
        position = WebHeaderMenu.Position.left,
        elementName = "a",
        elementClass = "navbar-brand text-lt",
        elementAttributes = {"href=\"#/\""}
)
package ghost.framework.web.angular1x.header.logo.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;