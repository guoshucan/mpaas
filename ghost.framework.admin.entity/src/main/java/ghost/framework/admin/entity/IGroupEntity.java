package ghost.framework.admin.entity;

/**
 * package: ghost.framework.admin.entity
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:权限组实体接口
 * @Date: 8:11 2018-09-16
 */
public interface IGroupEntity  {
    String GroupTableName = "group_8482da2b";
    String GroupIdName = "group_id";
    String GroupName = "group_name";
    /**
     * 获取主键Id。
     *
     * @return
     */
    String getGroupId();

    /**
     * 设置主键Id。
     *
     * @param groupId
     */
    void setGroupId(String groupId);
}