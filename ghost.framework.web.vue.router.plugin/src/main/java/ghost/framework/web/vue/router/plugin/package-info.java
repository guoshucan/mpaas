/**
 * package: ghost.framework.web.vue.router.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:vue动态路由插件
 * @Date: 2020/3/9:13:13
 */
@PluginPackage(
        depend = {DynamicRouterResolver.class, DynamicRouterController.class}
)
package ghost.framework.web.vue.router.plugin;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.vue.router.plugin.controller.DynamicRouterController;