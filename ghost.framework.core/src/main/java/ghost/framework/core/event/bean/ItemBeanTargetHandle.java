package ghost.framework.core.event.bean;

import ghost.framework.context.bean.factory.IItemBeanTargetHandle;
import ghost.framework.core.event.NameBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象事件目标处理
 * @Date: 16:28 2019/12/26
 * @param <O>
 * @param <T>
 */
public class ItemBeanTargetHandle<O, T, S>
        extends NameBeanTargetHandle<O, T, S>
        implements IItemBeanTargetHandle<O, T, S> {
    /**
     * @param owner
     * @param target
     */
    public ItemBeanTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * @param owner
     * @param target
     * @param name
     */
    public ItemBeanTargetHandle(O owner, T target, S name) {
        super(owner, target, name);
    }
}