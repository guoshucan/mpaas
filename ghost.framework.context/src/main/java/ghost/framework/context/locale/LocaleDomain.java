package ghost.framework.context.locale;

import ghost.framework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * package: ghost.framework.context.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/14:17:32
 */
public class LocaleDomain implements ILocaleDomain {
    public LocaleDomain(Object domain, Map<LocaleKey, Map<Object, Object>> localeMap) {
        Assert.notNull(domain, "LocaleDomain in domain is null error");
        Assert.notNull(domain, "LocaleDomain in localeMap is null error");
        this.domain = domain;
        this.localeMap = localeMap;
    }

    private Map<LocaleKey, Map<Object, Object>> localeMap;

    @Override
    public Map<LocaleKey, Map<Object, Object>> getLocaleMap() {
        return localeMap;
    }

    private final Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocaleDomain that = (LocaleDomain) o;
        return domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain);
    }

    @Override
    public String toString() {
        return "LocaleDomain{" +
                "domain=" + domain +
                '}';
    }
}