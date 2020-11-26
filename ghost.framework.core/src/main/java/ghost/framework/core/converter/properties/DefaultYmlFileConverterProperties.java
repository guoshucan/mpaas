package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.properties.YmlFileConverterProperties;
import ghost.framework.context.converter.properties.YmlStreamConverterProperties;
import ghost.framework.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@see Yml} 文件转 {@link Properties} 工厂
 * @Date: 2020/2/4:17:50
 * @param <S>    转换源对象
 * @param <T>    转换返回对象
 */
@ConverterFactory
public class DefaultYmlFileConverterProperties<S extends File, T extends Properties>
        extends AbstractConverter<S, T>
        implements YmlFileConverterProperties<S, T>, EncodingTypeConverter<S, T> {
    private final Class<?>[] sourceOriented = new Class[]{File.class};

    @Override
    public String toString() {
        return "DefaultYmlFileConverterProperties{" +
                "sourceOriented=" + Arrays.toString(sourceOriented) +
                ", type=" + Arrays.toString(type) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultYmlFileConverterProperties<?, ?> that = (DefaultYmlFileConverterProperties<?, ?>) o;
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

    /**
     * 是否可以转换
     *
     * @param source 转换源
     * @return
     */
    @Override
    public boolean canConvert(S source) {
        String name = FileUtil.getExtensionName(source.getName()).toLowerCase();
        return name.equals("yml") || name.equals("yaml");
    }

    @Autowired
    private ConverterContainer container;

    @Override
    public T convert(S source) throws ConverterException {
        return (T) this.container.getTypeConverter(YmlStreamConverterProperties.class).convert(source);
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
        return this.convert(source);
    }
}