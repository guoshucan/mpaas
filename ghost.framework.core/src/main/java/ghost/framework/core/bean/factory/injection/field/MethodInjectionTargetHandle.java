package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.field.IMethodInjectionTargetHandle;
import ghost.framework.core.event.ValueEventTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/22:23:24
 */
public class MethodInjectionTargetHandle
        <O extends ICoreInterface, T extends Object, F extends Method, V extends Object> extends ValueEventTargetHandle<O, T, V>
        implements IMethodInjectionTargetHandle<O, T, F, V> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param method 函数
     */
    public MethodInjectionTargetHandle(O owner, T target, F method) {
        super(owner, target);
        this.method = method;
    }

    /**
     * 声明例
     */
    private F method;

    /**
     * 获取注入声明例
     *
     * @return
     */
    @Override
    public F getMethod() {
        return method;
    }

    @Override
    public ParentPriority getParentPriority() {
        if (this.method.isAnnotationPresent(ParentPriority.class)) {
            return this.method.getAnnotation(ParentPriority.class);
        }
        return null;
    }
}