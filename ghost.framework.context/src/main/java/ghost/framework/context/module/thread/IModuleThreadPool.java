package ghost.framework.context.module.thread;

import ghost.framework.context.thread.IGetThreadNotification;
import ghost.framework.maven.FileArtifact;

import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 21:25 2019/12/27
 */
public interface IModuleThreadPool extends Map<String, IModuleThread> {
    /**
     * 创建模块线程
     *
     * @param artifact           模块信息
     * @param threadNotification 模块加载线程通知对象
     * @return
     */
    IModuleThread create(FileArtifact artifact, IGetThreadNotification threadNotification);
}