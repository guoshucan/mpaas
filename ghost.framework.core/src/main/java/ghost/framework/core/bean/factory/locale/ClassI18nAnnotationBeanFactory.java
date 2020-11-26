package ghost.framework.core.bean.factory.locale;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.locale.AbstractClassLocaleAnnotationBeanFactory;
import ghost.framework.context.bean.factory.locale.IClassI18nAnnotationBeanFactory;
import ghost.framework.context.locale.II18nDomainContainer;
import ghost.framework.context.locale.ILocaleDomain;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.bean.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link I18n} 注释类型或包绑定工厂
 * @Date: 2020/6/9:14:19
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassI18nAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassLocaleAnnotationBeanFactory<O, T, E, V>
        implements IClassI18nAnnotationBeanFactory<O, T, E, V> {
    @Override
    public String toString() {
        return "ClassI18nAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = I18n.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取注释对象
        I18n i18n = this.getAnnotation(event);
        //获取国际化容器接口
        II18nDomainContainer<ILocaleDomain> container = event.getExecuteOwner().getBean(II18nDomainContainer.class);
        //加载区域
        this.loaderLocale(event, i18n.value(), i18n.localeNames(), i18n.localeTitles(),  container);
    }
}