//package ghost.framework.core.event.maven;
//
//import ghost.framework.context.module.IModuleClassLoader;
//import ghost.framework.context.thread.IGetThreadNotification;
//
///**
// * package: ghost.framework.core.event.maven
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:maven加载线程事件目标处理接口
// * @Date: 16:19 2020/1/24
// */
//public interface IMavenLoaderThreadEventTargetHandle<O, T, M extends IModuleClassLoader> extends IMavenLoaderEventTargetHandle<O, T, M>, IGetThreadNotification {
//    /**
//     * 减去计数器
//     */
//    @Override
//    default void countDown() {
//        this.getCountDownLatch().countDown();
//    }
//
//    /**
//     * 等计数器待通知
//     *
//     * @throws InterruptedException
//     */
//    @Override
//    default void countAwait() throws InterruptedException {
//        this.getCountDownLatch().await();
//    }
//}