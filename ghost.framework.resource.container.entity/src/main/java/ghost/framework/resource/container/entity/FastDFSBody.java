package ghost.framework.resource.container.entity;

import ghost.framework.resource.container.entity.ResourceContainerEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * package: ghost.framework.web.angular1x.resource.container.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/24:19:08
 */
public class FastDFSBody extends ResourceContainerEntity {

    @NotNull(message = "null error")
    @Min(0)
    private long soTimeout;

    public long getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(long soTimeout) {
        this.soTimeout = soTimeout;
    }
    @NotNull(message = "null error")
    @Min(0)
    private long connectTimeout;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
