package ghost.framework.web.angular1x.plugin.management.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ghost.framework.beans.annotation.application.Application;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.angular1x.plugin.management.entitys
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/19:9:40
 */
@Application
@Entity
@Table(name= "plugin",
        indexes = {
                @Index(name = "uk", columnList = "plugin_id", unique = true),
                @Index(name = "pk", columnList = "plugin_id,group_id,artifact_id,version,create_time")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PluginEntity implements Serializable {
    private static final long serialVersionUID = -1767497985076878753L;
    /**
     * 插件id
     */
    @Id
    @Column(name = "plugin_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 36, min = 36, message = "length error")
    private String pluginId;

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginId() {
        return pluginId;
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

    /**
     * 包版本
     *
     * @return
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 2, message = "length error")
    @Column(name = "version", nullable = false, length = 128, columnDefinition = "char(128)")
    private String version;
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
     * 插件属性
     */
    @JsonIgnore
    @OneToOne(targetEntity = PluginAttributesEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "plugin_id", referencedColumnName = "plugin_id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private PluginAttributesEntity attributesEntity;
}