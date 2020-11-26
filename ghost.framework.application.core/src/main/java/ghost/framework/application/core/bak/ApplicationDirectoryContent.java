//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationDirectory;
//
//import java.io.File;
//import java.util.UUID;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用目录基础类
// * @Date: 12:30 2019/5/28
// */
//abstract class ApplicationDirectoryContent extends ApplicationEnvContent implements IApplicationDirectory {
//    /**
//     * 初始化应用内容基础类
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationDirectoryContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationDirectoryContent");
//    }
//
//    /**
//     * 临时目录
//     */
//    protected File tempDirectory;
//
//    /**
//     * 获取临时目录
//     *
//     * @return
//     */
//    @Override
//    public File getTempDirectory() {
//        return tempDirectory;
//    }
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception{
//        this.getLog().info("~ApplicationDirectoryContent.init->Before");
//        super.init();
//        //创建jar运行临时目录
//        this.tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().toLowerCase());
//        //创建目录
//        this.tempDirectory.mkdir();
//        //在jar程序退出时删除目录
//        this.tempDirectory.deleteOnExit();
//        this.getLog().info("~ApplicationDirectoryContent.init->After");
//    }
//}