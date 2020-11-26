package ghost.framework.json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.bean.annotation.json.JsonConverterFactory;
import ghost.framework.context.converter.json.ObjectToJsonConverter;

import java.io.IOException;

/**
 * package: ghost.framework.json.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:34
 */
@JsonConverterFactory
public class DefaultObjectToJsonConverter implements ObjectToJsonConverter {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public String toString(Object o) throws IOException {
        return this.mapper.writeValueAsString(o);
    }
}
