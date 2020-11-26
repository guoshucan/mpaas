package ghost.framework.web.angular1x.context.router;

import ghost.framework.util.Assert;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由对象
 * @Date: 2020/3/13:19:46
 */
public class WebRouterItem implements IWebRouterItem {
    public WebRouterItem(WebRouter router) {
        Assert.notNull(router, "WebRouterItem in router null error");
        this.router = router;
    }

    private String title;

    @Override
    public String getTitle() {
        if (StringUtils.isEmpty(title)) {
            return router.title();
        }
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    private String icon;

    @Override
    public String getIcon() {
        if (StringUtils.isEmpty(icon)) {
            return router.icon();
        }
        return icon;
    }

    @Override
    public boolean isUse() {
        return true;
    }

    @Override
    public boolean isDef() {
        return false;
    }

    @Override
    public String getName() {
        return router.name();
    }

    private WebRouter router;
    private String templateUrl;

    @Override
    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    @Override
    public String getTemplateUrl() {
        if (StringUtils.isEmpty(templateUrl)) {
            return router.templateUrl();
        }
        return router.templateUrl();
    }

    private String template;

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String getTemplate() {
        if (StringUtils.isEmpty(template)) {
            return router.template();
        }
        return template;
    }

    @Override
    public String[] getController() {
        return router.controller();
    }

    private String scope;

    @Override
    public String getScope() {
        if (StringUtils.isEmpty(scope)) {
            return router.scope();
        }
        return scope;
    }

    /**
     * 路由的地址
     */
    private String url;

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        if (StringUtils.isEmpty(url)) {
            return router.url();
        }
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebRouterItem that = (WebRouterItem) o;
        return icon.equals(that.icon) &&
                router.equals(that.router) &&
                scope.equals(that.scope) &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, router, scope, url);
    }

    @Override
    public String toString() {
        return "WebRouterItem{" +
                "scope=" + getScope() +
                ",name=" + getName() +
                ",title=" + getTitle() +
                ",template=" + getTemplate() +
                ",templateUrl=" + getTemplateUrl() +
                ",url=" + getUrl() +
                ",controller=" + getController() +
                ",icon=" + getIcon() +
                ",order=" + getOrder() +
                ",use=" + isUse() +
                ",def=" + isDef() +
                '}';
    }
}