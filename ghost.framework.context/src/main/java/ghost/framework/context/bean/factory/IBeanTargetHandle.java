package ghost.framework.context.bean.factory;

import ghost.framework.beans.event.IHandle;
import ghost.framework.beans.event.IIsHandle;
import ghost.framework.beans.event.ISetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定目标处理接口
 * 实现功能
 * {@link IHandle#setHandle(boolean)}
 * {@link IHandle#isHandle()}
 * {@link ISetHandle#setHandle(boolean)}
 * {@link IIsHandle#isHandle()}
 * {@link IBeanTargetHandle#getOwner()}
 * {@link IBeanTargetHandle#getTarget()}
 * @Date: 8:35 2019/12/25
 * @param <O> 发起方对象
 * @param <T> 绑定目标
 */
public interface IBeanTargetHandle<O, T> extends IHandle {
    /**
     * 获取事件目标对象拥有者
     *
     * @return
     */
    O getOwner();

    /**
     * 获取目标对象
     *
     * @return
     */
    T getTarget();
}