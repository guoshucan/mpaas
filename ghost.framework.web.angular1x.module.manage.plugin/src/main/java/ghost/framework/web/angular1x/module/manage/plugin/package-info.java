/**
 * package: ghost.framework.web.angular1x.module.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/21:10:09
 */
//插件注释
@PluginPackage(loadClass = {
        ModuleManagePluginConfig.class,
        ModuleController.class,
        PluginController.class,
        MavenRepositoryRestController.class
})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        group = "SystemManagement",
        title = "Module",
        //菜单的路由注释
        router = {
                //管理员路由
                @WebRouter(url = "c6b851c9-68d5-4b23-a398-1b37983dd330",
                        templateUrl = "47d9d77d-fe60-4b46-9362-cc5efa4bd1ed/moduleManagement.html",
                        controller = {"toaster", "js/controllers/toaster.js", "47d9d77d-fe60-4b46-9362-cc5efa4bd1ed/moduleManagement.js"},
                        name = "ModuleManagement"),
                //管理员路由
                @WebRouter(url = "9a997669-5637-46f4-85a9-91c7706adb51",
                        templateUrl = "47d9d77d-fe60-4b46-9362-cc5efa4bd1ed/pluginManagement.html",
                        controller = {"toaster", "js/controllers/toaster.js", "47d9d77d-fe60-4b46-9362-cc5efa4bd1ed/pluginManagement.js"},
                        name = "PluginManagement"),
                //管理员路由
                @WebRouter(url = "82541b98-3243-4b08-9f82-737f854e68f3",
                        templateUrl = "47d9d77d-fe60-4b46-9362-cc5efa4bd1ed/mavenRepository.html",
                        controller = {"toaster", "js/controllers/toaster.js", "47d9d77d-fe60-4b46-9362-cc5efa4bd1ed/mavenRepository.js"},
                        name = "MavenRepository")
        }
)
package ghost.framework.web.angular1x.module.manage.plugin;

import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.angular1x.module.manage.plugin.controller.MavenRepositoryRestController;
import ghost.framework.web.angular1x.module.manage.plugin.controller.ModuleController;
import ghost.framework.web.angular1x.module.manage.plugin.controller.PluginController;