package ghost.framework.context.converter;

import ghost.framework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/12:20:51
 */
public interface EncodingTypeConverter<S, T> extends TypeConverter<S, T> {
    /**
     * 指定字符编码转换类型
     *
     * @param source            转换源
     * @param characterEncoding 转换字符编码
     * @return 返回转换后对象
     * @throws ConverterException
     */
    default T convert(S source, String characterEncoding) throws ConverterException {
        try {
            if (StringUtils.isEmpty(characterEncoding)) {
                return this.convert(source);
            }
            return this.convert((S) new String(String.valueOf(source).getBytes(characterEncoding)));
        } catch (UnsupportedEncodingException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}
