package ghost.framework.context.base.exception;

import ghost.framework.context.application.IApplication;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:线程未处理基础接口
 * @Date: 15:04 2019/12/29
 */
public interface IUncaughtExceptionHandler extends Thread.UncaughtExceptionHandler{
    /**
     * 获取应用接口
     * @return
     */
    IApplication getApp();
}