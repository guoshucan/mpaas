package ghost.framework.web.angular1x.context.router;

import ghost.framework.beans.annotation.stereotype.Component;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由容器
 * @Date: 2020/5/22:21:09
 */
@Component
public class WebRouterContainer extends AbstractMap<String, IWebRouterItem> implements IWebRouterContainer{
    private Map<String, IWebRouterItem> map = new LinkedHashMap<>();

    @Override
    public IWebRouterItem put(String key, IWebRouterItem value) {
        return map.put(key, value);
    }

    @Override
    public Set<Entry<String, IWebRouterItem>> entrySet() {
        return map.entrySet();
    }
}
