package ghost.framework.data.commons.entity;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:实体基础接口
 * @Date: 21:43 2019/5/16
 */
public interface IEntity {
    /**
     * 获取主键Id。
     *
     * @return
     */
    String getId();
    /**
     * 设置主键Id。
     *
     * @param roleId
     */
    void setId(String roleId);
}