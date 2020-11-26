//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationInterceptorContent;
//import ghost.framework.app.core.interceptors.ApplicationAnnotationInterceptorContainer;
//import ghost.framework.app.core.interceptors.ApplicationScanTypeInterceptorContainer;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 20:10 2019/12/4
// */
//abstract class ApplicationInterceptorContent extends ApplicationMavenContent implements IApplicationInterceptorContent {
//    /**
//     * 初始化应用env基础类
//     * @param rootClass         引导类
//     * @throws Exception
//     */
//    protected ApplicationInterceptorContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//    }
//
//    @Override
//    protected void init() throws Exception {
//        super.init();
//        try {
//            this.bean(new ApplicationScanTypeInterceptorContainer());
//            this.bean(new ApplicationAnnotationInterceptorContainer());
//        } catch (Exception e) {
//            if (this.getLog().isDebugEnabled()) {
//                e.printStackTrace();
//                this.getLog().debug(e.getMessage());
//            } else {
//                this.getLog().error(e.getMessage());
//            }
//        }
//    }
//}
