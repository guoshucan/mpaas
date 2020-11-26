package ghost.framework.context.converter;

import java.util.Objects;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/22:20:50
 */
public abstract class AbstractConverter<S, T> implements TypeConverter<S, T> {
    public AbstractConverter(Object domain) {
        this.domain = domain;
    }

    public AbstractConverter() {
    }

    private Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractConverter<?, ?> that = (AbstractConverter<?, ?>) o;
        return domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain);
    }

    @Override
    public String toString() {
        return "AbstractConverter{" +
                "domain=" + (domain == null ? "" : domain.toString()) +
                '}';
    }
}