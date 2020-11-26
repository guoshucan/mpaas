package ghost.framework.context.io;

/**
 * package: ghost.framework.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/14:17:53
 */
public interface IInputStreamResource extends IInputStreamSource {
    /**
     * 获取文件修改事件
     *
     * @return
     */
    long getLastModified();
}