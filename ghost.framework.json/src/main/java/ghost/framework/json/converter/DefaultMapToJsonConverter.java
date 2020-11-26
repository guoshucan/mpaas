package ghost.framework.json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.bean.annotation.json.JsonConverterFactory;
import ghost.framework.context.converter.json.MapToJsonConverter;

import java.io.IOException;
import java.util.Map;

/**
 * package: ghost.framework.json.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/14:19:17
 */
@JsonConverterFactory
public class DefaultMapToJsonConverter implements MapToJsonConverter {
//    @Autowired
//    private Gson gson;
    @Autowired
    private ObjectMapper mapper;
    @Override
    public <K, V> String toString(Map<K, V> map) throws IOException {
//        return gson.toJson(map, String.class);
        return mapper.writeValueAsString(map);
    }
}
