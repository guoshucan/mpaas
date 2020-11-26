/**
 * package: ghost.framework.web.module
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块包
 * @Date: 2020/2/5:12:34
 */
//注释国际化
//@I18n
//注释本地化
//@L10n
@ModulePackage(
        depend = {
                ModuleConfig.class,
                ClassWebI18nNavAnnotationBeanFactory.class,
                ClassWebI18nUIAnnotationBeanFactory.class,
//                FilterContainer.class,
                InterceptorRegistry.class,
                HttpInterceptorContainer.class,
                ServletContainer.class,
                DispatcherServlet.class,
//                MethodRequestMappingAnnotationEventFactory.class,
//                DefaultClassRestControllerAnnotationEventFactory.class,
//                DefaultHttpRequestMethodContainer.class,
//                DefaultHttpRestControllerContainer.class,
//                ServletContextEventListenerContainer.class,
//                CharacterEncodingFilter.class,
//                CrossFilter.class,
//                RequestContextFilter.class,
//                HttpRequestInterceptorFilter.class,
//                HttpRequestMethodFilter.class,
                ServletContextContainer.class,
                ServletContextMapping.class,
        })
package ghost.framework.web.module;

import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.web.module.bean.factory.locale.ClassWebI18nNavAnnotationBeanFactory;
import ghost.framework.web.module.bean.factory.locale.ClassWebI18nUIAnnotationBeanFactory;
import ghost.framework.web.module.interceptors.HttpInterceptorContainer;
import ghost.framework.web.module.servlet.DispatcherServlet;
import ghost.framework.web.module.servlet.InterceptorRegistry;
import ghost.framework.web.module.servlet.ServletContainer;
import ghost.framework.web.module.servlet.context.ServletContextContainer;
import ghost.framework.web.module.servlet.context.ServletContextMapping;
