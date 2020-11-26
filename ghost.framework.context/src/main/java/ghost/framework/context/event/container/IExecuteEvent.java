package ghost.framework.context.event.container;

/**
 * package: ghost.framework.core.event.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:执行事件接口
 * @Date: 2020/2/3:12:31
 */
public interface IExecuteEvent {
    /**
     * 执行事件
     *
     * @param event 事件对象
     */
    void execute(IEvent event);
}
