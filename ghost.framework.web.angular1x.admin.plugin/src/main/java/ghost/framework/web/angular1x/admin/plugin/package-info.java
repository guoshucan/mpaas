/**
 * package: ghost.framework.web.admin.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:管理员插件
 * @Date: 2020/3/12:20:52
 */
//插件注释
@PluginPackage(loadClass = {AdminConfig.class, AdminRestController.class, AdminGroupRestController.class, AdminGroupPermissionRestController.class})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        group = "8e92359b",
        icon = "glyphicon glyphicon-wrench icon text-primary-dker",
        title = "36048b84",
        //菜单的路由注释
        router = {
        //管理员路由
        @WebRouter(url = "aca0cf95",
                templateUrl = "a87b955f/admin.html",
                controller = {
                "toaster",
//                        "js/controllers/toaster.js",
//                        "js/controllers/common/batchDeleteController.js",
//                        "js/controllers/common/deleteController.js",
//                        "js/controllers/common/showController.js",
//                        "js/controllers/common/modalConfirmController.js",
//                        "js/controllers/common/startAndEndDatepickerController.js",
                        "a87b955f/admin.js"},
                name = "5a1cdb1e"),
        //管理组路由
        @WebRouter(url = "f1ad62f5",
                templateUrl = "a87b955f/adminGroup.html",
                controller = {
                "toaster",
//                        "js/controllers/toaster.js",
//                        "js/controllers/common/batchDeleteController.js",
//                        "js/controllers/common/deleteController.js",
//                        "js/controllers/common/showController.js",
//                        "js/controllers/common/modalConfirmController.js",
//                        "js/controllers/common/startAndEndDatepickerController.js",
                        "a87b955f/adminGroup.js"},
                name = "c06bb67c")
}
)
@WebI18nNav(localeNames = {"zh_CN","zh_TW","en","ko"}, localeTitles = {"简体中文","繁体中文","English","Korea"})
package ghost.framework.web.angular1x.admin.plugin;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.admin.plugin.controller.AdminGroupPermissionRestController;
import ghost.framework.web.angular1x.admin.plugin.controller.AdminGroupRestController;
import ghost.framework.web.angular1x.admin.plugin.controller.AdminRestController;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
