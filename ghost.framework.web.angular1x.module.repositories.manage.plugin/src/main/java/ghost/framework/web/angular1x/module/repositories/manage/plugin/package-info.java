/**
 * package: ghost.framework.web.angular1x.module.repositories.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/22:18:20
 */
@PluginPackage
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        group = "ModuleGroup",
        title = "Module",
        //菜单的路由注释
        router = {
                //管理员路由
                @WebRouter(url = "6b1a2eaa-40d7-4bb2-af88-97034d76f059",
                        templateUrl = "3072dad1-9a95-4d44-b113-05fc2bddef0a/moduleRepositoriesManagement.html",
                        controller = {"toaster", "js/controllers/toaster.js", "3072dad1-9a95-4d44-b113-05fc2bddef0a/moduleRepositoriesManagement.js"},
                        name = "ModuleManagement")
        }
)

package ghost.framework.web.angular1x.module.repositories.manage.plugin;

import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;