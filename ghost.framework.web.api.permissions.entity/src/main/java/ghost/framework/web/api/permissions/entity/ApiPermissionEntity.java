package ghost.framework.web.api.permissions.entity;

import ghost.framework.admin.entity.IAdminEntity;
import ghost.framework.admin.entity.IGroupEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.api.permissions.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:api权限实体
 * @Date: 2020/5/26:10:08
 */
@Entity
@Table(name= "813d5f664c7744a5addbb3d14be9f275",
        indexes = {
                @Index(name = "uk", columnList = "id,name", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,status,"+IAdminEntity.AdminIdName+","+IGroupEntity.GroupIdName+"")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiPermissionEntity implements Serializable, IAdminEntity, IGroupEntity {
    private static final long serialVersionUID = -1030849155755898328L;
    @Length(max = 36, min = 36, message = "length error")
    @NotNull(message = "null error")
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36, columnDefinition = "char(36)")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = "null error")
    @Length(max = 50, min = 1, message = "length error")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 创建时间
     */
    @NotNull(message = "null error")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 状态
     */
    @Column(name = "status", nullable = false)
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * 管理员id
     * {@link ghost.framework.admin.entity.AdminEntity::adminId}
     */
    @Column(name = IAdminEntity.AdminIdName, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String adminId;

    @Override
    public String getAdminId() {
        return adminId;
    }

    @Override
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    /**
     * 管理组id
     * {@link ghost.framework.admin.entity.AdminGroupEntity::groupId}
     */
    @Column(name = IGroupEntity.GroupIdName, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String groupId;

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }
}