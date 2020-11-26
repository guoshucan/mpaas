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
 * package: ghost.framework.web.angular1x.ssh.script.plugin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/25:1:17
 */
@Entity
@Table(name= "script_62d039c4",
        indexes = {
                @Index(name = "uk", columnList = "id,name", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,description,type,python_version_id,golang_version_id,status,java_version_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScriptEntity implements Serializable {
    private static final long serialVersionUID = -4403873743987938928L;
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
     * 名称
     */
    @NotNull(message = "null error")
    @Length(max = 100, min = 1, message = "length error")
    @Column(name = "name", nullable = false, unique = true, length = 100/*, columnDefinition = "comment '名称'"*/)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
     * 脚本类型
     * 0：shell
     * 1：python
     * 2：golang
     * 3：java
     */
    @Min(0)
    @Max(3)
    @Column(name = "type", nullable = false)
    private short type;

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
    @Column(name = "golang_version_id", length = 36, columnDefinition = "char(36)")
    private String golangVersionId;

    public void setGolangVersionId(String golangVersionId) {
        this.golangVersionId = golangVersionId;
    }

    public String getGolangVersionId() {
        return golangVersionId;
    }
    @OneToOne(targetEntity = GoLangVersionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "golang_version_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private GoLangVersionEntity goLangVersionEntity;

    public GoLangVersionEntity getGoLangVersionEntity() {
        return goLangVersionEntity;
    }

    /**
     * 脚本版本
     * 一般为python脚本的指定版本号
     */
//    @Length(max = 36, min = 36, message = "length error")
    @Column(name = "python_version_id", length = 36, columnDefinition = "char(36)")
    private String pythonVersionId;

    public String getPythonVersionId() {
        return pythonVersionId;
    }

    public void setPythonVersionId(String pythonVersionId) {
        this.pythonVersionId = pythonVersionId;
    }

    @OneToOne(targetEntity = PythonVersionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "python_version_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private PythonVersionEntity pythonVersionEntity;

    public PythonVersionEntity getPythonVersionEntity() {
        return pythonVersionEntity;
    }
    @Column(name = "java_version_id", length = 36, columnDefinition = "char(36)")
    private String javaVersionId;

    public void setJavaVersionId(String javaVersionId) {
        this.javaVersionId = javaVersionId;
    }

    public String getJavaVersionId() {
        return javaVersionId;
    }
    @OneToOne(targetEntity = JavaVersionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "java_version_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private JavaVersionEntity javaVersionEntity;

    public JavaVersionEntity getJavaVersionEntity() {
        return javaVersionEntity;
    }
}