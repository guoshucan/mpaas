package ghost.framework.web.module;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.server.MimeMappings;
import ghost.framework.web.context.servlet.filter.IDispatcherFilter;
import ghost.framework.web.module.annotation.bean.factory.ClassWebFilterAnnotationBeanFactory;
import ghost.framework.web.module.annotation.bean.factory.ClassWebServletAnnotationBeanFactory;
import ghost.framework.web.module.bean.factory.ClassControllerAdviceAnnotationBeanFactory;
import ghost.framework.web.module.bean.factory.ClassRestControllerAnnotationBeanFactory;
import ghost.framework.web.module.bean.factory.ClassWebResourceAnnotationBeanFactory;
import ghost.framework.web.module.bean.factory.locale.ClassWebI18nNavAnnotationBeanFactory;
import ghost.framework.web.module.controller.ControllerExceptionHandlerContainer;
import ghost.framework.web.module.event.annotation.resource.ClassWebResourceResolverAnnotationEventFactory;
import ghost.framework.web.module.event.method.MethodRequestMappingAnnotationEventFactory;
import ghost.framework.web.module.event.servlet.context.container.ServletContextBeanListenerContainer;
import ghost.framework.web.module.http.HttpControllerContainer;
import ghost.framework.web.module.http.request.method.argument.*;
import ghost.framework.web.module.http.request.method.argument.bean.ClassHandlerMethodArgumentAnnotationResolverBeanFactory;
import ghost.framework.web.module.http.request.method.argument.bean.ClassHandlerMethodReturnValueAnnotationResolverBeanFactory;
import ghost.framework.web.module.http.request.method.returnValue.*;
import ghost.framework.web.module.http.response.DefaultWebDirectoryResourceResolver;
import ghost.framework.web.module.http.response.DefaultWebResourceResolver;
import ghost.framework.web.module.http.response.WebResourceResolverContainer;
import ghost.framework.web.module.io.WebIResourceLoader;
import ghost.framework.web.module.locale.WebI18nLayoutContainer;
import ghost.framework.web.module.locale.WebI18nLayoutDomainContainer;
import ghost.framework.web.module.locale.WebI18nUIContainer;
import ghost.framework.web.module.locale.WebI18nUIDomainContainer;
import ghost.framework.web.module.server.ConfigurableServletWebServerFactoryContainer;
import ghost.framework.web.module.server.WebServerContainer;
import ghost.framework.web.module.server.session.SessionRepositoryContainer;
import ghost.framework.web.module.servlet.ServletContextEventListener;
import ghost.framework.web.module.servlet.filter.*;

import javax.servlet.ServletException;

/**
 * package: ghost.framework.web.module
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块配置
 * @Date: 2020/2/18:20:09
 */
@Configuration
public class ModuleConfig {
    /**
     * 注入模块接口
     */
    @Autowired
    private IModule module;

    /**
     * 注册注释事件工厂
     */
    @Invoke
//    @Order(0)
    public void registerAnnotationEventFactory() throws ServletException {
        this.module.addBean(MimeMappings.class);
        //注册绑定web资源加载器
        this.module.addBean(WebIResourceLoader.class);
        //注释绑定web资源解析器容器
        this.module.addBean(WebResourceResolverContainer.class);
        //注释web资源解析器注释事件工厂
        this.module.addBean(ClassWebResourceResolverAnnotationEventFactory.class);
        this.module.addBean(DefaultWebResourceResolver.class);
        this.module.addBean(DefaultWebDirectoryResourceResolver.class);
        //绑定国际化
        this.module.addBean(WebI18nUIContainer.class);
        this.module.addBean(WebI18nLayoutContainer.class);
        this.module.addBean(WebI18nLayoutDomainContainer.class);
        this.module.addBean(WebI18nUIDomainContainer.class);
//        //注册绑定web路由容器
//        this.module.addBean(WebIResourceLoader.class);
//        //注册绑定web资源容器
//        this.module.addBean(WebResourceContainer.class);
//        //注册绑定web资源注释处理
//        this.module.addBean(ClassWebResourceAnnotationEventFactory.class);
        //注册处理函数参数解析器注释事件工厂
        this.module.addBean(ClassHandlerMethodArgumentAnnotationResolverBeanFactory.class);
        this.module.addBean(ClassHandlerMethodReturnValueAnnotationResolverBeanFactory.class);
        //注册处理函数参数解析器容器
        this.module.addBean(RequestMethodArgumentResolverContainer.class);
        this.module.addBean(RequestMethodReturnValueResolverContainer.class);
        //注册ServletContext事件监听容器
        this.module.addBean(ServletContextEventListener.class);
        //注释web服务容器
        this.module.addBean(ConfigurableServletWebServerFactoryContainer.class);
        this.module.addBean(WebServerContainer.class);
        //注册应用事件处理
        this.module.addBean(ApplicationEventDispatch.class);
        //注册类型著注释事件工厂
//        this.module.addBean(ClassAnnotationControllerBeanFactory.class);
        this.module.addBean(ClassControllerAdviceAnnotationBeanFactory.class);
        this.module.addBean(ClassRestControllerAnnotationBeanFactory.class);
        this.module.addBean(ClassWebFilterAnnotationBeanFactory.class);
        this.module.addBean(ClassWebServletAnnotationBeanFactory.class);
        this.module.addBean(ClassWebI18nNavAnnotationBeanFactory.class);
        this.module.addBean(ClassWebResourceAnnotationBeanFactory.class);
//        this.module.addBean(ClassWebI18NContainerAnnotationBeanFactory.class);
        //注册绑定控制器全局错误处理容器
        this.module.addBean(ControllerExceptionHandlerContainer.class);
        //注册函数注释事件工厂
        this.module.addBean(MethodRequestMappingAnnotationEventFactory.class);
        //注册会话仓库容器
        this.module.addBean(SessionRepositoryContainer.class);
        //
        this.module.addBean(HttpControllerContainer.class);
        this.module.addBean(ServletContextBeanListenerContainer.class);
        //注册拦截器
        IDispatcherFilter dispatcherFilter = this.module.addBean(DispatcherFilter.class);
        this.module.addBean(FilterContainer.class);//过滤器容器
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(ResponseGzipFilter.class));//-1
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(BeforeFilter.class));//0
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(CharacterEncodingFilter.class));//1
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(CookieFilter.class));//2
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(SessionRepositoryFilter.class));//3
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(CrossFilter.class));//4
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(AuthorizationFilter.class));//5
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(HttpRequestMultipartFilter.class));//6
//        this.module.addBean(FormContentFilter.class);//7
        dispatcherFilter.getFilterExecutionChain().add(this.module.addBean(HttpRequestMethodFilter.class));//8
        //注册处理函数解析器
        //注册处理函数参数各种解析器
        this.module.addBean(RequestMethodArgumentAutowiredAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentCookieValueAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentCookieValuesAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentPathVariableAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentRequestBodyAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentRequestHeaderAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentRequestParamAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentPrimitiveClassResolver.class);
        this.module.addBean(RequestMethodArgumentRequestPartAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentSessionAttributeAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentSessionAttributesAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentValueAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentServletClassResolver.class);
        this.module.addBean(RequestMethodArgumentMultipartRequestClassResolver.class);
        this.module.addBean(RequestMethodArgumentMultipartFileRequestParamAnnotationResolver.class);
        this.module.addBean(RequestMethodArgumentThrowableClassResolver.class);
        this.module.addBean(RequestMethodArgumentLocaleMapParamAnnotationClassResolver.class);
        //
        this.module.addBean(RequestMethodReturnValueApplicationJsonResolver.class);
        this.module.addBean(RequestMethodReturnValueApplicationXJavascriptResolver.class);
        this.module.addBean(RequestMethodReturnValueTextHtmlResolver.class);
        this.module.addBean(RequestMethodReturnValueImageClassResolver.class);
    }
//    @Invoke
//    @Order(1)
//    public void registerRequestMappingAnnotationEventFactory(){
//        this.module.addBean(MethodRequestMappingAnnotationEventFactory.class);
//    }
//    /**
//     * 注册应用事件监听
//     */
//    @Invoke
//    @Order(2)
//    public void registerApplicationEventListener(){
//        this.module.addBean(ApplicationEventDispatch.class);
//    }
//    @Bean
//    @Order
//    public Object bean0(){
//        return new Object();
//    }
//    @Bean
//    @Order(1)
//    public Object bean1(){
//        return new Object();
//    }
//
//    @Bean
//    @Order(2)
//    public Object bean2(){
//        return new Object();
//    }
//    @Invoke
//    @Order(3)
//    public void invoke(){
//
//    }
}
