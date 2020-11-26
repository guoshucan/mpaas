/**
 * package: ghost.framework.web.angular1x.ssh.software.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/16:1:43
 */
@PluginPackage
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
                @WebRouter(
                        url = "f94374af-e8cd-47b9-9bc4-d972c200ff79",
                        templateUrl = "cc5b08ce-7939-4b8e-bc75-c450f004d462/sshSoftwareManage.html",
                        controller = {"toaster",
//                                "js/controllers/toaster.js",
//                                "js/controllers/common/modalConfirmController.js",
//                                "js/controllers/common/startAndEndDatepickerController.js",
                                "cc5b08ce-7939-4b8e-bc75-c450f004d462/sshSoftwareManage.js"},
                        title = "Software manage",
                        //菜单名称
                        name = "83a6cec6")
        }
)
@WebI18nNav(localeNames = {"zh_CN","en"}, localeTitles = {"简体中文","English"})
package ghost.framework.web.angular1x.ssh.software.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
