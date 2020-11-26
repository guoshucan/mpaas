//package ghost.framework.app.core.interceptors;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.core.interceptors.IInterceptor;
//import ghost.framework.core.interceptors.InterceptorContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用拦截器容器基础类
// * @Date: 0:45 2019/12/1
// */
//public abstract class ApplicationInterceptorContainer<I extends IInterceptor> extends InterceptorContainer<I> {
//    /**
//     * 应用内容
//     */
//    @Autowired
//    private IApplication app;
//
//    /**
//     * 获取应用内容
//     *
//     * @return
//     */
//    public IApplication getApp() {
//        return app;
//    }
//
//    /**
//     * 初始化应用拦截器容器基础类
//     */
//    protected ApplicationInterceptorContainer() {
//        super();
//    }
//}