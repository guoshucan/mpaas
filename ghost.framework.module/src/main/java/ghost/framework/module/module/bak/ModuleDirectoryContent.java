//package ghost.framework.module.module.bak;
//
//import ghost.framework.module.context.IModuleDirectory;
//import ghost.framework.module.context.ModuleDirectoryFile;
//
//import java.io.File;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块目录内容
// * @Date: 18:24 2019/6/4
// */
//abstract class ModuleDirectoryContent extends ModuleEnvContent implements IModuleDirectory {
//    /**
//     * 初始化模块目录内容
//     *
//     */
//    protected ModuleDirectoryContent(){
//        super();
//        this.getLog().info("~ModuleDirectoryContent");
//    }
//
//
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
//        //初始化模块临时目录
//        this.tempDirectory = new ModuleDirectoryFile(System.getProperty("java.io.tmpdir") + File.separator + this.getName());
//        //在退出时删除临时目录
//        this.tempDirectory.deleteOnExit();
//    }
//
//    /**
//     * 获取模块路径
//     *
//     * @return
//     */
//    @Override
//    public String getPath() {
//        return this.getRootClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//    }
//
//    /**
//     * 包临时目录
//     * 此目录在释放资源时自动删除
//     */
//    private ModuleDirectoryFile tempDirectory;
//
//    /**
//     * 获取包临时目录
//     *
//     * @return
//     */
//    @Override
//    public ModuleDirectoryFile getTempDirectory() {
//        return tempDirectory;
//    }
//}
