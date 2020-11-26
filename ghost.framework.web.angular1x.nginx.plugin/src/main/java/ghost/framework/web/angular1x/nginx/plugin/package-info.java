/**
 * package: ghost.framework.web.angular1x.nginx.ui.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:nginx管理插件包
 * @Date: 2020/8/20:23:21
 */
@PluginPackage(loadClass = NginxConfig.class
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
        //nginx
        title = "546b66ec",
        //图标样式
        icon = "glyphicon glyphicon-wrench icon text-primary-dker",
        //菜单的路由注释
        router = {
                //终端账号管理
                @WebRouter(
                        url = "d8b59790",
                        templateUrl = "f30d6acc/nginxServer.html",
                        controller = {"toaster",
                                "f30d6acc/nginxServer.js"},
                        title = "NginxServer",
                        //菜单名称
                        name = "83ecab9c")
        }
)
@WebI18nNav(localeNames = {"zh_CN","zh_TW","en","ko"}, localeTitles = {"简体中文","繁体中文","English","Korea"})
package ghost.framework.web.angular1x.nginx.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
