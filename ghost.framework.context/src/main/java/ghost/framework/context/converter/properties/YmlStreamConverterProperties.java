package ghost.framework.context.converter.properties;

import ghost.framework.context.converter.TypeConverter;

import java.io.InputStream;
import java.util.Properties;

/**
 * package: ghost.framework.context.converter.properties
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link InputStream} to {@link Properties}
 * @Date: 2020/6/25:10:30
 */
public interface YmlStreamConverterProperties<S extends InputStream, T extends Properties> extends TypeConverter<S, T> {
}
