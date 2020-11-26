package ghost.framework.web.angular1x.script.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.angular1x.script.plugin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/27:22:43
 */
@Entity
@Table(name= "script_command_b3d671a6",
        indexes = {
                @Index(name = "uk", columnList = "id,name", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,status,description")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandEntity implements Serializable {
    private static final long serialVersionUID = 2273215251097397688L;
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
     * 状态
     */
    @Min(value = 0, message = "min error")
    @Max(value = 1, message = "max error")
    @Column(name = "status", nullable = false)
    private short status;

    public void setStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return status;
    }
    /**
     * 脚本版本
     * 一般为python脚本的指定版本号
     */
    @NotNull(message = "null error")
    @Length(max = 50, min = 1, message = "length error")
    @Column(name = "name", unique = true, nullable = false, length = 50, columnDefinition = "char(50)")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
     * 脚本内容
     */
    @NotNull(message = "null error")
    @Length(min = 1, message = "length error")
    @Column(name = "content", nullable = false)
    @Lob
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    /**
     * 描述
     */
    @Length(max = 255, message = "length error")
    @Column(name = "description", length = 255, columnDefinition="varchar(255) null comment '描述'")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
