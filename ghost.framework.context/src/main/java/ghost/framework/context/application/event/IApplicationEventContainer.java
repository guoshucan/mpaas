package ghost.framework.context.application.event;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用事件容器接口
 * @Date: 2020/2/16:1:14
 */
public interface IApplicationEventContainer
        extends ApplicationEventPublisher, ApplicationEventListener {
    /**
     * 删除事件推送接口
     * @param eventPublisher 事件推送接口
     * @return
     */
    boolean removeEventPublisher(ApplicationEventPublisher eventPublisher);

    /**
     * 添加事件推送接口
     * @param eventPublisher 事件推送接口
     * @return
     */
    boolean addEventPublisher(ApplicationEventPublisher eventPublisher);

    /**
     * 删除事件监听接口
     * @param eventListener 事件监听接口
     * @return
     */
    boolean removeEventListener(ApplicationEventListener eventListener);

    /**
     * 添加事件监听接口
     * @param eventListener 事件监听接口
     * @return
     */
    boolean addEventListener(ApplicationEventListener eventListener);

    /**
     * 添加事件容器接口
     * @param eventContainer 事件容器接口
     */
    void addEventContainer(IApplicationEventContainer eventContainer);

    /**
     * 删除事件容器接口
     * @param eventContainer 事件容器接口
     * @return
     */
    IApplicationEventContainer removeEventContainer(IApplicationEventContainer eventContainer);
}