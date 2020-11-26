package ghost.framework.web.angular1x.maintenance.manage.plugin.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @Description:终端账号表
 * @Date: 2020/4/26:16:35
 */
@Application
@Entity
@Table(name= "e14da7650fca422fb45f4aeb116939d1",
//        uniqueConstraints = {@UniqueConstraint(name = "uk", columnNames = {"terminal_account_id"})},
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,version,status,description,create_time,host,port,user")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TerminalAccountEntity implements Serializable {
    private static final long serialVersionUID = -1364226836002926085L;
    /**
     * 终端主键id
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36) not null comment '终端主键id'")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * 终端版本
     *
     * @return
     */
    @Length(max = 128, message = "length error")
    @Column(name = "version", nullable = false, length = 128, columnDefinition = "char(128) not null comment '版本'")
    private String version;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    /**
     * 终端创建时间
     */
    @NotNull(message = "null error")
    @Column(name = "create_time", nullable = false, columnDefinition = "datetime not null comment '终端创建时间'")
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
    @Column(name = "status", nullable = false, columnDefinition = "smallint not null comment '终端状态'")
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
    @Column(name = "description", length = 255, columnDefinition="varchar(255) null comment '终端描述'")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 终端主机地址
     */
    @Column(name = "host", nullable = false, length = 128, columnDefinition = "char(128) not null comment '终端主机地址'")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 36, min = 36, message = "length error")
    private String host;

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    /**
     * 终端端口
     */
    @Column(name = "port", nullable = false, columnDefinition = "int not null comment '终端端口'")
    @NotNull(message = "null error")
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 终端用户名称
     */
    @Column(name = "user", nullable = false, length = 128, columnDefinition = "varchar(128) not null comment '终端用户名称'")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 128, min = 2, message = "length error")
    private String user;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    /**
     * 终端密码
     */
    @Column(name = "password", nullable = false, length = 128, columnDefinition = "char(128) not null comment '终端密码'")
    @NotNull(message = "null error")
    @NotEmpty(message = "empty error")
    @Length(max = 128, min = 2, message = "length error")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    /**
     * id_rsa证书主键id
     */
    @Column(name = "terminal_pem_private_key_id", length = 36, columnDefinition = "char(36) null comment 'id_rsa证书主键id'")
    @Length(max = 36, min = 36, message = "length error")
    private String terminalPemPrivateKeyId;

    public void setTerminalPemPrivateKeyId(String terminalPemPrivateKeyId) {
        this.terminalPemPrivateKeyId = terminalPemPrivateKeyId;
    }

    public String getTerminalPemPrivateKeyId() {
        return terminalPemPrivateKeyId;
    }

    @JsonIgnore
    @OneToOne(targetEntity = TerminalPemPrivateKeyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private TerminalPemPrivateKeyEntity pemPrivateKeyEntity;
}