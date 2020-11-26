package ghost.framework.core.module.thread;

import ghost.framework.beans.annotation.MethodName;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationClassLoader;
import ghost.framework.beans.application.event.ApplicationEventTopic;
import ghost.framework.beans.application.event.GenericApplicationEvent;
import ghost.framework.context.maven.IMavenDependencyLoader;
import ghost.framework.context.maven.IMavenLoader;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.context.module.exception.IModuleUncaughtExceptionHandler;
import ghost.framework.context.module.exception.ModuleException;
import ghost.framework.context.module.thread.IModuleThread;
import ghost.framework.context.module.thread.IModuleThreadPool;
import ghost.framework.context.module.thread.ModuleThreadLocal;
import ghost.framework.context.thread.IGetThreadNotification;
import ghost.framework.context.assembly.BeansMavenUtil;
import ghost.framework.core.event.maven.MavenLoaderEventTargetHandle;
import ghost.framework.core.module.ModuleApplication;
import ghost.framework.maven.ArtifactUitl;
import ghost.framework.maven.FileArtifact;
import ghost.framework.thread.OneRunnable;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块线程
 * @Date: 23:54 2019/11/26
 */
@Application//注释装载入应用Bean容器
@Component
@BeanMapContainer(IModuleThreadPool.class)
public final class ModuleThread extends Thread implements IModuleThread {
    /**
     * 模块接口
     */
    private IModule module;

    /**
     * 设置模块接口
     *
     * @param module
     */
    @Override
    public void setModule(IModule module) {
        this.module = module;
    }

    /**
     * 线程通知
     */
    private IGetThreadNotification threadNotification;

    /**
     * 获取线程通知
     *
     * @return
     */
    @Override
    public IGetThreadNotification getThreadNotification() {
        return threadNotification;
    }

    /**
     * 获取模块接口
     *
     * @return
     */
    @Override
    public IModule getModule() {
        return module;
    }

    /**
     * 应用接口
     */
    private IApplication app;
    /**
     * 模块版本信息
     */
    private FileArtifact artifact;

    /**
     * 获取版本信息
     *
     * @return
     */
    @Override
    public FileArtifact getArtifact() {
        return artifact;
    }

    /**
     * 模块包列表
     */
    private List<FileArtifact> artifacts;

    /**
     * 获取模块包列表
     *
     * @return
     */
    @Override
    public List<FileArtifact> getArtifacts() {
        return artifacts;
    }

    /**
     * 模块类加载器
     */
    private IModuleClassLoader classLoader = null;

    /**
     * 获取模块类加载器
     *
     * @return
     */
    @Override
    public IModuleClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 重写线程id获取服务bean名称
     * 使用注释{@link MethodName}前缀
     *
     * @return
     */
    @BeanMapContainer.Name(prefix = "ModuleThread-")//注释名称
    @Service.Name(prefix = "ModuleThread-")//注释名称
    @Override
    public long getId() {
        return super.getId();
    }

    /**
     * 启动
     */
    @Invoke
    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * 模块依赖插件列表
     */
    private Map<FileArtifact, List<FileArtifact>> plugins;
    /**
     * 初始化模块线程
     *
     * @param app                模块应用
     * @param artifact           模块包
     * @param threadNotification 加载模块线程通知
     */
    public ModuleThread(@Autowired IApplication app, FileArtifact artifact, IGetThreadNotification threadNotification) {
        this.app = app;
        this.artifact = artifact;
        this.threadNotification = threadNotification;
    }

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(ModuleThread.class);
    /**
     * 模块线程锁定同步对象
     */
    private final Object root = new Object();

    /**
     * 运行模块初始化
     */
    @Override
    public void run() {
        try {
            //推送加载模块启动线程事件
            this.app.publishEvent(new GenericApplicationEvent(this, ApplicationEventTopic.loadModuleStartThread.name()));
            //设置模块线程同步上下文
            ModuleThreadLocal.set(this);
            //设置模块线程未处理错误
            this.setUncaughtExceptionHandler(this.app.getBean(IModuleUncaughtExceptionHandler.class));
            //获取maven依赖加载器接口
            IMavenDependencyLoader dependencyLoader = this.app.getBean(IMavenDependencyLoader.class);
            //加载模块包列表
            this.artifacts = dependencyLoader.loader(this.artifact);
            //获取更新模块包信息
            ArtifactUitl.findFirst(this.artifacts, this.artifact);
            //获取应用类加载器
            IApplicationClassLoader acl = this.app.getBean(IApplicationClassLoader.class);
            //在应用类加载器排除已经存在的包列表
            acl.filterRepeat(artifacts);
            acl.addArtifactList(artifacts);
            //初始化模块类加载器
            this.classLoader = acl.create();
//            this.classLoader.addArtifactList(artifacts);
            //获取模块注释信息
            Class<?> packageClass = BeansMavenUtil.getPackageClass((ClassLoader) this.classLoader, this.artifact.getFile());
            //判断获取模块是否有效
            if (packageClass == null) {
                throw new ModuleException(this.artifact.getFile().getPath());
            }
            //maven加载
            this.app.getBean(IMavenLoader.class).loader(new MavenLoaderEventTargetHandle(this.app, packageClass));
            //构建模块对象
            this.module = this.app.addBean(ModuleApplication.class, new Object[]{this, packageClass, this.threadNotification});
            System.out.println(this.module.toString());
            this.app.publishEvent(new GenericApplicationEvent(this, ApplicationEventTopic.loadModuleStartThreadCompleted.name()));
            //锁定线程等待通知
            new Thread(new OneRunnable<ModuleThread>(this) {
                @Override
                public void run() {
                    try {
                        synchronized (root) {
                            root.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //删除模块
                        if (this.getA().module != null) {
                            try {
                                this.getA().app.removeBean(this.getA().module);
                            } catch (Exception ex) {
                                if (log.isDebugEnabled()) {
                                    ex.printStackTrace();
                                    log.debug(ex.getMessage());
                                } else {
                                    log.error(ex.getMessage());
                                }
                            }
                        }
                        //删除模块线程
                        try {
                            this.getA().app.removeBean(this);
                        } catch (Exception ex) {
                            if (log.isDebugEnabled()) {
                                ex.printStackTrace();
                                log.debug(ex.getMessage());
                            } else {
                                log.error(ex.getMessage());
                            }
                        }
                        //删除模块类加载器
                        if (this.getA().classLoader != null) {
                            try {
                                this.getA().classLoader.close();
                            } catch (Exception ex) {
                                if (log.isDebugEnabled()) {
                                    ex.printStackTrace();
                                    log.debug(ex.getMessage());
                                } else {
                                    log.error(ex.getMessage());
                                }
                            }
                        }
                        //删除线程模块上线文
                        try {
                            ModuleThreadLocal.remove(this.getA());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            } else {
                log.error(e.getMessage());
            }
        }
    }
}