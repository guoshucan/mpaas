package ghost.framework.web.admin.module.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import ghost.framework.module.data.bind.entity.IRoleEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="admin_role_permission",
        indexes = {
                @Index(name = "uk", columnList = IRoleEntity.AdminRoleIdName, unique = true),
                @Index(name = "pk", columnList = IRoleEntity.AdminRoleIdName)
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminRolePermissionEntity implements Serializable , IRoleEntity {
    private static final long serialVersionUID = -2720653122818404059L;
    @Id
    @Column(name = IRoleEntity.AdminRoleIdName, updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull
    @JsonIgnore
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
