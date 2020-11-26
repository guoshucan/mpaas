package ghost.framework.core.bean.factory.locale;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.locale.AbstractClassLocaleAnnotationBeanFactory;
import ghost.framework.context.bean.factory.locale.IClassL10nAnnotationBeanFactory;
import ghost.framework.context.locale.IL10nDomainContainer;
import ghost.framework.context.locale.ILocaleDomain;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.bean.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link L10n} 注释类型或包绑定工厂
 * @Date: 2020/6/9:14:19
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassL10nAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassLocaleAnnotationBeanFactory<O, T, E, V>
        implements IClassL10nAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = L10n.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassL10nAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取注释对象
        L10n l10n = this.getAnnotation(event);
        //获取本地化容器接口
        IL10nDomainContainer<ILocaleDomain> container = event.getExecuteOwner().getBean(IL10nDomainContainer.class);
        //加载区域
        this.loaderLocale(event, l10n.value(), l10n.localeNames(), l10n.localeTitles(), container);
    }
}