/**
 * package: ghost.framework.web.angular1x.deploy.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/31:16:24
 */
@PluginPackage(loadClass = DeployManageConfig.class)
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        //运维
        group = "OperationMaintenance",
        //终端
        title = "Deploy",
        //图标样式
        icon = "glyphicon glyphicon-wrench icon text-primary-dker",
        //菜单的路由注释
        router = {
                //软件管理
                @WebRouter(
                        url = "27e77b05-1434-4a85-a803-05a24b7aeaea",
                        templateUrl = "56c8d477-b010-4ad9-ba3b-3e04c02dc8e2/SoftwareManage.html",
                        controller = {"toaster", "js/controllers/toaster.js", "56c8d477-b010-4ad9-ba3b-3e04c02dc8e2/SoftwareManage.js"},
                        title = "SoftwareManage",
                        //菜单名称
                        name = "Software"),
//                //终端账号区域管理
//                @WebRouter(
//                        url = "198c76b0",
//                        templateUrl = "3e1e51ed/sshServerRegionManage.html",
//                        controller = {"toaster", "js/controllers/toaster.js", "3e1e51ed/sshServerRegionManage.js"},
//                        title = "SshServerRegionManage",
//                        //菜单名称
//                        name = "SshServerRegion"),
//                //终端账号管理
//                @WebRouter(
//                        url = "6ece8333",
//                        templateUrl = "3e1e51ed/sshServerManage.html",
//                        controller = {"toaster", "js/controllers/toaster.js", "3e1e51ed/sshServerManage.js"},
//                        title = "SshServerManage",
//                        //菜单名称
//                        name = "SshServer")
        }
)
package ghost.framework.web.angular1x.deploy.manage.plugin;

import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;