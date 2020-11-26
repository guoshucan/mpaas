package ghost.framework.context.locale;

import ghost.framework.util.Assert;

import java.util.Objects;

/**
 * package: ghost.framework.context.locale
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:语言类
 * @Date: 2020/3/12:12:18
 */
public class Language implements ILanguage {
    private static final long serialVersionUID = -6315252548689818295L;
    private String title;

    public Language(String name, String title) {
        this(name, title, false);
    }

    public Language(String name, String title, boolean current) {
        Assert.notNullOrEmpty(name, "Language is name null error");
        Assert.notNullOrEmpty(title, "Language is title null error");
        this.name = name;
        this.title = title;
        this.current = current;
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 语言名称
     */
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private boolean current;

    @Override
    public boolean isCurrent() {
        return current;
    }

    @Override
    public void setCurrent(boolean current) {
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return current == language.current &&
                title.equals(language.title) &&
                name.equals(language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, name, current);
    }

    @Override
    public String toString() {
        return "Language{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", current=" + current +
                '}';
    }
}