package ghost.framework.web.admin.module.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ghost.framework.module.data.bind.entity.IRoleEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
@Entity
@Table(name="admin_role",
        indexes = {
                @Index(name = "uk", columnList = IRoleEntity.AdminRoleIdName, unique = true),
                @Index(name = "pk", columnList = IRoleEntity.AdminRoleIdName)
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminRoleEntity implements Serializable {
    private static final long serialVersionUID = 7548223297840712040L;
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
