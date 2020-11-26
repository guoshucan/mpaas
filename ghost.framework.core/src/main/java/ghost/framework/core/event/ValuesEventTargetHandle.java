package ghost.framework.core.event;

import ghost.framework.context.bean.factory.IValuesBeanTargetHandle;
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
public class ValuesEventTargetHandle<O, T, V extends Object> extends AnnotationBeanTargetHandle<O, T> implements IValuesBeanTargetHandle<O, T, V> {

    private V[] values;
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ValuesEventTargetHandle(O owner, T target, V[] values) {
        super(owner, target);
        this.values = values;
    }

    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ValuesEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 获取值
     * @return
     */
    @Override
    public V[] getValues() {
        return values;
    }

    /**
     * 设置值
     * @param value
     */
    @Override
    public void setValues(V[] value) {
        this.values = values;
    }
}