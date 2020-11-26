package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.properties.StreamConverterProperties;
import ghost.framework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link InputStream} 转 {@link Properties} 工厂
 * @Date: 2020/2/4:18:04
 */
@ConverterFactory
public class DefaultStreamConverterProperties
        <S extends InputStream, T extends Properties>
        extends AbstractConverter<S, T>
        implements StreamConverterProperties<S, T>, EncodingTypeConverter<S, T> {
    private final Class<?>[] sourceOriented = new Class[]{InputStream.class};

    @Override
    public String toString() {
        return "DefaultStreamConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", type=" + Arrays.toString(type) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultStreamConverterProperties<?, ?> that = (DefaultStreamConverterProperties<?, ?>) o;
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
    public T convert(S source) throws ConverterException {
        //创建配置
        Properties properties = new Properties();
        //加载文件流数据
        try {
            properties.load(new InputStreamReader(source, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ConverterException(e.getMessage(), e);
        }
        return (T) properties;
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
        //创建配置
        Properties properties = new Properties();
        //加载文件流数据
        try {
            if (StringUtils.isEmpty(characterEncoding)) {
                return this.convert(source);
            } else {
                properties.load(new InputStreamReader(source, characterEncoding));
            }
        } catch (IOException e) {
            throw new ConverterException(e.getMessage(), e);
        }
        return (T) properties;
    }
}