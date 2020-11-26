package ghost.framework.web.angular1x.login.plugin.controller;

import ghost.framework.admin.entity.AdminEntity;

import java.io.Serializable;

/**
 * package: ghost.framework.web.angular1x.login.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注册信息
 * @Date: 2020/8/11:15:03
 */
public class SignupInfo extends AdminEntity implements Serializable {
    private static final long serialVersionUID = -8387452267468179273L;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}