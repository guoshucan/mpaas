package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.primitive.ConverterUUID;

import java.util.Arrays;
import java.util.UUID;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/27:10:19
 */
@ConverterFactory
public class DefaultConverterUUID<S extends Object, T extends UUID>
        extends AbstractConverter<S, T>
        implements ConverterUUID<S, T> {
    @Constructor
    public DefaultConverterUUID(@Autowired(required = false) ICoreInterface domain) {
        super(domain);
    }

    public DefaultConverterUUID() {
        super();
    }

    @Override
    public String toString() {
        return "DefaultConverterUUID{" +
                "targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultConverterUUID<?, ?> that = (DefaultConverterUUID<?, ?>) o;
        return Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] targetType = {UUID.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public boolean canConvert(S source) {
        try {
            UUID.fromString(source.toString());
            return true;
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public T convert(S source) throws ConverterException {
        try {
            return (T) UUID.fromString(source.toString());
        } catch (Exception e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}