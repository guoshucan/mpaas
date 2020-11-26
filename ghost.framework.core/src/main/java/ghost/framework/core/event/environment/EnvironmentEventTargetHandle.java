package ghost.framework.core.event.environment;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.bean.factory.BeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env事件目标处理类
 * @Date: 1:04 2020/1/11
 */
public class EnvironmentEventTargetHandle<O extends ICoreInterface, T extends IEnvironment>
        extends BeanTargetHandle<O, T>
        implements IEnvironmentEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public EnvironmentEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}
