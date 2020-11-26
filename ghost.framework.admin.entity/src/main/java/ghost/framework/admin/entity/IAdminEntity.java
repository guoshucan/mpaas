package ghost.framework.admin.entity;

/**
 * package: ghost.framework.admin.entity
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:权限id实体接口
 * @Date: 19:45 2019-01-20
 */
public interface IAdminEntity {
    String AdminTableName = "admin_a5d0dea6";
    String AdminIdName = "admin_id";
    String AdminName = "admin_name";
    /**
     * 获取主键Id。
     *
     * @return
     */
    String getAdminId();

    /**
     * 设置主键Id。
     *
     * @param adminId
     */
    void setAdminId(String adminId);
}