package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.properties.YmlStreamConverterProperties;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: Yml {@link InputStream} 转 {@link Properties} 工厂
 * @Date: 2020/2/4:18:04
 */
@ConverterFactory
public class DefaultYmlStreamConverterProperties
        <S extends InputStream, T extends Properties>
        extends AbstractConverter<S, T>
        implements YmlStreamConverterProperties<S, T>, EncodingTypeConverter<S, T> {
    private final Class<?>[] sourceOriented = new Class[]{InputStream.class};

    @Override
    public String toString() {
        return "DefaultYmlStreamConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", type=" + Arrays.toString(type) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultYmlStreamConverterProperties<?, ?> that = (DefaultYmlStreamConverterProperties<?, ?>) o;
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
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.loadAs(source, Map.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue().toString());
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
        return this.convert(source);
    }
}