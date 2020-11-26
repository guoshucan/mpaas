package ghost.framework.web.context.io;

import ghost.framework.context.io.IResource;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.web.context.utils.ETag;

import java.util.Date;

/**
 * package: ghost.framework.web.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web资源接口
 * @Date: 2020/3/14:15:07
 */
public interface IWebResource extends IResource {
    /**
     * 获取资源所在域
     * @return
     */
    IResourceDomain getDomain();

    default ETag getETag(){
        return null;
    }
    /**
     * 获取过期时间
     * @return
     */
    Date getExpiredDate();
}
