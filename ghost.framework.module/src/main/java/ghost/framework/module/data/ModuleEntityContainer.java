package ghost.framework.module.data;
import ghost.framework.context.module.IModule;
import ghost.framework.module.data.permission.IAdminEntity;
import ghost.framework.module.data.permission.IRoleEntity;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块数据实体容器
 * @Date: 21:36 2019/5/16
 */
public class ModuleEntityContainer extends EntityContainer {
    /**
     * 拥有者模块
     */
    private IModule module;

    /**
     * 初始化模块数据实体容器
     *
     * @param module
     */
    public ModuleEntityContainer(IModule module) {
        this.module = module;
    }

    public IModule getModule() {
        return module;
    }

    /**
     * 获取拥有者模块
     *
     * @return
     */


    @Override
    public void add(Class<? extends IEntity> entity) {
        synchronized (this.root) {
            super.add(entity);
            if (entity.isAssignableFrom(IRoleEntity.class)) {
                this.rootEntityList.add(entity);
            }
            if (entity.isAssignableFrom(IAdminEntity.class)) {
                this.adminEntityList.add(entity);
            }
        }
    }

    @Override
    public void remove(Class<? extends IEntity> entity) {
        synchronized (this.root) {
            super.remove(entity);
            if (entity.isAssignableFrom(IRoleEntity.class)) {
                this.rootEntityList.remove(entity);
            }
            if (entity.isAssignableFrom(IAdminEntity.class)) {
                this.adminEntityList.remove(entity);
            }
        }
    }

    /**
     * 引导权限实体
     */
    private List<Class<? extends IEntity>> rootEntityList = new ArrayList<>();
    /**
     * 获取引导权限实体
     * @return
     */
    public List<Class<? extends IEntity>> getRootEntityList() {
        return rootEntityList;
    }
    /**
     * 权限实体列表
     */
    private List<Class<? extends IEntity>> adminEntityList = new ArrayList<>();
    /**
     * 获取权限实体列表
     * @return
     */
    public List<Class<? extends IEntity>> getAdminEntityList() {
        return adminEntityList;
    }

}
