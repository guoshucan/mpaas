/**
 * package: ghost.framework.web.angular1x.header.test2.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/17:20:20
 */
@PluginPackage(loadClass = {})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "c131be22/headerTest2Menu.html",
        elementStyle = "margin:0;padding:0;"
//        elementName = "li",
//        elementClass = "dropdown pos-stc",
//        elementAttributes = {"ng-controller=\"fcfb2a5f\"", "ng-init=\"init()\"", "dropdown"}
        )
package ghost.framework.web.angular1x.header.test2.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;