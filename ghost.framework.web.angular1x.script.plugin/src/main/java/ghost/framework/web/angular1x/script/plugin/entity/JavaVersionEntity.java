package ghost.framework.web.angular1x.script.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
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
@Table(name= "script_java_version_210b9b6d",
        indexes = {
                @Index(name = "uk", columnList = "id,name", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,type")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JavaVersionEntity implements Serializable {
    private static final long serialVersionUID = 1233558043487558114L;
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
     * 脚本版本
     * 一般为java脚本的指定版本号
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
     * jdk类型
     * true为jdk
     * false为openJdk
     */
    @Column(name = "type", nullable = false)
    private boolean type;

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isType() {
        return type;
    }
}
