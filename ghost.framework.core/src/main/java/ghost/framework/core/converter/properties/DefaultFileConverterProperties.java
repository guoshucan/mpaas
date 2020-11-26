package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.properties.FileConverterProperties;
import ghost.framework.context.converter.properties.StreamConverterProperties;
import ghost.framework.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link File} 转 {@link Properties} 工厂
 * @Date: 2020/2/4:17:26
 * @param <S>    转换源对象
 * @param <T>    转换返回对象
 */
@ConverterFactory
public class DefaultFileConverterProperties<S extends File, T extends Properties>
        extends AbstractConverter<S, T>
        implements FileConverterProperties<S, T>, EncodingTypeConverter<S, T> {
    private final Class<?>[] sourceOriented = new Class[]{File.class};

    @Override
    public String toString() {
        return "DefaultFileConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultFileConverterProperties<?, ?> that = (DefaultFileConverterProperties<?, ?>) o;
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
    public Class<?>[] getSourceType() {
        return sourceOriented;
    }

    private final Class<?>[] targetType = {Properties.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    /**
     * 是否可以转换
     *
     * @param source 转换源
     * @return
     */
    @Override
    public boolean canConvert(S source) {
        if (!source.exists()) {
            return false;
        }
        return FileUtil.getExtensionName(source.getName()).toLowerCase().equals("properties");
    }

    @Autowired
    private ConverterContainer container;

    @Override
    public T convert(S source) throws ConverterException {
        //获取文件流
        try (InputStream stream = new FileInputStream(source)) {
            return (T) this.container.getTypeConverter(StreamConverterProperties.class).convert(stream);
        } catch (IOException e) {
            throw new ConverterException(source, e.getMessage(), e);
        }
    }

    /**
     * 转换源
     *
     * @param source 转换源
     * @return 返回转换对象
     * @throws ConverterException
     */
    @Override
    public T convert(S source, String characterEncoding) throws ConverterException {
        //获取文件流
        try (InputStream stream = new FileInputStream(source)) {
            return (T) ((EncodingTypeConverter)this.container.getTypeConverter(StreamConverterProperties.class)).convert(stream, characterEncoding);
        } catch (IOException e) {
            throw new ConverterException(source, e.getMessage(), e);
        }
    }
}