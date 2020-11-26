package ghost.framework.web.context.resource;

/**
 * package: ghost.framework.web.context.http.response
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/15:10:05
 */
public interface IWebDirectoryResourceResolver extends IWebResourceResolver {
    /**
     * 获取是否使用目录解析
     * @return
     */
    boolean isDirectoryListingEnabled();
    void setDirectoryListingEnabled(boolean directoryListingEnabled);
}
