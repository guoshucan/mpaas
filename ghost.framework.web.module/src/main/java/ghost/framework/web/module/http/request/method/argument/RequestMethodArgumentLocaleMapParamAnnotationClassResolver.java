package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.locale.LocalePackage;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.bean.factory.locale.IClassI18nAnnotationBeanFactory;
import ghost.framework.context.bean.factory.locale.IClassL10nAnnotationBeanFactory;
import ghost.framework.context.bean.factory.locale.IClassLocaleAnnotationBeanFactory;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.core.locale.I18nContainer;
import ghost.framework.core.locale.L10nContainer;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nNavAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.locale.IClassWebI18nUIAnnotationBeanFactory;
import ghost.framework.web.context.bind.annotation.LocaleMapParam;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationResolver;
import ghost.framework.web.context.locale.IWebI18nLayoutContainer;
import ghost.framework.web.context.locale.IWebI18nUIContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

/**
 * package: ghost.framework.web.module.http.request.method.argument
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link Map} 注释参数
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
 * {@link LocaleMapParam}
 * @Date: 2020/6/17:21:44
 */
@Order
@HandlerMethodArgumentResolver
public class RequestMethodArgumentLocaleMapParamAnnotationClassResolver
        extends AbstractRequestMethodArgumentAnnotationResolver {
    @Autowired
    private I18nContainer i18nContainer;
    @Autowired
    private L10nContainer l10nContainer;
    @Autowired
    private IWebI18nUIContainer uiContainer;
    @Autowired
    private IWebI18nLayoutContainer layoutContainer;

    /**
     * 解析控制器函数参数
     *
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return 返回参数值
     * @throws ResolverException
     */
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //比对解析类型
        LocaleMapParam param = requestMethod.getMethod().getAnnotation(LocaleMapParam.class);
        Locale locale = (Locale) request.getAttribute(Locale.class.getName());
        switch (param.value()) {
            case I18n:
                return this.i18nContainer.getLocale(locale);
            case L10n:
                return this.l10nContainer.getLocale(locale);
            case WebI18nUI:
                return this.uiContainer.getLocale(locale);
            case WebI18nLayout:
                return this.layoutContainer.getLocale(locale);
        }
        throw new ResolverException(parameter.getName());
    }

    private final Class<? extends Annotation> annotation = LocaleMapParam.class;

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    /**
     * 重写只判断注释
     *
     * @param request
     * @param response
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    public boolean isResolver(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return super.isAnnotation(request, response, requestMethod, parameter) &&
                super.isResolverType(request, response, requestMethod, parameter);
    }

    /**
     * 获取解析类型
     *
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return classs;
    }

    private final Class[] classs = new Class[]{Map.class};
}