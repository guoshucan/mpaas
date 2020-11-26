//package ghost.framework.module.module.bak;
//
//import ghost.framework.core.locale.ILocaleContainer;
//import ghost.framework.module.context.IModuleLanguageContent;
//import ghost.framework.module.language.GlobalContainer;
//import ghost.framework.module.language.LocalContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块语言内容
// * @Date: 18:38 2019/6/4
// */
//abstract class ModuleLanguageContent extends ModuleResourceContent implements IModuleLanguageContent {
//    /**
//     * 初始化模块语言内容
//     */
//    protected ModuleLanguageContent() {
//        super();
//        this.getLog().info("~ModuleLanguageContent");
//    }
//
//    /**
//     * 释放资源
//     * @throws Exception
//     */
//    @Override
//    public synchronized void close() throws Exception {
//        super.close();
//    }
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        super.init();
//        this.globalContainer = new GlobalContainer(this);
//        this.localContainer = new LocalContainer(this);
//    }
//    /**
//     * 国际化
//     */
//    private GlobalContainer globalContainer;
//
//    /**
//     * 获取国际化
//     *
//     * @return
//     */
//    @Override
//    public ILocaleContainer getGlobalContainer() {
//        return globalContainer;
//    }
//
//    /**
//     * 本地化
//     */
//    private LocalContainer localContainer;
//
//    /**
//     * 获取本地化
//     *
//     * @return
//     */
//    @Override
//    public ILocaleContainer getLocalContainer() {
//        return localContainer;
//    }
//}
