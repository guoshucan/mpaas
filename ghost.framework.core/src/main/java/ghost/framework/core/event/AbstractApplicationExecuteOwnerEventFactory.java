package ghost.framework.core.event;

import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IApplicationExecuteOwnerBeanFactory;
import ghost.framework.context.bean.factory.IExecuteOwnerBeanTargetHandle;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用执行拥有者事件工厂基础类
 * @Date: 2020/2/22:12:18
 */
public abstract class AbstractApplicationExecuteOwnerEventFactory
        <O extends ICoreInterface, T, E extends IExecuteOwnerBeanTargetHandle<O, T>>
        extends AbstractApplicationOwnerEventFactory<O, T, E>
        implements IApplicationExecuteOwnerBeanFactory<O, T, E> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    protected AbstractApplicationExecuteOwnerEventFactory(IApplication app) {
        super(app);
    }
}