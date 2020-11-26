package ghost.framework.web.angular1x.ssh.plugin.entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.angular1x.ssh.plugin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:SSH服务器表
 * @Date: 2020/5/25:1:15
 */
@Entity
@Table(name= "ssh_server_77121035",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,status,group_id,host_name,port,region_id,type_id,account_id,channel_timeout")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SshServerEntity implements Serializable {
    private static final long serialVersionUID = -6843467857737783457L;
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
     * 组id
     */
    @NotNull(message = "null error")
    @Column(name = "group_id", nullable = false, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String groupId;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    /**
     * 组关联
     */
    @OneToOne(targetEntity = SshGroupEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private SshGroupEntity groupEntity;

    public SshGroupEntity getGroupEntity() {
        return groupEntity;
    }

    @NotNull(message = "null error")
    @Column(name = "type_id", nullable = false, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String typeId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @OneToOne(targetEntity = SshTypeEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private SshTypeEntity typeEntity;

    public SshTypeEntity getTypeEntity() {
        return typeEntity;
    }

    @NotNull(message = "null error")
    @Column(name = "account_id", nullable = false, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String accountId;

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    @OneToOne(targetEntity = SshAccountEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private SshAccountEntity accountEntity;

    public SshAccountEntity getAccountEntity() {
        return accountEntity;
    }

    /**
     * 区域关联
     */
    @OneToOne(targetEntity = SshRegionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private SshRegionEntity regionEntity;

    public SshRegionEntity getRegionEntity() {
        return regionEntity;
    }

    /**
     * 区域id
     */
    @NotNull(message = "null error")
    @Column(name = "region_id", nullable = false, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String regionId;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 名称
     */
    @NotNull(message = "null error")
    @Length(max = 100, min = 1, message = "length error")
    @Column(name = "name", nullable = false, length = 100/*, columnDefinition = "comment '名称'"*/)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
     * 主机地址，IP地址或域名
     */
    @NotNull(message = "null error")
    @Length(max = 255, min = 8, message = "length error")
    @Column(name = "host_name", nullable = false, length = 255, columnDefinition = "char(255)")
    private String hostName;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    @NotNull(message = "null error")
    @Column(name = "port", nullable = false)
    private int port = 22;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @NotNull(message = "null error")
    @Length(max = 255, min = 1, message = "length error")
    @Column(name = "remote_directory", nullable = false, length = 255)
    private String remoteDirectory = "/";

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    public void setRemoteDirectory(String remoteDirectory) {
        this.remoteDirectory = remoteDirectory;
    }

    @NotNull(message = "null error")
    @Column(name = "timeout", nullable = false)
    @Min(0)
    private int timeout = 300000;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    @NotNull(message = "null error")
    @Column(name = "channel_timeout", nullable = false)
    @Min(0)
    private int channelTimeout;

    public void setChannelTimeout(int channelTimeout) {
        this.channelTimeout = channelTimeout;
    }

    public int getChannelTimeout() {
        return channelTimeout;
    }

    /**
     * 描述
     */
    @Length(max = 255, message = "length error")
    @Column(name = "description", length = 255, columnDefinition = "varchar(255) null comment '描述'")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
