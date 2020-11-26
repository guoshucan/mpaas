/**
 * package: ghost.framework.web.angular1x.resource.container.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/23:20:12
 */
//插件注释
@PluginPackage(
        //添加依赖
        loadClass = {ResourceContainerManageConfig.class, ResourceContainerManageRestController.class},
        //引用插件
        pluginDependencys = {
                //引用资源容器插件
                @PluginDependency(artifactId = "ghost.framework.resource.container.plugin"),
                //引用阿里云oss插件
                @PluginDependency(artifactId = "ghost.framework.aliyun.oss.plugin"),
                //引用腾讯云cos插件
                @PluginDependency(artifactId = "ghost.framework.tencent.cloud.cos.plugin")
        }
)
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
//左边菜单注释
@WebNavMenu(
        //系统资源
        group = "SystemManagement",
        //资源容器
        title = "ResourceManage",
        //菜单的路由注释
        router = {
                //管理员路由
                @WebRouter(url = "ba9000cf-1b59-4079-9b03-73f2f4db135b",
                        templateUrl = "441aa645-4c81-478f-a3e7-a18c7e3e1ac6/resourceContainerManagement.html",
                        controller = {"toaster", "js/controllers/toaster.js", "441aa645-4c81-478f-a3e7-a18c7e3e1ac6/resourceContainerManagement.js"},
                        name = "ContainerManage")
        }
)
package ghost.framework.web.angular1x.resource.container.manage.plugin;
import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
