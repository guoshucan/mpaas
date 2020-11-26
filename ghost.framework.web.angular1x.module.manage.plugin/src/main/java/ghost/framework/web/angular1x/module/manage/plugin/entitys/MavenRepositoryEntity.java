package ghost.framework.web.angular1x.module.manage.plugin.entitys;

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
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven仓库实体
 * @Date: 2020/4/24:23:26
 */
@Application
@Entity
@Table(name= "maven_repository",
        indexes = {
                @Index(name = "uk", columnList = "maven_repository_id", unique = true),
                @Index(name = "pk", columnList = "maven_repository_id,username,password,status,description,url,create_time")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MavenRepositoryEntity implements Serializable {
    private static final long serialVersionUID = 6569063653230455831L;
    /**
     * 模块id
     */
    @Id
    @Column(name = "maven_repository_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 36, min = 36, message = "length error")
    private String mavenRepositoryId;

    public void setMavenRepositoryId(String mavenRepositoryId) {
        this.mavenRepositoryId = mavenRepositoryId;
    }

    public String getMavenRepositoryId() {
        return mavenRepositoryId;
    }

    /**
     * 仓库地址
     */
    @NotEmpty(message = "empty error")
    @NotNull(message = "null error")
    @Column(length = 128, name = "url", nullable = false)
    @Length(max = 128, message = "仓库地址。")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 仓库类型
     */
    @Column(length = 128, name = "type")
    @Length(max = 128, message = "仓库类型，默认为default。")
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 仓库id
     */
    @Column(length = 128, name = "id")
    @Length(max = 128, message = "仓库id。")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * 仓库帐号。
     */
    @Column(length = 128, name = "username")
    @Length(max = 128, message = "仓库帐号内容长度错误。")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 仓库密码。
     */
    @Column(length = 128, name = "password")
    @Length(max = 128, message = "仓库密码内容长度错误。")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
     * 仓库状态
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
     * 仓库描述
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
}
