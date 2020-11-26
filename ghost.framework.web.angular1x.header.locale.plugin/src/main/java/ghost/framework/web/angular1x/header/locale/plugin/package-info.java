/**
 * package: ghost.framework.web.angular1x.header.locale.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/18:13:24
 */
@PluginPackage
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "e308a616/headerLocale.html",
        position = WebHeaderMenu.Position.right,
        elementName = "li",
        elementClass = "dropdown hidden-sm",
        elementAttributes = {"dropdown", "is-open=\"lang.isopen\""}
)
package ghost.framework.web.angular1x.header.locale.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;