package ghost.framework.core.event;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IOperatingBeanBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/7:16:16
 */
public class OperatingBeanEventTargetHandle<O extends ICoreInterface, T extends IBeanDefinition> extends MethodEventTargetHandle<O, T> implements IOperatingBeanBeanTargetHandle<O, T> {
    public OperatingBeanEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    public OperatingBeanEventTargetHandle(O owner, T target, Method method) {
        super(owner, target, method);
    }

    /**
     * 获取父及优先注释
     *
     * @return
     */
    @Override
    public ParentPriority getParentPriority() {
        Class<?> c = this.getTarget().getObject().getClass();
        if (c.isAnnotationPresent(ParentPriority.class)) {
            return c.getAnnotation(ParentPriority.class);
        }
        return null;
    }
}