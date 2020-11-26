package ghost.framework.context.bean.factory;

/**
 * package: ghost.framework.context.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:执行拥有者事件目标处理接口
 * @Date: 2020/2/22:12:11
 */
public interface IExecuteOwnerBeanTargetHandle<O, T> extends IOwnerBeanTargetHandle<O, T> {
    /**
     * 获取执行拥有者
     *
     * @return
     */
    O getExecuteOwner();

    /**
     * 设置执行拥有者
     *
     * @param executeOwner
     */
    void setExecuteOwner(O executeOwner);
}