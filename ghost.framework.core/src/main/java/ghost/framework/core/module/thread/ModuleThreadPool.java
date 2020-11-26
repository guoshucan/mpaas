package ghost.framework.core.module.thread;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.module.thread.IModuleThread;
import ghost.framework.context.module.thread.IModuleThreadPool;
import ghost.framework.context.thread.IGetThreadNotification;
import ghost.framework.maven.FileArtifact;

import java.util.*;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块线程池
 * @Date: 21:22 2019/12/27
 */
public class ModuleThreadPool extends AbstractMap<String, IModuleThread> implements IModuleThreadPool {
    /**
     * 应用接口
     */
    private IApplication app;
    private Map<String, IModuleThread> map = new HashMap<>();
    @Override
    public Set<Entry<String, IModuleThread>> entrySet() {
        return map.entrySet();
    }

    @Override
    public IModuleThread put(String key, IModuleThread value) {
        synchronized (map) {
            return map.put(key, value);
        }
    }

    /**
     * 初始化模块线程池
     *
     * @param app 应用接口
     */
    public ModuleThreadPool(@Application @Autowired IApplication app) {
        this.app = app;
    }
    /**
     * 创建模块线程
     *
     * @param artifact  模块信息
     * @param threadNotification 加载线程通知对象
     * @return
     */
    @Override
    public IModuleThread create(FileArtifact artifact, IGetThreadNotification threadNotification) {
        return this.app.addBean(ModuleThread.class, new Object[]{artifact, threadNotification});
    }
}