package ghost.framework.context.converter.primitive;

import ghost.framework.context.converter.TypeConverter;
import ghost.framework.context.converter.ConverterException;

import java.text.DateFormat;

/**
 * package: ghost.framework.context.converter.primitive
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/21:19:09
 */
public interface ConverterDate<S, T> extends TypeConverter<S, T> {
    T convert(S source, DateFormat format, String characterEncoding) throws ConverterException;
    T convert(S source, DateFormat format) throws ConverterException;
}