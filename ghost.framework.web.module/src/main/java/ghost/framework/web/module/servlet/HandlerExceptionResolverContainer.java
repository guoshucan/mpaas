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
 * @Date: 2020/2/13:21:08
 */
public class HandlerExceptionResolverContainer extends AbstractMap<String, Object>  implements IHandlerExceptionResolverContainer {
    private Map<String, Object> map= new HashMap<>();

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }
}
