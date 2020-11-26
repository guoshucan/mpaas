package ghost.framework.web.angular1x.context.router;

import ghost.framework.beans.annotation.stereotype.Component;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web菜单路由容器
 * @Date: 2020/3/13:19:45
 */
@Component
public class WebMenuRouterContainer extends AbstractMap<String, IWebRouterMenuGroup> implements IWebMenuRouterContainer {
    /**
     * 路由地图
     * @see IWebRouterMenuGroup
     * @see IWebRouterNavMenuGroup
     * @see IWebRouterHeaderMenuGroup
     */
    private Map<String, IWebRouterMenuGroup> map = new LinkedHashMap();

    @Override
    public Set<Entry<String, IWebRouterMenuGroup>> entrySet() {
        return map.entrySet();
    }

    /**
     * 添加路由地图
     * @param key
     * @param value
     * @return
     */
    @Override
    public IWebRouterMenuGroup put(String key, IWebRouterMenuGroup value) {
        return map.put(key, value);
    }
}