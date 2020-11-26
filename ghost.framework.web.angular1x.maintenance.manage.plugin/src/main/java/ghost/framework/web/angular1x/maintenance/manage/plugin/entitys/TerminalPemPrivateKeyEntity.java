package ghost.framework.web.angular1x.maintenance.manage.plugin.entitys;
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
 * package: ghost.framework.web.angular1x.maintenance.manage.plugin.entitys
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:终端证书表
 * @Date: 2020/4/26:17:01
 */
@Application
@Entity
@Table(name= "ae5be9f177b740289f2201a1c2b95466",
//        uniqueConstraints = {@UniqueConstraint(name = "uk", columnNames = {"terminal_pem_private_key_id"})},
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,description,create_time,status")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TerminalPemPrivateKeyEntity implements Serializable {
    private static final long serialVersionUID = 579851097116901335L;
    /**
     * id_rsa证书主键id
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * id_rsa内容
     */
    @Column(name = "pem_private_key", updatable = false, nullable = false, length = 4000, columnDefinition = "char(4000)")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 4000,  message = "length error")
    private String pemPrivateKey;
    public String getPemPrivateKey() {
        return pemPrivateKey;
    }
    public void setPemPrivateKey(String pemPrivateKey) {
        this.pemPrivateKey = pemPrivateKey;
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
    /**
     * 终端状态
     */
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
     * 终端描述
     */
    @Length(max = 255, message = "length error")
    @Column(name = "description", length = 255)
    private String description;
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}