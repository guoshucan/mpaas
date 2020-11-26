/**
 * package: ghost.framework.web.angular1x.ssh.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ssh服务器插件
 * @Date: 2020/5/25:1:12
 */
@PluginPackage(
        //添加依赖
        loadClass = {
                SshConfig.class,
                SshServerRestController.class,
                SshRegionRestController.class,
                SshGroupRestController.class,
                SshTypeRestController.class,
                SshAccountRestController.class,
                SshSocket.class
        }
)
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        //运维
        group = "ff0a5895",
        //终端
        title = "757f5216",
        //图标样式
        icon = "glyphicon glyphicon-wrench icon text-primary-dker",
        //菜单的路由注释
        router = {
                //终端账号组管理
                @WebRouter(
                        url = "7d2a444c",
                        templateUrl = "3e1e51ed/sshAccountManage.html",
                        controller = {"toaster",
                                "3e1e51ed/sshAccountManage.js"},
                        title = "sshAccountManage",
                        //菜单名称
                        name = "7fb4cee1"),
                //终端账号组管理
                @WebRouter(
                        url = "45a0ebd7",
                        templateUrl = "3e1e51ed/sshGroupManage.html",
                        controller = {"toaster",
                                "3e1e51ed/sshGroupManage.js"},
                        title = "SshGroupManage",
                        //菜单名称
                        name = "51bcedf6"),
                //终端账号区域管理
                @WebRouter(
                        url = "198c76b0",
                        templateUrl = "3e1e51ed/sshRegionManage.html",
                        controller = {"toaster",
                                "3e1e51ed/sshRegionManage.js"},
                        title = "SshRegionManage",
                        //菜单名称
                        name = "391cc586"),
                //终端账号区域管理
                @WebRouter(
                        url = "21317a37",
                        templateUrl = "3e1e51ed/sshTypeManage.html",
                        controller = {"toaster",
                                "3e1e51ed/sshTypeManage.js"},
                        title = "SshTypeManage",
                        //菜单名称
                        name = "48c99035"),
                //终端账号管理
                @WebRouter(
                        url = "6ece8333",
                        templateUrl = "3e1e51ed/sshServerManage.html",
                        controller = {"toaster",
                                "3e1e51ed/sshServerManage.js"},
                        title = "SshServerManage",
                        //菜单名称
                        name = "9fb14bff")
        }
)
@WebI18nNav(localeNames = {"zh_CN","zh_TW","en","ko"}, localeTitles = {"简体中文","繁体中文","English","Korea"})
package ghost.framework.web.angular1x.ssh.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.angular1x.ssh.plugin.ws.SshSocket;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
