package ghost.framework.maven.plugin.loader;
import ghost.framework.beans.BeanException;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationPropertiess;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.beans.annotation.plugin.PluginDependencys;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.application.event.ApplicationEventTopic;
import ghost.framework.beans.execute.LoadingMode;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.plugin.event.PluginApplicationEvent;
import ghost.framework.beans.plugin.exception.PluginLoaderException;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.ClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactoryContainer;
import ghost.framework.context.bean.factory.scan.container.IScanResourceBeanFactoryContainer;
import ghost.framework.context.event.maven.IMavenPluginEventTargetHandle;
import ghost.framework.context.event.maven.MavenPluginEventTargetHandle;
import ghost.framework.context.event.scan.ScanResourceEventTargetHandle;
import ghost.framework.context.maven.IMavenDependencyLoader;
import ghost.framework.context.maven.IMavenPluginLoader;
import ghost.framework.context.pack.IClassResolve;
import ghost.framework.context.pack.IPackageClassResolve;
import ghost.framework.context.plugin.IPluginContainer;
import ghost.framework.context.plugin.IPluginSource;
import ghost.framework.context.plugin.PluginSource;
import ghost.framework.maven.ArtifactUitl;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.MavenLoaderException;
import ghost.framework.maven.VerifyDownloadCallback;
import ghost.framework.util.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * package: ghost.framework.core.application.loader
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven插件加载类
 * 按照 {@link IMavenPluginEventTargetHandle::isAlreadyLoaded()}获取状态判断是否已经加载依赖包，如果未加载依赖包时先加载依赖包后从新开始加载插件列表
 * @Date: 2020/2/3:23:27
 * @param <O> 为应用或模块核心接口，作为插件加载的位置
 * @param <T> 目标对象，为Class<?>或Map<FileArtifact, List<FileArtifact>>对象
 * @param <E> maven事件目标处理
 */
@Component
public class MavenPluginLoader
        <O extends ICoreInterface, T extends Object, E extends IMavenPluginEventTargetHandle<O, T>>
        implements IMavenPluginLoader<O, T, E> {
    /**
     * 注入包类型解析接口
     */
    @Autowired
    private IPackageClassResolve packageClassResolve;
    /**
     * 注入类型解析器
     */
    @Autowired
    private IClassResolve classResolve;

    /**
     * 初始化模块加载器
     *
     * @param app 应用接口
     */
    public MavenPluginLoader(@Autowired IApplication app) {
        this.app = app;
    }

    /**
     * 应用接口
     */
    private IApplication app;
    /**
     * 日志
     */
    private Log log = LogFactory.getLog(MavenPluginLoader.class);

    /**
     * 加载插件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //加载指定FileArtifact对象的插件
        if (!event.isAlreadyLoaded() && event.getTarget() instanceof Class) {
            //声明插件依赖地图
            Map<FileArtifact, List<FileArtifact>> map = new LinkedHashMap<>();
            //获取maven依赖加载器接口
            IMavenDependencyLoader dependencyLoader = this.app.getBean(IMavenDependencyLoader.class);
            //获取类型的插件注释列表
            List<PluginDependency> dependencyList = this.classResolve.getAnnotationList((Class<?>) event.getTarget(), PluginDependencys.class, PluginDependency.class);
            for (PluginDependency dependency : dependencyList/*MavenAnnotationUtil.getAnnotationMavenPluginDependencyList((Class<?>) event.getTarget())*/) {
                //声明插件信息
                FileArtifact artifact = new FileArtifact(dependency.groupId(), dependency.artifactId(), dependency.version());
                //判断是否为应用加载插件，如果为应用加载插件时才支持全局插件状态
                if (event.getOwner() instanceof IApplication) {
//                    artifact.getProperties().put("global", String.valueOf(dependency.global()));
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug(event.getOwner().getName() + ">" + artifact.toString());
                }
                //加载插件依赖列表
                List<FileArtifact> artifactList = dependencyLoader.loader(artifact);
                //获取插件信息
                ArtifactUitl.findFirst(artifactList, artifact);
                if (this.log.isDebugEnabled()) {
                    this.log.debug(event.getOwner().getName() + ">" + artifact.toString());
                }
                //添加插件地图
                map.put(artifact, artifactList);
            }
            //没有插件退出
            if (map.size() == 0) {
                return;
            }
            //设置插件模块地图
            event.setPlugins(map);
            //设置已经加载依赖包
            event.setAlreadyLoaded(true);
            //加载模块
            this.loader(event);
            //退出加载类型
            return;
        }
        //判断是否已经加载插件包依赖
        if (event.isAlreadyLoaded()) {
            //获取拥有者类加载器
            IClassLoader classLoader = event.getOwner().getClassLoader();
            //开始加载插件
            Map<FileArtifact, List<FileArtifact>> map = event.getPlugins();
            //遍历插件列表
            for (Map.Entry<FileArtifact, List<FileArtifact>> artifact : map.entrySet()) {
                //获取模块注释信息
                try {
                    //先添加依赖包
                    classLoader.addArtifactList(artifact.getValue());
                    //获取插件包类型
//                    Class<?> packageClass = BeansMavenUtil.getPluginPackageClass((ClassLoader) classLoader, artifact.getKey().getFile());
                    Class<?> packageClass = this.packageClassResolve.getPackageAnnotationClass((ClassLoader) classLoader, artifact.getKey().getFile(), PluginPackage.class);
                    //判断插件包是否有效
                    if (packageClass == null) {
                        throw new PluginLoaderException(artifact.toString());
                    }
                    //判断是否有依赖插件
                    this.loader((E) new MavenPluginEventTargetHandle(event.getOwner(), packageClass));
                    //获取插件包注释
                    PluginPackage pluginPackage = packageClass.getAnnotation(PluginPackage.class);
                    //加载插件配置
                    Properties pro = new Properties();
                    List<ConfigurationProperties> propertiesList = this.classResolve.getAnnotationList(packageClass, ConfigurationPropertiess.class, ConfigurationProperties.class);
                    for (ConfigurationProperties properties : propertiesList/*MavenAnnotationUtil.getAnnotationConfigurationPropertiesList(packageClass)*/) {
                        try {
                            pro.putAll(PropertiesUtil.readProperties(artifact.getKey().getFile(), properties.path()));
                        } catch (Exception e) {
                            throw new BeanException(e.getMessage(), e);
                        }
                    }
                    //创建插件源
                    IPluginSource source = new PluginSource(
                            (pluginPackage.name().equals("") ? packageClass.getName().replace(".package-info", "") : pluginPackage.name()),
                            packageClass,
                            artifact.getKey(),
                            artifact.getValue(),
                            (pro.size() == 0 ? null : pro));
                    //获取插件容器接口
                    IPluginContainer pluginContainer = event.getOwner().getBean(IPluginContainer.class);
                    //验证插件是否已经存在
                    if (pluginContainer.containsKey(source.getName())) {
                        //插件已经存在
                        this.app.publishEvent(new PluginApplicationEvent(this, ApplicationEventTopic.PluginLoaderExist.name(), source.getName(), artifact.getKey()));
                        //卸载插件相关依赖包
//                        classLoader.removeArtifactList(artifact.getValue());
                        continue;
                    }
                    //添加到插件容器中
                    pluginContainer.put(source.getName(), source);
                    //合并插件配置
                    if (pro != null && pro.size() > 0) {
                        event.getOwner().getEnv().merge(pro);
                    }
                    //加载差价依赖类型列表
                    if (pluginPackage.mode() == LoadingMode.annotation) {
                        //注释加载依赖
                        for (Class<?> c : pluginPackage.loadClass()) {
                            //设置插件加载类型
                            event.setType(c);
                            //定位类型拥有者
                            this.positionOwner(event);
                            //执行定位到的拥有者执行类型绑定
                            event.getExecuteOwner().addBean(c);
                        }
                    } else {
                        //使用扫描模式加载包类型
                        event.getOwner().getBean(IScanResourceBeanFactoryContainer.class).loader(new ScanResourceEventTargetHandle(event.getOwner(), source, artifact.getKey()));
                    }
                    //发送加载插件完成事件
                    this.app.publishEvent(new PluginApplicationEvent(this, ApplicationEventTopic.PluginLoaderCompleted.name(), source.getName(), artifact.getKey()));
                    //引发类型加载事件
                    event.getOwner().getBean(IClassAnnotationBeanFactoryContainer.class).loader(new ClassAnnotationBeanTargetHandle(event.getOwner(), packageClass, source.getName(), source));
                } catch (MalformedURLException e) {
                    throw new PluginLoaderException(artifact.toString(), e);
                }
            }
            return;
        }
        //创建下载回调
        VerifyDownloadCallback downloadCallback = new VerifyDownloadCallback() {
            //判断应用是否存在该抱，无需下载该抱
            @Override
            public boolean isDownload(FileArtifact artifact) throws MavenLoaderException {
                if (log.isDebugEnabled()) {
                    log.debug(artifact.toString());
                }
                //默认需要下载此包
                return !app.getClassLoader().contains(artifact.getArtifact());
            }
        };
        //加载未加载依赖的插件
        Map<FileArtifact, List<FileArtifact>> map = event.getPlugins();
        //获取maven依赖加载器接口
        IMavenDependencyLoader dependencyLoader = this.app.getBean(IMavenDependencyLoader.class);
        //获取应用启动类的插件注释列表
        for (Map.Entry<FileArtifact, List<FileArtifact>> entry : map.entrySet()) {
            if (this.log.isDebugEnabled()) {
                this.log.debug(event.getOwner().getName() + ">" + entry.getKey().toString());
            }
            //加载插件依赖列表
            List<FileArtifact> artifactList = dependencyLoader.loader(entry.getKey(), downloadCallback);
            //获取插件信息
            ArtifactUitl.findFirst(artifactList, entry.getKey());
            if (this.log.isDebugEnabled()) {
                this.log.debug(event.getOwner().getName() + ">" + entry.getKey().toString());
            }
            entry.getValue().addAll(artifactList);
        }
        //没有插件退出
        if (map.size() == 0) {
            return;
        }
        //设置插件模块地图
        event.setPlugins(map);
        //设置已经加载依赖包
        event.setAlreadyLoaded(true);
        //加载模块
        this.loader(event);
    }

    /**
     * 卸载插件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {

    }

    @Override
    public IApplication getApp() {
        return app;
    }
}