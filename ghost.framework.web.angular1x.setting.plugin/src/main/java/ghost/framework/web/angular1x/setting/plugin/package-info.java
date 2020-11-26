/**
 * package: ghost.framework.web.angular1x.setting.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/8:12:43
 */
@PluginPackage(loadClass = {SettingConfig.class, SettingRestController.class})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        group = "8e92359b",
        title = "36048b84",
        //菜单的路由注释
        router = {
                //系统设置
                @WebRouter(url = "52fc9541",
                        templateUrl = "b0388959/setting.html",
                        controller = {"toaster", "js/controllers/toaster.js", "b0388959/setting.js"},
                        name = "421fe219", title = "SystemSetting")
        }
)
@WebI18nNav(localeNames = {"zh_CN","zh_TW","en","ko"}, localeTitles = {"简体中文","繁体中文","English","Korea"})
package ghost.framework.web.angular1x.setting.plugin;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
