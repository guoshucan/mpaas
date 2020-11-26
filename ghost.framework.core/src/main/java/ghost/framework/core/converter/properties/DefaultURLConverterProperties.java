package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.*;
import ghost.framework.context.converter.properties.StreamConverterProperties;
import ghost.framework.context.converter.properties.URLConverterProperties;
import ghost.framework.context.converter.properties.YmlStreamConverterProperties;
import ghost.framework.util.FileUtil;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link URL} 转 {@link Properties} 工厂
 * @Date: 2020/2/4:17:49
 * @param <S>    转换源对象
 * @param <T>    转换返回对象
 */
@ConverterFactory
public class DefaultURLConverterProperties<S extends URL, T extends Properties>
        extends AbstractConverter<S, T>
        implements URLConverterProperties<S, T>, EncodingTypeConverter<S, T> {
    private final Class<?>[] sourceOriented = new Class[]{URL.class};

    @Override
    public String toString() {
        return "DefaultURLConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", type=" + Arrays.toString(type) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultURLConverterProperties<?, ?> that = (DefaultURLConverterProperties<?, ?>) o;
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

    @Autowired
    private ConverterContainer container;

    /**
     * 是否可以转换
     *
     * @param source 转换源
     * @return
     */
    @Override
    public boolean canConvert(S source) {
        String path = source.getPath().toLowerCase();
        String extensionName = FileUtil.getExtensionName(path).toLowerCase();
        if (extensionName.equals("properties")) {
            this.current = this.container.getTypeConverter(StreamConverterProperties.class);
            return true;
        }
        if (extensionName.equals("yml") || extensionName.equals("yaml")) {
            this.current = this.container.getTypeConverter(YmlStreamConverterProperties.class);
        }
        return false;
    }

    /**
     * 当前转换器
     */
    private TypeConverter<InputStream, ?> current;

    @Override
    public T  convert(S source) throws ConverterException {
        if (this.canConvert(source)) {
            //获取文件流
            try (InputStream stream = source.openStream()) {
                return (T) this.current.convert(stream);
            } catch (Exception e) {
                throw new ConverterException(source, e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 转换源
     *
     * @param source 转换源
     * @return 返回转换对象
     * @throws ConverterException
     */
    @Override
    public synchronized T convert(S source, String characterEncoding) throws ConverterException {
        if (this.canConvert(source)) {
            //获取文件流
            try (InputStream stream = source.openStream()) {
                return (T) ((EncodingTypeConverter)this.current).convert(stream, characterEncoding);
            } catch (Exception e) {
                throw new ConverterException(source, e.getMessage(), e);
            }
        }
        return null;
    }
}