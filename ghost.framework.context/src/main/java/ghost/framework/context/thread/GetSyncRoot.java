package ghost.framework.context.thread;

/**
 * package: ghost.framework.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:作为同步对象获取接口
 * @Date: 2020/6/9:18:05
 */
public interface GetSyncRoot {
    /**
     * 获取同步对象
     *
     * @return
     */
    Object getSyncRoot();
}