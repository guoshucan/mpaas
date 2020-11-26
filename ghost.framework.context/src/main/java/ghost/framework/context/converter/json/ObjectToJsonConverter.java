package ghost.framework.context.converter.json;

import ghost.framework.context.converter.json.JsonConverter;

import java.io.IOException;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:34
 */
public interface ObjectToJsonConverter extends JsonConverter {
    String toString(Object o) throws IOException;
}
