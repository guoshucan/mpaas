package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.field.IFieldInjectionTargetHandle;
import ghost.framework.core.event.ValueEventTargetHandle;

import java.lang.reflect.Field;

/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注入声明时间目标处理类
 * @Date: 2020/1/7:17:00
 */
public class FieldInjectionTargetHandle
        <O extends ICoreInterface, T extends Object, F extends Field, V extends Object> extends ValueEventTargetHandle<O, T, V>
        implements IFieldInjectionTargetHandle<O, T, F, V> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param field  声明例
     */
    public FieldInjectionTargetHandle(O owner, T target, F field) {
        super(owner, target);
        this.field = field;
    }

    /**
     * 声明例
     */
    private F field;

    /**
     * 获取注入声明例
     *
     * @return
     */
    @Override
    public F getField() {
        return field;
    }

    @Override
    public ParentPriority getParentPriority() {
        if (this.field.isAnnotationPresent(ParentPriority.class)) {
            return this.field.getAnnotation(ParentPriority.class);
        }
        return null;
    }
}