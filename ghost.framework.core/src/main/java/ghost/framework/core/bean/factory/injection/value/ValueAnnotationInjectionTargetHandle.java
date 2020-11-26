package ghost.framework.core.bean.factory.injection.value;

import ghost.framework.context.bean.factory.injection.value.IValueAnnotationInjectionTargetHandle;
import ghost.framework.context.bean.factory.AnnotationBeanTargetHandle;

/**
 * package: ghost.framework.core.bean.factory.injection.value
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:21 2020/2/1
 */
public class ValueAnnotationInjectionTargetHandle<O, T> extends AnnotationBeanTargetHandle<O, T> implements IValueAnnotationInjectionTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ValueAnnotationInjectionTargetHandle(O owner, T target) {
        super(owner, target);
    }
}