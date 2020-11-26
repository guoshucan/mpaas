package ghost.framework.web.context.bens.factory.locale;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.context.bens.annotation.locale.WebI18nUI;

/**
 * package: ghost.framework.web.context.bens.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link WebI18nUI} 注释绑定工厂接口
 * @Date: 2020/6/13:23:38
 */
public interface IClassWebI18nUIAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassWebI18nAnnotationBeanFactory<O, T, E, V> {
}