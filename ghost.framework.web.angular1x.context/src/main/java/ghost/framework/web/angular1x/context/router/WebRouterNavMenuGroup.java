package ghost.framework.web.angular1x.context.router;

import ghost.framework.util.Assert;

import java.util.*;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由左边菜单组
 * @Date: 2020/4/25:15:17
 */
public class WebRouterNavMenuGroup extends AbstractMap<String, IWebRouterNavMenuTitle> implements IWebRouterNavMenuGroup {

    public WebRouterNavMenuGroup(Object domain, String group) {
        Assert.notNull(domain, "WebRouterNavMenuGroup in domain null error");
//        Assert.notNullOrEmpty(group, "WebRouterNavMenuGroup in group null error");
        this.domain = domain;
        this.group = group;
    }

    private final Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    private Map<String, IWebRouterNavMenuTitle> map = new LinkedHashMap<>();

    @Override
    public String getGroup() {
        return group;
    }

    private String group;

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean isUse() {
        return use;
    }

    private boolean use = true;
    @Override
    public void setUse(boolean use) {
        this.use = use;
    }

    @Override
    public Set<Entry<String, IWebRouterNavMenuTitle>> entrySet() {
        return map.entrySet();
    }

    @Override
    public IWebRouterNavMenuTitle put(String key, IWebRouterNavMenuTitle value) {
        return map.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WebRouterNavMenuGroup that = (WebRouterNavMenuGroup) o;
        return map.equals(that.map) &&
                group.equals(that.group) &&
                domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), map, group, domain);
    }

    @Override
    public String toString() {
        return "WebRouterNavMenuGroup{" +
                ", use='" + use + '\'' +
                ", domain=" + domain == null ? "" : domain.toString() +
                ", group='" + group + '\'' +
                '}';
    }
}