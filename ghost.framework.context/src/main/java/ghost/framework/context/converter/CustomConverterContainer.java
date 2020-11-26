package ghost.framework.context.converter;

/**
 * package: ghost.framework.context.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:自动转换器容器
 * @Date: 2020/6/26:12:37
 */
public class CustomConverterContainer<T extends TypeConverter> extends AbstractConverterContainer<T> {
    /**
     * 初始化转换器工厂容器类
     *
     * @param parent 父级转换器工厂容器接口
     */
    public CustomConverterContainer(ConverterContainer parent) {
        super(parent);
    }

    /**
     * 初始化转换器工厂容器类
     */
    public CustomConverterContainer() {
        super();
    }

    /**
     * 初始化转换器工厂容器类
     */
    public CustomConverterContainer(int initialCapacity) {
        super(initialCapacity);
    }
}