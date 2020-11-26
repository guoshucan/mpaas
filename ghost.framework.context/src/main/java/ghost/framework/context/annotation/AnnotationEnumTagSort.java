package ghost.framework.context.annotation;

import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.util.Assert;

import java.util.Objects;

/**
 * package: ghost.framework.core.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/18:18:35
 */
public class AnnotationEnumTagSort {
    /**
     * 作为内部或外部指定注释标签的枚举排序
     * @param domain 域对象
     * @param tag 枚举标签
     */
    public AnnotationEnumTagSort(Object domain, AnnotationTag.AnnotationTags tag) {
        Assert.notNull(domain, "AnnotationEnumSort is domain null error");
        Assert.notNull(tag, "AnnotationEnumSort is tag null error");
        this.domain = domain;
        this.tag = tag;
        this.name = this.tag.name();
    }

    /**
     *
     * @param domain 域对象
     * @param name 枚举标签名称
     */
    public AnnotationEnumTagSort(Object domain, String name) {
        Assert.notNull(domain, "AnnotationEnumSort is domain null error");
        Assert.notNullOrEmpty(domain, "AnnotationEnumSort is name null error");
        this.domain = domain;
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    private AnnotationTag.AnnotationTags tag = AnnotationTag.AnnotationTags.Unknown;

    public AnnotationTag.AnnotationTags getTag() {
        return tag;
    }

    private Object domain;

    public Object getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return "AnnotationEnumSort{" +
                "tag='" + tag + '\'' +
                ", domain=" + domain.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationEnumTagSort that = (AnnotationEnumTagSort) o;
        return tag.equals(that.tag) &&
                domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, domain);
    }
}