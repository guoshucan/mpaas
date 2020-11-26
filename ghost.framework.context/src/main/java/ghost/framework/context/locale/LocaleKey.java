package ghost.framework.context.locale;

import ghost.framework.util.Assert;

import java.util.Objects;

/**
 * package: ghost.framework.context.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域键，只对 {@link LocaleKey#locale} 参数作为键比对对象
 * @Date: 2020/6/14:20:20
 */
public class LocaleKey {
    /**
     * 初始化区域键
     * 只对 {@link LocaleKey#locale} 参数作为键比对对象
     *
     * @param locale 区域
     * @param title  区域标题
     */
    public LocaleKey(String locale, String title) {
        Assert.notNullOrEmpty(locale, "LocaleKey in locale is null error");
        Assert.notNullOrEmpty(title, "LocaleKey in title is null error");
        this.locale = locale;
        this.title = title;
    }
    /**
     * 初始化区域键
     * 只对 {@link LocaleKey#locale} 参数作为键比对对象
     *
     * @param locale 区域
     */
    public LocaleKey(String locale) {
        Assert.notNullOrEmpty(locale, "LocaleKey in locale is null error");
        this.locale = locale;
        this.title = null;
    }
    /**
     * 区域标题
     * 比如 {@link LocaleKey#locale} 为zh_CN时{@link LocaleKey#title}为简体中文
     */
    private final String title;
    private final String locale;

    public String getTitle() {
        return title;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocaleKey localeKey = (LocaleKey) o;
        return locale.equals(localeKey.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale);
    }

    @Override
    public String toString() {
        return "LocaleKey{" +
                "title='" + title + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}