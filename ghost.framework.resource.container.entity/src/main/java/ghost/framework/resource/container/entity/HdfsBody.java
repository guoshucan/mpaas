package ghost.framework.resource.container.entity;

import ghost.framework.resource.container.entity.ResourceContainerEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * package: ghost.framework.web.angular1x.resource.container.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/24:22:08
 */
public class HdfsBody extends ResourceContainerEntity {
    @NotNull(message = "null error")
    @Length(max = 64, min = 1, message = "length error")
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
