package ghost.framework.web.module.bean.factory.locale;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.web.context.bens.annotation.locale.WebI18nUI;
import ghost.framework.web.context.bens.factory.locale.AbstractClassWebI18nAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nUIAnnotationBeanFactory;
import ghost.framework.web.context.locale.IWebI18nUIDomainContainer;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.module.bean.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link WebI18nUI} 注释类型或包绑定工厂
 * @Date: 2020/6/9:14:19
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassWebI18nUIAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassWebI18nAnnotationBeanFactory<O, T, E, V>
        implements IClassWebI18nUIAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = WebI18nUI.class;

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
        WebI18nUI i18N = this.getAnnotation(event);
        //判断是否使用虚拟路径
        if (i18N.virtualPath().equals("")) {
            //获取国际化容器接口
            IWebI18nUIDomainContainer<ILocaleDomain> container = event.getExecuteOwner().getBean(IWebI18nUIDomainContainer.class);
            //是否为容器国际化注释
            this.loaderLocale(event, i18N.value(), i18N.localeNames(), i18N.localeTitles(), container);
        } else {
            this.loaderLocale(event, i18N.value(), i18N.virtualPath());
        }
    }
}