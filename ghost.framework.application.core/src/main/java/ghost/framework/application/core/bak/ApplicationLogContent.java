//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationLog;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用日志基础类
// * @Date: 1:47 2019-05-28
// */
//abstract class ApplicationLogContent extends ApplicationContent implements IApplicationLog {
//    /**
//     * 初始化应用日志基础类
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationLogContent( final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.log.info("~ApplicationLogContent");
//    }
//
//    /**
//     * 重写设置引导类
//     * @param rootClass
//     */
//    @Override
//    protected void setRootClass(Class<?> rootClass) {
//        //初始化日志
//        this.log = LoggerFactory.getLogger(rootClass);
//        super.setRootClass(rootClass);
//    }
//
//    /**
//     * 应用日志
//     */
//    private Logger log;
//    /**
//     * 获取应用日志
//     * @return
//     */
//    @Override
//    public Log getLog() {
//        return this.log;
//    }
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
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.log.info("~ApplicationLogContent.init->Before");
//        super.init();
//        this.log.info("~ApplicationLogContent.init->After");
//    }
//}