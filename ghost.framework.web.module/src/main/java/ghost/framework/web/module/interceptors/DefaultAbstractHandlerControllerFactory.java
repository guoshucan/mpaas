package ghost.framework.web.module.interceptors;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.module.IModule;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:控制器工厂模式
 * @Date: 0:39 2019-10-06
 */
public class DefaultAbstractHandlerControllerFactory implements AutoCloseable, IHandlerControllerFactory {
    /**
     * 绑定完成回调
     */
//    @Call(depend = Bean.class, mode = CallMode.complete)
//    private void beanCompleteCall(){
//        //请求拦截器
//        System.out.println("beanCompleteCall");
////        thi事件s.interceptorList.loader(this.acceptHeaderIPAccessInterceptor);
////        this.interceptorList.loader(this.acceptHeaderLocaleResolverInterceptor);
////        this.interceptorList.loader(this.acceptHeaderParameterResolverInterceptor);
////        this.interceptorList.loader(this.acceptHeaderMethodResolverInterceptor);
//        //响应拦截器
////        this.interceptorList.loader(this.responseHeaderResolverInterceptor);
////        this.interceptorList.loader(this.responseBodyResolverInterceptor);
//    }
    /**
     * 注册绑定后事件
     *
     * @param definition
     */
    public void addBeanEventAfter(IBeanDefinition definition) {
        //请求拦截器
        //ip处理拦截器绑定事件处理
//        if (definition.getObject() instanceof IHttpAcceptHeaderIPAccessInterceptor) {
//            this.acceptHeaderIPAccessInterceptor = (IHttpAcceptHeaderIPAccessInterceptor) definition.getObject();
//        }
//        //
//        if (definition.getObject() instanceof IHttpAcceptHeaderLocaleResolverInterceptor) {
//            this.acceptHeaderLocaleResolverInterceptor = (IHttpAcceptHeaderLocaleResolverInterceptor) definition.getObject();
//        }
        //
//        if (definition.getObject() instanceof IHttpAcceptHeaderParameterResolverInterceptor) {
//            this.acceptHeaderParameterResolverInterceptor = (IHttpAcceptHeaderParameterResolverInterceptor) definition.getObject();
//        }
//        //
//        if (definition.getObject() instanceof IHttpAcceptHeaderMethodResolverInterceptor) {
//            this.acceptHeaderMethodResolverInterceptor = (IHttpAcceptHeaderMethodResolverInterceptor) definition.getObject();
//        }
//        //响应拦截器
//        if (definition.getObject() instanceof IHttpResponseHeaderResolverInterceptor) {
//            this.responseHeaderResolverInterceptor = (IHttpResponseHeaderResolverInterceptor) definition.getObject();
//        }
//        //
//        if (definition.getObject() instanceof IHttpResponseBodyResolverInterceptor) {
//            this.responseBodyResolverInterceptor = (IHttpResponseBodyResolverInterceptor) definition.getObject();
//        }
    }

    /**
     * 引发卸载绑定后事件
     *
     * @param definition
     */
    public void removeBeanEventAfter(IBeanDefinition definition) {
        //请求拦截器
        //ip处理拦截器绑定事件处理
//        if (definition.getObject() instanceof IHttpAcceptHeaderIPAccessInterceptor) {
//            this.acceptHeaderIPAccessInterceptor = null;
//        }
//        //
//        if (definition.getObject() instanceof IHttpAcceptHeaderLocaleResolverInterceptor) {
//            this.acceptHeaderLocaleResolverInterceptor = null;
//        }
        //
//        if (definition.getObject() instanceof IHttpAcceptHeaderParameterResolverInterceptor) {
//            this.acceptHeaderParameterResolverInterceptor = null;
//        }
//        //
//        if (definition.getObject() instanceof IHttpAcceptHeaderMethodResolverInterceptor) {
//            this.acceptHeaderMethodResolverInterceptor = null;
//        }
//        //响应拦截器
//        if (definition.getObject() instanceof IHttpResponseHeaderResolverInterceptor) {
//            this.responseHeaderResolverInterceptor = null;
//        }
//        //
//        if (definition.getObject() instanceof IHttpResponseBodyResolverInterceptor) {
//            this.responseBodyResolverInterceptor = null;
//        }
    }

    /**
     * 释放资源
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        try {
            //this.content.getApplicationContent().uninstallBeanEvent(this, this.interceptorList);
        } catch (Exception e) {

        }
    }

    /**
     * 拦截器列表
     */
    @Autowired
    private IHttpInterceptorContainer interceptorList;
    /**
     * 拥有者
     */
    @Autowired
    private IModule module;
//    /**
//     * 默认响应内容包装函数拦截器
//     */
//    @Autowired
//    private IHttpAcceptHeaderMethodResolverInterceptor acceptHeaderMethodResolverInterceptor;
//    /**
//     * 默认响应内容包装拦截器
//     */
//    @Autowired
//    private IHttpResponseHeaderResolverInterceptor responseHeaderResolverInterceptor;
//    /**
//     * 默认响应内容包装拦截器
//     */
//    @Autowired
//    private IHttpResponseBodyResolverInterceptor responseBodyResolverInterceptor;
//    /**
//     * 默认http头参数拦截器
//     */
//    @Autowired
//    private IHttpAcceptHeaderParameterResolverInterceptor acceptHeaderParameterResolverInterceptor;
//    /**
//     * 默认语言环境解析拦截器
//     */
//    @Autowired
//    private IHttpAcceptHeaderLocaleResolverInterceptor acceptHeaderLocaleResolverInterceptor;
//    /**
//     * ip拦截器
//     */
//    @Autowired
//    private IHttpAcceptHeaderIPAccessInterceptor acceptHeaderIPAccessInterceptor;

//    @Override
//    public void bean(IEventTargetHandle event) throws Exception {
//
//    }
//
//    @Override
//    public String getBeanName(Object obj) throws Exception {
//        return null;
//    }

//    @Override
//    public void addBeanEvent(E event) {
//
//    }
}
