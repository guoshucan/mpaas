package ghost.framework.context.converter.json;

import ghost.framework.context.converter.json.JsonConverter;

import java.io.IOException;
import java.util.Map;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/14:19:16
 */
public interface MapToJsonConverter extends JsonConverter {
    <K, V> String toString(Map<K, V> map) throws IOException;
}
