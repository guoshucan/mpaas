package ghost.framework.core.converter.properties;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.*;
import ghost.framework.context.converter.properties.ResourceBytesConverterProperties;
import ghost.framework.context.converter.properties.StreamConverterProperties;
import ghost.framework.context.io.ResourceBytes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link ResourceBytes} to {@link Properties}
 * @Date: 2020/3/18:19:44
 */
@ConverterFactory
public class DefaultResourceBytesConverterProperties
        <S extends ResourceBytes, T extends Properties>
        extends AbstractConverter<S, T>
        implements ResourceBytesConverterProperties<S, T>, EncodingTypeConverter<S, T> {
    private final Class<?>[] sourceOriented = new Class[]{ResourceBytes.class};

    @Override
    public Class<?>[] getSourceType() {
        return sourceOriented;
    }

    private final Class<?>[] targetType = {Properties.class};

    @Override
    public String toString() {
        return "DefaultResourceBytesConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultResourceBytesConverterProperties<?, ?> that = (DefaultResourceBytesConverterProperties<?, ?>) o;
        return Arrays.equals(sourceOriented, that.sourceOriented) &&
                Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(sourceOriented);
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Autowired
    private ConverterContainer container;

    @Override
    public T convert(S source) throws ConverterException {
        //加载文件流数据
        try (ByteArrayInputStream stream = new ByteArrayInputStream(source.getBytes())) {
            return (T) this.container.getTypeConverter(StreamConverterProperties.class).convert(stream);
        } catch (IOException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }

    /**
     * 转换源
     *
     * @param source 转换源
     * @return
     * @throws ConverterException
     */
    @Override
    public T convert(S source, String characterEncoding) throws ConverterException {
        //加载文件流数据
        try (ByteArrayInputStream stream = new ByteArrayInputStream(source.getBytes())) {
            return (T) ((EncodingTypeConverter) this.container.getTypeConverter(StreamConverterProperties.class)).convert(stream, characterEncoding);
        } catch (IOException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}