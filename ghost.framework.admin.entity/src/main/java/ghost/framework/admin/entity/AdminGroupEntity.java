package ghost.framework.admin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * package: ghost.framework.admin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:管理员实体包
 * @Date: 2020/5/24:14:16
 */
//@Application
@Entity
@Table(name=IGroupEntity.GroupTableName,
        indexes = {
                @Index(name = "uk", columnList = IGroupEntity.GroupIdName, unique = true),
                @Index(name = "pk",
                        columnList = IGroupEntity.GroupIdName + "," + IGroupEntity.GroupName + ",create_time,status,is_default")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminGroupEntity implements Serializable, IGroupEntity {
    private static final long serialVersionUID = 7548223297840712040L;
    @Length(max = 36, min = 36, message = "length error")
    @NotNull
    @Id
    @Column(name = IGroupEntity.GroupIdName, updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    private String groupId;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @NotNull(message = "null error")
    @Length(max = 50, min = 2, message = "length error")
    @Column(name = IGroupEntity.GroupName, nullable = false, length = 50)
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @NotNull(message = "null error")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Min(value = 0, message = "min error")
    @Max(value = 1, message = "max error")
    @Column(name = "status", nullable = false)
    private short status;

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    /**
     * 是否为默认组
     */
    @Column(name = "is_default", nullable = false)
    private boolean def;

    public void setDef(boolean def) {
        this.def = def;
    }

    public boolean isDef() {
        return def;
    }
}