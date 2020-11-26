package ghost.framework.web.angular1x.context.router;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由菜单组接口
 * @Date: 2020/4/25:15:22
 */
public interface IWebRouterMenuGroup extends IWebRouterMenuItem {
    /**
     * 获取域
     *
     * @return
     */
    @Override
    default Object getDomain() {
        return null;
    }

    /**
     * 获取组名称
     *
     * @return
     */
    String getGroup();

    /**
     * 设置组名称
     *
     * @param group
     */
    void setGroup(String group);
    /**
     * 获取是否使用此路由
     *
     * @return
     */
    boolean isUse();
    void setUse(boolean use);
}