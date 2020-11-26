package ghost.framework.web.context.bens.factory.locale;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.locale.IClassI18nAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IWebClassAnnotationBeanFactory;

/**
 * package: ghost.framework.web.context.bens.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/13:23:38
 */
public interface IClassWebI18nAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IWebClassAnnotationBeanFactory<O, T, E, V>, IClassI18nAnnotationBeanFactory<O, T, E, V> {
}