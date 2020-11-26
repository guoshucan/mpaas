package ghost.framework.context.thread;

import java.util.concurrent.CountDownLatch;

/**
 * package: ghost.framework.core.thread
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取线程通知对象接口
 * @Date: 17:36 2020/1/24
 */
public interface IGetThreadNotification {
    /**
     * 获取线程计数器
     * @return
     */
    CountDownLatch getCountDownLatch();

    /**
     * 减去计数器
     */
    void countDown();

    /**
     * 等计数器待通知
     * @throws InterruptedException
     */
    void countAwait() throws InterruptedException;
}