package ghost.framework.data.cache.transaction;

import java.util.Collection;
import java.util.List;

/**
 * package: ghost.framework.data.cache.transaction
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/26:0:35
 */
public interface CacheTransactionManager {
    List<String> getCacheNames();

    <C extends Cache> C lookupCache(String name);

    <C extends Cache> Collection<C> loadCaches();

    <C extends Cache> C getMissingCache(String name);
}