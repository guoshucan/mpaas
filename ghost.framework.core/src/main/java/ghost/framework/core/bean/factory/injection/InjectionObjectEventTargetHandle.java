package ghost.framework.core.bean.factory.injection;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.BeanTargetHandle;
import ghost.framework.context.bean.factory.injection.IInjectionObjectBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注入对象事件目标处理类
 * @Date: 17:07 2020/1/11
 */
public class InjectionObjectEventTargetHandle
        <O extends ICoreInterface, T extends Object>
        extends BeanTargetHandle<O, T>
        implements IInjectionObjectBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public InjectionObjectEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}
