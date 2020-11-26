package ghost.framework.core.event.environment;

import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.bean.factory.IBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env事件目标处理接口
 * @Date: 1:04 2020/1/11
 */
public interface IEnvironmentEventTargetHandle<O, T extends IEnvironment> extends IBeanTargetHandle<O, T> {
}
