//package ghost.framework.app.core;
//
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.util.StopWatch;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:引用引导类
// * @Date: 0:05 2019-04-15
// */
///**
// * 注释默认数据源，如果在启动类没有注释自定义 {@link ghost.framework.beans.data.annotation.CustomDataSource} 数据源类型侧默认使用此注释数据源中间件
// */
//@ConfigurationProperties//注释默认配置文件
//public final class ApplicationBoot extends ApplicationObjectContent {
//    /**
//     * 初始化
//     */
//    @Override
//    protected void init() {
//        StopWatch watch = new StopWatch();
//        try {
//            watch.Before("init");
//            super.init();
//            this.getLog().info("~ApplicationBoot.init->Before");
//            if (this.getLog().isDebugEnabled()) {
//                this.getLog().debug(this.getHome().getSource().getPath());
//            } else {
//                this.getLog().info(this.getHome().getSource().getPath());
//            }
//            //注册程序退出钩子
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    getLog().info("shutdown hook!!!");
//                }
//            }));
//            //判断是否为开发模式
////            if (this.isDev()) {
////                //开发模式扫描run包
////                this.getBootLoader().loadAppClasssLoader();
////            } else {
////                //jar包模式扫描run包
////                this.getBootLoader().loadAppJarLoader();
////            }
//            //初始化完成
//            //this.initialize = true;
//            this.getLog().info("~ApplicationBoot.init->After");
//        } catch (Exception e) {
//            if (this.getLog().isDebugEnabled()) {
//                e.printStackTrace();
//                this.getLog().debug(e.getMessage());
//            } else {
//                this.getLog().error(e.getMessage());
//            }
//        } finally {
//            watch.stop();
//            this.getLog().info(watch.prettyPrint());
//        }
//    }
//
//
//
//    /**
//     * 初始化应用env基础类
//     *
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    public ApplicationBoot(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationBoot");
//        //调用初始化
//        this.init();
//    }
//}