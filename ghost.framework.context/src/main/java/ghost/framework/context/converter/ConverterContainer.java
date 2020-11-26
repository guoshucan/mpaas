package ghost.framework.context.converter;

import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.thread.GetSyncRoot;

import java.util.List;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转换器工厂容器接口
 * @Date: 2020/2/4:17:21
 */
public interface ConverterContainer<T extends Converter> extends ISmartList<T>, GetSyncRoot {
    /**
     * 获取目标类型转换器
     * @param targetTypeConverter 目标转换器类型
     * @param <R> 返回目标类型转换器类型
     * @return 返回目标类型转换器对象
     */
    <R extends TypeConverter> R getTypeConverter(Class<T> targetTypeConverter);

    /**
     * 获取指定返回类型的转换器
     *
     * @param targetType 目标类型
     * @return
     */
    <R extends TypeConverter> R getTargetTypeConverter(Class<?> targetType);

    /**
     * 获取指定返回类型与源类型转换器
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return
     */
    T getTargetTypeConverter(Class<?> sourceType, Class<?> targetType);

    /**
     * 指定类型转换器
     *
     * @param sourceType        源类型
     * @param targetType        目标类型
     * @param source            转换源
     * @param characterEncoding 指定编码
     * @param <S>               源类型
     * @param <R>               目标类型
     * @return
     * @throws ConverterException
     */
    <S, R> R convert(Class<?> sourceType, Class<?> targetType, S source, String characterEncoding) throws ConverterException;

    /**
     * 指定类型转换器
     *
     * @param targetType        目标类型
     * @param source            转换源
     * @param characterEncoding 指定编码
     * @param <S>               源类型
     * @param <R>               目标类型
     * @return
     * @throws ConverterException
     */
    <S, R> R convert(Class<?> targetType, S source, String characterEncoding) throws ConverterException;

    /**
     * 指定类型转换器
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @param source     转换源
     * @param <S>        源类型
     * @param <R>        目标类型
     * @return
     * @throws ConverterException
     */
    <S, R> R convert(Class<?> sourceType, Class<?> targetType, S source) throws ConverterException;

    /**
     * 指定类型转换器
     *
     * @param targetType 目标类型
     * @param source     转换源
     * @param <S>        源类型
     * @param <R>        目标类型
     * @return
     * @throws ConverterException
     */
    <S, R> R convert(Class<?> targetType, S source) throws ConverterException;

    /**
     * 判断源转换目标类型是否可以
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return
     */
    default boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return this.getTargetTypeConverter(sourceType, targetType) != null;
    }

    /**
     * 判断是否可以转换为目标类型
     *
     * @param targetType
     * @return
     */
    boolean isSimpleType(Class<?> targetType);

    /**
     * @param source
     * @param targetType
     * @param <R>
     * @return
     */
    <R> R convert(Object source, Class<?> targetType);
    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);
    <R> R convert(Object value, TypeDescriptor sourceType, TypeDescriptor targetType);
    /**
     * 获取目标类型转换器列表
     * @param targetType
     * @return
     */
    List<T> getTargetTypes(Class<?> targetType);
    /**
     * 获取源类型转换器列表
     * @param sourceType
     * @return
     */
    List<T> getSourceTypes(Class<?> sourceType);
}