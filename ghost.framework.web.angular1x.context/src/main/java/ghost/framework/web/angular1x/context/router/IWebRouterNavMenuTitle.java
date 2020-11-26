package ghost.framework.web.angular1x.context.router;

import ghost.framework.context.IGetDomain;

import java.util.Map;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由nav菜单标题
 * @Date: 2020/3/15:15:08
 */
public interface IWebRouterNavMenuTitle extends IWebRouterMenuTitle, IGetDomain, Map<String, IWebRouterItem> {
    /**
     * 标题
     * @return
     */
    String getTitle();

    /**
     * 图标
     * @return
     */
    String getIcon();
    /**
     * 获取是否使用此路由
     *
     * @return
     */
    boolean isUse();
    void setUse(boolean use);
}