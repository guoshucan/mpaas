package ghost.framework.util.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.util.MapMergeUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * package: ghost.framework.util
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/15:22:49
 */
public class MapMergeUtilTests {
    @Test
    public void merge() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String s0 = "{\n" +
                "  \"header\": {\n" +
                "  },\n" +
                "  \"aside\": {\n" +
                "    \"nav\": {\n" +
                "      \"SystemManagement\": {\n" +
                "        \"Admin\": {\n" +
                "          \"Setting\": {\n" +
                "            \"Name\": \"Setting\",\n" +
                "            \"Title\": \"System settings\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String s1 = "{\n" +
                "  \"header\": {\n" +
                "    \"navbar\": {\n" +
                "    }\n" +
                "  },\n" +
                "  \"aside\": {\n" +
                "    \"nav\": {\n" +
                "      \"SystemManagement\": {\n" +
                "        \"Name\": \"System Management\",\n" +
                "        \"Admin\": {\n" +
                "          \"Name\": \"Admin\",\n" +
                "          \"AdminGroup\": {\n" +
                "            \"Name\": \"Admin Group\"\n" +
                "          },\n" +
                "          \"Admin\": {\n" +
                "            \"Name\": \"Admin Management\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        Map<String, Map<Object, Object>> map0 = new HashMap<>();
        map0.put("en", mapper.readValue(s0, HashMap.class));
        Map<String, Map<Object, Object>> map1 = new HashMap<>();
        map1.put("en", mapper.readValue(s1, HashMap.class));
        MapMergeUtil.merge(map0, map1);
        String s = mapper.writeValueAsString(map0);
        System.out.println(s);
    }
}