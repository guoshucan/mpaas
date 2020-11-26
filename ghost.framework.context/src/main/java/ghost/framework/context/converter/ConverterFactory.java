package ghost.framework.context.converter;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转换工厂接口
 * @Date: 2020/6/26:13:10
 */
public interface ConverterFactory<S, R> extends Converter{
    /**
     * 获取转换器
     * @param targetType
     * @param <T>
     * @return
     */
    <T extends R> TypeConverter<S, T> getConverter(Class<T> targetType);
}