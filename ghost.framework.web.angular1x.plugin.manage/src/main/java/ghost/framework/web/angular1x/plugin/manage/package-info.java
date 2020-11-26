/**
 * package: ghost.framework.web.angular1x.plugin.management
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/18:21:17
 */
//插件注释
@PluginPackage(depend = {PluginManagementConfig.class})
//注释web资源
@WebResource(value = {
        //注释web资源路径
        @WebResourcePath(value = "static.html")
})
//左边菜单注释
@WebNavMenu(title = "pluginManagement",
        //菜单的路由注释
        router = {
                //管理员路由
                @WebRouter(url = "4413170c-a162-4c89-bdc4-1e6698956523", template = "cd8fd5d6-d1d1-408c-bd65-b37f165fb523/pluginManagement.html", controller = {"toaster", "js/controllers/toaster.js", "cd8fd5d6-d1d1-408c-bd65-b37f165fb523/pluginManagement.js"}, name = "pluginManagement")
        }
)
package ghost.framework.web.angular1x.plugin.management;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.router.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebResource;
import ghost.framework.web.angular1x.context.router.annotation.WebResourcePath;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;