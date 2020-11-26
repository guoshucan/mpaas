package ghost.framework.core.event;

import ghost.framework.context.bean.factory.IValueBeanTargetHandle;
import ghost.framework.context.bean.factory.AnnotationBeanTargetHandle;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:值事件目标处理类型
 * @Date: 2020/1/8:8:52
 * @param <O> 事件发起方类型
 * @param <T> 目标类型
 * @param <V> 返回值类型
 */
public class ValueEventTargetHandle<O, T, V extends Object> extends AnnotationBeanTargetHandle<O, T> implements IValueBeanTargetHandle<O, T, V> {

    private V value;
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ValueEventTargetHandle(O owner, T target, V value) {
        super(owner, target);
        this.value = value;
    }

    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ValueEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 获取值
     * @return
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * 设置值
     * @param value
     */
    @Override
    public void setValue(V value) {
        this.value = value;
    }
}