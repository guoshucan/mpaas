package ghost.framework.web.context.bind.annotation;

import ghost.framework.beans.annotation.locale.LocalePackage;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.context.bean.factory.locale.IClassI18nAnnotationBeanFactory;
import ghost.framework.context.bean.factory.locale.IClassL10nAnnotationBeanFactory;
import ghost.framework.context.bean.factory.locale.IClassLocaleAnnotationBeanFactory;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nNavAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nUIAnnotationBeanFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.context.bind.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域参数注释
 * {@link ModulePackage}
 * {@link PluginPackage}
 * {@link LocalePackage}    `
 * {@link IClassLocaleAnnotationBeanFactory}
 * {@link IClassI18nAnnotationBeanFactory}
 * {@link IClassL10nAnnotationBeanFactory}
 * {@link ghost.framework.beans.annotation.locale.I18n}
 * {@link ghost.framework.beans.annotation.locale.L10n}
 * {@link WebI18nNav}
 * {@link ghost.framework.web.context.bens.annotation.locale.WebI18nUI}
 * {@link ghost.framework.core.locale.I18nDomainContainer}
 * {@link ghost.framework.context.locale.II18nContainer}
 * {@link ghost.framework.context.locale.IL10nDomainContainer}
 * {@link ghost.framework.core.locale.L10nContainer}
 * {@link IClassWebI18nAnnotationBeanFactory}
 * {@link IClassWebI18nUIAnnotationBeanFactory}
 * {@link IClassWebI18nNavAnnotationBeanFactory}
 * {@link ghost.framework.web.context.locale.IWebI18nUIDomainContainer}
 * {@link ghost.framework.web.context.locale.IWebI18nUIContainer}
 * {@link ghost.framework.web.context.locale.IWebI18nLayoutDomainContainer}
 * {@link ghost.framework.web.context.locale.IWebI18nLayoutContainer}
 * @Date: 2020/6/17:21:55
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocaleMapParam {
    /**
     * 注入区域类型
     * @return
     */
    Type value() default Type.WebI18nUI;
    /**
     * 指定注入区域
     * 比如zh_CN
     * @return
     */
    String locale() default "";
    /**
     * 注释类型
     */
    enum Type {
        I18n,
        L10n,
        WebI18nUI,
        WebI18nLayout
    }
}