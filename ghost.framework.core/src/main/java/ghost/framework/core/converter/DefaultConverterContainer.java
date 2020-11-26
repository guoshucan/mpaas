package ghost.framework.core.converter;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.converter.AbstractConverterContainer;
import ghost.framework.context.converter.TypeConverter;
import ghost.framework.context.converter.ConverterContainer;

/**
 * package: ghost.framework.core.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转换器工厂容器类
 * @Date: 2020/2/4:17:19
 */
@Component
public class DefaultConverterContainer<T extends TypeConverter> extends AbstractConverterContainer<T> {
    /**
     * 初始化转换器工厂容器类
     *
     * @param parent 父级转换器工厂容器接口
     */
    public DefaultConverterContainer(@Application @Autowired @Nullable ConverterContainer parent) {
        super(parent);
    }
}