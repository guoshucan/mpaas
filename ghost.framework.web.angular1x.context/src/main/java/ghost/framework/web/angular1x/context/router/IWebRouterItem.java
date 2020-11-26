package ghost.framework.web.angular1x.context.router;


/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由接口
 * @Date: 2020/3/13:19:43
 */
public interface IWebRouterItem {
    String getTitle();

    void setTitle(String title);

    /**
     * 获取菜单位置
     *
     * @return
     */
    default int getOrder() {
        return 0;
    }

    String getIcon();

    /**
     * 获取是否使用此路由
     *
     * @return
     */
    boolean isUse();

    /**
     * @return
     */
    boolean isDef();

    /**
     * 获取路由名称
     *
     * @return
     */
    String getName();

    /**
     * 路由模板内容
     *
     * @return
     */
    String getTemplate();

    void setTemplate(String template);

    /**
     * 路由模板url
     * 一般为html网页文件
     *
     * @return
     */
    String getTemplateUrl();

    void setTemplateUrl(String templateUrl);

    /**
     * 路由控制器
     * 一般为js脚本文件
     *
     * @return
     */
    String[] getController();

    /**
     * 获取范围
     * 默认为app
     *
     * @return
     */
    default String getScope() {
        return "app";
    }

    /**
     * 路由地址
     *
     * @return
     */
    String getUrl();

    void setIcon(String icon);

    void setScope(String scope);

    void setUrl(String url);
}