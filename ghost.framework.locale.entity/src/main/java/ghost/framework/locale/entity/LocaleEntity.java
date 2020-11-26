package ghost.framework.locale.entity;

import ghost.framework.admin.entity.IAdminEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.locale.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域语言表
 * @Date: 2020/6/8:12:58
 */
@Entity
@Table(name="8cb299d4f4344c34a764b51476985457",
        indexes = {
                @Index(name = "uk", columnList = "id," + IAdminEntity.AdminIdName, unique = true),
                @Index(name = "pk", columnList = "id,create_time,selected,locale," + IAdminEntity.AdminIdName)
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocaleEntity implements Serializable, IAdminEntity {
    private static final long serialVersionUID = -6463283196007195046L;
    /**
     * 管理员id
     */
    @Column(name = IAdminEntity.AdminIdName, updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
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
     * 主键id
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
     * 选中语言
     */
    @NotNull(message = "null error")
    @Column(name = "selected", nullable = false)
    private boolean selected;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    /**
     * 语言区域
     * 比如格式zh-CN
     */
    @Column(name = "locale", updatable = false, nullable = false, length = 20, columnDefinition = "char(20)")
    @NotNull(message = "null error")
    @Length(max = 20, min = 1, message = "length error")
    private String locale;

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }
}