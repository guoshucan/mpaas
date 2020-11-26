package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.utils.OrderAnnotationUtil;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.context.IFilterContainer;

import javax.servlet.Filter;
import java.util.*;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:过滤器容器
 * @Date: 2020/2/13:11:09
 */
@Component
public class FilterContainer extends AbstractMap<String, Filter> implements IFilterContainer {
    public FilterContainer(@Autowired IModule module){

    }
    private Map<String, Filter> map = new LinkedHashMap<>();

    @Override
    public Set<Entry<String, Filter>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Filter remove(Object key) {
        Filter b = map.remove(key);
        OrderAnnotationUtil.mapValueSort(map);
        return b;
    }

    @Override
    public Filter put(String key, Filter value) {
        synchronized (map) {
            map.put(key, value);
            OrderAnnotationUtil.mapValueSort(map);
        }
        return value;
    }
}