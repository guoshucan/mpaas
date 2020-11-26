package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.properties.MapConverterProperties;

import java.util.Map;
import java.util.Properties;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link  Map<?, ?>} to {@link Properties}
 * @Date: 2020/6/26:12:07
 */
@ConverterFactory
public class DefaultMapConverterProperties implements MapConverterProperties<Map<?,?>, Properties> {
    @Override
    public Properties convert(Map<?, ?> source) {
        Properties target = new Properties();
        target.putAll(source);
        return target;
    }
}
