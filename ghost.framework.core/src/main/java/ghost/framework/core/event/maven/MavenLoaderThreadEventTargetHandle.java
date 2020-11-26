//package ghost.framework.core.event.maven;
//
//import ghost.framework.context.module.IModuleClassLoader;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * package: ghost.framework.core.event.maven
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:maven加载线程事件目标处理类
// * @Date: 16:19 2020/1/24
// */
//public class MavenLoaderThreadEventTargetHandle<O, T, M extends IModuleClassLoader> extends MavenLoaderEventTargetHandle<O, T, M> implements IMavenLoaderThreadEventTargetHandle<O, T, M> {
//    /**
//     * 初始化事件不表处理头
//     *
//     * @param owner  设置事件目标对象拥有者
//     * @param target 设置目标对象
//     */
//    public MavenLoaderThreadEventTargetHandle(O owner, T target) {
//        super(owner, target);
//    }
//
//    /**
//     * 加载线程通知对象
//     */
//    public final CountDownLatch loadThreadNotification = new CountDownLatch(1);
//
//    /**
//     * 获取加载线程通知对象
//     *
//     * @return
//     */
//    @Override
//    public CountDownLatch getCountDownLatch() {
//        return loadThreadNotification;
//    }
//}