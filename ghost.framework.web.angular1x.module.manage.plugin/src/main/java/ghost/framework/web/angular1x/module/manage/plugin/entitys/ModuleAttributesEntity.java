package ghost.framework.web.angular1x.module.manage.plugin.entitys;

import ghost.framework.beans.annotation.application.Application;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * package: ghost.framework.web.angular1x.module.manage.plugin.entitys
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块扩展属性表
 * @Date: 2020/4/21:11:14
 */
@Application
@Entity
@Table(name= "module_attributes",
        indexes = {
                @Index(name = "uk", columnList = "module_attributes_id", unique = true),
                @Index(name = "pk", columnList = "module_attributes_id,module_id,name")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ModuleAttributesEntity implements Serializable {
    private static final long serialVersionUID = 3985355244136995227L;
    /**
     * 插件属性id
     */
    @Id
    @Column(name = "module_attributes_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String moduleAttributesId;

    public void setModuleAttributesId(String moduleAttributesId) {
        this.moduleAttributesId = moduleAttributesId;
    }

    public String getModuleAttributesId() {
        return moduleAttributesId;
    }

    /**
     * 插件id
     */
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