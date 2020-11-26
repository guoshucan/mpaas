package ghost.framework.core.converter.serialization;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.serialization.ObjectToBytesConverter;

import java.util.Arrays;

/**
 * package: ghost.framework.serialization.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link Object} to {@link byte[]}
 * @Date: 2020/6/20:10:27
 */
@ConverterFactory
public class DefaultObjectToBytesConverter implements ObjectToBytesConverter {
    private final Class<?>[] sourceType = {Object.class};

    @Override
    public Class<?>[] getSourceType() {
        return sourceType;
    }

    private final Class<?>[] targetType = {byte[].class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public String toString() {
        return "DefaultObjectToBytesConverter{" +
                "sourceType=" + Arrays.toString(sourceType) +
                ", targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultObjectToBytesConverter that = (DefaultObjectToBytesConverter) o;
        return Arrays.equals(sourceType, that.sourceType) &&
                Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(sourceType);
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    @Override
    public byte[] convert(Object source) throws ConverterException {
        return SerializeUtils.serialize(source);
    }
}