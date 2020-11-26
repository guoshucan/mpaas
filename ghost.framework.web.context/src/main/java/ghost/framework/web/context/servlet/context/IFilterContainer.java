package ghost.framework.web.context.servlet.context;

import javax.servlet.Filter;
import java.util.Map;

/**
 * package: ghost.framework.web.context.servlet.context
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/13:11:10
 */
public interface IFilterContainer extends Map<String, Filter> {
    @Override
    default boolean remove(Object key, Object value) {
        return this.remove(key) != null;
    }
}
