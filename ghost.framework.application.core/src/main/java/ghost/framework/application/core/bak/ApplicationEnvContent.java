//package ghost.framework.app.core;
//import ghost.framework.context.application.IApplicationEnv;
//import ghost.framework.app.context.IApplicationMaven;
//import ghost.framework.app.core.env.ApplicationEnvironment;
//import ghost.framework.context.app.IApplicationEnvironment;
//import ghost.framework.beans.configuration.ConfigurationUtil;
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.context.utils.AssemblyUtil;
//import java.io.File;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用env基础类
// * @Date: 1:25 2019-05-28
// */
//abstract class ApplicationEnvContent extends ApplicationLogContent implements IApplicationEnv {
//    /**
//     * 初始化应用env基础类
//     *
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationEnvContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationEnvContent");
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//        super.close();
//    }
//
//    /**
//     * 配置
//     */
//    protected ApplicationEnvironment env;
//
//    /**
//     * 获取应用内容env
//     *
//     * @return
//     */
//    @Override
//    public IApplicationEnvironment getEnv() {
//        return this.env;
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationEnvContent.init->Before");
//        super.init();
//        //env初始化应用内容
//        this.env = new ApplicationEnvironment();
//        //判断是否为开发模式
//        if (AssemblyUtil.isPathDev(this.getHome().getSource().getPath())) {
//            //开发模式
//            this.env.setBoolean("ghost.framework.app.core.dev", true);
//        } else {
//            //不为开发模式
//            this.env.setBoolean("ghost.framework.app.core.dev", false);
//        }
//        //设置系统类型
//        //win为否，linux为是
//        this.env.setBoolean("ghost.framework.app.core.os", File.separator.equals("/"));
//        for (ConfigurationProperties p : ConfigurationUtil.getClassOrderList(this.getRootClass())) {
//            if (this.isDev()) {
//                this.env.merge(this.getHome().getSource().getPath() + File.separator + p.path());
//            } else {
//                //获取jar包模式配置文件
//                this.env.merge(p.prefix(), this.getUrlPath(p.path()));
//            }
//        }
////        //判断是否需要初始化env的maven配置
////        if (!this.env.containsKey(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH)) {
////            //获取默认运行包配置
////            //判断是否注释默认配置文件，使用getAnnotationsByType方法才能获取到父类的注释
////            ConfigurationProperties[] propertiess = this.getRootClass().getAnnotations(ConfigurationProperties.class);
////            if (propertiess != null) {
////                List<ConfigurationProperties> list = ConfigurationUtil.getOrderList(propertiess);
////                for (ConfigurationProperties p : list) {
////                    if (this.isDev()) {
////                        this.env.merge(this.getHome().getSource().getPath() + File.separator + p.path());
////                    } else {
////                        //获取jar包模式配置文件
////                        this.env.merge(p.prefix(), this.getUrlPath(p.path()));
////                    }
////                }
////            }
////            //创建m2目录
////            if (isLinux()) {
////                //linux的m2本都仓库目录
////                if (this.env.containsKey(IApplicationMaven.LINUX_MAVEN_LOCAL_REPOSITORY_PATH)) {
////                    this.env.setString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, env.getString(IApplicationMaven.LINUX_MAVEN_LOCAL_REPOSITORY_PATH));
////                    this.env.remove(IApplicationMaven.LINUX_MAVEN_LOCAL_REPOSITORY_PATH);
////                }
////            } else {
////                //win的m2本都仓库目录
////                if (this.env.containsKey(IApplicationMaven.WIN_MAVEN_LOCAL_REPOSITORY_PATH)) {
////                    this.env.setString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, env.getString(IApplicationMaven.WIN_MAVEN_LOCAL_REPOSITORY_PATH));
////                    this.env.remove(IApplicationMaven.WIN_MAVEN_LOCAL_REPOSITORY_PATH);
////                }
////            }
//        //判断是否有maven目录配置
//        if (!this.env.containsKey(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH)) {
//            //设置临时maven目录参数
//            this.env.setString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, System.getProperty("java.io.tmpdir") + File.separator + "m2" + File.separator + "repository");
//        }
////        }
//        this.getLog().info("~ApplicationEnvContent.init->After");
//    }
//}