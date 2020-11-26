package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.primitive.ConverterString;

import java.util.Arrays;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/27:10:19
 */
@ConverterFactory
public class DefaultConverterString<S extends Object, T extends String>
        extends AbstractConverter<S, T>
        implements ConverterString<S, T>, EncodingTypeConverter<S, T> {
    @Constructor
    public DefaultConverterString(@Autowired(required = false) ICoreInterface domain) {
        super(domain);
    }

    public DefaultConverterString() {
        super();
    }

    @Override
    public String toString() {
        return "DefaultConverterString{" +
                "targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultConverterString<?, ?> that = (DefaultConverterString<?, ?>) o;
        return Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] targetType = {String.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public boolean canConvert(S source) {
        try {
            if (source == null) {
                return true;
            }
            if(source instanceof byte[]){
                new String((byte[])source);
            }
            if(source instanceof Byte[]){

                new String((byte[])source);
            }
            source.toString();
            return true;
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public T convert(S source) {
        try {
            return (T) source.toString();
        } catch (Exception e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}