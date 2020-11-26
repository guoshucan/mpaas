package ghost.framework.context.converter.json;

import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.thread.GetSyncRoot;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:json转换容器接口
 * {@link ISmartList< JsonConverter >} 简单列表接口实现
 * {@link JsonConverter}
 * @Date: 2020/6/9:21:34
 */
public interface JsonConverterContainer extends ISmartList<JsonConverter>, GetSyncRoot {
    <R extends JsonConverter> R getConverter(Class<? extends JsonConverter> c);
    <R extends JsonConverter> R getReturnTypeConverter(Class<?> returnType);
}
