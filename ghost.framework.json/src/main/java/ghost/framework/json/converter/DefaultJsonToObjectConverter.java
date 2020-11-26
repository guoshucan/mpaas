package ghost.framework.json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.bean.annotation.json.JsonConverterFactory;
import ghost.framework.context.converter.json.JsonToObjectConverter;

import java.io.IOException;

/**
 * package: ghost.framework.json.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:33
 */
@JsonConverterFactory
public class DefaultJsonToObjectConverter implements JsonToObjectConverter {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public Object toObject(String s) throws IOException {
        return this.mapper.readValue(s, Object.class);
    }
    @Override
    public <O> O toObject(String s, Class<O> c) throws IOException {
        return this.mapper.readValue(s, c);
    }
}
