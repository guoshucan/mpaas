package ghost.framework.module.data;
import ghost.framework.context.module.IModule;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据实体容器
 * @Date: 21:23 2019/5/16
 */
public class DataEntityContainer extends EntityContainer {

    public Object getRoot() {
        return root;
    }

    private Object root = new Object();
    private ConcurrentSkipListMap<String, ModuleEntityContainer> map = new ConcurrentSkipListMap<>();

    public void add(IModule module) {
        synchronized (this.root) {
            this.map.put(module.getName(), new ModuleEntityContainer(module));
        }
    }

    public void add(ModuleEntityContainer entityContainer) {
        synchronized (this.root) {
            this.map.put(entityContainer.getModule().getName(), entityContainer);
        }
    }

    public void remove(IModule module) {
        synchronized (this.root) {
            this.map.remove(module.getName());
        }
    }

    public ModuleEntityContainer get(IModule module) {
        return this.map.get(module.getName());
    }
}
