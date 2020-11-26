package ghost.framework.context.maven;

import ghost.framework.context.bean.factory.BeanTargetHandle;
import ghost.framework.context.bean.factory.IOwnerBeanTargetHandle;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:拥有者事件目标处理接类
 * @Date: 15:03 2020/1/17
 */
public class OwnerEventTargetHandle<O, T> extends BeanTargetHandle<O, T> implements IOwnerBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public OwnerEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}