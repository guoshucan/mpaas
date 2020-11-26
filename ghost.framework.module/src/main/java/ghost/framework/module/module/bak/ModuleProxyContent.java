//package ghost.framework.module.module.bak;
//
//import ghost.framework.core.proxy.cglib.CglibProxyFactoryContainer;
//import ghost.framework.module.context.IModuleProxy;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块代理内容
// * @Date: 8:44 2019/11/24
// */
//abstract class ModuleProxyContent extends ModuleBeanContent implements IModuleProxy {
//    /**
//     * 促使华模块代理内容
//     */
//    public ModuleProxyContent() {
//        super();
//        this.getLog().info("~ModuleProxyContent");
//    }
//    /**
//     * 获取代理容器 {@link ghost.framework.core.proxy.cglib.CglibProxyFactoryContainer}
//     * @return
//     */
//    @Override
//    public CglibProxyFactoryContainer getProxyFactoryContainer() {
//        return proxyFactoryContainer;
//    }
//
//    /**
//     * 代理容器
//     */
//    private CglibProxyFactoryContainer proxyFactoryContainer;
//
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        super.init();
//        //创建代理容器绑定
//        this.proxyFactoryContainer = new CglibProxyFactoryContainer();
//        this.bean(this.proxyFactoryContainer);
//    }
//}