package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.primitive.ConverterString;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link byte[]} to {@link String}
 * @Date: 2020/6/25:10:49
 */
@ConverterFactory(defaultValue = true)
public class DefaultBytesToStringConverter
        extends AbstractConverter<byte[], String>
        implements ConverterString<byte[], String>, EncodingTypeConverter<byte[], String> {
    @Override
    public boolean canConvert(byte[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return "DefaultBytesToStringConverter{" +
                "sourceType=" + Arrays.toString(sourceType) +
                ", targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultBytesToStringConverter that = (DefaultBytesToStringConverter) o;
        return Arrays.equals(sourceType, that.sourceType) &&
                Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(sourceType);
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] sourceType = {byte[].class};

    @Override
    public Class<?>[] getSourceType() {
        return sourceType;
    }

    private Class<?>[] targetType = {String.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public String convert(byte[] source) throws ConverterException {
        return new String(source);
    }

    @Override
    public String convert(byte[] source, String characterEncoding) throws ConverterException {
        try {
            return new String(source, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}