//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationProxyContent;
//import ghost.framework.core.proxy.cglib.CglibProxyFactoryContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用代理内容类
// * @Date: 8:35 2019/11/24
// */
//abstract class ApplicationProxyContent extends ApplicationInterceptorContent implements IApplicationProxyContent {
//    /**
//     *
//     * @param rootClass
//     * @throws Exception
//     */
//    protected ApplicationProxyContent( Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationProxyContent");
//    }
//
//    /**
//     * 获取代理容器 {@link ghost.framework.core.proxy.cglib.CglibProxyFactoryContainer}
//     * @return
//     */
//    @Override
//    public CglibProxyFactoryContainer getProxyFactoryContainer() {
//        return proxyFactoryContainer;
//    }
//    private CglibProxyFactoryContainer proxyFactoryContainer;
//    @Override
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationProxyContent.init->Before");
//        super.init();
//        //创建代理容器绑定
//        this.proxyFactoryContainer = new CglibProxyFactoryContainer();
//        try {
//            this.bean(this.proxyFactoryContainer);
//        }catch (Exception e){
//            if (this.getLog().isDebugEnabled()) {
//                e.printStackTrace();
//                this.getLog().debug(e.getMessage());
//            } else {
//                this.getLog().error(e.getMessage());
//            }
//        }
//        this.getLog().info("~ApplicationProxyContent.init->After");
//    }
//}