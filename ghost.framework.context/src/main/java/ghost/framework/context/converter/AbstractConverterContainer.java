package ghost.framework.context.converter;

import ghost.framework.context.collections.generic.AbstractSyncSmartList;

import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转化器容器基础类
 * @Date: 2020/6/25:12:27
 */
public abstract class AbstractConverterContainer<T extends Converter> extends AbstractSyncSmartList<T> implements ConverterContainer<T> {
    /**
     * 初始化转换器工厂容器类
     *
     * @param parent 父级转换器工厂容器接口
     */
    protected AbstractConverterContainer(ConverterContainer parent) {
        super(5);
        this.parent = parent;
    }

    /**
     * 初始化转换器工厂容器类
     */
    protected AbstractConverterContainer(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 初始化转换器工厂容器类
     */
    protected AbstractConverterContainer() {
        super(5);
    }

    /**
     * 获取目标类型转换器
     *
     * @param targetTypeConverter 目标转换器类型
     * @param <R>                 返回目标类型转换器类型
     * @return 返回目标类型转换器对象
     */
    @Override
    public <R extends TypeConverter> R getTypeConverter(Class<T> targetTypeConverter) {
        for (Converter converter : this.getList()) {
            if (converter instanceof TypeConverter) {
                if (targetTypeConverter.isAssignableFrom(converter.getClass())) {
                    return (R) converter;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否可以转换为目标类型
     *
     * @param targetType
     * @return
     */
    @Override
    public boolean isSimpleType(Class<?> targetType) {
        return this.getTargetTypeConverter(targetType) != null;
    }

    @Override
    public <R extends TypeConverter> R getTargetTypeConverter(Class<?> targetType) {
        R c1 = null;
        for (Converter converter : this.getList()) {
            if (converter instanceof TypeConverter) {
                TypeConverter typeConverter = (TypeConverter) converter;
                if (typeConverter.getTargetType() != null && typeConverter.getSourceType() == null) {
                    for (Class<?> c : typeConverter.getTargetType()) {
                        if (c.equals(targetType)) {
                            c1 = (R) converter;
                            break;
                        }
                    }
                }
            }
            if (c1 == null) {
                continue;
            }
        }
        if (c1 == null && this.parent != null) {
            c1 = (R) this.parent.getTargetTypeConverter(targetType);
        }
        return c1;
    }

    /**
     * 父级转换工厂容器接口
     * 一般为应用容器的转换工厂容器接口
     */
    private ConverterContainer parent;

    /**
     * @param targetType        返回类型
     * @param source            转换源
     * @param characterEncoding 指定编码
     * @param <S>
     * @param <R>
     * @return
     * @throws ConverterException
     */
    @Override
    public <S, R> R convert(Class<?> targetType, S source, String characterEncoding) throws ConverterException {
        T t = this.getTargetTypeConverter(targetType);
        if (t instanceof EncodingTypeConverter) {
            return (R) ((EncodingTypeConverter) t).convert(source, characterEncoding);
        }
        return (R) ((TypeConverter) t).convert(source);
    }

    /**
     * @param targetType 返回类型
     * @param source     转换源
     * @param <S>
     * @param <R>
     * @return
     * @throws ConverterException
     */
    @Override
    public <S, R> R convert(Class<?> targetType, S source) throws ConverterException {
        return (R) ((TypeConverter) this.getTargetTypeConverter(targetType)).convert(source);
    }

    /**
     * @param sourceType 源类型
     * @param targetType 返回类型
     * @return
     */
    @Override
    public T getTargetTypeConverter(Class<?> sourceType, Class<?> targetType) {
        List<T> list = this.getTargetTypes(targetType);
        for (T t : list) {
            if (t instanceof TypeConverter) {
                TypeConverter typeConverter = (TypeConverter) t;
                if (typeConverter.getSourceType() != null) {
                    for (Class<?> c : typeConverter.getSourceType()) {
                        if (c.equals(sourceType)) {
                            return t;
                        }
                    }
                }
            }
        }
        if (this.parent != null) {
            return (T) this.parent.getTargetTypeConverter(sourceType, targetType);
        }
        return null;
    }

    /**
     * @param sourceType        源类型
     * @param targetType        返回类型
     * @param source            转换源
     * @param characterEncoding 指定编码
     * @param <S>
     * @param <R>
     * @return
     * @throws ConverterException
     */
    @Override
    public <S, R> R convert(Class<?> sourceType, Class<?> targetType, S source, String characterEncoding) throws ConverterException {
        T t = this.getTargetTypeConverter(sourceType, targetType);
        if (t instanceof EncodingTypeConverter) {
            return (R) ((EncodingTypeConverter) t).convert(source, characterEncoding);
        }
        return (R) ((TypeConverter) t).convert(source);
//        return (R) t.convert(source);
    }

    /**
     * @param sourceType 源类型
     * @param targetType 返回类型
     * @param source     转换源
     * @param <S>
     * @param <R>
     * @return
     * @throws ConverterException
     */
    @Override
    public <S, R> R convert(Class<?> targetType, Class<?> sourceType, S source) throws ConverterException {
        return (R) ((TypeConverter) this.getTargetTypeConverter(sourceType, targetType)).convert(source);
    }

    /**
     * 按照都西
     *
     * @param source
     * @param targetType
     * @param <R>
     * @return
     */
    @Override
    public <R> R convert(Object source, Class<?> targetType) {
        return (R) ((TypeConverter) this.getTargetTypeConverter(source.getClass(), targetType)).convert(source);
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        List<T> list = this.getTargetTypes(targetType);
        for (T t : list) {
            if (t instanceof TypeConverter) {
                TypeConverter typeConverter = (TypeConverter) t;
                if (typeConverter.getSourceType() != null) {
                    for (Class<?> c : typeConverter.getSourceType()) {
                        if (c.equals(sourceType)) {
                            return true;
                        }
                    }
                }
            }
        }
        if (this.parent != null) {
            return this.parent.canConvert(sourceType, targetType);
        }
        return false;
    }

    @Override
    public <R> R convert(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return null;
    }

    @Override
    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return false;
    }

    @Override
    public List<T> getSourceTypes(Class<?> sourceType) {
        List<T> list = new ArrayList<>();
        for (T t : this.getList()) {
            if (t instanceof TypeConverter) {
                TypeConverter typeConverter = (TypeConverter) t;
                if (typeConverter.getSourceType() != null) {
                    for (Class<?> c : typeConverter.getSourceType()) {
                        if (c.equals(sourceType)) {
                            list.add(t);
                            break;
                        }
                    }
                }
            }
        }
        if (this.parent != null) {
            list.addAll(this.parent.getSourceTypes(sourceType));
        }
        return list;
    }

    /**
     * 获取目标类型列表
     *
     * @param targetType
     * @return
     */
    @Override
    public List<T> getTargetTypes(Class<?> targetType) {
        List<T> list = new ArrayList<>();
        for (T t : this.getList()) {
            if (t instanceof TypeConverter) {
                TypeConverter typeConverter = (TypeConverter) t;
                if (typeConverter.getTargetType() != null) {
                    for (Class<?> c : typeConverter.getTargetType()) {
                        if (c.equals(targetType)) {
                            list.add(t);
                            break;
                        }
                    }
                }
            }
        }
        if (this.parent != null) {
            list.addAll(this.parent.getTargetTypes(targetType));
        }
        return list;
    }
}