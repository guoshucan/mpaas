package ghost.framework.module.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 22:10 2019/5/16
 */
abstract class EntityContainer {

    public Object getRoot() {
        return root;
    }

    protected Object root = new Object();
    private List<Class<? extends IEntity>> entityList = new ArrayList<>();

    public List<Class<? extends IEntity>> getEntityList() {
        return this.entityList;
    }

    public boolean contains(Class<? extends IEntity> entity) {
        return this.entityList.contains(entity);
    }
    public boolean isEmpty() {
        return this.entityList.isEmpty();
    }
    public void clear() {
        this.entityList.clear();
    }

    public void add(Class<? extends IEntity> entity) {
        synchronized (this.root) {
            this.entityList.add(entity);
        }
    }

    public void remove(Class<? extends IEntity> entity) {
        synchronized (this.root) {
            this.entityList.remove(entity);
        }
    }
}
