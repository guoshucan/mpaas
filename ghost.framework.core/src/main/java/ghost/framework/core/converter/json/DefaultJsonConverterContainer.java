package ghost.framework.core.converter.json;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.collections.generic.AbstractSyncSmartList;
import ghost.framework.context.converter.json.JsonConverter;
import ghost.framework.context.converter.json.JsonConverterContainer;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转化器容器基础类
 * @Date: 2020/6/25:12:27
 */
public class DefaultJsonConverterContainer extends AbstractSyncSmartList<JsonConverter> implements JsonConverterContainer {
    /**
     * 初始化转换器工厂容器类
     *
     * @param parent 父级转换器工厂容器接口
     */
    @Constructor
    public DefaultJsonConverterContainer(@Application @Autowired(required = false) JsonConverterContainer parent) {
        super(5);
        this.parent = parent;
    }

    /**
     * 初始化转换器工厂容器类
     */
    public DefaultJsonConverterContainer() {
        super(5);
    }

    @Override
    public <R extends JsonConverter> R getConverter(Class<? extends JsonConverter> c) {
        for (JsonConverter converter : this.getList()) {
            if (converter.getClass().equals(c) || c.isAssignableFrom(converter.getClass())) {
                return (R) converter;
            }
        }
        if (this.parent != null) {
            return this.parent.getConverter(c);
        }
        return null;
    }

    @Override
    public <R extends JsonConverter> R getReturnTypeConverter(Class<?> returnType) {
        R c1 = null;
        for (JsonConverter converter : this.getList()) {
            if (converter.getTargetType().equals(returnType)) {
                c1 = (R) converter;
                break;
            }
            if (c1 == null) {
                continue;
            }
        }
        if (c1 == null && this.parent != null) {
            c1 = (R) this.parent.getReturnTypeConverter(returnType);
        }
        return c1;
    }

    /**
     * 父级转换工厂容器接口
     * 一般为应用容器的转换工厂容器接口
     */
    private JsonConverterContainer parent;
}
