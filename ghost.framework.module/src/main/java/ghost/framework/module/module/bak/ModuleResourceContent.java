//package ghost.framework.module.module.bak;
//
//import ghost.framework.module.context.IModuleResource;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块资源内容
// * @Date: 18:31 2019/6/4
// */
//abstract class ModuleResourceContent extends ModuleDirectoryContent implements IModuleResource {
//
//    /**
//     * 初始化模块目录内容
//     *
//     */
//    protected ModuleResourceContent() {
//        super();
//        this.getLog().info("~ModuleResourceContent");
//    }
//
//    /**
//     * 释放资源
//     * @throws Exception
//     */
//    @Override
//    public synchronized void close() throws Exception {
//        if (this.resourceContainer != null) {
//            synchronized (this.resourceContainer) {
//                if (this.resourceContainer != null) {
//                    this.resourceContainer.close();
//                    this.resourceContainer = null;
//                }
//            }
//        }
//        super.close();
//    }
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        super.init();
//        //初始化模块资源容器
//        this.resourceContainer = new ModuleResourceContainer(this);
//    }
//    /**
//     * 模块资源容器
//     */
//    private ModuleResourceContainer resourceContainer;
//
//    /**
//     * 获取模块资源容器
//     *
//     * @return
//     */
//    @Override
//    public ModuleResourceContainer getResourceContainer() {
//        return resourceContainer;
//    }
//}
