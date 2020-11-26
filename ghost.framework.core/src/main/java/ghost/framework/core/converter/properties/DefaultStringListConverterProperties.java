package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.properties.StringListConverterProperties;
import ghost.framework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link List<String>} to {@link Properties}
 * @Date: 2020/6/26:12:12
 */
@ConverterFactory
public class DefaultStringListConverterProperties
        extends AbstractConverter<List<String>, Properties>
        implements StringListConverterProperties, EncodingTypeConverter<List<String>, Properties> {
    private final Class<?>[] sourceOriented = new Class[]{List.class};

    @Override
    public String toString() {
        return "DefaultStringListConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", type=" + Arrays.toString(type) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultStringListConverterProperties that = (DefaultStringListConverterProperties) o;
        return Arrays.equals(sourceOriented, that.sourceOriented) &&
                Arrays.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(sourceOriented);
        result = 31 * result + Arrays.hashCode(type);
        return result;
    }

    @Override
    public Class<?>[] getSourceType() {
        return sourceOriented;
    }

    private final Class<?>[] type = {Properties.class};

    @Override
    public Class<?>[] getTargetType() {
        return type;
    }

    @Override
    public Properties convert(List<String> source, String characterEncoding) throws ConverterException {
        return convert(source);
    }

    @Override
    public Properties convert(List<String> source) throws ConverterException {
        Assert.notNull(source, "source list must not be null!");
        Assert.isTrue(source.size() % 2 == 0, "source list must contain an even number of entries!");
        Properties properties = new Properties();
        for (int i = 0; i < source.size(); i += 2) {
            properties.setProperty(source.get(i), source.get(i + 1));
        }
        return properties;
    }
}