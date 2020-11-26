/**
 * package: ghost.framework.web.angular1x.ssh.software.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ssh脚本插件
 * @Date: 2020/8/16:0:54
 */
@PluginPackage(loadClass = {
        ScriptConfig.class,
        PythonVersionRestController.class,
        GoLangVersionRestController.class,
        ScriptTaskRestController.class,
        JavaVersionRestController.class,
        CommandRestController.class,
        ScriptRestController.class})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        //运维
        group = "ff0a5895",
        //脚本
        title = "ea6515a8",
        //图标样式
        icon = "glyphicon glyphicon-wrench icon text-primary-dker",
        //菜单的路由注释
        router = {
                //Java版本管理
                @WebRouter(
                        url = "e5afb2d6",
                        templateUrl = "5540b2eb/scriptJavaVersionManage.html",
                        controller = {"toaster",
                                "5540b2eb/scriptJavaVersionManage.js"},
                        title = "JavaVersionManage",
                        //菜单名称
                        name = "57b8942f"),
                //Golnag版本管理
                @WebRouter(
                        url = "f0dcca38",
                        templateUrl = "5540b2eb/scriptGolangVersionManage.html",
                        controller = {"toaster",
                                "5540b2eb/scriptGolangVersionManage.js"},
                        title = "GoLangVersionManage",
                        //菜单名称
                        name = "60ab83e3"),
                //Python版本管理
                @WebRouter(
                        url = "f5f6ba0f",
                        templateUrl = "5540b2eb/scriptPythonVersionManage.html",
                        controller = {"toaster",
                                "5540b2eb/scriptPythonVersionManage.js"},
                        title = "PythonVersionManage",
                        //菜单名称
                        name = "41288f9c"),
                //command版本管理
                @WebRouter(
                        url = "12f49490",
                        templateUrl = "5540b2eb/scriptCommandManage.html",
                        controller = {"toaster",
                                "5540b2eb/scriptCommandManage.js"},
                        title = "ScriptCommandManage",
                        //菜单名称
                        name = "d7505b34"),
                //ssh脚本管理
                @WebRouter(
                        url = "12d2d104",
                        templateUrl = "5540b2eb/scriptManage.html",
                        controller = {"toaster",
                                "5540b2eb/scriptManage.js"},
                        title = "ScriptManage",
                        //菜单名称
                        name = "af7de07a"),
                //ssh脚本任务管理
                @WebRouter(
                        url = "d91a2166",
                        templateUrl = "5540b2eb/scriptTaskManage.html",
                        controller = {"toaster",
                                "5540b2eb/scriptTaskManage.js"},
                        title = "ScriptTaskManage",
                        //菜单名称
                        name = "2deca16b"),
        }
)
@WebI18nNav(localeNames = {"zh_CN","zh_TW","en","ko"}, localeTitles = {"简体中文","繁体中文","English","Korea"})
package ghost.framework.web.angular1x.script.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
