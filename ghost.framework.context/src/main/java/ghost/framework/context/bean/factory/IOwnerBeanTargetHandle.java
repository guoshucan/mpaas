package ghost.framework.context.bean.factory;

import ghost.framework.beans.event.IHandle;
import ghost.framework.beans.event.IIsHandle;
import ghost.framework.beans.event.ISetHandle;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:拥有者事件目标处理接口
 * 实现功能
 * {@link IHandle#setHandle(boolean)}
 * {@link IHandle#isHandle()}
 * {@link ISetHandle#setHandle(boolean)}
 * {@link IIsHandle#isHandle()}
 * {@link IBeanTargetHandle#getOwner()}
 * {@link IBeanTargetHandle#getTarget()}
 * @Date: 15:00 2020/1/17
 * @param <O> 发起方对象
 * @param <T> 绑定目标
 */
public interface IOwnerBeanTargetHandle<O, T> extends IBeanTargetHandle<O, T> {
}