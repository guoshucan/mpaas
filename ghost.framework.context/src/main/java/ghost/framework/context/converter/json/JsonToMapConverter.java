package ghost.framework.context.converter.json;

import java.io.IOException;
import java.util.Map;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/9:23:02
 */
public interface JsonToMapConverter extends JsonConverter {
    <K, V> Map<K, V> toMap(String json) throws IOException;
    <K, V> Map<K, V> toMap(String json, Class<? extends Map> map) throws IOException;
}
