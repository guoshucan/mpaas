package ghost.framework.web.angular1x.context.menu;

import com.google.common.base.Objects;
import ghost.framework.context.IGetDomain;
import ghost.framework.util.Assert;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenus;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:头部菜单
 * {@link WebHeaderMenus}
 * {@link WebHeaderMenu}
 * {@link IWebHeaderMenuItem}
 * {@link IWebHeaderMenuItem#getPosition()}
 * {@link IWebHeaderMenuItem#getTemplateUrl()}
 * {@link IGetDomain}
 * {@link IGetDomain#getDomain()}
 * @Date: 2020/8/17:23:32
 */
public class WebHeaderMenuItem implements IWebHeaderMenuItem {
    public WebHeaderMenuItem(Object domain, WebHeaderMenu headerMenu) {
        Assert.notNull(domain, "WebHeaderMenuItem in domain null error");
        Assert.notNull(headerMenu, "WebHeaderMenuItem in headerMenu null error");
        this.domain = domain;
        this.headerMenu = headerMenu;
    }

    private final Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    private final WebHeaderMenu headerMenu;

    @Override
    public String[] getElementAttributes() {
        return headerMenu.elementAttributes();
    }

    @Override
    public WebHeaderMenu.Position getPosition() {
        return headerMenu.position();
    }

    @Override
    public String getTemplateUrl() {
        return headerMenu.templateUrl();
    }

    @Override
    public String getElementName() {
        return headerMenu.elementName();
    }

    @Override
    public String getElementClass() {
        return headerMenu.elementClass();
    }

    @Override
    public String getElementStyle() {
        return headerMenu.elementStyle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebHeaderMenuItem item = (WebHeaderMenuItem) o;
        return Objects.equal(domain, item.domain) &&
                Objects.equal(headerMenu, item.headerMenu);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(domain, headerMenu);
    }

    @Override
    public String toString() {
        return "WebHeaderMenuItem{" +
                "domain=" + domain.toString() +
                ", headerMenu=" + headerMenu.toString() +
                '}';
    }
}