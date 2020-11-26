package ghost.framework.beans.event;

/**
 * package: ghost.framework.context.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:设置处理状态接口
 * 实现功能
 * {@link ISetHandle#setHandle(boolean)}
 * @Date: 2020/6/2:13:16
 */
public interface ISetHandle {
    /**
     * 设置是否已经处理
     *
     * @param handle
     */
    void setHandle(boolean handle);
}
