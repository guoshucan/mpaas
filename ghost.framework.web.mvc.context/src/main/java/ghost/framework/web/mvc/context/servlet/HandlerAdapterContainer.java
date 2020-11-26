package ghost.framework.web.mvc.context.servlet;

import ghost.framework.web.mvc.context.handler.HandlerAdapter;

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
 * @Date: 2020/2/13:21:07
 */
public class HandlerAdapterContainer extends AbstractMap<String, HandlerAdapter>  implements IHandlerAdapterContainer {
    private Map<String, HandlerAdapter> map = new HashMap<>();

    @Override
    public Set<Entry<String, HandlerAdapter>> entrySet() {
        return map.entrySet();
    }

    @Override
    public HandlerAdapter put(String key, HandlerAdapter value) {
        return map.put(key, value);
    }
}
