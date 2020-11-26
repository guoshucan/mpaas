package ghost.framework.context.bean.factory.locale;

import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.context.locale.ILocaleDomainContainer;

/**
 * package: ghost.framework.context.bean.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型区域注释绑定工厂接口
 * 作为下面注释类型的绑定工厂基础接口
 * {@link L10n}
 * {@link I18n}
 * @Date: 2020/6/14:12:42
 */
public interface IClassLocaleAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 获取区域容器接口
     *
     * @param event 事件对象
     * @param <R>   返回区域容器类型接口
     * @return 返回区域容器接口
     */
    default <R extends ILocaleDomainContainer<ILocaleDomain>> R getLocaleDomainContainer(E event) {
        throw new UnsupportedOperationException(IClassLocaleAnnotationBeanFactory.class.getName() + "#getLocaleDomainContainer(event)");
    }
}