/**
 * package: ghost.framework.web.angular1x.header.message.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/18:2:31
 */
@PluginPackage(loadClass = {})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "d1df4ca1/headerMessage.html",
        position = WebHeaderMenu.Position.right,
        elementName = "li",
        elementClass = "dropdown",
        elementAttributes = {"dropdown"}
)
package ghost.framework.web.angular1x.header.message.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;