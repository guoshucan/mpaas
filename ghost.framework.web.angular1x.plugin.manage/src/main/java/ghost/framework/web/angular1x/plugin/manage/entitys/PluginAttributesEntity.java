package ghost.framework.web.angular1x.plugin.management.entitys;

import ghost.framework.beans.annotation.application.Application;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * package: ghost.framework.web.angular1x.plugin.management.entitys
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/19:9:52
 */
@Application
@Entity
@Table(name= "plugin_attributes",
        indexes = {
                @Index(name = "uk", columnList = "plugin_attributes_id", unique = true),
                @Index(name = "pk", columnList = "plugin_attributes_id,plugin_id,name")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PluginAttributesEntity implements Serializable {
    private static final long serialVersionUID = 7408983143482360263L;
    /**
     * 插件属性id
     */
    @Id
    @Column(name = "plugin_attributes_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String pluginAttributesId;

    public String getPluginAttributesId() {
        return pluginAttributesId;
    }

    public void setPluginAttributesId(String pluginAttributesId) {
        this.pluginAttributesId = pluginAttributesId;
    }

    /**
     * 插件id
     */
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

    @Column(name = "name", updatable = false, nullable = false, length = 128, columnDefinition = "char(128)")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 128, min = 1, message = "length error")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob //lob类型的字段
    @Column(name = "value", updatable = false, nullable = false, columnDefinition = "TEXT NULL")
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}