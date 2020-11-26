package ghost.framework.software.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

/**
 * package: ghost.framework.software.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:安装前执行
 * @Date: 2020/5/31:21:24
 */
@Entity
@Table(name="7a4df05d8bfe4812aece92d42941f35c",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,version_id,create_time,status,description,execute_order")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoftwareBeforeInstallExecuteEntity implements Serializable {
    private static final long serialVersionUID = -7897011467981364319L;
    /**
     * 执行顺序
     * 小为先
     */
    @Column(name = "execute_order", nullable = false)
    @NotNull(message = "null error")
    @Min(0)
    private int executeOrder;

    public int getExecuteOrder() {
        return executeOrder;
    }

    public void setExecuteOrder(int executeOrder) {
        this.executeOrder = executeOrder;
    }

    /**
     * 主键id
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    /**
     * 软件版本id
     * {@link SoftwareVersionEntity#getId()}
     */
    @Column(name = "version_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String versionId;

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getVersionId() {
        return versionId;
    }

    /**
     * 创建时间
     */
    @NotNull(message = "null error")
    @Column(name = "create_time", nullable = false/*, columnDefinition = "comment '创建时间'"*/)
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
    @Min(value = 0, message = "min error")
    @Max(value = 1, message = "max error")
    @Column(name = "status", nullable = false/*, columnDefinition = "comment '状态'"*/)
    private short status;

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    /**
     * 描述
     */
    @Length(max = 255, message = "length error")
    @Column(name = "description")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    /**
     * 执行脚本
     */
    @Column(name = "execute_shell")
    @Lob
    @Basic(fetch = LAZY)
    private String executeShell;

    public String getExecuteShell() {
        return executeShell;
    }

    public void setExecuteShell(String executeShell) {
        this.executeShell = executeShell;
    }
}
