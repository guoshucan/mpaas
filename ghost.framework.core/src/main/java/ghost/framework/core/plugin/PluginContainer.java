package ghost.framework.core.plugin;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.plugin.IPluginContainer;
import ghost.framework.context.plugin.IPluginSource;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.core.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件容器
 * @Date: 2020/2/3:21:16
 */
@Component
public final class PluginContainer extends AbstractMap<String, IPluginSource> implements IPluginContainer {
    /**
     * 插件地图
     */
    private Map<String, IPluginSource> map = new HashMap();
//    /**
//     * 注入核心接口
//     * 如果插件安装应用接口侧为应用核心接口
//     * 如果插件安装模块接口侧为模块核心接口
//     */
//    @Autowired
//    private ICoreInterface coreInterface;

    @Override
    public IPluginSource put(String key, IPluginSource value) {
        synchronized (this.map) {
            map.put(key, value);
        }
        return value;
    }

    @Override
    public IPluginSource remove(Object key) {
        synchronized (this.map) {
            IPluginSource source = this.get(key);
            super.remove(key);
            return source;
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        return this.remove(key) != null;
    }

    /**
     * @return
     */
    @Override
    public Set<Entry<String, IPluginSource>> entrySet() {
        return map.entrySet();
    }
}