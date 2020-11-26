//package ghost.framework.app.context;
//
//import ghost.framework.core.maven.MavenRepositoryContainer;
//import ghost.framework.util.NotImplementedException;
//
//import java.io.File;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用maven接口
// * @Date: 12:58 2019/5/28
// */
//public interface IApplicationMaven {
//
//    /**
//     * 获取maven本地仓库路径
//     *
//     * @return
//     */
//    String getMavenLocalRepositoryPath();
//
//    /**
//     * 获取maven本地仓库目录
//     *
//     * @return
//     */
//    File getMavenLocalRepositoryFile();
//
//    /**
//     * 获取maven远程仓库容器
//     *
//     * @return
//     */
//    MavenRepositoryContainer getMavenRepositoryContainer();
//
//    /**
//     * maven本地仓库env参数名称
//     */
//    String MAVEN_LOCAL_REPOSITORY_PATH = "ghost.framework.maven.local.repository.path";
////    /**
////     * win系统本地默认m2仓库目录env参数名称
////     */
////    String WIN_MAVEN_LOCAL_REPOSITORY_PATH = "ghost.framework.win.maven.local.repository.path";
////    /**
////     * linux系统本地默认m2仓库目录env参数名称
////     */
////    String LINUX_MAVEN_LOCAL_REPOSITORY_PATH = "ghost.framework.linux.maven.local.repository.path";
//    /**
//     * 引导加载包
//     *
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws NotImplementedException
//     */
//    void bootLoader() throws IllegalArgumentException, IllegalAccessException, NotImplementedException;
//    /**
//     * 运行模块名称
//     * 格式ghost.framework:ghost-framework-app-core:1.0-SNAPSHOT,ghost.framework:ghost-framework-app-core:1.0-SNAPSHOT
//     * 执行循序：0
//     */
////    String APPLICATION_PROPERTIES_MAVEN_INSTALL_APP_NAMES = "ghost.framework.maven.install.app.names";
//    /**
//     * 先执行安装{APPLICATION_PROPERTIES_MAVEN_INSTALL_APP_NAMES}指定模块后再执行初始化{APPLICATION_PROPERTIES_MAVEN_INSTALL_MODULE_NAMES}指定模块
//     * 配置文件maven安装模块名称常量
//     * 格式ghost.framework:ghost-framework-maven-module:1.0-SNAPSHOT,ghost.framework:ghost-framework-maven-module:1.0-SNAPSHOT
//     * 一般情况ghost-framework-maven-module模块是最先安装的初始化模块，安装后管理所有安装的其它模块，maven模块作为其它模块的容器模块
//     * 执行循序：1
//     */
//    String APPLICATION_PROPERTIES_MAVEN_INSTALL_MODULE_NAMES = "ghost.framework.maven.install.module.names";
//    /**
//     * 配置文件maven仓库地址名称常量
//     * 格式[value=maven-public,type=,user=,password=,url=|value=maven-public,type=,user=,password=,url=]
//     */
//    String APPLICATION_PROPERTIES_MAVEN_REPOSITORY_URLS = "ghost.framework.maven.repository.urls";
//}
