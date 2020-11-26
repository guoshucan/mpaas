package ghost.framework.core.module;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.beans.maven.annotation.MavenAnnotationUtil;
import ghost.framework.context.application.IApplication;
import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.beans.application.event.ApplicationEventTopic;
import ghost.framework.beans.application.event.GenericApplicationEvent;
import ghost.framework.context.application.event.IEventPublisherContainer;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.base.ApplicationHome;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.maven.IMavenPluginLoader;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.context.module.IModuleContainer;
import ghost.framework.context.module.ModuleConstant;
import ghost.framework.context.module.environment.IModuleEnvironment;
import ghost.framework.context.module.main.IModuleMainContainer;
import ghost.framework.context.module.thread.IModuleThread;
import ghost.framework.context.thread.IGetThreadNotification;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.core.base.ApplicationBase;
import ghost.framework.core.event.locale.LocaleEventTargetHandle;
import ghost.framework.core.event.locale.factory.container.LocaleEventListenerFactoryContainer;
import ghost.framework.context.event.maven.MavenPluginEventTargetHandle;
import ghost.framework.core.module.environment.ModuleEnvironment;
import ghost.framework.core.module.main.ModuleMainContainer;
import ghost.framework.maven.FileArtifact;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.StopWatch;
import ghost.framework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块
 * @Date: 0:56 2019-02-01
 */
@BeanMapContainer(IModuleContainer.class)//注释addBean完成后自动装在入IModuleContainer类型的容器中
@Application//注释装载入应用Bean容器
@Service
public final class ModuleApplication extends ApplicationBase implements IModule {
    /**
     * 初始化模块
     *
     * @param app                应用接口
     * @param thread             模块线程
     * @param packageClass       模块包
     * @param threadNotification 加载线程通知对象
     */
    public ModuleApplication(
            @Application @Autowired IApplication app,
            IModuleThread thread, Class<?> packageClass,
            IGetThreadNotification threadNotification) {
        this.app = app;
        this.thread = thread;
        this.thread.setModule(this);
        this.packageClass = packageClass;
        //初始化模块目录
        this.home = new ApplicationHome(this.packageClass);
        this.moduleAnnotation = this.packageClass.getAnnotation(ModulePackage.class);
        this.classLoader = this.thread.getClassLoader();
        this.threadNotification = threadNotification;
        this.log.info("~" + this.getClass().getName() + "(" + this.home.getSource().getPath() + ")");
    }

    /**
     * 加载线程通知对象
     */
    private final IGetThreadNotification threadNotification;

    /**
     * 获取加载线程通知对象
     *
     * @return
     */
    @Override
    public IGetThreadNotification getThreadNotification() {
        return threadNotification;
    }

    /**
     * 获取模块版本信息
     *
     * @return
     */
    @Override
    public FileArtifact getArtifact() {
        return this.thread.getArtifact();
    }
    /**
     * 验证模块是否存在
     * @param groupId 模块组id
     * @param artifactId 模块组织id
     * @param version 模块版本
     * @return 返回模块是否存在
     */
    @Override
    public boolean contains(String groupId, String artifactId, String version) {
        return this.thread.getArtifact().getGroupId().equals(groupId) &&
                this.thread.getArtifact().getArtifactId().equals(artifactId) &&
                this.thread.getArtifact().getVersion().equals(version);
    }

    /**
     * 推送事件
     * @param event 事件对象
     */
    @Override
    public void publishEvent(AbstractApplicationEvent event) {
        //判断为本身事件
        if (event.getSource().equals(this)) {
            //判断是否为加载插件事件
            if(event.getTopic().equals(ApplicationEventTopic.ModuleLaunchPlugin.name())){
                //初始化插件
                this.initPlugins();
                return;
            }
        }
        super.publishEvent(event);
    }

    /**
     * @throws Exception
     */
    @Invoke
    public void init() {
        this.log.info("init->Start");
        StopWatch watch = new StopWatch();
        try {
            watch.start("init");
            //
            this.addBean(this.app);
            //初始化绑定模块日志
            this.addBean(ModuleConstant.LOG, this.log);
            //
            super.init();
            //---------------------------------------ModuleEventProcessor----------------------------------------------------------------------------------------------------------------
            //发送模块启动事件
            this.app.getBean(IEventPublisherContainer.class).publishEvent(new GenericApplicationEvent(this,  ApplicationEventTopic.ModuleLaunch.name()));
            //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            //
            this.initEnv();
            //初始化其它函数
            this.initBean();
//            this.initEvent();
            this.initDirectory();
//            this.initResource();
//            this.initMaven();
//            this.initInterceptor();
//            this.initObject();
//            this.initProxy();
//            this.initData();
//            this.initTransaction();
            //对应用注释内容进行加载
            this.getBean(LocaleEventListenerFactoryContainer.class).loader(new LocaleEventTargetHandle(this, this.packageClass));
            //加载依赖
            for (Class<?> c : this.moduleAnnotation.depend()) {
                this.addBean(c);
            }
            this.initialize = true;
            //发送模块启动完成事件
            this.app.getBean(IEventPublisherContainer.class).publishEvent(new GenericApplicationEvent(this, ApplicationEventTopic.ModuleLaunchCompleted.name()));
            this.log.info("init->End");
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage());
            } else {
                this.log.error(e.getMessage());
            }
        } finally {
            watch.stop();
            this.log.info(watch.prettyPrint());
            //有加载线程通知对象时发出线程通知
            this.threadNotification.countDown();
        }
    }

    /**
     * 重写加载插件
     * 处理应用类注释依赖模块是否存在依赖插件处理
     */
    @Override
    protected void initPlugins() {
        //处理应用类注释依赖模块是否存在依赖插件处理
        Map<FileArtifact, List<FileArtifact>> map = MavenAnnotationUtil.getAnnotationMavenPluginDependencyList(this.getArtifact(), this.getApp().getRootClass());
        if (map.size() > 0) {
            this.getApp().getBean(IMavenPluginLoader.class).loader(new MavenPluginEventTargetHandle(this, null, map));
        }
        //重载基础加载模块包注释插件依赖
        super.initPlugins();
    }

    /**
     * 获取是否自动绑定
     * 自动绑定主要是在类型调用getBean函数时如果未绑定在容器中时先绑定该类型后再返回该类型的绑定对象
     *
     * @return
     */
    @Override
    public boolean isAutoBean() {
        //判断获取是否自动绑定
        IEnvironment env = null;
        synchronized (this.getBeanMap()) {
            if (this.getBeanMap().containsKey(ModuleEnvironment.class.getName())) {
                env = (IEnvironment) this.getBeanMap().get(ModuleEnvironment.class.getName()).getObject();
            }
        }
        if (env != null && env.containsKey(ModuleConstant.Bean.AUTO_BEAN)) {
            return env.getBoolean(ModuleConstant.Bean.AUTO_BEAN);
        }
        return this.app.isAutoBean();
    }

    /**
     * 初始化绑定
     */
    private void initBean() {
        this.log.info("initBean->Start");
        synchronized (this.getBeanMap()) {
            //初始化绑定模块运行类容器
            this.addBean(ModuleMainContainer.class);
            //绑定应用类加载器
            this.addBean(this.getClassLoader());
        }
        this.log.info("initBean->End");
    }

    /**
     * 获取模块主运行容器
     *
     * @return
     */
    @Override
    public IModuleMainContainer getModuleMainContainer() {
        return this.getBean(IModuleMainContainer.class);
    }

    /**
     * 初始化目录
     */
    private void initDirectory() {
        this.log.info("initDirectory->Start");
        //获取模块env
        IEnvironment env = this.getBean(IModuleEnvironment.class);
        //加载模块默认配置文件
        this.addBean(ModuleConstant.HOME, this.home);
        //创建jar运行临时目录
        File temp = new File(System.getProperty("java.io.tmpdir") + File.separator + this.getName());
        //创建目录
        if (!temp.exists()) {
            temp.mkdir();
            temp.setExecutable(true);//设置可执行权限
            temp.setReadable(true);//设置可读权限
            temp.setWritable(true);//设置可写权限
        }
        //在jar程序退出时删除目录
        temp.deleteOnExit();
        this.addBean(ModuleConstant.TEMP_DIRECTORY, temp);
        this.log.info("initDirectory->End");
    }

    /**
     * 初始化模块env
     */
    private void initEnv() throws Exception{
        this.log.info("initEnv->Start");
        //绑定env
        synchronized (this.getBeanMap()) {
            //获取模块env
            IModuleEnvironment environment = this.addBean(ModuleEnvironment.class);
            //合并包内置默认配置文件
            this.environmentMerge(null);
            //判断模块注释是否指定模块名称，如果模块注释未指定模块名称时，模块名称将使用注释所在的包名称作为模块名称
            if (this.moduleAnnotation.name().equals("")) {
                //使用模块注释所在的包名称作为模块名称
                environment.setString(ModuleConstant.NAME, this.packageClass.getName().replace(ReflectUtil.DotPackageInfo, ""));
            } else {
                //使用模块注释名称作为模块名称
                environment.setString(ModuleConstant.NAME, this.moduleAnnotation.name());
            }
            //判断是否为开发模式
            if (AssemblyUtil.isPathDev(this.home.getSource().getPath())) {
                //开发模式
                environment.setBoolean(ModuleConstant.DEV, true);
            } else {
                //不为开发模式
                environment.setBoolean(ModuleConstant.DEV, false);
            }
            this.getLog().info(this.getName() + "->dev:" + environment.getBoolean(ModuleConstant.DEV));
        }
        this.log.info("initEnv->End");
    }

    /**
     * 模块日志
     */
     private Log log = LogFactory.getLog(this.getClass());

    /**
     * 获取模块排序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return this.getEnv().getInt(ModuleConstant.ORDER);
    }

    /**
     * 获取模块日志
     *
     * @return
     */
    @Override
    public Log getLog() {
        return log;
    }

    /**
     * 获取模块加载线程
     *
     * @return
     */
//    @Override
    public IModuleThread getThread() {
        return thread;
    }

    /**
     * 模块主线程
     */
    private IModuleThread thread;

    /**
     * 模块注释包类型
     */
    private Class<?> packageClass;
    /**
     * 获取包类型
     *
     * @return
     */
    @Override
    public Class<?> getPackageClass() {
        return packageClass;
    }

    /**
     * 应用接口
     */
    private IApplication app;
    /**
     * 模块类加载器
     */
    private IModuleClassLoader classLoader;
    /**
     * 模块注释
     */
    private ModulePackage moduleAnnotation;

    /**
     * 获取模块注释
     *
     * @return
     */
    public ModulePackage getModuleAnnotation() {
        return moduleAnnotation;
    }

    /**
     * 获取模块env
     *
     * @return
     */
    @NotNull
    @Override
    public IEnvironment getEnv() {
        return this.getBean(IModuleEnvironment.class);
    }

    /**
     * 获取模块可空env
     *
     * @return
     */
    @Nullable
    @Override
    public IEnvironment getNullableEnv() {
        return this.getNullableBean(IModuleEnvironment.class);
    }

    /**
     * 获取模块ome
     *
     * @return
     */
    @NotNull
    @Override
    public ApplicationHome getHome() {
        return home;
    }

    /**
     * 获取是否为windows系统
     *
     * @return
     */
    @Override
    public boolean isWindows() {
        return this.app.isWindows();
    }

    /**
     * 后去是否为linux系统
     *
     * @return
     */
    @Override
    public boolean isLinux() {
        return this.app.isLinux();
    }

    /**
     * 模块目录
     */
    private ApplicationHome home;

    /**
     * 释放资源
     *
     * @throws Exception
     */
    @Override
    public synchronized void close() throws Exception {
        if (this.classLoader != null) {
            this.classLoader.close();
            this.classLoader = null;
        }
    }

    /**
     * 获取是否为开发模式
     * 只是当前模块是否为开发模式
     *
     * @return
     */
    @Override
    public boolean isDev() {
        return this.getEnv().getBoolean(ModuleConstant.DEV);
    }

    /**
     * 获取类加载器
     *
     * @return
     */
    @NotNull
    @Override
    public IClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 获取模块临时目录
     *
     * @return
     */
    @NotNull
    @Override
    public File getTempDirectory() {
        return this.getBean(ModuleConstant.TEMP_DIRECTORY);
    }



    /**
     * 获取模块路径
     *
     * @return
     */
    @Override
    public URL getUrl() throws MalformedURLException {
        return this.getArtifacts().get(0).getFile().toURL();
    }

    /**
     * 获取模块版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return this.getEnv().getString(ModuleConstant.VERSION);
    }

    /**
     * 获取模块id
     *
     * @return
     */
    @BeanMapContainer.Name//作为@BeanMapContainer注释绑定名称的注释
    @Service.Name//作为@Service注释绑定名称的注释
    @Override
    public String getName() {
        IEnvironment environment = this.getNullableEnv();
        if (environment != null) {
            String name = environment.getNullable(ModuleConstant.NAME);
            if (!StringUtils.isEmpty(name)) {
                return name;
            }
        }
        //如果模块未指定模块名称的注释时使用模块注释包所在的包名称作为模块名称
        if (this.moduleAnnotation.name().equals("")) {
            return this.packageClass.getName().replace(ReflectUtil.DotPackageInfo, "");
        }
        return this.moduleAnnotation.name();
    }

    /**
     * 是否已经初始化
     */
    private boolean initialize;

    /**
     * 获取是否已经初始化
     *
     * @return
     */
    @Override
    public boolean isInitialize() {
        return initialize;
    }

    /**
     * 获取模块容器
     *
     * @return
     */
    @NotNull
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 获取引导类
     *
     * @return
     */
    @NotNull
    @Override
    public Class<?> getRootClass() {
        return this.app.getRootClass();
    }

    /**
     * 重写哈希值
     *
     * @return
     */
    @Override
    public int hashCode() {
        IEnvironment env = this.getNullableBean(IModuleEnvironment.class);
        if (env == null) {
            return super.hashCode();
        }
        String name = this.getName();
        if (StringUtils.isEmpty(name)) {
            return super.hashCode();
        }
        return name.hashCode() + 31;
    }

    /**
     * 重写比对模块
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModuleApplication) {
            ModuleApplication m = (ModuleApplication) obj;
            return m.getName().equals(this.getName());
        }
        return super.equals(obj);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        if (this.getBeanMap() != null) {
            String name = this.getName();
            if (!StringUtils.isEmpty(name)) {
                return name;
            }
        }
        return super.toString();
    }
}