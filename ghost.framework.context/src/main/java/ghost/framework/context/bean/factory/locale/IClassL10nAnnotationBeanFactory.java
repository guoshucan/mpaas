package ghost.framework.context.bean.factory.locale;

import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;

/**
 * package: ghost.framework.context.bean.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link L10n} 注释类型或包绑定工厂
 * @Date: 2020/6/9:14:20
 */
public interface IClassL10nAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
}