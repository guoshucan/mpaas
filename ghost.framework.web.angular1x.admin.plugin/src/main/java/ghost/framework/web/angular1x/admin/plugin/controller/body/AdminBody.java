package ghost.framework.web.angular1x.admin.plugin.controller.body;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class AdminBody implements Serializable {
    private static final long serialVersionUID = -6889109706027786884L;
    @NotNull(message = "null error")
    @Length(max = 50, min = 2, message = "length error")
    private String adminName;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @NotNull(message = "null error")
    @Length(max = 32, min = 2, message = "length error")
    private String adminUser;

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminUser() {
        return adminUser;
    }

//    @NotNull(message = "null error")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Min(value = 0, message = "min error")
    @Max(value = 1, message = "max error")
    private short status;

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Length(max = 64, min = 8, message = "length error")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(max = 15, min = 11, message = "length error")
    private String mobilePhone;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Length(max = 128, min = 2, message = "length error")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(max = 32, min = 2, message = "length error")
    private String weixin;

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWeixin() {
        return weixin;
    }

    @Length(max = 32, min = 5, message = "length error")
    private String qq;

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getQq() {
        return qq;
    }

    @Length(max = 128, min = 6, message = "length error")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}