/**
 * package: ghost.framework.web.angular1x.header.search.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/8:14:03
 */
@PluginPackage(loadClass = {})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
@WebHeaderMenu(
        templateUrl = "a440a0d8/headerSearchMenu.html",
        elementStyle = "margin:0;padding:0;"
//        elementName = "form",
//        elementClass = "navbar-form navbar-form-sm navbar-left shift",
//        elementAttributes = {"ui-shift=\"prependTo\"", "target=\".navbar-collapse\"", "role=\"search\"", "ng-controller=\"e7a1375f\"", "ng-init=\"init()\""}
        )
package ghost.framework.web.angular1x.header.search.plugin;

import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;