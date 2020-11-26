package ghost.framework.json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.bean.annotation.json.JsonConverterFactory;
import ghost.framework.context.converter.json.JsonToMapConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * package: ghost.framework.json.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/9:23:10
 */
@JsonConverterFactory
public class DefaultJsonToMapConverter implements JsonToMapConverter {
    //    @Autowired
//    private Gson gson;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public <K, V> Map<K, V> toMap(String json) throws IOException {
        return mapper.readValue(json, HashMap.class);
    }

    @Override
    public <K, V> Map<K, V> toMap(String json, Class<? extends Map> map) throws IOException {
        return mapper.readValue(json, map);
    }
}
