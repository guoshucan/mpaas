package ghost.framework.context.converter;
import ghost.framework.context.IGetDomain;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转换器基础接口
 * @Date: 2020/2/4:16:56
 * @param <S> 转换源
 * @param <T> 转换类型
 */
public interface TypeConverter<S, T> extends Converter, IGetDomain {
    /**
     * 判断是否可以转换
     * ，
     *
     * @param source
     * @return
     */
    default boolean canConvert(S source) {
        return false;
    }

    /**
     * 获取当前转换器支持的转换源类型
     *
     * @return
     */
    default Class<?>[] getSourceType() {
        return null;
    }

    /**
     * 转化目标类型
     *
     * @return
     */
    default Class<?>[] getTargetType() {
        return null;
    }

    /**
     * 转换类型
     *
     * @param source 转换源
     * @return 返回转换后对象
     * @throws ConverterException
     */
    T convert(S source) throws ConverterException;
}