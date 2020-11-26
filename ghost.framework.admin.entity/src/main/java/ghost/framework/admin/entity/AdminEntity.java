package ghost.framework.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * package: ghost.framework.admin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:管理员实体包
 * @Date: 2020/5/24:14:16
 */
//@Application
@Entity
@Table(name=IAdminEntity.AdminTableName,
        indexes = {
                @Index(name = "uk", columnList = IAdminEntity.AdminIdName, unique = true),
                @Index(name = "pk", columnList = IAdminEntity.AdminIdName)
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminEntity implements Serializable, IAdminEntity, IGroupEntity {
    private static final long serialVersionUID = 7548223297840712040L;
    @Id
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

    @NotNull(message = "null error")
    @Length(max = 50, min = 2, message = "length error")
    @Column(name = IAdminEntity.AdminName, nullable = false, length = 50)
    private String adminName;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
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


    @JsonIgnore
    @OneToOne(targetEntity = AdminGroupEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = IGroupEntity.GroupIdName, referencedColumnName = IGroupEntity.GroupIdName, insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private AdminGroupEntity groupEntity;

    @NotNull(message = "null error")
    @Column(name = IGroupEntity.GroupIdName, nullable = false, length = 36, columnDefinition = "char(36)")
    @Length(max = 36, min = 36, message = "length error")
    private String groupId;
    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    @Override
    public String getGroupId() {
        return groupId;
    }

    @Length(max = 128, min = 2, message = "length error")
    @Column(name = "address", length = 128)
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(max = 64, min = 8, message = "length error")
    @Column(name = "email", length = 64, columnDefinition = "char(64)")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(max = 15, min = 11, message = "length error")
    @Column(name = "mobile_phone", length = 15, columnDefinition = "char(15)")
    private String mobilePhone;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Length(max = 32, min = 2, message = "length error")
    @Column(name = "weixin", length = 32, columnDefinition = "char(32)")
    private String weixin;

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWeixin() {
        return weixin;
    }

    @Length(max = 20, min = 5, message = "length error")
    @Column(name = "qq", length = 20, columnDefinition = "char(20)")
    private String qq;

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getQq() {
        return qq;
    }

    @Length(max = 128, min = 6, message = "length error")
    @Column(name = "password", length = 128, nullable = false, columnDefinition = "char(128)")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @NotNull(message = "null error")
    @Length(max = 32, min = 2, message = "length error")
    @Column(name = "admin_user", length = 32, nullable = false, columnDefinition = "char(32)")
    private String adminUser;

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminUser() {
        return adminUser;
    }
}