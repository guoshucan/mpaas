/**
 * package: ghost.framework.web.angular1x.header.user.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/18:2:17
 */
@PluginPackage(loadClass = {})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "dff0e192/headerUser.html",
        position = WebHeaderMenu.Position.right,
        elementName = "li",
        elementClass = "dropdown",
        elementAttributes = {"dropdown"}
)
package ghost.framework.web.angular1x.header.user.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;