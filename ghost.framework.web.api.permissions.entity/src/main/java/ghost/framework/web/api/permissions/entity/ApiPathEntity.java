package ghost.framework.web.api.permissions.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.api.permissions.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:api权限路径实体
 * @Date: 2020/5/28:23:30
 */
@Entity
@Table(name= "342ee0cd73a04fc49a25facb696bf7f2",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,name,api_permission_id,create_time,status,path")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiPathEntity implements Serializable {
    private static final long serialVersionUID = -3859179836637721266L;
    /**
     * 主键id
     */
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
//    @NotNull(message = "null error")
    @Length(max = 128, message = "length error")
    @Column(name = "name", length = 128)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    /**
     * api权限id
     * {@link ApiPermissionEntity::id}
     */
    @NotNull(message = "null error")
    @Column(name = "api_permission_id", length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String apiPermissionId;

    public String getApiPermissionId() {
        return apiPermissionId;
    }

    public void setApiPermissionId(String apiPermissionId) {
        this.apiPermissionId = apiPermissionId;
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
     * 接口
     */
    @Column(name = "type", updatable = false)
    @Min(0)
    private byte type;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    /**
     * api接口路径
     */
    @NotNull(message = "null error")
    @Length(max = 255, min = 1, message = "length error")
    @Column(name = "path", nullable = false, length = 255, columnDefinition = "char(255)")
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}