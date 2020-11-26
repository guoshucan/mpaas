/**
 * package: ghost.framework.web.mvc.nginx.ui.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/19:20:49
 */
@PluginPackage(loadClass = {})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//@WebHeaderMenu(
//        templateUrl = "ab5702ef/headerSettings.html",
//        position = WebHeaderMenu.Position.right,
//        elementName = "li",
//        elementClass = "hidden-md"
//)
package ghost.framework.web.mvc.nginx.ui.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;