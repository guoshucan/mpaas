package ghost.framework.thread;

import ghost.framework.context.thread.IGetThreadNotification;
import ghost.framework.util.Assert;

import java.util.concurrent.CountDownLatch;

/**
 * package: ghost.framework.core.thread
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:线程通知类
 * @Date: 18:37 2020/1/24
 */
public class ThreadNotification implements IGetThreadNotification {
    /**
     * 初始化线程通知类
     */
    public ThreadNotification() {
        this.countDownLatch = new CountDownLatch(1);
    }

    /**
     * 初始化线程通知类
     *
     * @param count 指定通知数量
     */
    public ThreadNotification(int count) {
        Assert.minimumValue(count, 0, "ThreadNotification count <= 0 error");
        this.countDownLatch = new CountDownLatch(1);
    }

    /**
     * 线程计数器
     */
    public final CountDownLatch countDownLatch;

    /**
     * 获取线程计数器
     *
     * @return
     */
    @Override
    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    /**
     * 减去计数器
     */
    @Override
    public void countDown() {
        this.countDownLatch.countDown();
    }

    /**
     * 等计数器待通知
     *
     * @throws InterruptedException
     */
    @Override
    public void countAwait() throws InterruptedException {
        this.countDownLatch.await();
    }
}