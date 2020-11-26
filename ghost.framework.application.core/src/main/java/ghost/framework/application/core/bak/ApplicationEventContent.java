//package ghost.framework.app.core;
//
//import ghost.framework.app.context.ApplicationEvent;
//import ghost.framework.context.bean.IBeanDefinition;
//import ghost.framework.context.env.IEnvironment;
//import ghost.framework.core.event.bean.BeanEventListenerContainer;
//import ghost.framework.core.event.bean.IBeanEventListenerContainer;
//import ghost.framework.core.event.classs.ClassEventListenerContainer;
//import ghost.framework.core.event.env.EnvEventListenersContainer;
//import ghost.framework.core.event.jar.IJarEventListenerContainer;
//import ghost.framework.core.event.jar.JarEventListenerContainer;
//import ghost.framework.core.event.maven.MavenEventListenerContainer;
//import ghost.framework.core.event.obj.IObjectEventListenerContainer;
//import ghost.framework.core.event.obj.ObjectEventListenerContainer;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用事件基础类
// * @Date: 1:24 2019-05-28
// */
//abstract class ApplicationEventContent extends ApplicationResourceContent implements ApplicationEvent {
////
////    //-----------------------------IApplicationModuleObjectEventListenerContainer----------------------------//
////    @Override
////    public IBeanEventListenerContainer<Object, IBeanDefinition> getBeanEventListenerFactoryContainer() {
////        return this.getBean(IBeanEventListenerContainer.class);
////    }
////
////    /**
////     * 获取应用模块对象事件监听容器
////     *
////     * @return
////     */
////    @Override
////    public IObjectEventListenerContainer<Object> getObjectEventListenerFactoryContainer() {
////        return this.getBean(IObjectEventListenerContainer.class);
////    }
//
////    /**
////     * 注册对象
////     *
////     * @param content 模块对象
////     * @param value   对象
////     */
////    public void registrarObjectEvent(IModuleContent content, Object value) {
////        this.getModuleObjectEventListenerContainer().registrarObjectEvent(content, value);
////    }
////
////    /**
////     * 注册对象列表
////     *
////     * @param content 模块对象
////     * @param values  对象列表
////     */
////    public void registrarObjectsEvent(IModuleContent content, Object[] values) {
////        this.getModuleObjectEventListenerContainer().registrarObjectsEvent(content, values);
////    }
////
////    /**
////     * 注册对象列表
////     *
////     * @param content 模块对象
////     * @param values  对象列表
////     */
////    public void registrarObjectsEvent(IModuleContent content, List<Object> values) {
////        this.getModuleObjectEventListenerContainer().registrarObjectsEvent(content, values);
////    }
//    //-----------------------------IApplicationModuleObjectEventListenerContainer----------------------------//
//    //-----------------------------IApplicationJarLoaderEventListenerContainer----------------------------//
//
////    /**
////     * 获取应用jar加载事件监听容器
////     *
////     * @return
////     */
////    @Override
////    public IApplicationJarEventListenerContainer<Object> getJarEventListenerFactoryContainer() {
////        return this.getBean(IApplicationJarEventListenerContainer.class);
////    }
////
////    /**
////     * 加载dev包
////     *
////     * @param entry 包对象
////     */
////    public void loadClasssLoader(Map.Entry<CodeSource, ProtectionDomain> entry) {
////        this.getJarLoaderEventListenerContainer().loadClasssLoader(entry);
////    }
////
////    /**
////     * 卸载dev包
////     *
////     * @param entry 包对象
////     */
////    public void uninstallClasssLoader(Map.Entry<CodeSource, ProtectionDomain> entry) {
////        this.getJarLoaderEventListenerContainer().uninstallClasssLoader(entry);
////    }
////
////    /**
////     * 加载jar包
////     *
////     * @param entry 包对象
////     */
////    public void loadJarLoader(JarEntry entry) {
////        this.getJarLoaderEventListenerContainer().loadJarLoader(entry);
////    }
////
////    /**
////     * 卸载jar包
////     *
////     * @param entry 包对象
////     */
////    public void uninstallJarLoader(JarEntry entry) {
////        this.getJarLoaderEventListenerContainer().uninstallJarLoader(entry);
////    }
////
////    /**
////     * 加载jar包
////     *
////     * @param appPackages 运行app包列表
////     * @param appClasss   运行app类列表
////     * @param entry       包对象
////     */
////    public void loadJarLoader(HashMap<String, Package> appPackages, ConcurrentHashMap<String, Object> appClasss, Map.Entry<CodeSource, ProtectionDomain> entry) {
////        this.getJarLoaderEventListenerContainer().loadJarLoader(appPackages, appClasss, entry);
////    }
////
////    /**
////     * 卸载jar包
////     *
////     * @param appPackages 运行app包列表
////     * @param appClasss   运行app类列表
////     * @param entry       包对象
////     */
////    public void uninstallJarLoader(HashMap<String, Package> appPackages, ConcurrentHashMap<String, Object> appClasss, Map.Entry<CodeSource, ProtectionDomain> entry) {
////        this.getJarLoaderEventListenerContainer().uninstallJarLoader(appPackages, appClasss, entry);
////    }
//    //-----------------------------IApplicationJarLoaderEventListenerContainer----------------------------//
//    //-----------------------------IApplicationModuleEnvEventListenerContainer----------------------------//
//
////    /**
////     * 获取应用模块env事件监听容器
////     *
////     * @return
////     */
////    @Override
////    public IEnvEventListenerContainer<Object, IEnvironment> getEnvEventListenerContainer() {
////        return this.getBean(IEnvEventListenerContainer.class);
////    }
//
////    /**
////     * 清除模块env内容\
////     *
////     * @param content
////     */
////    public void moduleEnvClear(IModuleContent content) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvClear(content);
////    }
////
////    /**
////     * 添加模块env内容
////     *
////     * @param content 模块内容
////     * @param value     键
////     * @param v       值
////     */
////    public void moduleEnvAdd(IModuleContent content, String value, Object v) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvAdd(content, value, v);
////    }
////
////    /**
////     * 删除模块env内容
////     *
////     * @param content 模块内容
////     * @param value     键
////     */
////    public void moduleEnvRemove(IModuleContent content, String value) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvRemove(content, value);
////    }
////
////    /**
////     * 模块env更改后
////     *
////     * @param content 模块内容
////     * @param value     键
////     * @param v       值
////     */
////    public void moduleEnvChangeAfter(IModuleContent content, String value, Object v) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvChangeAfter(content, value, v);
////    }
////
////    /**
////     * 模块env更改前
////     *
////     * @param content 模块内容
////     * @param value     键
////     * @param v       值
////     */
////    public void moduleEnvChangeBefore(IModuleContent content, String value, Object v) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvChangeBefore(content, value, v);
////    }
////
////    /**
////     * 模块env合并后
////     *
////     * @param content    模块内容
////     * @param prefix     前缀
////     * @param properties 配置
////     */
////    public void moduleEnvMergeAfter(IModuleContent content, String prefix, Properties properties) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvMergeAfter(content, prefix, properties);
////    }
////
////    /**
////     * 模块env合并前
////     *
////     * @param content    模块内容
////     * @param prefix     前缀
////     * @param properties 配置
////     */
////    public void moduleEnvMergeBefore(IModuleContent content, String prefix, Properties properties) {
////        this.getBean(IApplicationModuleEnvEventListenerContainer.class).moduleEnvMergeBefore(content, prefix, properties);
////    }
//    //-----------------------------IApplicationModuleEnvEventListenerContainer----------------------------//
//    //-----------------------------IApplicationModuleEventListenerContainer----------------------------//
//
////    /**
////     * 获取模块事件监听容器
////     *
////     * @return
////     */
////    @Override
////    public IApplicationModuleEventListenerContainer getModuleEventListenerContainer() {
////        return this.getBean(IApplicationModuleEventListenerContainer.class);
////    }
//
////    /**
////     * 模块加载后事件
////     *
////     * @param content 模块对象
////     */
////    public void moduleLoaderAfter(IModuleContent content) {
////        this.getBean(IApplicationModuleEventListenerContainer.class).moduleLoaderAfter(content);
////    }
////
////    /**
////     * 模块加载前事件
////     *
////     * @param content       模块对象
////     * @param loaderContent 模块加载内容
////     */
////    public void moduleLoaderBefore(IModuleContent content, IModuleLoaderContent loaderContent) {
////        this.getBean(IApplicationModuleEventListenerContainer.class).moduleLoaderBefore(content, loaderContent);
////    }
////
////    /**
////     * 卸载模块前事件
////     *
////     * @param content 模块对象
////     */
////    public void uninstallModuleBefore(IModuleContent content) {
////        this.getBean(IApplicationModuleEventListenerContainer.class).uninstallModuleBefore(content);
////    }
////
////    /**
////     * 卸载模块后事件
////     *
////     * @param content 模块对象
////     */
////    public void uninstallModuleAfter(IModuleContent content) {
////        this.getBean(IApplicationModuleEventListenerContainer.class).uninstallModuleAfter(content);
////    }
//
//    //-----------------------------IApplicationModuleEventListenerContainer----------------------------//
//    //-----------------------------IApplicationObjectEventListenerContainer----------------------------//
//
//    /**
//     * 获取应用对象事件监听容器
//     *
//     * @return
//     */
////    @Override
////    public IApplicationObjectEventListenerContainer getObjectEventListenerFactoryContainer() {
////        return this.getBean(IApplicationObjectEventListenerContainer.class);
////    }
//
////    /**
////     * 注册对象
////     *
////     * @param value 对象
////     */
////    public void registrarObjectEvent(Object value) {
////        this.getObjectEventListenerFactoryContainer().registrarObjectEvent(value);
////    }
////
////    /**
////     * 注册对象列表
////     *
////     * @param values 对象列表
////     */
////    public void registrarObjectsEvent(Object[] values) {
////        this.getObjectEventListenerFactoryContainer().registrarObjectsEvent(values);
////    }
////
////    /**
////     * 注册对象列表
////     *
////     * @param values 对象列表
////     */
////    public void registrarObjectsEvent(List<Object> values) {
////        this.getObjectEventListenerFactoryContainer().registrarObjectsEvent(values);
////    }
//    //-----------------------------IApplicationObjectEventListenerContainer----------------------------//
//    //----------------------------------IApplicationBeanEventListenerContainer-----------------------------------//
//
////    /**
////     * 获取事件拥有者的绑定事件定义对象
////     *
////     * @param eventOwner 事件拥有者
////     * @return
////     */
////    public BeanEventDefinition getBeanEvent(IBeanEventListener eventOwner) {
////        return this.getBeanEventListenerFactoryContainer().getBeanEvent(eventOwner);
////    }
////
////    /**
////     * 注册绑定事件
////     *
////     * @param eventOwner  事件拥有者
////     * @param eventClasss 事件类型
////     */
////    public void registeredBeanEvent(IBeanEventListener eventOwner, List<Class<?>> eventClasss) {
////        this.getBeanEventListenerFactoryContainer().registeredBeanEvent(eventOwner, eventClasss);
////    }
////
////    /**
////     * 注册绑定事件
////     *
////     * @param eventOwner 事件拥有者
////     * @param eventClass 事件类型
////     */
////    public void registeredBeanEvent(IBeanEventListener eventOwner, Class<?> eventClass) {
////        this.getBeanEventListenerFactoryContainer().registeredBeanEvent(eventOwner, eventClass);
////    }
////
////    /**
////     * 卸载绑定事件
////     *
////     * @param eventOwner  事件拥有者
////     * @param eventClasss 事件类型
////     */
////    public void uninstallBeanEvent(IBeanEventListener eventOwner, List<Class<?>> eventClasss) {
////        this.getBeanEventListenerFactoryContainer().uninstallBeanEvent(eventOwner, eventClasss);
////    }
////
////    /**
////     * 卸载绑定事件
////     *
////     * @param eventOwner 事件拥有者
////     * @param eventClass 事件类型
////     */
////    public void uninstallBeanEvent(IBeanEventListener eventOwner, Class<?> eventClass) {
////        this.getBeanEventListenerFactoryContainer().uninstallBeanEvent(eventOwner, eventClass);
////    }
////
////    /**
////     * 注册绑定后事件
////     *
////     * @param definition 绑定定义
////     */
////    public void registeredBeanEventAfter(IBeanDefinition definition) {
////        this.getBeanEventListenerFactoryContainer().registeredBeanEventAfter(definition);
////    }
////
////    /**
////     * 注册绑定前事件
////     *
////     * @param definition 绑定定义
////     */
////    public void registeredBeanEventBefore(IBeanDefinition definition) {
////        this.getBeanEventListenerFactoryContainer().registeredBeanEventBefore(definition);
////    }
////
////    /**
////     * 卸载绑定后事件
////     *
////     * @param definition 绑定定义
////     */
////    public void uninstallBeanEventAfter(IBeanDefinition definition) {
////        this.getBeanEventListenerFactoryContainer().uninstallBeanEventAfter(definition);
////    }
////
////    /**
////     * 卸载绑定前事件
////     *
////     * @param definition 绑定定义
////     */
////    public void uninstallBeanEventBefore(IBeanDefinition definition) {
////        this.getBeanEventListenerFactoryContainer().uninstallBeanEventBefore(definition);
////    }
//
////    /**
////     * 获取绑定事件监听容器
////     *
////     * @return
////     */
////    @Override
////    public IApplicationBeanEventListenerContainer getBeanEventListenerFactoryContainer() {
////        return this.getBean(IApplicationBeanEventListenerContainer.class);
////    }
//    //----------------------------------IApplicationBeanEventListenerContainer-----------------------------------//
//    //----------------------------------IApplicationEnvEventListenerContainer-----------------------------------//
//
////    /**
////     * 获取应用env事件简体容器
////     *
////     * @return
////     */
////    @Override
////    public IApplicationEnvEventListenerContainer getEnvEventListenerContainer() {
////        return this.getBean(IApplicationEnvEventListenerContainer.class);
////    }
//
////    /**
////     * 添加模块env内容
////     *
////     * @param value 键
////     * @param v   值
////     */
////    public void envAdd(String value, Object v) {
////        this.getEnvEventListenerContainer().envAdd(value, v);
////    }
////
////    /**
////     * 清除事件
////     */
////    public void envClear() {
////        this.getEnvEventListenerContainer().envClear();
////    }
////
////    /**
////     * 删除事件
////     *
////     * @param value 键
////     */
////    public void envRemove(String value) {
////        this.getEnvEventListenerContainer().envRemove(value);
////    }
////
////    /**
////     * 更改后事件
////     *
////     * @param value
////     * @param v
////     */
////    public void envChangeAfter(String value, Object v) {
////        this.getEnvEventListenerContainer().envChangeAfter(value, v);
////    }
////
////    /**
////     * 更改前事件
////     *
////     * @param value
////     * @param v
////     */
////    public void envChangeBefore(String value, Object v) {
////        this.getEnvEventListenerContainer().envChangeBefore(value, v);
////    }
////
////    /**
////     * 合并后事件
////     *
////     * @param prefix     合并前缀
////     * @param properties
////     */
////    public void envMergeAfter(String prefix, Properties properties) {
////        this.getEnvEventListenerContainer().envMergeAfter(prefix, properties);
////    }
////
////    /**
////     * 合并前事件
////     *
////     * @param prefix     合并前缀
////     * @param properties
////     */
////    public void envMergeBefore(String prefix, Properties properties) {
////        this.getEnvEventListenerContainer().envMergeBefore(prefix, properties);
////    }
//    //----------------------------------IApplicationEnvEventListenerContainer-----------------------------------//
//    //----------------------------------ApplicationModuleBeanEventListenerContainer-----------------------------------//
//
////    /**
////     * 获取应用模块绑定事件监听容器
////     *
////     * @return
////     */
////    @Override
////    public IApplicationModuleBeanEventListenerContainer getModuleBeanEventListenerContainer() {
////        return this.getBean(IApplicationModuleBeanEventListenerContainer.class);
////    }
//    //----------------------------------ApplicationModuleBeanEventListenerContainer-----------------------------------//
//
//    /**
//     * 初始化应用env基础类
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationEventContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationEventContent");
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        //创建时间监听容器
//        this.getLog().info("~ApplicationEventContent.init->Before");
//        //优先创建对象后再初始化类
//        this.bean(new EnvEventListenersContainer<Object, IEnvironment>());
//        this.bean(new BeanEventListenerContainer<Object, IBeanDefinition>());
//        this.bean(new ObjectEventListenerContainer<Object>());
//        this.bean(new ClassEventListenerContainer<Object>());
//        this.bean(new MavenEventListenerContainer<Object>());
//        this.bean(new JarEventListenerContainer<Object>());
//        super.init();
//        this.getLog().info("~ApplicationEventContent.init->After");
//    }
//
//    protected abstract void bean(Object objectObjectEventListenerContainer);
//
//    /**
//     * 关闭释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//        super.close();
//    }
//
//    @Override
//    public IBeanEventListenerContainer<Object, IBeanDefinition> getBeanEventListenerFactoryContainer() {
//        return this.getBean(IBeanEventListenerContainer.class);
//    }
//
//    public abstract <T> T getBean(Class<? > iBeanEventListenerContainerClass);
//
//    @Override
//    public IJarEventListenerContainer<Object> getJarEventListenerFactoryContainer() {
//        return this.getBean(IJarEventListenerContainer.class);
//    }
//
//    @Override
//    public IObjectEventListenerContainer<Object> getObjectEventListenerFactoryContainer() {
//        return this.getBean(IObjectEventListenerContainer.class);
//    }
//}