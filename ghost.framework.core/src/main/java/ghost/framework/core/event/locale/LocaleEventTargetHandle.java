package ghost.framework.core.event.locale;

import ghost.framework.context.bean.factory.AnnotationBeanTargetHandle;

/**
 * package: ghost.framework.core.event.locale
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 17:45 2020/1/20
 */
public class LocaleEventTargetHandle<O, T> extends AnnotationBeanTargetHandle<O, T> implements ILocaleEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public LocaleEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}