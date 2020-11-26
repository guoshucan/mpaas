/**
 * package: ghost.framework.web.angular1x.login.plugin
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/22:18:38
 */
@PluginPackage(
        loadClass = {LoginRestController.class},
        // 插件依赖插件
        pluginDependencys = {
                //引用依赖验证码插件
                @PluginDependency(artifactId = "ghost.framework.web.image.code.plugin"),
                //引用管理员插件，作为管理员表数据库的初始化基础插件
                @PluginDependency(artifactId = "ghost.framework.web.angular1x.admin.plugin")
        }
)
// 注释web资源
@Resource(
        value = {
                // 注释web资源路径
                "static.html"
        })
// 登录路由
@WebRouters(
        //        templateUrl = "197a64da-7ed7-4726-a429-ad88af419fab",
        value = {
                // 路由视图模板
                @WebRouter(
                        scope = "access",
                        url = "access",
                        template = "<div ui-view class=\"fade-in-right-big smooth\"></div>"),
                // 登录
                @WebRouter(
                        scope = "access",
                        url = "b03a82b1-a197-48a0-a6d1-4d2e1a996fc4",
                        templateUrl = "197a64da-7ed7-4726-a429-ad88af419fab/signin.html",
                        controller = {"197a64da-7ed7-4726-a429-ad88af419fab/signin.js"},
                        name = "signin"),
                // 注册
                @WebRouter(
                        scope = "access",
                        url = "7cecda7d-8003-4785-88b8-cabb4dacfc5d",
                        templateUrl = "197a64da-7ed7-4726-a429-ad88af419fab/signup.html",
                        controller = {"197a64da-7ed7-4726-a429-ad88af419fab/signup.js"},
                        name = "signup"),
                // 忘记密码
                @WebRouter(
                        scope = "access",
                        url = "d2381c75-cdc8-4d7c-827a-f65eff7be47e",
                        templateUrl = "197a64da-7ed7-4726-a429-ad88af419fab/forgotpwd.html",
                        controller = {"197a64da-7ed7-4726-a429-ad88af419fab/forgotpwd.js"},
                        name = "forgotpwd"),
                // 初始化管理员
                @WebRouter(
                        scope = "access",
                        url = "62e8f66c-e7be-4b24-9848-e36859411601",
                        templateUrl = "197a64da-7ed7-4726-a429-ad88af419fab/initAdmin.html",
                        controller = {"197a64da-7ed7-4726-a429-ad88af419fab/initAdmin.js"},
                        name = "init"),
                // 初始化数据库
                @WebRouter(
                        scope = "access",
                        url = "f21c6498-9df7-4613-b8c3-c98d2c56329c",
                        templateUrl = "197a64da-7ed7-4726-a429-ad88af419fab/initDatabase.html",
                        controller = {"197a64da-7ed7-4726-a429-ad88af419fab/initDatabase.js"},
                        name = "init")
        })
package ghost.framework.web.angular1x.login.plugin;

import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.angular1x.context.router.annotation.WebRouters;
import ghost.framework.web.angular1x.login.plugin.controller.LoginRestController;