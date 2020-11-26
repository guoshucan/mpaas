package ghost.framework.web.vue.router.plugin.controller;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.context.bens.annotation.RequestMapping;
import ghost.framework.web.context.bens.annotation.RestController;
import ghost.framework.web.vue.router.plugin.DynamicRouterResolver;

/**
 * package: ghost.framework.web.vue.router.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:vue动态路由控制器
 * @Date: 2020/3/9:13:16
 */
@RestController(value = "/94b24d18-53af-4d34-a1f2-dc657f2c6acc")
public class DynamicRouterController {
    /**
     * 获取动态路由数据
     *
     * @return
     */
    @RequestMapping
    public Object getRouter() {
        return null;
    }
    /**
     * 注入路由解析器
     */
    @Autowired
    private DynamicRouterResolver routerResolver;
}