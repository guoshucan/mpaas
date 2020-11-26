package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.primitive.ConverterBoolean;
import ghost.framework.util.StringUtils;

import java.util.Arrays;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/27:10:19
 */
@ConverterFactory(defaultValue = true)
public class DefaultConverterBoolean<S extends Object, T extends Boolean>
        extends AbstractConverter<S, T>
        implements ConverterBoolean<S, T> {
    @Constructor
    public DefaultConverterBoolean(@Autowired(required = false) ICoreInterface domain) {
        super(domain);
    }

    public DefaultConverterBoolean() {
        super();
    }

    @Override
    public String toString() {
        return "DefaultConverterBoolean{" +
                "targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultConverterBoolean<?, ?> that = (DefaultConverterBoolean<?, ?>) o;
        return Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] targetType = {Boolean.class, boolean.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public boolean canConvert(S source) {
        try {
            //判断是否可以转换
            if (StringUtils.isEmpty(source)) {
                return false;
            }
            Boolean.valueOf(source.toString());
            return true;
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public T convert(S source) throws ConverterException {
        try {
            if (source instanceof String && StringUtils.isEmpty(source)) {
                return (T) Boolean.FALSE;
            }
            return (T) Boolean.valueOf(source.toString());
        } catch (NumberFormatException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}