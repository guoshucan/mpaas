package ghost.framework.core.base;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.beans.maven.IGetArtifacts;
import ghost.framework.context.IGetName;
import ghost.framework.context.application.ApplicationConstant;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationEnvironment;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.application.event.IEventPublisherContainer;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.base.IInstance;
import ghost.framework.context.base.IObjectInjection;
import ghost.framework.context.bean.IBean;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.exception.AnnotationNotBeanException;
import ghost.framework.context.bean.factory.*;
import ghost.framework.context.bean.factory.injection.field.IInjectionContainer;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionTargetHandle;
import ghost.framework.context.bean.factory.injection.parameter.ParameterInjectionContainer;
import ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactoryContainer;
import ghost.framework.context.bean.utils.BeanUtil;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.IGetConverterContainer;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.event.maven.MavenPluginEventTargetHandle;
import ghost.framework.context.exception.*;
import ghost.framework.context.log.IGetLog;
import ghost.framework.context.maven.IMavenPluginLoader;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.exception.AnnotationMethodInvokeException;
import ghost.framework.context.plugin.IGetPluginContainer;
import ghost.framework.context.plugin.IPluginContainer;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.core.application.event.*;
import ghost.framework.core.bean.BeanDefinition;
import ghost.framework.core.bean.factory.ClassAnnotationBeanFactoryContainer;
import ghost.framework.core.bean.factory.ClassBeanFactoryContainer;
import ghost.framework.core.bean.factory.ClassBeanTargetHandle;
import ghost.framework.core.bean.factory.conditional.ClassConditionalOnBeanAnnotationBeanFactory;
import ghost.framework.core.bean.factory.conditional.ClassConditionalOnClassAnnotationBeanFactory;
import ghost.framework.core.bean.factory.conditional.ClassConditionalOnMissingBeanAnnotationBeanFactory;
import ghost.framework.core.bean.factory.conditional.ClassConditionalOnMissingClassAnnotationBeanFactory;
import ghost.framework.core.bean.factory.configuration.ClassConfigurationClassPropertiesAnnotationBeanFactory;
import ghost.framework.core.bean.factory.configuration.ClassConfigurationPropertiesAnnotationBeanFactory;
import ghost.framework.core.bean.factory.configuration.ClassConfigurationPropertiessAnnotationBeanFactory;
import ghost.framework.core.bean.factory.container.*;
import ghost.framework.core.bean.factory.converter.ClassConverterFactoryAnnotationBeanFactory;
import ghost.framework.core.bean.factory.injection.InjectionObjectEventTargetHandle;
import ghost.framework.core.bean.factory.injection.field.*;
import ghost.framework.core.bean.factory.injection.parameter.*;
import ghost.framework.core.bean.factory.locale.ClassI18nAnnotationBeanFactory;
import ghost.framework.core.bean.factory.locale.ClassL10nAnnotationBeanFactory;
import ghost.framework.core.bean.factory.other.DefaultClassAnnotationBeanFactory;
import ghost.framework.core.bean.factory.resolver.ClassResolverFactoryAnnotationBeanFactory;
import ghost.framework.core.bean.factory.scan.DefaultScanResourceBeanFactory;
import ghost.framework.core.bean.factory.stereotype.ClassComponentAnnotationBeanFactory;
import ghost.framework.core.bean.factory.stereotype.ClassConfigurationAnnotationBeanFactory;
import ghost.framework.core.bean.factory.stereotype.ClassServiceAnnotationBeanFactory;
import ghost.framework.core.bean.factory.valid.ClassaValidAnnotationBeanFactory;
import ghost.framework.core.converter.DefaultConverterContainer;
import ghost.framework.core.converter.properties.DefaultFileConverterProperties;
import ghost.framework.core.converter.properties.DefaultStreamConverterProperties;
import ghost.framework.core.converter.properties.DefaultYmlFileConverterProperties;
import ghost.framework.core.event.MethodEventTargetHandle;
import ghost.framework.core.event.OperatingBeanEventTargetHandle;
import ghost.framework.core.event.bean.ItemBeanTargetHandle;
import ghost.framework.core.event.bean.container.BeanEventFactoryContainer;
import ghost.framework.core.event.bean.container.IBeanEventFactoryContainer;
import ghost.framework.core.event.bean.operating.container.IOperatingBeanEventFactoryContainer;
import ghost.framework.core.event.bean.operating.container.OperatingBeanEventFactoryContainer;
import ghost.framework.core.event.locale.factory.container.LocaleEventListenerFactoryContainer;
import ghost.framework.core.event.method.container.DefaultMethodAnnotationBeanFactoryContainer;
import ghost.framework.core.event.method.factory.bean.MethodBeanAnnotationEventFactory;
import ghost.framework.core.event.method.factory.conditional.MethodConditionalOnBeanAnnotationEventFactory;
import ghost.framework.core.event.method.factory.conditional.MethodConditionalOnClassAnnotationEventFactory;
import ghost.framework.core.event.method.factory.conditional.MethodConditionalOnMissingBeanAnnotationEventFactory;
import ghost.framework.core.event.method.factory.conditional.MethodConditionalOnMissingClassAnnotationEventFactory;
import ghost.framework.core.event.method.factory.configuration.MethodConfigurationPropertiesAnnotationEventFactory;
import ghost.framework.core.event.method.factory.configuration.MethodConfigurationPropertiessAnnotationEventFactory;
import ghost.framework.core.event.method.factory.event.MethodBeanMethodEventListenerAnnotationEventFactory;
import ghost.framework.core.event.method.factory.method.MethodInvokeAnnotationEventFactory;
import ghost.framework.core.event.method.factory.method.MethodLoaderAnnotationEventFactory;
import ghost.framework.core.event.method.factory.method.MethodTriggerEventAnnotationEventFactory;
import ghost.framework.core.event.method.factory.method.MethodUnloaderAnnotationEventFactory;
import ghost.framework.core.event.object.container.IObjectEventFactoryContainer;
import ghost.framework.core.event.object.container.ObjectEventFactoryContainer;
import ghost.framework.core.event.scan.container.ScanResourceEventFactoryContainer;
import ghost.framework.core.event.scan.factory.ScanClassesResourceEventFactory;
import ghost.framework.core.event.scan.factory.ScanJarResourceEventFactory;
import ghost.framework.core.locale.I18nContainer;
import ghost.framework.core.locale.I18nDomainContainer;
import ghost.framework.core.locale.L10nContainer;
import ghost.framework.core.locale.L10nDomainContainer;
import ghost.framework.core.pack.ClassResolve;
import ghost.framework.core.pack.PackageClassResolve;
import ghost.framework.core.plugin.PluginContainer;
import ghost.framework.core.proxy.cglib.CglibInstanceClassException;
import ghost.framework.core.proxy.cglib.CglibInstanceConstructorException;
import ghost.framework.core.proxy.cglib.CglibInstanceNullException;
import ghost.framework.core.resolverFactory.ResolverFactoryContainer;
import ghost.framework.core.valid.DefaultAnnotationValidFactoryContainer;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.plugin.loader.MavenPluginLoader;
import ghost.framework.util.Assert;
import ghost.framework.util.ExceptionUtil;
import ghost.framework.util.ReflectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
/**
 * package: ghost.framework.core.bean
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用基础类
 * @Date: 2020/1/8:16:43
 */
public abstract class ApplicationBase
        implements IBean, IInstance, ICoreInterface,
        AutoCloseable, IGetLog, IGetClassLoader, IGetPluginContainer,
        IObjectInjection, IGetApplication, IGetArtifacts, IGetConverterContainer {

    /**
     * 获取插件容器接口
     *
     * @return
     */
    @Override
    public IPluginContainer getPluginContainer() {
        return this.getBean(IPluginContainer.class);
    }

    /**
     * 推送消息
     *
     * @param event 消息对象
     */
    @Override
    public void publishEvent(AbstractApplicationEvent event) {
        //本身属于应用推送事件
        if (this instanceof IApplication) {
            this.getBean(IEventPublisherContainer.class).publishEvent(event);
            return;
        }
        //本身属于模块推送事件
        this.getBean(IApplication.class).getBean(IEventPublisherContainer.class).publishEvent(event);
    }

    /**
     * 日志
     */
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 获取转换器工厂容器接口
     *
     * @return
     */
    @Override
    public ConverterContainer getConverterContainer() {
        if (this instanceof IModule) {
            return this.getApp().getBean(ConverterContainer.class);
        }
        return this.getBean(ConverterContainer.class);
    }

    /**
     * 获取日志
     *
     * @return
     */
    @Override
    public Log getLog() {
        return log;
    }

    /**
     * 获取模块包列表
     *
     * @return
     */
    @Override
    public List<FileArtifact> getArtifacts() {
        return this.getClassLoader().getArtifacts();
    }

    /**
     * 获取类加载器
     *
     * @return
     */
    @Override
    public abstract IClassLoader getClassLoader();

    /**
     * 释放资源
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        synchronized (beanMap) {
            beanMap.clear();
        }
    }

    /**
     * 绑定地图
     */
    private Map<String, IBeanDefinition> beanMap = new LinkedHashMap<>();

    /**
     * 获取绑定地图
     *
     * @return
     */
    @Override
    public Map<String, IBeanDefinition> getBeanMap() {
        return beanMap;
    }

    /**
     * 加载插件
     */
    protected void initPlugins() {
        //调用maven插件加载接口加载插件
        this.getApp().getBean(IMavenPluginLoader.class).loader(
                new MavenPluginEventTargetHandle(
                        this,
                        //如果为应用加载插件时为启动类型加载
                        (this instanceof IApplication ?
                                //应用初始化加载插件
                                ((IApplication) this).getRootClass() :
                                //模块初始化加载插件
                                ((IModule) this).getPackageClass())));
    }

    /**
     * 初始化
     */
    protected void init() {
        synchronized (this.beanMap) {
            //初始化添加绑定对象
            this.addBean(this);
            //绑定类加载器
            this.addBean(this.getClassLoader());
            //---------------------------------------------------ObjectEventFactoryContainer------------------------------------------
            this.addBean(ObjectEventFactoryContainer.class);
            //------------------------------------------------1 DefaultInjectorIContainer -------------------------------------------------------
            //绑定获取注释事件工厂容器
            IInjectionContainer injectorFieldContainer = this.addBean(DefaultInjectorIContainer.class);
            //Autowired注释事件工厂
            injectorFieldContainer.add(this.addBean(FieldAutowiredAnnotationInjectionFactory.class));
            injectorFieldContainer.add(this.addBean(MethodAutowiredAnnotationInjectionFactory.class));
            //TempDirectory注释事件工厂
            injectorFieldContainer.add(this.addBean(FieldTempDirectoryAnnotationInjectionFactory.class));
            //Value注释注射器
            injectorFieldContainer.add(this.addBean(FieldValueAnnotationInjectionFactory.class));
            //---------------------------------------------------InjectorParameterContainer--------------------------------
            //绑定获取注释事件工厂容器
            ParameterInjectionContainer injectorParameterContainer = this.addBean(DefaultParameterInjectionContainer.class);
            //Autowired注释事件工厂
            injectorParameterContainer.add(this.addBean(ParameterAutowiredAnnotationInjectionFactory.class));
            //TempDirectory注释事件工厂
            injectorParameterContainer.add(this.addBean(ParameterTempDirectoryAnnotationInjectionFactory.class));
            //Value注释注射器
            injectorParameterContainer.add(this.addBean(ParameterValueAnnotationInjectionFactory.class));
            //------------------------------------------------- OperatingBeanEventListenerFactoryContainer --------------------------------------------
            //绑定动作事件监听工厂容器
            IOperatingBeanEventFactoryContainer operatingBeanEventFactoryContainer = this.addBean(OperatingBeanEventFactoryContainer.class);
            //
//            operatingBeanEventFactoryContainer.add(this.addBean(OperatingBeanClassAnnotationEventFactory.class));
            //
//            operatingBeanEventFactoryContainer.add(this.addBean(OperatingBeanMethodAnnotationEventFactory.class));
            //---------------------------------------------------BeanEventFactoryContainer------------------------------------------
            this.addBean(BeanEventFactoryContainer.class);
//            this.addBean(BeanEventFactory.class);
            //--------------------------------------------BeanMethodAnnotationEventFactoryContainer---------------------------------
            //绑定函数列表容器注释事件工厂
            MethodAnnotationBeanFactoryContainer methodAnnotationEventFactoryContainer = this.addBean(DefaultMethodAnnotationBeanFactoryContainer.class);
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodBeanAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodConditionalOnBeanAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodConditionalOnClassAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodConditionalOnMissingBeanAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodConditionalOnMissingClassAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodInvokeAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodTriggerEventAnnotationEventFactory.class));
            //ConfigurationProperties注释事件工厂
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodConfigurationPropertiesAnnotationEventFactory.class));
            //ConfigurationPropertiess注释事件工厂
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodConfigurationPropertiessAnnotationEventFactory.class));
            //
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodLoaderAnnotationEventFactory.class));
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodUnloaderAnnotationEventFactory.class));
            //BeanMethodEventListener
            methodAnnotationEventFactoryContainer.add(this.addBean(MethodBeanMethodEventListenerAnnotationEventFactory.class));
            //-------------------------------------------- ClassIObjectEventFactory --------------------------------------------------
            // IObjectEventFactory
//            this.addBean(ClassIObjectEventFactory.class);
            //--------------------------------------------4 ClassBeanFactoryContainer ---------------------------------------------------
            //绑定绑定事件监听处理工程
            IClassBeanFactoryContainer classBeanFactoryContainer = this.addBean(ClassBeanFactoryContainer.class);
            //--------------------------------------------4 ClassAnnotationBeanFactoryContainer -----------------------------------------
            //绑定注释绑定事件监听处理工程
            IClassAnnotationBeanFactoryContainer classAnnotationBeanFactoryContainer = this.addBean(ClassAnnotationBeanFactoryContainer.class);
            //Component注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassComponentAnnotationBeanFactory.class));
            //Service注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassServiceAnnotationBeanFactory.class));
            //Plugin注释事件工厂
//            classAnnotationEventFactoryContainer.add(this.addBean(ClassPluginAnnotationEventFactory.class));
            //ClassAnnotationEventFactory绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(DefaultClassAnnotationBeanFactory.class));
            //Configuration注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConfigurationAnnotationBeanFactory.class));
            //ConfigurationProperties注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConfigurationPropertiesAnnotationBeanFactory.class));
            //ConfigurationPropertiess注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConfigurationPropertiessAnnotationBeanFactory.class));
            //ConditionalOnBean注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConditionalOnBeanAnnotationBeanFactory.class));
            //ConditionalOnClass注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConditionalOnClassAnnotationBeanFactory.class));
            //ConditionalOnMissingBean注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConditionalOnMissingBeanAnnotationBeanFactory.class));
            //ConditionalOnMissingClass注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConditionalOnMissingClassAnnotationBeanFactory.class));
            //BeanMapContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(MapContainerAnnotationBeanFactory.class));
            //BeanMapInterfaceContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(MapInterfaceContainerAnnotationBeanFactory.class));
            //BeanCollectionContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(CollectionContainerAnnotationBeanFactory.class));
            //BeanCollectionInterfaceContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(CollectionInterfaceContainerAnnotationBeanFactory.class));
            //BeanListContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ListContainerAnnotationBeanFactory.class));
            //BeanSmartListContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(SmartListContainerAnnotationBeanFactory.class));
            //BeanListInterfaceContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ListInterfaceContainerAnnotationBeanFactory.class));
            //BeanClassContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassContainerAnnotationBeanFactory.class));
            //BeanClassNameContainer绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassNameContainerAnnotationBeanFactory.class));
            //
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassResolverFactoryAnnotationBeanFactory.class));
            //
            classAnnotationBeanFactoryContainer.add(this.addBean(JsonConverterAnnotationBeanFactory.class));
            //------------------------绑定解析器容器 ResolverContainer-------------------------------------------------
            this.addBean(ResolverFactoryContainer.class);
            //------------------------绑定转换器容器 ConverterFactoryContainer----------------------------------------------
            this.addBean(DefaultConverterContainer.class);
            //ConfigurationClassProperties注释事件工厂
            this.addBean(ClassConfigurationClassPropertiesAnnotationBeanFactory.class);
            //---------------
            //ConverterFactory绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassConverterFactoryAnnotationBeanFactory.class));
            //-------------------------------------------------------------------------------------------------------------
            //AnnotationValidFactory绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(ClassaValidAnnotationBeanFactory.class));
            //ScanResourceEventFactory绑定列表容器注释事件工厂
            classAnnotationBeanFactoryContainer.add(this.addBean(DefaultScanResourceBeanFactory.class));
            //----------------------------DefaultAnnotationValidFactoryContainer--------------------------------
            this.addBean(DefaultAnnotationValidFactoryContainer.class);
            //---------------------------ScanResourceEventFactoryContainer------------------------------
            this.addBean(ScanResourceEventFactoryContainer.class);
            this.addBean(ScanJarResourceEventFactory.class);
            this.addBean(ScanClassesResourceEventFactory.class);
            //绑定国际化与本地化绑定工厂
            this.addBean(ClassI18nAnnotationBeanFactory.class);
            this.addBean(ClassL10nAnnotationBeanFactory.class);
            //------------------------------------------------------------------------------------------
            //BeanApplicationEventListener绑定列表容器注释事件工厂
//            classAnnotationEventFactoryContainer.add(this.addBean(ClassBeanApplicationEventListenerAnnotationEventFactory.class));
            //BeanApplicationEventListenerProcessor绑定列表容器注释事件工厂
//            classAnnotationEventFactoryContainer.add(this.addBean(ClassBeanEventListenerProcessorAnnotationEventFactory.class));
            //--------------------ApplicationEventPublisherContainer----------------------------
            //如果为应用主体绑定应用事件监听测试类
            if (this instanceof IApplication) {
                //绑定应用事件推送容器
                this.addBean(ApplicationEventPublisherContainer.class);
                //绑定应用事件监听测试打印
                this.addBean(ApplicationEventListenerTest.class);
            } else {
                //绑定模块事件推送容器后将模块事件推送容器添加入应用事件推送容器监听
                this.getApp().getBean(IApplicationEventPublisherContainer.class).add(this.addBean(ModuleEventPublisherContainer.class));
                //绑定模块事件监听测试打印
                this.addBean(ModuleEventListenerTest.class);
            }
            //DevTest注释事件工厂
//            annotationBeanEventListenerFactoryContainer.add((IClassDevTestAnnotationEventFactory) this.addBean(DefaultClassDevTestAnnotationEventFactory.class));
//            annotationBeanEventListenerFactoryContainer.put(IBootAnnotationBeanInjector.class);
//            //CustomDataSource注释事件工厂
//            this.addBean(DefaultCustomDataSourceAnnotationBeanInjector.class);
//            //CustomDataSource注释事件工厂
//            annotationBeanEventListenerFactoryContainer.put(ICustomDataSourceAnnotationBeanInjector.class);
            //--------------------------------------------------- Plugin -----------------------------------
            //绑定插件容器
            this.addBean(PluginContainer.class);
            //
            this.addBean(ClassResolve.class);
            this.addBean(PackageClassResolve.class);
            //绑定插件加载器
            this.addBean(MavenPluginLoader.class);
            //------------------------绑定区域------------------------
            this.addBean(I18nContainer.class, new Object[]{this});
            this.addBean(L10nContainer.class, new Object[]{this});
            this.addBean(I18nDomainContainer.class);
            this.addBean(L10nDomainContainer.class);
            //--------------------------------------- DefaultLocaleEventListenerFactoryContainer
            this.addBean(LocaleEventListenerFactoryContainer.class);
//            this.addBean(DefaultLocaleGlobalEventFactory.class);
//            this.addBean(LocaleGlobalEventFactory.class);
//            this.addBean(DefaultLocaleLocalEventFactory.class);
//            this.addBean(LocaleLocalEventFactory.class);
        }
    }
    /**
     * 合并指定配置
     *
     * @param active
     * @return
     * @throws MalformedURLException
     */
    protected boolean environmentMerge(String active) throws MalformedURLException {
        if (this.isDev()) {
            //判断properties文件
            File file = new File(this.getHome().getSource().getPath() + File.separator + (active == null ? "application" : ("application-" + active)) + ".properties");
            if (file.exists()) {
                this.getEnv().merge((Properties) this.getConverterContainer().getTypeConverter(DefaultFileConverterProperties.class).convert(file));
                //返回true表实已经合并配置文件
                return true;
            } else {
                //判断yml文件
                file = new File(this.getHome().getSource().getPath() + File.separator + (active == null ? "application" : ("application-" + active)) + ".yml");
                if (file.exists()) {
                    this.getEnv().merge((Properties) this.getConverterContainer().getTypeConverter(DefaultYmlFileConverterProperties.class).convert(file));
                    //返回true表实已经合并配置文件
                    return true;
                }
            }
        } else {
            //判断是否为应用读取包配置文件
            if (this instanceof IApplication) {
                //应用读取配置文件
                //加载properties配置文件
                try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream((active == null ? "application" : ("application-" + active)) + ".properties")) {
                    this.getEnv().merge((Properties) this.getConverterContainer().getTypeConverter(DefaultStreamConverterProperties.class).convert(stream));
                    return true;
                } catch (Exception e) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage(), e);
                    }
                    //加载yml配置文件
                    try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream((active == null ? "application" : ("application-" + active)) + ".yml")) {
                        this.getEnv().merge((Properties) this.getConverterContainer().getTypeConverter(DefaultStreamConverterProperties.class).convert(stream));
                        return true;
                    } catch (Exception ex) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug(e.getMessage(), e);
                        }
                    }
                }
            } else {
                //模块jar读取配置文件
                URL url = this.getUrlPath();
                try (InputStream stream = AssemblyUtil.getResourceStream(url, (active == null ? "application" : ("application-" + active)) + ".properties")) {
                    this.getEnv().merge((Properties) this.getConverterContainer().getTypeConverter(DefaultStreamConverterProperties.class).convert(stream));
                    return true;
                } catch (Exception e) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage(), e);
                    }
                    try (InputStream stream = AssemblyUtil.getResourceStream(url, (active == null ? "application" : ("application-" + active)) + ".yml")) {
                        this.getEnv().merge((Properties) this.getConverterContainer().getTypeConverter(DefaultStreamConverterProperties.class).convert(stream));
                        return true;
                    } catch (Exception ex) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug(ex.getMessage(), ex);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取模块包指定文件url路径
     *
     * @param filePath 文件路径
     * @return
     * @throws MalformedURLException
     */
    @Override
    public URL getUrlPath(String filePath) throws MalformedURLException {
        return new URL(this.getUrlPath() + "!/" + filePath);
//        return new URL(this.getUrlPath().getPath() + "!/" + filePath);
    }

    /**
     * 获取模块包url路径
     *
     * @return
     * @throws MalformedURLException
     */
    @Override
    public URL getUrlPath() throws MalformedURLException {
        return new URL("file:" + this.getHome().getSource().getPath());
//        return new URL("jar:" + this.home.getSource().toURL());
    }

    /**
     * 获取是否自动绑定
     *
     * @return
     */
    @Override
    public boolean isAutoBean() {
        //判断获取是否自动绑定
        IEnvironment env = null;
        synchronized (this.getBeanMap()) {
            if (this.getBeanMap().containsKey(IApplicationEnvironment.class.getName())) {
                env = (IEnvironment) this.getBeanMap().get(IApplicationEnvironment.class.getName()).getObject();
            }
        }
        if (env != null && env.containsKey(ApplicationConstant.Bean.AUTO_BEAN)) {
            return env.getBoolean(ApplicationConstant.Bean.AUTO_BEAN);
        }
        return false;
    }

    /**
     * 带构建参数的绑定对象
     *
     * @param c          绑定类型
     * @param parameters 数组绑定类型构建参数
     */
    @Override
    public <T> T addBean(@NotNull Class<?> c, @NotNull Object[] parameters) {
        Assert.notNull(c, "addBean null class error");
        Assert.notNull(c, "addBean null parameters error");
        //返回绑定对象
        return this.internalAddBean(null, c, parameters);
    }

    /**
     * @param name
     * @param c
     * @param parameters
     * @param <T>
     * @return
     */
    protected <T> T internalAddBean(String name, Class<?> c, Object[] parameters) {
        //类型注释处理
        //获取事件监听工厂容器接口
        IClassAnnotationBeanTargetHandle<ICoreInterface, Class<?>, T, String, Object> annotationBeanTargetHandle = this.classAnnotationBeanAchieve(name, c, parameters);
        //判断注释绑定是否已经处理构建对象
        if (annotationBeanTargetHandle.isHandle()) {
            //返回注释绑定对象
            return annotationBeanTargetHandle.getValue();
        }
        //类型处理
        //获取事件监听工厂容器接口
        IClassBeanTargetHandle<ICoreInterface, Class<?>, T, String, Object> classBeanTargetHandle = this.classBeanAchieve(name, c, parameters);
        //判断注释绑定是否已经处理构建对象
        if (classBeanTargetHandle.isHandle()) {
            //返回注释绑定对象
            return classBeanTargetHandle.getValue();
        }
        //没有注释的类型构建实例
        //创建类实例
        Object o;
        if (parameters == null) {
            o = this.newInstance(c);
        } else {
            o = this.newInstance(c, parameters);
        }
        //获取创建对象事件监听容器
        this.newInstanceObjectEvent(o);
        //对象注入
        this.injection(o);
        //获取绑定工厂容器接口
        this.beanEvent(o, name);
        //返回绑定对象
        return (T) o;
    }

    /**
     * 绑定事件
     *
     * @param o    绑定对象
     * @param name 绑定名称，如果未指定绑定名称侧为null
     */
    @Override
    public void beanEvent(Object o, String name) {
        //获取绑定工厂容器接口
        IBeanEventFactoryContainer container = this.getNullableBean(IBeanEventFactoryContainer.class);
        //判断是否指定使用绑定工厂容器进行绑定
        if (container == null || container.isEmpty()) {
            //使用默认绑定
            this.addBean(o);
        } else {
            //使用绑定工厂容器绑定
            container.loader(new ItemBeanTargetHandle(this, o, name));
        }
    }

    @Override
    public <T> T getNullableBeanInvoke(@NotNull String className, @NotNull String methodName, Object[] parameters)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        Object o = this.getNullableBean(className);
        if (o == null) {
            return null;
        }
        return ReflectUtil.invokeMethod(o, methodName, parameters);
    }

    @Override
    public <T> T getNullableBeanInvoke(@NotNull String className, @NotNull String methodName, Object parameter)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        Object o = this.getNullableBean(className);
        if (o == null) {
            return null;
        }
        if (parameter == null) {
            return ReflectUtil.invokeMethod(o, methodName, null);
        }
        return ReflectUtil.invokeMethod(o, methodName, new Object[]{parameter});
    }

    @Override
    public <T> T getNullableBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object[] parameters)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        Object o = this.getNullableBean(c);
        if (o == null) {
            return null;
        }
        return ReflectUtil.invokeMethod(o, methodName, parameters);
    }

    @Override
    public <T> T getNullableBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        Object o = this.getNullableBean(c);
        if (o == null) {
            return null;
        }
        if (parameter == null) {
            return ReflectUtil.invokeMethod(o, methodName, null);
        }
        return ReflectUtil.invokeMethod(o, methodName, new Object[]{parameter});
    }

    @Override
    public <T> T getBeanInvoke(@NotNull String className, @NotNull String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        return ReflectUtil.invokeMethod(this.getBean(className), methodName, parameters);
    }

    @Override
    public <T> T getBeanInvoke(@NotNull String className, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        if (parameter == null) {
            return ReflectUtil.invokeMethod(this.getBean(className), methodName, null);
        }
        return ReflectUtil.invokeMethod(this.getBean(className), methodName, new Object[]{parameter});
    }

    @Override
    public <T> T getBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        return ReflectUtil.invokeMethod(this.getBean(c), methodName, parameters);
    }

    @Override
    public <T> T getBeanInvoke(@NotNull Class<?> c, @NotNull String methodName, Object parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        if (parameter == null) {
            return ReflectUtil.invokeMethod(this.getBean(c), methodName, null);
        }
        return ReflectUtil.invokeMethod(this.getBean(c), methodName, new Object[]{parameter});
    }

    /**
     * 绑定对象事件
     *
     * @param o 绑定对象
     */
    @Override
    public void newInstanceObjectEvent(Object o) {
        //获取创建对象事件监听容器
        IObjectEventFactoryContainer container = this.getNullableBean(IObjectEventFactoryContainer.class);
        //新建对象前事件
        if (container == null || container.isEmpty()) {
            return;
        }
        container.loader(new BeanTargetHandle(this, o));
    }

    /**
     * @param name
     * @param <T>
     * @return
     */
    @Override
    public <T> T getNullableBean(@NotNull String name) {
        Assert.notNullOrEmpty(name, "getNullableBean null name error");
        IBeanDefinition definition = this.getBeanDefinition(name);
        if (definition == null) {
            return null;
        }
        return (T) definition.getObject();
    }

    /**
     * @param c
     * @param <T>
     * @return
     */
    @Override
    public <T> T getNullableBean(@NotNull Class<?> c) {
        Assert.notNull(c, "getNullableBean null class error");
        IBeanDefinition definition = this.getBeanDefinition(c);
        if (definition == null) {
            return null;
        }
        return (T) definition.getObject();
    }

    @Override
    public <T> T getAnnotationBean(@NotNull Class<? extends Annotation> a) {
        T t = BeanUtil.getAnnotationBean(this.beanMap, a);
        if (t == null) {
            throw new AnnotationNotBeanException(a.toString());
        }
        return t;
    }

    @Override
    public <T> List<T> getAnnotationBeanList(@NotNull Class<? extends Annotation> a) {
        return BeanUtil.getAnnotationBeanList(this.beanMap, a);
    }

    @Override
    public <T> T getAnnotationNullableBean(@NotNull Class<? extends Annotation> a) {
        return BeanUtil.getAnnotationBean(this.beanMap, a);
    }

    /**
     * @param name 绑定指定名称
     * @param o    绑定定义对象
     */
    @Override
    public void addBean(@NotNull String name, @NotNull Object o) {
        Assert.notNullOrEmpty(name, "addBean null name error");
        Assert.notNull(o, "addBean null object error");
        //创建绑定定义
        this.addBean(new BeanDefinition(name, o));
    }

    /**
     * 注入绑定对象
     *
     * @param o
     */
    @Override
    public void injection(@NotNull Object o) {
        Assert.notNull(o, "injection null object error");
        //获取参数或声明注入容器接口
        IInjectionContainer injectorContainer = this.getNullableBean(IInjectionContainer.class);
        //判断注入容器接口是否存在
        if (injectorContainer == null || injectorContainer.isEmpty()) {
            return;
        }
        //注入声明
        injectorContainer.injector(new InjectionObjectEventTargetHandle(this, o));
    }

    /**
     * 类型绑定实现
     *
     * @param name       绑定名称
     * @param c          创建类型
     * @param parameters 自定义参数
     * @param <T>
     * @return
     */
    protected <T> IClassBeanTargetHandle<ICoreInterface, Class<?>, T, String, Object> classBeanAchieve(String name, Class<?> c, Object[] parameters) {
        //创建处理目标事件对象
        IClassBeanTargetHandle<ICoreInterface, Class<?>, T, String, Object> eventTargetHandle = new ClassBeanTargetHandle(this, c, name, parameters);
        //获取事件监听工厂容器接口
        IClassBeanFactoryContainer container = this.getNullableBean(IClassBeanFactoryContainer.class);
        //判断是否有事件处理接口
        if (container != null) {
            //执行注释绑定事件
            container.loader(eventTargetHandle);
        }
        return eventTargetHandle;
    }

    /**
     * 类型注释绑定实现
     * @param name       绑定名称
     * @param c          创建类型
     * @param parameters 自定义参数
     * @param <T>
     * @return
     */
    protected <T> IClassAnnotationBeanTargetHandle<ICoreInterface, Class<?>, T, String, Object> classAnnotationBeanAchieve(String name, Class<?> c, Object[] parameters) {
        //创建处理目标事件对象
        IClassAnnotationBeanTargetHandle<ICoreInterface, Class<?>, T, String, Object> eventTargetHandle = new ClassAnnotationBeanTargetHandle(this, c, name, parameters);
        //获取事件监听工厂容器接口
        IClassAnnotationBeanFactoryContainer container = this.getNullableBean(IClassAnnotationBeanFactoryContainer.class);
        //判断是否有事件处理接口
        if (container != null) {
            //执行注释绑定事件
            container.loader(eventTargetHandle);
        }
        return eventTargetHandle;
    }

    /**
     * 带构建参数的绑定对象
     *
     * @param name       绑定名称
     * @param c          绑定类型
     * @param parameters 数组绑定类型构建参数
     * @param <T>
     * @return
     */
    @NotNull
    @Override
    public <T> T addBean(@NotNull String name, @NotNull Class<?> c, @NotNull Object[] parameters) {
        Assert.notNullOrEmpty(name, "addBean null name error");
        Assert.notNull(c, "addBean null class error");
        //返回绑定对象
        return this.internalAddBean(name, c, parameters);
    }

    /**
     * 添加绑定类型
     *
     * @param c   绑定类型
     * @param <T> 返回类型
     * @return 返回添加绑定对象
     */
    @Override
    public <T> T addBean(@NotNull Class<?> c) {
        Assert.notNull(c, "addBean null class error");
        //返回构建对象
        return this.internalAddBean(null, c, null);
    }

    /**
     * @param definition 绑定定义对象
     */
    @Override
    public void addBean(@NotNull IBeanDefinition definition) {
        Assert.notNull(definition, "addBean null definition error");
        try {
            //锁定同步
            synchronized (this.beanMap) {
                //判断模块添加
                if (definition.getObject() instanceof IModule) {
                    //添加绑定模块
                    this.beanMap.put(((IGetName) definition.getObject()).getName(), definition);
                    //判断绑定插件
                } else {
                    this.beanMap.put(definition.getName(), definition);
                }
            }
            //获取绑定事件容器
//            IOperatingBeanEventFactoryContainer operatingBean = this.getNullableBean(IOperatingBeanEventFactoryContainer.class);
//            if (operatingBean != null) {
//                operatingBean.loader(new OperatingBeanEventTargetHandle(this, definition));
//            }
            //绑定完成，处理函数注释事件工厂容器
            //处理注释有 Bean 绑定函数
            MethodAnnotationBeanFactoryContainer container = this.getNullableBean(MethodAnnotationBeanFactoryContainer.class);
            if (container != null) {
                container.loader(new MethodEventTargetHandle(this, definition.getObject()));
            }
            this.log.info("put Bean:" + definition.toString());
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 添加指定名称构建类型绑定
     *
     * @param name 绑定名称
     * @param c    绑定类型
     * @param <T>  返回类型
     * @return 返回绑定对象
     */
    @Override
    public <T> T addBean(@NotNull String name, @NotNull Class<?> c) {
        Assert.notNullOrEmpty(name, "addBean null name error");
        Assert.notNull(c, "addBean null class error");
        //返回绑定对象
        return this.internalAddBean(name, c, null);
    }

    /**
     * @param name 绑定id
     * @return
     */
    @Nullable
    @Override
    public IBeanDefinition getBeanDefinition(@NotNull String name) {
        Assert.notNullOrEmpty(name, "getBeanDefinition null name error");
        return BeanUtil.getBeanDefinition(this.beanMap, name, (ClassLoader) this.getClassLoader());
    }

    /**
     * @param c 绑定类型
     * @return
     */
    @Nullable
    @Override
    public IBeanDefinition getBeanDefinition(@NotNull Class<?> c) {
        Assert.notNull(c, "getBeanDefinition null class error");
        //获取绑定名称注释定义
//        if (c.isAnnotationPresent(Named.class)) {
//            return BeanUtil.getNameBeanDefinition(this.beanMap, c.getAnnotation(Named.class).value());
//        }
        if (c.isInterface()) {
            //判断是否跨类加载器获取绑定对象
            return BeanUtil.getInterfaceBeanDefinition(this.beanMap, c);
//            if (c.getClassLoader().equals(this.getClass().getClassLoader())) {
//                return BeanUtil.getInterfaceBeanDefinition(this.beanMap, c);
//            } else {
//                return BeanUtil.getInterfaceBeanDefinition(this.beanMap, this.forName(c.getName()));
//            }
        }
        return BeanUtil.getBeanDefinition(this.beanMap, c.getName(), (ClassLoader) this.getClassLoader());
    }

    /**
     * @param name 绑定name
     */
    @Override
    public void removeBean(@NotNull String name) {
        Assert.notNullOrEmpty(name, "removeBean null name error");
        //获取要删除的绑定定义对象
        IBeanDefinition definition = this.getBeanDefinition(name);
        //如果绑定定义无效直接退出
        if(definition == null){
            return;
        }
        this.removeBean(this.getBeanDefinition(name));
    }

    /**
     * @param definition 绑定定义
     */
    @Override
    public void removeBean(@NotNull IBeanDefinition definition) {
        Assert.notNull(definition, "removeBean null definition error");
        //锁定同步
        synchronized (this.beanMap) {
            this.beanMap.remove(definition.getName());
        }
        //卸载前事件
        this.getBean(IOperatingBeanEventFactoryContainer.class).unloader(new OperatingBeanEventTargetHandle(this, definition));
        this.closeDefinition(definition);
    }

    @Override
    public <T> Collection<T> getBeans(Class<T> c) {
        return null;
    }

    /**
     * 释放资源
     *
     * @param d
     */
    protected void closeDefinition(IBeanDefinition d) {
        //判断绑定定义对象是否需要释放资源
        if (d != null && d.getObject() != null) {
            //
            if (d.getObject() instanceof Closeable) {
                try {
                    ((Closeable) d.getObject()).close();
                } catch (Exception e) {
                    this.log.error(e.getMessage(), e);
                    ExceptionUtil.debugOrError(this.log, e);
                }
                return;
            }
            //
            if (d.getObject() instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) d.getObject()).close();
                } catch (Exception e) {
                    this.log.error(e.getMessage(), e);
                    ExceptionUtil.debugOrError(this.log, e);
                }
            }
        }
    }

    /**
     * @param c 绑定类型
     */
    @Override
    public void removeBean(@NotNull Class<?> c) {
        Assert.notNull(c, "removeBean null class error");
        this.removeBean(this.getBeanDefinition(c));
    }

    /**
     * @param name 绑定id，如果没有指定绑定id侧为绑定类的全称
     * @param <T>
     * @return
     */
    @NotNull
    @Override
    public <T> T getBean(@NotNull String name) {
        Assert.notNullOrEmpty(name, "getBean null name error");
        return (T) this.getBeanDefinition(name).getObject();
    }

    /**
     * @param o 绑定对象
     */
    @Override
    public void addBean(@NotNull Object o) {
        Assert.notNull(o, "addBean null object error");
        //创建绑定定义
        this.addBean(new BeanDefinition(o));
    }

    /**
     * @param o 绑定对象
     */
    @Override
    public void removeBean(@NotNull Object o) {
        Assert.notNull(o, "removeBean null object error");
//        this.removeBean(new BeanDefinition(o));
    }

    /**
     * @param c 绑定类型
     * @return
     */
    @Override
    public boolean containsBean(@NotNull Class<?> c) {
        Assert.notNull(c, "beanContains null class error");
        return BeanUtil.beanContains(this.beanMap, c);
    }

    /**
     * @param name 绑定id
     * @return
     */
    @Override
    public boolean containsBean(@NotNull String name) {
        Assert.notNullOrEmpty(name, "beanContains null name error");
        return BeanUtil.beanContains(this.beanMap, name);
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean containsBean(@NotNull Object obj) {
        Assert.notNull(obj, "beanContains null object error");
        return BeanUtil.beanContains(this.beanMap, obj);
    }

    /**
     * @param c   绑定类型
     * @param <T>
     * @return
     */
    @NotNull
    @Override
    public <T> T getBean(@NotNull Class<T> c) {
        Assert.notNull(c, "getBean null class error");
        try {
            //判断是否自动绑定类型
            if (this.isAutoBean()) {
                IBeanDefinition definition = null;
                synchronized (this.beanMap) {
                    definition = this.getBeanDefinition(c);
                }
                if (definition == null) {
                    return this.addBean(c);
                }
                return (T) this.getBeanDefinition(c).getObject();
            }
            return (T) this.getBeanDefinition(c).getObject();
        } catch (NullPointerException e) {
            this.log.error(e.getMessage(), e);
            throw new NullPointerException(c.getName());
        }
    }

    /**
     * @param className 构建类名称
     * @return
     */
    @NotNull
    @Override
    public Object newInstance(@NotNull String className) {
        Assert.notNullOrEmpty(className, "newInstance null name error");
        try {
            return this.newInstance(((ClassLoader) this.getClassLoader()).loadClass(className));
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
            throw new InstanceException(className, e);
        }
    }

    /**
     * 构建后注入
     * @param c 构建类型
     * @return
     */
    @NotNull
    @Override
    public Object newInstanceInjection(@NotNull Class<?> c) {
        Assert.notNull(c, "newInstanceInjection null class error");
        Object o = this.newInstance(c);
        this.injection(o);
        return o;
    }

    /**
     * @param c 析构类型
     * @return
     */
    @NotNull
    @Override
    public Object newInstance(@NotNull Class<?> c) {
        Assert.notNull(c, "newInstance null class error");
        Object o = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
            try {
                o = c.newInstance();
                this.log.debug("newInstance>class:" + c.getName());
            } catch (Exception e) {
                if (this.log.isDebugEnabled()) {
                    e.printStackTrace();
                }
                this.log.error(e.getMessage(), e);
                throw new InstanceNullException(c.getName(), e);
            }
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            o = this.newInstance(constructors[0]);
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    o = this.newInstance(constructor);
                    break;
                }
            }
        }
        if (o == null) {
            //该类没有实例化错误
            throw new InstanceNullException(c.getName());
        }
        //返回创建对象
        return o;
    }

    /**
     * @param constructor 析构者
     * @return
     */
    @NotNull
    @Override
    public Object newInstance(@NotNull Constructor constructor) {
        Assert.notNull(constructor, "newInstance null constructor error");
        try {
            //有一个构建函数实例类对象
            if (constructor.getParameterCount() == 0) {
                //没有参数构建类
                if (this.log.isDebugEnabled()) {
                    Object o = constructor.newInstance();
                    this.log.debug("newInstance>class:" + constructor.getDeclaringClass().getName());
                    return o;
                } else {
                    return constructor.newInstance();
                }
            }
            //判断是否为调试模式
            if (this.log.isDebugEnabled()) {
                //有参数构建类
                Object o = constructor.newInstance(this.newInstanceParameters(constructor));
                this.log.debug("newInstance>class:" + constructor.getDeclaringClass().getName() + ">parameters(" + ReflectUtil.getLogParameters(constructor.getParameters()) + ")");
                return o;
            } else {
                return constructor.newInstance(this.newInstanceParameters(constructor));
            }
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
            throw new InjectionConstructorException(constructor.getName());
        }
    }

    /**
     * @param constructor 析构者
     * @return
     */
    @Nullable
    @Override
    public Object[] newInstanceParameters(@NotNull Constructor constructor) {
        Assert.notNull(constructor, "newInstanceParameters null constructor error");
        if (constructor.getParameterCount() == 0) {
            return null;
        }
        //获取参数列表
        return this.newInstanceTargetParameters(null, constructor, null, constructor.getParameters());
    }

    /**
     * 默认新建构建参数
     *
     * @param c
     * @return
     */
    private Object defaultNewInstanceParameter(Class<?> c) {
        //没有注入处理，在此简单注入处理
        if (IApplication.class.isAssignableFrom(c)) {
            //判断本身是否为应用
            if (this instanceof IApplication) {
                return this;
            }
            //判断本身是否为模块
            if (this instanceof IModule) {
                //为模块时获取绑定的应用接口
                return this.getBean(IApplication.class);
            }
        }
        if (IModule.class.isAssignableFrom(c)) {
            //判断本身是否为模块
            if (this instanceof IModule) {
                //为模块时获取绑定的应用接口
                return this;
            }
        }
        if (ICoreInterface.class.isAssignableFrom(c)) {
            return this;
        }
        if (IBean.class.isAssignableFrom(c)) {
            return this;
        }
        return null;
    }

    /**
     * @param target
     * @param constructor
     * @param method
     * @param parameters
     * @return
     */
    @Nullable
    @Override
    public Object[] newInstanceTargetParameters(@NotNull Object target, @Nullable Constructor constructor, @Nullable Method method, @NotNull Parameter[] parameters) {
        Assert.notNull(parameters, "newInstanceParameters null parameters error");
        //获取基础类型转换器工厂
//        ConverterPrimitive primitiveFactory = this.getApp().getNullableBean(ConverterPrimitive.class);
        Object[] objects = new Object[parameters.length];
        int i = 0;
        //获取注释事件工厂容器接口
        ParameterInjectionContainer parameterContainer = this.getNullableBean(ParameterInjectionContainer.class);
        //遍历参数列表
        for (Parameter p : parameters) {
            //判断是否有注入处理
            if (parameterContainer == null || parameterContainer.isEmpty()) {
                //默认新建注入
                objects[i] = this.defaultNewInstanceParameter(p.getType());
                i++;
                continue;
            }
            //创建参数事件目标处理接口
            IParameterInjectionTargetHandle<ICoreInterface, Object, Constructor, Method, Parameter, Object> parameterEventTargetHandle
                    = new ParameterInjectionTargetHandle(this, target, p);
            //获取事件工厂操作
            parameterContainer.injector(parameterEventTargetHandle);
            ConverterContainer container = this.getNullableBean(ConverterContainer.class);
            //判断是否为基础类型需要转换类型
            if (container != null && p.getType().isPrimitive()) {
                //转换值
                //获取注入完成值
                objects[i] = container.getTargetTypeConverter(p.getType()).convert(parameterEventTargetHandle.getValue());
            } else {
                //获取事件工厂获取的参数对象
                objects[i] = parameterEventTargetHandle.getValue();
            }
            i++;
        }
        return objects;
    }

    /**
     * @param parameter
     * @param target
     * @return
     */
    @Nullable
    @Override
    public Object newInstanceParameter(@NotNull Object target, @NotNull Parameter parameter) {
        Assert.notNull(parameter, "newInstanceParameter null parameter error");
        //创建参数事件目标处理接口
        IParameterInjectionTargetHandle<ICoreInterface, Object, Constructor, Method, Parameter, Object> parameterEventTargetHandle
                = new ParameterInjectionTargetHandle(this, target, parameter);
        //获取事件工厂操作
        this.getBean(IInjectionContainer.class).injector(parameterEventTargetHandle);
        //获取事件工厂获取的参数对象
        return parameterEventTargetHandle.getValue();
    }

    /**
     * @param target
     * @param method
     * @return
     */
    @Nullable
    @Override
    public Object[] newInstanceParameters(@NotNull Object target, @NotNull Method method) {
        Assert.notNull(method, "newInstanceParameters null method error");
        if (method.getParameterCount() == 0) {
            return null;
        }
        //获取参数列表
        return this.newInstanceTargetParameters(target, null, method, method.getParameters());
    }

    /**
     * 获取自定义参数析构函数
     *
     * @param className  析构类型名称
     * @param parameters 指定析构参数
     * @return 返回析构对象
     */
    @NotNull
    @Override
    public Object newInstance(String className, Object[] parameters) {
        Assert.notNullOrEmpty(className, "newInstance null className error");
        Assert.notNull(parameters, "newInstance null parameters error");
        return this.newInstanceParameters(this.forName(className), parameters);
    }

    /**
     * @param c          析构类型
     * @param parameters 指定析构参数
     * @return
     */
    @NotNull
    @Override
    public Object newInstance(@NotNull Class<?> c, @NotNull Object[] parameters) {
        Assert.notNull(c, "newInstance null class error");
        Assert.notNull(parameters, "newInstance null parameters error");
        Object o = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
            try {
                o = c.newInstance();
                if (this.log.isDebugEnabled()) {
                    this.log.debug("newInstance>class:" + c.getName());
                }
            } catch (Exception e) {
                this.log.error(e.getMessage(), e);
                throw new InstanceClassException(e, c);
            }
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            o = this.newInstanceParameters(constructors[0], parameters);
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    o = this.newInstanceParameters(constructor, parameters);
                    break;
                }
            }
        }
        if (o == null) {
            //该类没有实例化错误
            throw new RuntimeException(new InstantiationException(c.getName()));
        }
        return o;
    }

    /**
     * @param constructor 析构者
     * @param parameters  数组自定义参数
     * @return
     */
    @NotNull
    @Override
    public Object newInstanceParameters(@NotNull Constructor constructor, @NotNull Object[] parameters) {
        Assert.notNull(constructor, "newInstanceParameters null constructor error");
        Assert.notNull(parameters, "newInstanceParameters null parameters error");
        try {
            //有一个构建函数实例类对象
            if (constructor.getParameterCount() == 0/* && parameters != null && parameters.length > 0*/) {
                //没有参数构建类
                throw new IllegalArgumentException("newInstanceParameters");
            }
            if (constructor.getParameterCount() == 0) {
                //判断是否为调试模式
                if (this.log.isDebugEnabled()) {
                    Object o = constructor.newInstance();
                    this.log.debug("newInstance>class:" + constructor.getDeclaringClass().getName());
                    return o;
                } else {
                    return constructor.newInstance();
                }
            }
            //有参数构建类
            return constructor.newInstance(this.newInstanceCustomParameters(constructor, parameters));
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
            throw new InjectionClassConstructorException(e, constructor);
        }
    }

    /**
     * @param constructor 析构者
     * @param parameters  数组自定义参数
     * @return
     */
    @Nullable
    @Override
    public Object[] newInstanceCustomParameters(@NotNull Constructor constructor, @NotNull Object[] parameters) {
        Assert.notNull(constructor, "newInstanceCustomParameters null constructor error");
        Assert.notNull(parameters, "newInstanceCustomParameters null parameters error");
        ParameterInjectionContainer parameterContainer = this.getBean(ParameterInjectionContainer.class);
        Object[] objects = new Object[constructor.getParameterCount()];
        int i = 0;
        int n = 0;
        for (Parameter p : constructor.getParameters()) {
            //判断没有注释的参数处理
            if (p.getAnnotations().length > 0) {
                //判断是否有注入处理
                if (parameterContainer.isEmpty()) {
                    //默认新建注入
                    objects[i] = this.defaultNewInstanceParameter(p.getType());
                    i++;
                    continue;
                }
                //创建参数事件目标处理接口
                IParameterInjectionTargetHandle<ICoreInterface, Object, Constructor, Method, Parameter, Object> parameterEventTargetHandle
                        = new ParameterInjectionTargetHandle(this, null, p);
                //获取事件工厂操作
                parameterContainer.injector(parameterEventTargetHandle);
                //获取事件工厂获取的参数对象
                objects[i] = parameterEventTargetHandle.getValue();
            } else {
                objects[i] = parameters[n];
                n++;
            }
            i++;
        }
        return objects;
    }

    /**
     * @param constructor
     * @return
     */
    @Override
    public Map<Class<?>, Object> newInstanceMapParameters(Constructor constructor) {
        Assert.notNull(constructor, "newInstanceMapParameters null constructor error");
        if (constructor.getParameterCount() == 0) {
            return null;
        }
        //获取参数列表
        return this.newInstanceMapParameters(constructor.getParameters());
    }

    /**
     * @param parameters
     * @return
     */
    @Override
    public Map<Class<?>, Object> newInstanceMapParameters(@Nullable Parameter[] parameters) {
        if (parameters == null) {
            return null;
        }
        Map<Class<?>, Object> map = new HashMap<>();
        ParameterInjectionContainer parameterContainer = this.getNullableBean(ParameterInjectionContainer.class);
        for (Parameter p : parameters) {
            //判断没有注释的参数处理
            //判断是否有注入处理
            if (parameterContainer == null || parameterContainer.isEmpty()) {
                //默认新建注入
                map.put(p.getType(), this.defaultNewInstanceParameter(p.getType()));
                continue;
            }
            //创建参数事件目标处理接口
            IParameterInjectionTargetHandle<ICoreInterface, Object, Constructor, Method, Parameter, Object> parameterEventTargetHandle
                    = new ParameterInjectionTargetHandle(this, null, p);
            //获取事件工厂操作
            parameterContainer.injector(parameterEventTargetHandle);
            //获取事件工厂获取的参数对象
            map.put(p.getType(), parameterEventTargetHandle.getValue());
        }
        return map;
    }

    /**
     * 获取类型构建参数
     *
     * @param c 要获取构建参数的类型
     * @return 返回构建参数类型与值
     */
    @Override
    public Map<Class<?>, Object> newInstanceMapParameters(@NotNull Class<?> c) {
        Assert.notNull(c, "newInstanceMapParameters null class error");
        //返回数组构建参数
        Map<Class<?>, Object> parameters = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            parameters = this.newInstanceMapParameters(constructors[0]);
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    parameters = this.newInstanceMapParameters(constructor);
                    break;
                }
            }
        }
        //返回构建参数
        return parameters;
    }

    /**
     * @param c 构建类型
     * @return
     */
    @Nullable
    @Override
    public Object[] newInstanceParameters(@NotNull Class<?> c) {
        Assert.notNull(c, "newInstanceParameters null class error");
        //返回数组构建参数
        Object[] parameters = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            parameters = this.newInstanceParameters(constructors[0]);
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    parameters = this.newInstanceParameters(constructor);
                    break;
                }
            }
        }
        //返回构建参数
        return parameters;
    }

    /**
     * @param c          构建类型
     * @param parameters 数组自定义参数
     * @return 返回代理构建对象
     */
    @Nullable
    @Override
    public Object[] newInstanceParameters(@NotNull Class<?> c, @NotNull Object[] parameters) {
        Assert.notNull(c, "newInstanceParameters null class error");
        Object[] objects = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
            throw new NewInstanceNotConstructorsException(c.getName(), parameters);
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            objects = this.newInstanceCustomParameters(constructors[0], parameters);
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    objects = this.newInstanceCustomParameters(constructor, parameters);
                    break;
                }
            }
        }
        return objects;
    }
    /**
     * @param c        构建类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler 代理回调对象
     * @param <T>       返回类型
     * @return 返回代理构建对象
     */
    @NotNull
    @Override
    public <T> T newCglibInstance(@NotNull Class<?> c, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler) {
        Assert.notNull(c, "newCglibInstance null class error");
        Assert.notNull(c, "newCglibInstance null handler error");
        T o = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
            try {
                o = this.createCglibEnhancer(c, interfaces, handler);
            } catch (Exception e) {
                this.log.error(e.getMessage(), e);
                throw new CglibInstanceNullException(c.getName(), e);
            }
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            o = this.newCglibInstance(constructors[0], interfaces, handler);
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    o = this.newCglibInstance(constructor, interfaces, handler);
                    break;
                }
            }
        }
        if (o == null) {
            //该类没有实例化错误
            throw new CglibInstanceNullException(c.getName());
        }
        //返回创建对象
        return o;
    }

    /**
     * @param c          构建类型
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler   代理回调对象
     * @param parameters 指定构建参数
     * @param <T>       返回类型
     * @return 返回代理构建对象
     */
    @NotNull
    @Override
    public <T> T newCglibInstance(@NotNull Class<?> c, @Nullable Class<?>[] interfaces,
                                  @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) {
        Assert.notNull(c, "newCglibInstance null class error");
        Assert.notNull(handler, "newCglibInstance null handler error");
        Assert.notNull(parameters, "newCglibInstance null parameters error");
        T o = null;
        //获取类构建函数列表
        Constructor[] constructors = c.getConstructors();
        //判断是否有函数构建函数
        if (constructors == null || constructors.length == 0) {
            //没有构建函数
            try {
                o = this.createCglibEnhancer(c, interfaces, handler);
            } catch (Exception e) {
                this.log.error(e.getMessage(), e);
                throw new CglibInstanceClassException(c.getName(), e);
            }
        } else if (constructors.length == 1) {
            //有一个构建函数实例类对象
            o = this.newCglibInstance(constructors[0], interfaces, handler, this.newInstanceCustomParameters(constructors[0], parameters));
        } else {
            //遍历构建函数列表
            for (Constructor constructor : constructors) {
                //判断是否注释了入口函数
                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
                    //指定入口函数构建对象
                    o = this.newCglibInstance(constructor, interfaces, handler, this.newInstanceCustomParameters(constructor, parameters));
                    break;
                }
            }
        }
        if (o == null) {
            //该类没有实例化错误
            throw new CglibInstanceConstructorException(c.getName());
        }
        return o;
    }
    /**
     *
     * @param constructor 构建对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param parameters  指定构建参数
     * @param <T>       返回类型
     * @return 返回代理构建对象
     */
    @NotNull
    @Override
    public <T> T newCglibInstance(@NotNull Constructor constructor, @Nullable Class<?>[] interfaces,
                                  @NotNull net.sf.cglib.proxy.InvocationHandler handler, @NotNull Object[] parameters) {
        Assert.notNull(constructor, "newCglibInstance null constructor error");
        Assert.notNull(handler, "newCglibInstance null handler error");
        Assert.notNull(parameters, "newCglibInstance null parameters error");
        //初始化构建类型参数
        Class[] argumentTypes = new Class[parameters.length];
        //遍历构建参数
        int i = 0;
        for (Object parameter : parameters) {
            //判断构建参数是否为空
            if (parameter == null) {
                //空参数
                argumentTypes[i] = null;
            } else {
                //有对象参数
                argumentTypes[i] = parameter.getClass();
            }
            i++;
        }
        //使用参数构建类型
        return this.createCglibEnhancer(constructor.getDeclaringClass(), interfaces, handler, argumentTypes, parameters);
    }

    /**
     *
     * @param constructor 构建对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler    代理回调对象
     * @param <T>       返回类型
     * @return 返回代理构建对象
     */
    @NotNull
    @Override
    public <T> T newCglibInstance(@NotNull Constructor constructor, @Nullable Class<?>[] interfaces, @NotNull net.sf.cglib.proxy.InvocationHandler handler) {
        Assert.notNull(constructor, "newCglibInstance null constructor error");
        Assert.notNull(handler, "newCglibInstance null handler error");
        try {
            //有一个构建函数实例类对象
            if (constructor.getParameterCount() == 0) {
                //没有参数构建类
                return this.createCglibEnhancer(constructor.getDeclaringClass(), interfaces, handler);
            }
            //获取构建参数列表
            Map<Class<?>, Object> map = this.newInstanceMapParameters(constructor);
            //初始化构建参数
            Object[] parameters = new Object[map.size()];
            //初始化构建参数类型
            Class[] argumentTypes = new Class[parameters.length];
            //遍历构建参数
            int i = 0;
            for (Map.Entry<Class<?>, Object> parameter : map.entrySet()) {
                //判断构建参数是否为空
                if (parameter.getValue() == null) {
                    //参数值
                    parameters[i] = null;
                    //参数类型
                    argumentTypes[i] = null;
                } else {
                    //参数值
                    parameters[i] = parameter.getValue();
                    //参数类型
                    argumentTypes[i] = parameter.getKey();
                }
                i++;
            }
            //使用参数构建类型
            return this.createCglibEnhancer(constructor.getDeclaringClass(), interfaces, handler, argumentTypes, parameters);
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
            throw new CglibInstanceConstructorException(constructor.getName());
        }
    }
    /**
     * 查询指定注释的函数并调用返回值
     *
     * @param target     调用注释函数对象
     * @param annotation 注释类型
     * @param <T>
     * @return
     */
    @Override
    public <T> T findAnnotationMethodInvoke(Object target, Class<? extends Annotation> annotation) {
        try {
            //获取注释函数
            Method method = ReflectUtil.findMethod(target.getClass(), annotation);
            //调用注释函数并且返回值
            return (T) method.invoke(target, this.newInstanceParameters(target, method));
        } catch (Exception e) {
            if (this.log.isDebugEnabled()) {
                e.printStackTrace();
                this.log.debug(e.getMessage(), e);
            } else {
                this.log.error(e.getMessage(), e);
            }
            throw new AnnotationMethodInvokeException(e);
        }
    }
}