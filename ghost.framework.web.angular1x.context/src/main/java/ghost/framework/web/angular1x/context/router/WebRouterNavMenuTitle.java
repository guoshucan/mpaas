package ghost.framework.web.angular1x.context.router;

import ghost.framework.util.Assert;

import java.util.*;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/15:15:08
 */
public class WebRouterNavMenuTitle extends AbstractMap<String, IWebRouterItem> implements IWebRouterNavMenuTitle {
    public WebRouterNavMenuTitle(Object domain, String title, String icon) {
        Assert.notNull(domain, "WebRouterNavMenuTitle in domain null error");
        Assert.notNullOrEmpty(title, "WebRouterNavMenuTitle in title null error");
        this.domain = domain;
        this.title = title;
        this.icon = icon;
    }

    private final String icon;
    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public boolean isUse() {
        return use;
    }

    private String title;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    private final Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    private Map<String, IWebRouterItem> map = new LinkedHashMap<>();

    @Override
    public IWebRouterItem put(String key, IWebRouterItem value) {
        return map.put(key, value);
    }

    @Override
    public Set<Entry<String, IWebRouterItem>> entrySet() {
        return map.entrySet();
    }

    private boolean use = true;

    @Override
    public void setUse(boolean use) {
        this.use = use;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WebRouterNavMenuTitle that = (WebRouterNavMenuTitle) o;
        return icon.equals(that.icon) &&
                title.equals(that.title) &&
                domain.equals(that.domain) &&
                use == that.use &&
                map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), icon, title, domain, map);
    }

    @Override
    public String toString() {
        return "WebRouterNavMenuTitle{" +
                "use='" + use + '\'' +
                "icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", domain=" + domain == null ? "" : domain.toString() +
                '}';
    }
}