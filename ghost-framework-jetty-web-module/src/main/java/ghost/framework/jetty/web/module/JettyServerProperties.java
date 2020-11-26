//package ghost.framework.jetty.web.module;
//
//import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentValue;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 11:49 2019/12/24
// */
//public class JettyServerProperties {
//    /**
//     *
//     */
//    @BindEnvironmentValue("default.dirAllowed")
//    private boolean dirAllowed;
//
//    public boolean isDirAllowed() {
//        return dirAllowed;
//    }
//
//    public void setDirAllowed(boolean dirAllowed) {
//        this.dirAllowed = dirAllowed;
//    }
//
//    /**
//     *
//     */
//    @BindEnvironmentValue("default.useFileMappedBuffer")
//    private boolean useFileMappedBuffer;
//
//    public boolean isUseFileMappedBuffer() {
//        return useFileMappedBuffer;
//    }
//
//    public void setUseFileMappedBuffer(boolean useFileMappedBuffer) {
//        this.useFileMappedBuffer = useFileMappedBuffer;
//    }
//
//    private boolean dumpBeforeStop;
//    private boolean dumpAfterStart;
//    /**
//     * 在启动过程中允许抛出异常终止启动并退出 JVM
//     */
//    private boolean throwUnavailableOnStartupException;
//    /**
//     * 重复启动Jetty居然报端口冲突
//     */
//    private boolean reuseAddress;
//    /**
//     * JVM退出时关闭Jetty
//     */
//    private boolean stopAtShutdown;
//
//    public boolean isStopAtShutdown() {
//        return stopAtShutdown;
//    }
//
//    public void setStopAtShutdown(boolean stopAtShutdown) {
//        this.stopAtShutdown = stopAtShutdown;
//    }
//
//    public boolean isReuseAddress() {
//        return reuseAddress;
//    }
//
//    public void setReuseAddress(boolean reuseAddress) {
//        this.reuseAddress = reuseAddress;
//    }
//
//    public boolean isThrowUnavailableOnStartupException() {
//        return throwUnavailableOnStartupException;
//    }
//
//    public void setThrowUnavailableOnStartupException(boolean throwUnavailableOnStartupException) {
//        this.throwUnavailableOnStartupException = throwUnavailableOnStartupException;
//    }
//
//    public boolean isDumpBeforeStop() {
//        return dumpBeforeStop;
//    }
//
//    public boolean isDumpAfterStart() {
//        return dumpAfterStart;
//    }
//
//    public void setDumpAfterStart(boolean dumpAfterStart) {
//        this.dumpAfterStart = dumpAfterStart;
//    }
//
//    public void setDumpBeforeStop(boolean dumpBeforeStop) {
//        this.dumpBeforeStop = dumpBeforeStop;
//    }
//    private JettyServerRequestProperties request;
//
//    public void setRequest(JettyServerRequestProperties request) {
//        this.request = request;
//    }
//
//    public JettyServerRequestProperties getRequest() {
//        return request;
//    }
//}
