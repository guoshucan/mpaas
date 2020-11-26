package ghost.framework.context.converter.json;

import java.io.IOException;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:34
 */
public interface JsonToObjectConverter extends JsonConverter {
    Object toObject(String s) throws IOException;
    <O> O toObject(String s, Class<O> c) throws IOException;
}
