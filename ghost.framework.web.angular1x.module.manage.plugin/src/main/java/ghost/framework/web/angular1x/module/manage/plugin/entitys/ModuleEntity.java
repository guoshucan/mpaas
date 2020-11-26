package ghost.framework.web.angular1x.module.manage.plugin.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ghost.framework.beans.annotation.application.Application;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.angular1x.module.manage.plugin.entitys
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块表
 * @Date: 2020/4/21:11:05
 */
@Application
@Entity
@Table(name= "module",
        indexes = {
                @Index(name = "uk", columnList = "module_id", unique = true),
                @Index(name = "pk", columnList = "module_id,group_id,artifact_id,version,status,description,create_time")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ModuleEntity implements Serializable {
    private static final long serialVersionUID = 7012539694557167139L;
    /**
     * 模块id
     */
    @Id
    @Column(name = "module_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 36, min = 36, message = "length error")
    private String moduleId;

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    /**
     * 包组
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 2, message = "length error")
    @NotEmpty(message = "empty error")
    @Column(name = "group_id", nullable = false, length = 128, columnDefinition = "char(128)")
    private String groupId;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    /**
     * 包id
     *
     * @return
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 2, message = "length error")
    @NotEmpty(message = "empty error")
    @Column(name = "artifact_id", nullable = false, length = 128, columnDefinition = "char(128)")
    private String artifactId;

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    /**
     * 包版本
     *
     * @return
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 2, message = "length error")
    @Column(name = "version", nullable = false, length = 128, columnDefinition = "char(128)")
    private String version;
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
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
     * 模块描述
     */
    @Length(max = 255, min = 0, message = "length error")
    @Column(name = "description", length = 255)
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 模块属性
     */
    @JsonIgnore
    @OneToOne(targetEntity = ModuleAttributesEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", referencedColumnName = "module_id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ModuleAttributesEntity attributesEntity;

    /**
     * 模块数据
     */
    @JsonIgnore
    @OneToOne(targetEntity = ModuleDataEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", referencedColumnName = "module_id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ModuleDataEntity dataEntity;
}
