package ghost.framework.module.data.bind.entity;

/**
 * 权限角色接口
 */
public interface IRoleEntity {
    String AdminRoleIdName = "roleId";
    /**
     * 获取权限角色id
     * @return
     */
    String getRoleId();
    /**
     * 设置权限角色id
     * @param roleId
     */
    void setRoleId(String roleId);
}