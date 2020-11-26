package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.*;
import ghost.framework.context.converter.properties.ConverterProperties;
import ghost.framework.context.converter.properties.FileConverterProperties;
import ghost.framework.context.converter.properties.URLConverterProperties;
import ghost.framework.context.converter.properties.YmlFileConverterProperties;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

/**
 * package: ghost.framework.core.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/4:21:10
 */
@ConverterFactory
public class DefaultConverterProperties
        <S extends Object, T extends Properties>
        extends AbstractConverter<S, T>
        implements ConverterProperties<S, T> {
    @Constructor
    public DefaultConverterProperties(@Autowired(required = false) ICoreInterface domain) {
        super(domain);
    }

    public DefaultConverterProperties() {
        super();
    }

    @Override
    public String toString() {
        return "DefaultConverterProperties{" +
                "targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultConverterProperties<?, ?> that = (DefaultConverterProperties<?, ?>) o;
        return Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] targetType = {Properties.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Autowired
    private ConverterContainer container;

    /**
     * 判断是否可以转换
     *
     * @param source 转换源
     * @return
     */
    @Override
    public boolean canConvert(S source) {
        if (source instanceof String || source instanceof File) {
            this.current = this.container.getTypeConverter(FileConverterProperties.class);
            if (this.current.canConvert(source instanceof String ? new File(source.toString()) : (File) source)) {
                return true;
            }
        }
        if (source instanceof URL) {
            this.current = this.container.getTypeConverter(URLConverterProperties.class);
            if (this.current.canConvert(source)) {
                return true;
            }
        }
        if (source instanceof String || source instanceof File) {
            this.current = this.container.getTypeConverter(YmlFileConverterProperties.class);
            if (this.current.canConvert(source instanceof String ? new File(source.toString()) : (File) source)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前转换器
     */
    private TypeConverter current;

    @Override
    public T convert(S source) throws ConverterException {
        if (this.canConvert(source)) {
            return (T) current.convert(source);
        }
        return null;
    }
}