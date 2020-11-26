package ghost.framework.web.angular1x.context.router;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由菜单标题接口
 * @Date: 2020/4/25:15:25
 */
public interface IWebRouterMenuTitle extends IWebRouterMenuItem {
    /**
     * 获取标题名称
     * @return
     */
    String getTitle();

    /**
     * 设置标题名称
     * @param title
     */
    void setTitle(String title);
}