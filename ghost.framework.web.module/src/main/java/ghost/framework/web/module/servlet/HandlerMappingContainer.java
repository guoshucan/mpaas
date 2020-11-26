package ghost.framework.web.module.servlet;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/13:21:05
 */
public class HandlerMappingContainer extends AbstractMap<String, HandlerMapping> implements IHandlerMappingContainer {
    private Map<String, HandlerMapping> map = new HashMap<>();

    @Override
    public Set<Entry<String, HandlerMapping>> entrySet() {
        return map.entrySet();
    }

    @Override
    public HandlerMapping put(String key, HandlerMapping value) {
        return map.put(key, value);
    }
}
