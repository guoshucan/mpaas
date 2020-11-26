package ghost.framework.application.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import ghost.framework.application.core.environment.ApplicationEnvironment;
import ghost.framework.application.core.exception.ApplicationUncaughtExceptionHandler;
import ghost.framework.application.core.loader.ApplicationLoader;
import ghost.framework.application.core.loader.maven.*;
import ghost.framework.beans.annotation.application.ApplicationDependency;
import ghost.framework.beans.annotation.application.ApplicationDependencys;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.conditional.ConditionalOnBean;
import ghost.framework.beans.annotation.conditional.ConditionalOnClass;
import ghost.framework.beans.annotation.conditional.ConditionalOnMissingBean;
import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationPropertiess;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.container.*;
import ghost.framework.beans.annotation.event.BeanEventListenerProcessor;
import ghost.framework.beans.annotation.event.BeanMethodEventListener;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.TempDirectory;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.beans.annotation.locale.L10n;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.annotation.stereotype.ApplicationBoot;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.application.event.ApplicationEventTopic;
import ghost.framework.beans.application.event.GenericApplicationEvent;
import ghost.framework.beans.maven.annotation.MavenAnnotationUtil;
import ghost.framework.beans.maven.annotation.MavenDepository;
import ghost.framework.beans.maven.annotation.MavenDepositorys;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependencys;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.context.CommonConstant;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.*;
import ghost.framework.context.application.event.IEventPublisherContainer;
import ghost.framework.context.base.ApplicationHome;
import ghost.framework.context.converter.*;
import ghost.framework.core.converter.json.DefaultJsonConverterContainer;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.maven.IApplicationLoader;
import ghost.framework.context.maven.IMavenApplicationDependencyLoader;
import ghost.framework.context.maven.IMavenRepositoryContainer;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleContainer;
import ghost.framework.context.module.exception.ModuleUncaughtExceptionHandler;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.core.annotation.AnnotationRootExecutionChain;
import ghost.framework.core.application.exception.IApplicationUncaughtExceptionHandler;
import ghost.framework.core.base.ApplicationBase;
import ghost.framework.core.bean.factory.stereotype.ClassApplicationBootAnnotationBeanFactory;
import ghost.framework.core.converter.primitive.*;
import ghost.framework.core.converter.properties.*;
import ghost.framework.core.converter.serialization.DefaultBytesToObjectConverter;
import ghost.framework.core.converter.serialization.DefaultObjectToBytesConverter;
import ghost.framework.core.event.locale.LocaleEventTargetHandle;
import ghost.framework.core.event.locale.factory.container.LocaleEventListenerFactoryContainer;
import ghost.framework.core.event.maven.MavenLoaderEventTargetHandle;
import ghost.framework.core.event.proxy.cglib.factory.CglibProxyEventFactoryContainer;
import ghost.framework.core.jackson.ObjectMapperDateFormatExtend;
import ghost.framework.core.maven.CoreMavenUtil;
import ghost.framework.core.maven.MavenRepositoryContainer;
import ghost.framework.core.maven.annotation.reader.AnnotationApplicationMavenDependencyReader;
import ghost.framework.core.maven.annotation.reader.AnnotationMavenDependencyReader;
import ghost.framework.core.maven.annotation.reader.AnnotationMavenDepositoryReader;
import ghost.framework.core.maven.annotation.reader.AnnotationModuleMavenDependencyReader;
import ghost.framework.core.module.ModuleContainer;
import ghost.framework.core.module.thread.ModuleThreadPool;
import ghost.framework.core.parser.locale.LocaleParserContainer;
import ghost.framework.core.parser.locale.json.LocaleJsonParser;
import ghost.framework.core.resolverFactory.EnvironmentResolverObjectFactory;
import ghost.framework.core.resolverFactory.PropertiesResolverObjectFactory;
import ghost.framework.core.security.PasswordDrive;
import ghost.framework.json.converter.DefaultJsonToMapConverter;
import ghost.framework.json.converter.DefaultJsonToObjectConverter;
import ghost.framework.json.converter.DefaultMapToJsonConverter;
import ghost.framework.json.converter.DefaultObjectToJsonConverter;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.MavenRepositoryServer;
import ghost.framework.thread.task.TaskExecutorBuilder;
import ghost.framework.util.AlternativeJdkIdGenerator;
import ghost.framework.util.FileUtil;
import ghost.framework.util.StopWatch;
import ghost.framework.util.StringUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.aether.artifact.Artifact;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用平台运行基础类
 * @Date: 20:41 2019-05-30
 */
@ConfigurationProperties(order = Integer.MIN_VALUE)
public final class Application extends ApplicationBase implements IApplication, IApplicationEnv, IApplicationContent, AutoCloseable {
    /**
     * 初始化应用env基础类
     *
     * @param rootClass 引导类
     * @throws Exception
     */
    public Application(final Class<?> rootClass) {
        super();
        //初始化应用类加载器
        this.classLoader = new ApplicationClassLoader(this, Thread.currentThread().getContextClassLoader());
        this.setRootClass(rootClass);
        this.log = LogFactory.getLog(rootClass);
        this.log.info("~ApplicationBoot");
    }

    /**
     * 获取maven本地仓库目录
     *
     * @return
     */
    @Override
    public File getMavenLocalRepositoryFile() {
        return this.getBean(ApplicationConstant.Maven.LOCAL_REPOSITORY_PATH);
    }

    /**
     * 获取maven仓库容器接口
     *
     * @return
     */
    @Override
    public IMavenRepositoryContainer getMavenRepositoryContainer() {
        return this.getBean(IMavenRepositoryContainer.class);
    }

    /**
     * 日志
     */
    private Log log;

    /**
     * 引导类
     */
    private Class<?> rootClass;

    /**
     * 设置引导类
     *
     * @param rootClass
     */
    private void setRootClass(final Class<?> rootClass) {
        this.rootClass = rootClass;
        this.home = new ApplicationHome(rootClass);
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    @Override
    public String getName() {
        return this.getEnv().getString(ApplicationConstant.NAME);
    }

    /**
     * 重写哈希
     *
     * @return
     */
    @Override
    public int hashCode() {
        IApplicationEnvironment env = this.getNullableBean(IApplicationEnvironment.class);
        if (env == null) {
            return super.hashCode();
        }
        String name = this.getName();
        if (StringUtils.isEmpty(name)) {
            return super.hashCode();
        }
        return name.hashCode() + super.hashCode();
    }

    /**
     * 重写应用转字符
     *
     * @return
     */
    @Override
    public String toString() {
        IApplicationEnvironment env = this.getNullableBean(IApplicationEnvironment.class);
        if (env == null) {
            return super.toString();
        }
        String name = this.getName();
        if (StringUtils.isEmpty(name)) {
            return super.toString();
        }
        return name;
    }

    /**
     * 应用类加载器
     */
    private final IApplicationClassLoader classLoader;

    /**
     * 获取应用类加载器
     *
     * @return
     */
    @Override
    public ghost.framework.context.assembly.IClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 应用home
     */
    private ApplicationHome home;
    /**
     * 获取应用日志
     *
     * @return
     */
    @Override
    public Log getLog() {
        return log;
    }

    /**
     * 初始化
     *
     * @param args 运行参数
     */
    protected IApplication init(String[] args) {
        this.log.info("init->Start");
        StopWatch watch = new StopWatch();
        try {
            watch.start("init");
            //绑定全局注释依赖链
            IAnnotationRootExecutionChain annotationExecutionChainMap = this.addBean(AnnotationRootExecutionChain.class);
            //基础配置注释
            annotationExecutionChainMap.add(new Class[]{ConfigurationPropertiess.class, ConfigurationProperties.class}, AnnotationTag.AnnotationTags.Configuration);
            //区域化注释
            annotationExecutionChainMap.add(new Class[]{I18n.class, L10n.class}, AnnotationTag.AnnotationTags.Locale);
            //区域化注释依赖
            annotationExecutionChainMap.addDepend(new Class[]{I18n.class, L10n.class}, new Class[]{ConfigurationPropertiess.class, ConfigurationPropertiess.class});
            //构建注释
            annotationExecutionChainMap.add(Constructor.class, AnnotationTag.AnnotationTags.Constructor);
            //构建注释依赖
            annotationExecutionChainMap.addDepend(Constructor.class, new Class[]{ConfigurationPropertiess.class, ConfigurationPropertiess.class});
            //存储类型注释
//            annotationExecutionChainMap.add(Configuration.class, AnnotationTag.AnnotationTags.Configuration);
//            annotationExecutionChainMap.addDepend(Configuration.class, new Class[]{ConfigurationPropertiess.class, ConfigurationPropertiess.class, Global.class, Local.class});
            annotationExecutionChainMap.addDepend(new Class[]{I18n.class, L10n.class}, new Class[]{ConfigurationPropertiess.class, ConfigurationPropertiess.class});
            //
            annotationExecutionChainMap.add(Configuration.class, AnnotationTag.AnnotationTags.StereoType);
            annotationExecutionChainMap.add(Component.class, AnnotationTag.AnnotationTags.StereoType);
            annotationExecutionChainMap.add(Service.class, AnnotationTag.AnnotationTags.StereoType);
//            annotationExecutionChainMap.add(Plugin.class, AnnotationTag.AnnotationTags.StereoType);
            annotationExecutionChainMap.addDepend(
                    new Class[]{Component.class, Service.class},
                    new Class[]{ConfigurationPropertiess.class, ConfigurationProperties.class, I18n.class, L10n.class});
            //
            annotationExecutionChainMap.add(Bean.class, AnnotationTag.AnnotationTags.Bean);
            annotationExecutionChainMap.addDepend(Bean.class, new Class[]{ConfigurationPropertiess.class, ConfigurationProperties.class, I18n.class, L10n.class});

            annotationExecutionChainMap.add(BeanCollectionContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanMapContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanListContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanCollectionInterfaceContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanMapInterfaceContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanListInterfaceContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanClassContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanClassNameContainer.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(ClassAnnotationBeanFactory.class, AnnotationTag.AnnotationTags.Container);
//            annotationExecutionChainMap.add(BeanApplicationEventListener.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.add(BeanEventListenerProcessor.class, AnnotationTag.AnnotationTags.Container);
            annotationExecutionChainMap.addDepend(
                    new Class[]{
                            BeanCollectionContainer.class,
                            BeanMapContainer.class,
                            BeanListContainer.class,
                            BeanCollectionInterfaceContainer.class,
                            BeanMapInterfaceContainer.class,
                            BeanListInterfaceContainer.class,
                            BeanClassContainer.class,
                            BeanClassNameContainer.class,
                            ClassAnnotationBeanFactory.class,
                            BeanEventListenerProcessor.class
                    },
                    new Class[]{
                            ConfigurationPropertiess.class,
                            ConfigurationProperties.class,
                            Component.class,
                            Service.class,
                            Configuration.class,
//                            Plugin.class,
                            I18n.class,
                            L10n.class
                    });

            annotationExecutionChainMap.addDepend(
                    new Class[]{Service.class, Component.class, Configuration.class},
                    new Class[]{
                            ConfigurationPropertiess.class, ConfigurationProperties.class, I18n.class, L10n.class,
                            BeanEventListenerProcessor.class
                    });


            annotationExecutionChainMap.add(new Class[]{MavenDepositorys.class, ApplicationDependency.class, ApplicationDependencys.class, MavenModuleDependency.class, MavenModuleDependencys.class}, AnnotationTag.AnnotationTags.Maven);
            annotationExecutionChainMap.addDepend(
                    new Class[]{MavenDepositorys.class, ApplicationDependency.class, ApplicationDependencys.class, MavenModuleDependency.class, MavenModuleDependencys.class},
                    new Class[]{Configuration.class, ConfigurationPropertiess.class, ConfigurationProperties.class, I18n.class, L10n.class});

            annotationExecutionChainMap.add(ApplicationBoot.class, AnnotationTag.AnnotationTags.StereoType);
            annotationExecutionChainMap.addDepend(ApplicationBoot.class,
                    new Class[]{
                            ConfigurationPropertiess.class,
                            ConfigurationProperties.class,
                            Component.class,
                            Service.class,
                            Configuration.class,
                            BeanCollectionContainer.class,
                            BeanMapContainer.class,
                            BeanListContainer.class,
                            BeanCollectionInterfaceContainer.class,
                            BeanMapInterfaceContainer.class,
                            BeanListInterfaceContainer.class,
                            BeanClassContainer.class,
                            BeanClassNameContainer.class,
                            BeanEventListenerProcessor.class,
                            MavenDepositorys.class,
                            ApplicationDependency.class,
                            ApplicationDependencys.class,
                            MavenModuleDependency.class,
                            MavenModuleDependencys.class,
                            I18n.class,
                            L10n.class

                    });

            annotationExecutionChainMap.add(ConditionalOnClass.class, AnnotationTag.AnnotationTags.Conditional);
            annotationExecutionChainMap.add(ConditionalOnBean.class, AnnotationTag.AnnotationTags.Conditional);
            annotationExecutionChainMap.add(ConditionalOnMissingClass.class, AnnotationTag.AnnotationTags.Conditional);
            annotationExecutionChainMap.add(ConditionalOnMissingBean.class, AnnotationTag.AnnotationTags.Conditional);
            annotationExecutionChainMap.addDepend(
                    new Class[]{Bean.class, Component.class, Service.class},
                    new Class[]{ConditionalOnClass.class, ConditionalOnBean.class, ConditionalOnMissingClass.class, ConditionalOnMissingBean.class});
            annotationExecutionChainMap.addDepend(
                    new Class[]{ConditionalOnClass.class, ConditionalOnBean.class, ConditionalOnMissingClass.class, ConditionalOnMissingBean.class},
                    new Class[]{ConfigurationProperties.class, ConfigurationPropertiess.class, Configuration.class, I18n.class, L10n.class});


            annotationExecutionChainMap.add(Value.class, AnnotationTag.AnnotationTags.Injection);
            annotationExecutionChainMap.add(Autowired.class, AnnotationTag.AnnotationTags.Injection);
            annotationExecutionChainMap.add(TempDirectory.class, AnnotationTag.AnnotationTags.Injection);

            annotationExecutionChainMap.add(BeanMethodEventListener.class, AnnotationTag.AnnotationTags.Invoke);
            annotationExecutionChainMap.add(Loader.class, AnnotationTag.AnnotationTags.Invoke);
            annotationExecutionChainMap.add(Unloader.class, AnnotationTag.AnnotationTags.Invoke);
            annotationExecutionChainMap.add(Invoke.class, AnnotationTag.AnnotationTags.Invoke);
            annotationExecutionChainMap.addDepend(
                    new Class[]{BeanMethodEventListener.class, Loader.class, Unloader.class, Invoke.class},
                    new Class[]{ConditionalOnClass.class, ConditionalOnBean.class, ConditionalOnMissingClass.class, ConditionalOnMissingBean.class, ConfigurationProperties.class, ConfigurationPropertiess.class});

            annotationExecutionChainMap.add(ModulePackage.class, AnnotationTag.AnnotationTags.Package);
            annotationExecutionChainMap.add(PluginPackage.class, AnnotationTag.AnnotationTags.Package);

            annotationExecutionChainMap.addDepend(
                    new Class[]{ModulePackage.class, PluginPackage.class},
                    new Class[]{
                            ConfigurationPropertiess.class, ConfigurationProperties.class, Configuration.class,
                            MavenDepositorys.class, ApplicationDependency.class, ApplicationDependencys.class, MavenModuleDependency.class, MavenModuleDependencys.class, I18n.class, L10n.class
                    });
            //绑定密码驱动器
            this.addBean(new PasswordDrive());
            //绑定类处理
            this.addBean(new ByteBuddy(ClassFileVersion.ofJavaVersion(8)));
            //绑定id生成类
            this.addBean(new AlternativeJdkIdGenerator());
            //--------------------------------------------------------------------------------------
            //绑定任务生成器
            TaskExecutorBuilder taskExecutorBuilder = this.addBean(TaskExecutorBuilder.class);
            //重写初始化
            super.init();
            //初始化解析器
            this.addBean(PropertiesResolverObjectFactory.class);
            this.addBean(EnvironmentResolverObjectFactory.class);
            //绑定默认Properties转换器
            this.addBean(DefaultStreamConverterProperties.class);
            this.addBean(DefaultFileConverterProperties.class);
            this.addBean(DefaultYmlFileConverterProperties.class);
            this.addBean(DefaultURLConverterProperties.class);
            this.addBean(DefaultConverterProperties.class);
            this.addBean(DefaultResourceBytesConverterProperties.class);
            this.addBean(DefaultStringConverterProperties.class);
            this.addBean(DefaultMapConverterProperties.class);
            //绑定基础类型转换器
            this.addBean(DefaultConverterBoolean.class);
            this.addBean(DefaultConverterByte.class);
            this.addBean(DefaultConverterCharacter.class);
            this.addBean(DefaultConverterDouble.class);
            this.addBean(DefaultConverterFloat.class);
            this.addBean(DefaultConverterInteger.class);
            this.addBean(DefaultConverterLong.class);
            this.addBean(DefaultConverterShort.class);
            this.addBean(DefaultConverterString.class);
            this.addBean(DefaultConverterDate.class);
            this.addBean(DefaultConverterUUID.class);
            this.addBean(DefaultLongConverterBoolean.class);
            //spring 转换器
            this.addBean(StringToUUIDConverter.class);
            this.addBean(StringToTimeZoneConverter.class);
            this.addBean(StringToPropertiesConverter.class);
            this.addBean(StringToNumberConverterFactory.class);
            this.addBean(StringToLocaleConverter.class);
            this.addBean(StringToEnumConverterFactory.class);
            this.addBean(StringToCurrencyConverter.class);
            this.addBean(StringToCollectionTypeDescriptorConverter.class);
            this.addBean(StringToCharsetConverter.class);
            this.addBean(StringToCharacterConverter.class);
            this.addBean(StringToBooleanConverter.class);
            this.addBean(StringToArrayTypeDescriptorConverter.class);
            this.addBean(PropertiesToStringConverter.class);
            this.addBean(ObjectToStringConverter.class);
            this.addBean(ObjectToOptionalTypeDescriptorConverter.class);
            this.addBean(ObjectToObjectTypeDescriptorConverter.class);
            this.addBean(ObjectToCollectionTypeDescriptorConverter.class);
            this.addBean(ObjectToArrayTypeDescriptorConverter.class);
            this.addBean(NumberToCharacterConverter.class);
            this.addBean(MapToMapTypeDescriptorConverter.class);
            this.addBean(FallbackObjectToStringTypeDescriptorConverter.class);
            this.addBean(EnumToStringConverter.class);
            this.addBean(EnumToIntegerConverter.class);
            this.addBean(CollectionToStringTypeDescriptorConverter.class);
            this.addBean(CollectionToObjectTypeDescriptorConverter.class);
            this.addBean(CollectionToCollectionTypeDescriptorConverter.class);
            this.addBean(CollectionToArrayTypeDescriptorConverter.class);
            this.addBean(ByteBufferTypeDescriptorConverter.class);
            this.addBean(ArrayToStringTypeDescriptorConverter.class);
            this.addBean(ArrayToObjectTypeDescriptorConverter.class);
            this.addBean(ArrayToCollectionTypeDescriptorConverter.class);
            this.addBean(ArrayToArrayTypeDescriptorConverter.class);

            this.addBean(StreamTypeDescriptorConverter.class);
            //创建模块线程池
//            taskExecutorBuilder.build()
            //绑定函数排序事件工厂
//            this.addBean(DefaultMethodOrderEventFactory.class);
            //绑定区域化json解析器
            this.addBean(LocaleParserContainer.class);
            this.addBean(LocaleJsonParser.class);
            //Boot注释事件工厂
            this.addBean(ClassApplicationBootAnnotationBeanFactory.class);
            this.initBean();
            this.initDirectory();
            this.initResource();
            this.initEnv(args);
            this.initModule();
            this.initInterceptor();
            this.initObject();
            this.initProxy();
            this.initData();
            this.initTransaction();
            this.initMaven();
            //对模块注释内容进行加载
            this.getBean(LocaleEventListenerFactoryContainer.class).loader(new LocaleEventTargetHandle(this, this.getRootClass()));
            if (this.log.isDebugEnabled()) {
                this.log.debug(this.getHome().getSource().getPath());
            } else {
                this.log.info(this.getHome().getSource().getPath());
            }
            //注册程序退出钩子
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("shutdown hook!!!");
                }
            }));
            //优先加载应用依赖包，因为加载应用插件时可能需要应用注释包，所有应用的maven包引用必须在初始化插件之前加载
            this.getBean(IMavenApplicationDependencyLoader.class).loader(new MavenLoaderEventTargetHandle(this, this.getRootClass()));
            //加载应用插件
            this.initPlugins();
            //初始化完成
            this.initialize = true;
            //执行启动类注释处理
            this.getBean(IApplicationLoader.class).loader(new MavenLoaderEventTargetHandle(this, this.getRootClass()));
            //推送应用初始化完成
            this.getBean(IEventPublisherContainer.class).publishEvent(new GenericApplicationEvent(this, ApplicationEventTopic.AppLaunchCompleted.name()));
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
        }
        this.log.info("init->End");
        return this;
    }

    /**
     * 初始化事务
     */
    private void initTransaction() {
        this.log.info("initTransaction->Start");
        this.log.info("initTransaction->End");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        this.log.info("initData->Start");
        this.log.info("initData->End");
    }

    /**
     * 初始化代理
     *
     * @throws Exception
     */
    private void initProxy() throws Exception {
        this.log.info("initProxy->Start");
        //绑定代理事件监听容器
        this.addBean(CglibProxyEventFactoryContainer.class);
        //绑定默认应用绑定env代理事件工厂
//        this.addBean(DefaultApplicationBindEnvironmentCglibProxyEventFactory.class);
//        this.getBean(ICglibProxyEventListenerFactoryContainer.class).loader(this.getBean(IAbstractBindEnvironmentCglibProxyEventFactory.class));
        this.log.info("initProxy->End");
    }

    /**
     * 初始化对象
     */
    private void initObject() {
        this.log.info("initObject->Start");
        this.log.info("initObject->End");
    }

    /**
     * 初始化任务
     */
    private void initTask() {
        this.log.info("initTask->Start");
        //绑定线程池任务工厂容器
//        IThreadPoolExecutorFactoryContainer poolExecutorFactoryContainer = this.addBean(ThreadPoolExecutorFactoryContainer.class);
//        this.addBean(ModuleThreadPoolExecutorFactory.class);
//        poolExecutorFactoryContainer.add(IModuleThreadPoolExecutorFactory.class);
        this.log.info("initTask->End");
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
        IEnvironment env = this.getNullableBean(IApplicationEnvironment.class);
        if (env != null && env.containsKey(ApplicationConstant.Bean.AUTO_BEAN)) {
            return env.getBoolean(ApplicationConstant.Bean.AUTO_BEAN);
        }
        return false;
    }

    /**
     * 初始化Bean
     */
    private void initBean() {
        this.log.info("initBean->Start");
        //创建绑定地图
        synchronized (this.getBeanMap()) {
            //绑定注释 {@link ghost.framework.beans.maven.annotation.module.ModuleMavenDependency} 读取器类
            this.addBean(AnnotationModuleMavenDependencyReader.class);
            this.addBean(AnnotationMavenDepositoryReader.class);
            this.addBean(AnnotationMavenDependencyReader.class);
//            this.addBean(AnnotationBeanMavenDependencyReader.class);
            this.addBean(AnnotationApplicationMavenDependencyReader.class);
//            this.addBean(AnnotationApplicationBeanMavenDependencyReader.class);
            //3、绑定env
            this.addBean(ApplicationEnvironment.class);
            //3、绑定默认注释替换env值解析器
//            this.addBean(DefaultAnnotationReflectEnvironmentValueParser.class);
            //初始化json对象
            ObjectMapper objectMapper = new ObjectMapper();
            // 转换为格式化的json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            // 如果json中有新增的字段并且是实体类类中不存在的，不报错
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //序列化的时候序列对象的所有属性
            objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            //
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            objectMapper.registerModule(new JavaTimeModule());
            //兼容日期格式化
            DateFormat dateFormat = objectMapper.getDateFormat();
            objectMapper.setConfig(objectMapper.getDeserializationConfig().with(new ObjectMapperDateFormatExtend(dateFormat)));//反序列化扩展日期格式支持
            this.addBean(objectMapper);
            //绑定谷歌Gson转换器
            this.addBean(Gson.class);
            //绑定json转换容器
            this.addBean(DefaultJsonConverterContainer.class);
            //
            this.addBean(DefaultJsonToMapConverter.class);
            this.addBean(DefaultMapToJsonConverter.class);
            //
            this.addBean(DefaultObjectToJsonConverter.class);
            this.addBean(DefaultJsonToObjectConverter.class);
            //绑定序列化转换容器
            this.addBean(DefaultBytesToObjectConverter.class);
            this.addBean(DefaultObjectToBytesConverter.class);
            //创建绑定cglib代理创建接口类
            this.addBean(InterfaceMaker.class);
            //
            this.addBean(this.home);
            //处理应用线程未处理错误
            this.addBean(ApplicationUncaughtExceptionHandler.class);
            //设置应用主线程错误处理
            Thread.currentThread().setUncaughtExceptionHandler(this.getBean(IApplicationUncaughtExceptionHandler.class));
            //
            this.addBean(ModuleContainer.class);
        }
        this.log.info("initBean->End");
    }

    /**
     * 初始化拦截器
     *
     * @throws Exception
     */
    private void initInterceptor() throws Exception {
        this.log.info("initInterceptor->Start");
        this.log.info("initInterceptor->End");
    }

    /**
     * 获取模块
     *
     * @param name 模块name
     * @return
     */
    @Nullable
    @Override
    public IModule getModule(@NotNull String name) {
        return this.getBean(IModuleContainer.class).getModule(name);
    }

    /**
     * 获取模块
     * @param artifact 模块信息
     * @return
     */
    @Nullable
    @Override
    public IModule getModule(@NotNull FileArtifact artifact) {
        return this.getBean(IModuleContainer.class).getModule(artifact);
    }
    /**
     * 获取模块
     * @param artifact 模块信息
     * @return
     */
    @Nullable
    @Override
    public IModule getModule(@NotNull Artifact artifact) {
        return this.getBean(IModuleContainer.class).getModule(artifact);
    }
    /**
     * @param artifact 模块版本信息注释
     * @return
     */
    @Override
    public IModule getModule(ModuleArtifact artifact) {
        return this.getBean(IModuleContainer.class).getModule(artifact);
    }

    /**
     * 使用类型获取所属模块
     *
     * @param target 类型目标
     * @return
     */
    @Override
    public IModule getApplicationHomeModule(Class<?> target) {
        IModuleContainer container = this.getNullableBean(IModuleContainer.class);
        if (container == null) {
            return null;
        }
        return container.getApplicationHomeModule(target);
    }

    /**
     * 初始化模块
     *
     * @throws Exception
     */
    private void initModule() throws Exception {
        this.log.info("initModule->Start");
        //绑定模块线程池
        this.addBean(ModuleThreadPool.class);
        //初始化模块容器
        //初始化模块线程未处理错误处理
        this.addBean(ModuleUncaughtExceptionHandler.class);
        this.log.info("initModule->End");
    }

    /**
     * 初始化maven
     *
     * @throws Exception
     */
    private void initMaven() throws Exception {
        this.log.info("initMaven->Start");
        this.addBean(ApplicationLoader.class);
        //初始化maven仓库容器
        this.addBean(MavenRepositoryContainer.class);
        //maven加载器
        this.addBean(MavenLoader.class);
        //maven仓库加载
        this.addBean(MavenDepositoryLoader.class);
        //maven包依赖加载器
        this.addBean(MavenDependencyLoader.class);
        //maven应用依赖加载器
        this.addBean(MavenApplicationDependencyLoader.class);
        //maven模块依赖加载器
        this.addBean(MavenModuleDependencyLoader.class);
        //初始化模块加载器
        this.addBean(MavenModuleLoader.class);
        //创建maven本地仓库目录
        IEnvironment env = this.getBean(IApplicationEnvironment.class);
        File file = new File(env.getString(ApplicationConstant.Maven.LOCAL_REPOSITORY_PATH));
        this.addBean(ApplicationConstant.Maven.LOCAL_REPOSITORY_PATH, file);
        this.addBean(ApplicationConstant.Maven.LOCAL_REPOSITORY_URL, file.toURL());
        //判断m2目录是否存在
        if (file.exists()) {
            this.log.info("exists maven directory:" + file.getPath());
        } else {
            //创建m2目录
            file.mkdirs();
            this.log.info("create maven directory:" + file.getPath());
        }
        //获取maven仓库容器接口
        IMavenRepositoryContainer mavenRepositoryServers = this.getBean(IMavenRepositoryContainer.class);
        //配置env仓库列表
        if (env.containsKey(ApplicationConstant.Maven.REPOSITORY_URLS)) {
            //获取配置文件maven仓库列表
            mavenRepositoryServers.addNotContainsAll(CoreMavenUtil.toMavenRepositoryList(env.getString(ApplicationConstant.Maven.REPOSITORY_URLS)));
        }
        //获取maven仓库列表
        List<MavenDepository> list = MavenAnnotationUtil.getAnnotationMavenDepositoryList(this.rootClass);
        for (MavenDepository depository : list) {
            if (this.log.isDebugEnabled()) {
                MavenRepositoryServer repositoryServer = new MavenRepositoryServer(depository.id(), depository.type(), depository.username(), depository.password(), depository.url());
                mavenRepositoryServers.add(repositoryServer);
                this.log.debug("MavenRepositoryContainer add:" + repositoryServer.toString());
            } else {
                mavenRepositoryServers.add(new MavenRepositoryServer(depository.id(), depository.type(), depository.username(), depository.password(), depository.url()));
            }
        }
        this.log.info("initMaven->End");
    }

    /**
     * 初始化资源
     */
    private void initResource() {
        this.log.info("initResource->Start");
        this.log.info("initResource->End");
    }

    /**
     * 初始化目录
     */
    private void initDirectory() {
        this.log.info("initDirectory->Start");
        //创建jar运行临时目录
        File temp = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().toLowerCase());
        //创建目录
        temp.mkdir();
        temp.setExecutable(true);//设置可执行权限
        temp.setReadable(true);//设置可读权限
        temp.setWritable(true);//设置可写权限
        //在jar程序退出时删除目录
        temp.deleteOnExit();
        this.addBean(ApplicationConstant.TEMP_DIRECTORY, temp);
        this.getEnv().setString(ApplicationConstant.TEMP_DIRECTORY, temp.getPath());
        // 保存生成的代理子类Class文件
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, temp.getPath());
        this.log.info("initDirectory->End");
    }



    /**
     * 初始化env
     * args参数在初始化所有配置文件后才解析到env作为最后的配置参数
     *
     * @param args 运行参数
     * @throws Exception
     */
    private void initEnv(String[] args) throws Exception {
        this.log.info("initEnv->Start");
        //
        IEnvironment env = this.getBean(IApplicationEnvironment.class);
        //处理运行参数
        if (args != null && args.length > 0) {
            for (String p : args) {
                //处理@开头参数 比如配置格式 @ghost.framework.profiles.active=dev
                if (p.startsWith("@")) {
                    String s = p.substring(1);
                    if (StringUtils.isEmpty(s)) {
                        throw new IllegalArgumentException("args " + p + " error");
                    }
                    String[] ps = StringUtils.split(s, "=");
                    if (ps.length != 2) {
                        throw new IllegalArgumentException("args " + p + "format error");
                    }
                    ps[0] = ps[0].trim();
                    ps[1] = ps[1].trim();
                    if (StringUtils.isEmpty(ps[0])) {
                        throw new IllegalArgumentException("args " + p + " value error");
                    }
                    //添加env参数
                    env.setString(ps[0], ps[1]);
                    continue;
                }
            }
        }
        //设置应用默认名称
        env.setString(ApplicationConstant.NAME, "app");
        //判断是否为开发模式
        if (AssemblyUtil.isPathDev(this.home.getSource().getPath())) {
            //开发模式
            env.setBoolean(ApplicationConstant.DEV, true);
        } else {
            //不为开发模式
            env.setBoolean(ApplicationConstant.DEV, false);
        }
        //设置系统类型
        //win为否，linux为是
        env.setBoolean(ApplicationConstant.OS, File.separator.equals("/"));
        //判断 ghost.framework.profiles.active 默认配置
        String active = null;
        if (env.containsKey(CommonConstant.Profiles.ACTIVE)) {
            //获取配置名称
            active = env.getNullable(CommonConstant.Profiles.ACTIVE);
            //判断是否有配置名称
            if (StringUtils.isEmpty(active)) {
                //没有配置名称处理
                //按照默认application.properties或application.yml文件加载合并，如果application.properties或application.yml文件存在时合并
                this.environmentMerge(null);
            } else {
                //有指定配置环境
                //按照默认application.properties或application.yml文件加载合并，如果application.properties或application.yml文件存在时合并
                if (!this.environmentMerge(env.getString(CommonConstant.Profiles.ACTIVE))) {
                    //没有找到合并配置，重新找默认配置文件合并配置
                    this.environmentMerge(null);
                }
            }
        } else {
            //没有指定配置环境，
            //使用默认加载配置文件
            this.environmentMerge(null);
        }
        this.log.info("run " + CommonConstant.Profiles.ACTIVE + "=" + active);
        //加载指定注释配置
        for (ConfigurationProperties p : MavenAnnotationUtil.getAnnotationConfigurationPropertiesList(this.getRootClass())) {
            if (this.isDev()) {
                env.merge((Properties) this.getConverterContainer().getTypeConverter(DefaultFileConverterProperties.class).convert(new File(this.home.getSource().getPath() + File.separator + p.path())));
            } else {
                //获取jar包模式配置文件
                env.merge(p.prefix(), this.getUrlPath(p.path()));
            }
        }
        //判断是否有maven目录配置
        if (!env.containsKey(ApplicationConstant.Maven.LOCAL_REPOSITORY_PATH)) {
            //设置临时maven目录参数
            env.setString(ApplicationConstant.Maven.LOCAL_REPOSITORY_PATH, env.get(ApplicationConstant.TEMP_DIRECTORY) + File.separator + "m2" + File.separator + "repository");
        }
        //处理运行参数
        if (args != null && args.length > 0) {
            for (String p : args) {
                //处理--开头参数
                if (p.startsWith("--")) {
                    String s = p.substring(2);
                    if (StringUtils.isEmpty(s)) {
                        throw new IllegalArgumentException("args " + p + " error");
                    }
                    String[] ps = StringUtils.split(s, "=");
                    if (ps.length != 2) {
                        throw new IllegalArgumentException("args " + p + "format error");
                    }
                    ps[0] = ps[0].trim();
                    ps[1] = ps[1].trim();
                    if (StringUtils.isEmpty(ps[0])) {
                        throw new IllegalArgumentException("args " + p + " value error");
                    }
//                    if (StringUtils.isEmpty(ps[1])) {
//                        throw new IllegalArgumentException("args " + p + " value error");
//                    }
                    //添加env参数
                    env.setString(ps[0], ps[1]);
                    continue;
                }
                //处理-开头参数
                if (p.startsWith("-")) {
                    String s = p.substring(1);
                    if (StringUtils.isEmpty(s)) {
                        throw new IllegalArgumentException("args " + p + " error");
                    }
                    String[] ps = StringUtils.split(s, "=");
                    if (ps.length != 2) {
                        throw new IllegalArgumentException("args " + p + "format error");
                    }
                    ps[0] = ps[0].trim();
                    ps[1] = ps[1].trim();
                    if (StringUtils.isEmpty(ps[0])) {
                        throw new IllegalArgumentException("args " + p + " value error");
                    }
                    //添加env参数
                    env.setString(ps[0], ps[1]);
                    continue;
                }
            }
        }
        this.log.info("initEnv->End");
    }
    /**
     * 获取应用包指定文件url路径
     *
     * @param filePath 文件路径
     * @return
     */
    @Override
    public URL getUrlPath(String filePath) throws MalformedURLException {
        return new URL("jar:file:" + this.rootClass.getProtectionDomain().getCodeSource().getLocation().getPath() + "!/" + filePath);
    }

    /**
     * 获取应用包url路径
     *
     * @return
     * @throws MalformedURLException
     */
    @Override
    public URL getUrlPath() throws MalformedURLException {
        return new URL("jar:file:" + this.rootClass.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    /**
     * 获取应用内容env
     *
     * @return
     */
    @NotNull
    @Override
    public IApplicationEnvironment getEnv() {
        return this.getBean(IApplicationEnvironment.class);
    }

    /**
     * 获取模块可空env
     *
     * @return
     */
    @Nullable
    @Override
    public IApplicationEnvironment getNullableEnv() {
        return this.getNullableBean(IApplicationEnvironment.class);
    }

    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        run(Application.class, args);
    }

    /**
     * 不带参数运行
     *
     * @param rootClass
     * @return
     */
    public static IApplication run(Class<?> rootClass) throws Exception {
        return run(rootClass, null);
    }

    /**
     * 带参数运行
     *
     * @param rootClass 引导类
     * @param args      运行参数
     * @return
     */
    public static IApplication run(Class<?> rootClass, String[] args) throws Exception {
        //打印logo
        System.out.println(FileUtil.streamToString(Application.class.getClassLoader().getResourceAsStream("banner.txt")));
        //初始化应用引导类
        return new Application(rootClass).init(args);
    }

    /**
     * 释放资源
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {

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


    @Override
    public ApplicationHome getHome() {
        return this.home;
    }

    /**
     * 获取是否为开发模式
     *
     * @return
     */
    @Override
    public boolean isDev() {
        return this.getEnv().getBoolean(ApplicationConstant.DEV);
    }

    /**
     * 获取是否为windows
     *
     * @return
     */
    @Override
    public boolean isWindows() {
        return !this.getEnv().getBoolean(ApplicationConstant.OS);
    }

    /**
     * 获取是否为linux
     *
     * @return
     */
    @Override
    public boolean isLinux() {
        return this.getEnv().getBoolean(ApplicationConstant.OS);
    }

    /**
     * 删除模块
     *
     * @param name 模块名称
     * @return
     */
    @Override
    public IModule removeModule(@NotNull String name) {
        return this.getBean(IModuleContainer.class).removeModule(name);
    }

    /**
     * 验证模块是否存在
     *
     * @param artifact 版本信息
     * @return 返回模块是否存在
     */
    @Override
    public boolean containsModule(@NotNull Artifact artifact) {
        return this.getBean(IModuleContainer.class).containsModule(artifact);
    }

    /**
     * 验证模块是否存在
     *
     * @param module 模块对象
     * @return 返回模块是否存在
     */
    @Override
    public boolean containsModule(@NotNull IModule module) {
        return this.getBean(IModuleContainer.class).containsModule(module);
    }

    /**
     * 验证模块是否存在
     *
     * @param groupId    模块组id
     * @param artifactId 模块组织id
     * @param version    模块版本
     * @return 返回模块是否存在
     */
    @Override
    public boolean containsModule(String groupId, String artifactId, String version) {
        return this.getBean(IModuleContainer.class).containsModule(groupId, artifactId, version);
    }

    /**
     * 验证模块是否存在
     *
     * @param name 模块名称
     * @return
     */
    @Override
    public boolean containsModule(@NotNull String name) {
        return this.getBean(IModuleContainer.class).containsModule(name);
    }

    /**
     * 删除模块
     *
     * @param module 模块对象
     */
    @Override
    public void removeModule(@NotNull IModule module) {
        this.getBean(IModuleContainer.class).removeModule(module);
    }

    @Override
    public void removeModule(Artifact artifact) {

    }

    /**
     * @param artifact 模块注释版本
     */
    @Override
    public void removeModule(ModuleArtifact artifact) {
        this.getBean(IModuleContainer.class).removeModule(artifact);
    }

    /**
     * @param artifact 版本信息
     * @return
     */
    @Override
    public boolean containsModule(ModuleArtifact artifact) {
        return this.getBean(IModuleContainer.class).containsModule(artifact);
    }

    /**
     * 获取应用临时目录
     *
     * @return
     */
    @Override
    public File getTempDirectory() {
        return this.getBean(ApplicationConstant.TEMP_DIRECTORY);
    }

    /**
     * 获取引导类型
     *
     * @return
     */
    @NotNull
    @Override
    public final Class<?> getRootClass() {
        return rootClass;
    }
}
