package ghost.framework.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@Table(name="admin_group_permission",
        indexes = {
                @Index(name = "uk", columnList = IGroupEntity.GroupIdName, unique = true),
                @Index(name = "pk", columnList = IGroupEntity.GroupIdName)
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminGroupPermissionEntity implements Serializable , IGroupEntity {
    private static final long serialVersionUID = -2720653122818404059L;
    @Id
    @Column(name = IGroupEntity.GroupIdName, updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull
    @JsonIgnore
    private String groupId;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    @JsonIgnore
    @OneToOne(targetEntity = AdminGroupEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = IGroupEntity.GroupIdName, referencedColumnName = IGroupEntity.GroupIdName, insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT))
    private AdminGroupEntity groupEntity;

}