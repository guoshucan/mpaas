/**
 * package: ghost.framework.web.angular1x.cerebro.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/3:15:41
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
        group = "OperationMaintenance",
        //终端
        title = "Maintenance",
        //图标样式
        icon = "glyphicon glyphicon-wrench icon text-primary-dker",
        //菜单的路由注释
        router = {}
)
package ghost.framework.web.angular1x.cerebro.plugin;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
