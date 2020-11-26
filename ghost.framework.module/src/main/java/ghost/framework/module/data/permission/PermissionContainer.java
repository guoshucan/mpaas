package ghost.framework.module.data.permission;//package ghost.framework.module.data.permission;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:权限容器
// * @Date: 8:21 2018-09-16
// */
//public final class PermissionContainer {
//    /**
//     * 引导权限实体
//     */
//    private List<Class<? extends IRoleEntity>> rootEntityList = new ArrayList<>();
//
//    /**
//     * 设置引导权限实体。
//     * @param rootEntity 实体类型
//     */
//    public void addRoot(Class<? extends IRoleEntity> rootEntity) {
//        this.rootEntityList.put(rootEntity);
//    }
//    /**
//     * 获取引导权限实体
//     * @return
//     */
//    public List<Class<? extends IRoleEntity>> getRootEntityList() {
//        return rootEntityList;
//    }
//
//    /**
//     * 删除角色实体
//     * @param rootEntity
//     */
//    public void removeRoot(Class<? extends IRoleEntity> rootEntity) {
//        this.rootEntityList.remove(rootEntity);
//    }
//
//    /**
//     * 删除角色实体
//     * @param index 删除位置
//     */
//    public void removeRoot(int index) {
//        this.rootEntityList.remove(index);
//    }
//    /**
//     * 权限实体列表
//     */
//    private List<Class<? extends IAdminEntity>> adminEntityList = new ArrayList<>();
//
//    /**
//     * 添加权限实体
//     * @param adminEntity 实体类型
//     */
//    public void addAdmin(Class<? extends IAdminEntity> adminEntity) {
//        this.adminEntityList.put(adminEntity);
//    }
//
//    /**
//     * 删除权限实体
//     * @param adminEntity 实体类型
//     * @return
//     */
//    public boolean removeAdmin(Class<? extends IAdminEntity> adminEntity) {
//        return this.adminEntityList.remove(adminEntity);
//    }
//
//    /**
//     * 删除权限实体
//     * @param index 删除位置
//     * @return
//     */
//    public Class<?> removeAdmin(int index) {
//        return this.adminEntityList.remove(index);
//    }
//
//    /**
//     * 获取权限实体列表
//     * @return
//     */
//    public List<Class<? extends IAdminEntity>> getAdminEntityList() {
//        return adminEntityList;
//    }
//}