//package ghost.framework.module.module.bak;
//
//import ghost.framework.context.bean.IBeanDefinition;
//import ghost.framework.context.env.IEnvironment;
//import ghost.framework.core.event.bean.BeanEventListenerContainer;
//import ghost.framework.core.event.bean.IBeanEventListenerContainer;
//import ghost.framework.core.event.classs.ClassEventListenerContainer;
//import ghost.framework.core.event.classs.IClassEventListenerContainer;
//import ghost.framework.core.event.env.EnvEventListenersContainer;
//import ghost.framework.core.event.env.IEnvEventListenersContainer;
//import ghost.framework.core.event.jar.IJarEventListenerContainer;
//import ghost.framework.core.event.jar.JarEventListenerContainer;
//import ghost.framework.core.event.maven.IMavenEventListenerContainer;
//import ghost.framework.core.event.maven.MavenEventListenerContainer;
//import ghost.framework.core.event.obj.IObjectEventListenerContainer;
//import ghost.framework.core.event.obj.ObjectEventListenerContainer;
//import ghost.framework.module.context.IModuleEvent;
//import org.eclipse.aether.artifact.Artifact;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 17:00 2019-06-09
// */
//abstract class ModuleEventContent extends ModuleLanguageContent implements IModuleEvent {
//
//    /**
//     * 初始化模块语言内容
//     *
//     */
//    protected ModuleEventContent() {
//        super();
//        this.getLog().info("~ModuleEventContent");
//    }
//
//    @Override
//    public IJarEventListenerContainer<Object> getJarEventListenerFactoryContainer() {
//        return this.getBean(IJarEventListenerContainer.class);
//    }
//
//    @Override
//    public IMavenEventListenerContainer<Object, Artifact> getMavenEventListenerFactoryContainer() {
//        return this.getBean(IMavenEventListenerContainer.class);
//    }
//
//    @Override
//    public IClassEventListenerContainer<Object> getClassEventListenerFactoryContainer() {
//        return this.getBean(IClassEventListenerContainer.class);
//    }
//    //--------------------------IModuleEnvEventListenersContainer-------------------------//
//
//    /**
//     * 获取模块env事件监听容器
//     *
//     * @return
//     */
//    @Override
//    public IEnvEventListenersContainer<Object, IEnvironment> getEnvEventListenersContainer() {
//        return this.getBean(IEnvEventListenersContainer.class);
//    }
//    //--------------------------IModuleEnvEventListenersContainer-------------------------//
//    //--------------------------IModuleObjectEventListenerContainer--------------------------//
//
//    /**
//     * 获取模块注册对象事件监听容器
//     *
//     * @return
//     */
//    @Override
//    public IObjectEventListenerContainer getObjectEventListenerFactoryContainer() {
//        return this.getBean(IObjectEventListenerContainer.class);
//    }
//
////    /**
////     * 类注对象
////     *
////     * @param value 对象列
////     */
////    @Override
////    public void registrarObjectEvent(Object value) {
////        this.objectEventListenerContainer.registrarObjectEvent(value);
////    }
////
////    /**
////     * 类注对象
////     *
////     * @param values 对象列表
////     */
////    @Override
////    public void registrarObjectsEvent(List<Object> values) {
////        this.objectEventListenerContainer.registrarObjectsEvent(values);
////    }
////
////    /**
////     * 类注对象
////     *
////     * @param values 对象列表
////     */
////    @Override
////    public void registrarObjectsEvent(Object[] values) {
////        this.objectEventListenerContainer.registrarObjectsEvent(values);
////    }
//    //------------------------------IModuleObjectEventListenerContainer-------------------------------//
//    //----------------------------------IBeanEventListenerContainer-----------------------------------//
//
//    /**
//     * 获取绑定事件监听容器
//     *
//     * @return
//     */
//    @Override
//    public IBeanEventListenerContainer<Object, IBeanDefinition> getBeanEventListenerFactoryContainer() {
//        return this.getBean(IBeanEventListenerContainer.class);
//    }
//
////    /**
////     * 获取事件拥有者的绑定事件定义对象
////     *
////     * @param eventOwner 事件拥有者
////     * @return
////     */
////    @Override
////    public BeanEventDefinition getBeanEvent(IBeanEventListener eventOwner) {
////        return this.beanEventListenerContainer.getBeanEvent(eventOwner);
////    }
////
////    /**
////     * 注册绑定事件
////     *
////     * @param eventOwner  事件拥有者
////     * @param eventClasss 事件类型
////     */
////    @Override
////    public void registeredBeanEvent(IBeanEventListener eventOwner, List<Class<?>> eventClasss) {
////        this.beanEventListenerContainer.bean(eventOwner, eventClasss);
////    }
////
////    /**
////     * 注册绑定事件
////     *
////     * @param eventOwner 事件拥有者
////     * @param eventClass 事件类型
////     */
////    @Override
////    public void registeredBeanEvent(IBeanEventListener eventOwner, Class<?> eventClass) {
////        this.beanEventListenerContainer.bean(eventOwner, eventClass);
////    }
////
////    /**
////     * 卸载绑定事件
////     *
////     * @param eventOwner  事件拥有者
////     * @param eventClasss 事件类型
////     */
////    @Override
////    public void uninstallBeanEvent(IBeanEventListener eventOwner, List<Class<?>> eventClasss) {
////        this.beanEventListenerContainer.removeBeanEvent(eventOwner, eventClasss);
////    }
////
////    /**
////     * 卸载绑定事件
////     *
////     * @param eventOwner 事件拥有者
////     * @param eventClass 事件类型
////     */
////    @Override
////    public void uninstallBeanEvent(IBeanEventListener eventOwner, Class<?> eventClass) {
////        this.beanEventListenerContainer.removeBeanEvent(eventOwner, eventClass);
////    }
////
////    /**
////     * 注册绑定后事件
////     *
////     * @param definition 绑定定义
////     */
////    @Override
////    public void registeredBeanEventAfter(IBeanDefinition definition) {
////        this.beanEventListenerContainer.registeredBeanEventAfter(definition);
////    }
////
////    /**
////     * 注册绑定前事件
////     *
////     * @param definition 绑定定义
////     */
////    @Override
////    public void registeredBeanEventBefore(IBeanDefinition definition) {
////        this.beanEventListenerContainer.registeredBeanEventBefore(definition);
////    }
////
////    /**
////     * 卸载绑定后事件
////     *
////     * @param definition 绑定定义
////     */
////    @Override
////    public void uninstallBeanEventAfter(IBeanDefinition definition) {
////        this.beanEventListenerContainer.uninstallBeanEventAfter(definition);
////    }
////
////    /**
////     * 卸载绑定前事件
////     *
////     * @param definition 绑定定义
////     */
////    @Override
////    public void uninstallBeanEventBefore(IBeanDefinition definition) {
////        this.beanEventListenerContainer.uninstallBeanEventBefore(definition);
////    }
//    //----------------------------------IBeanEventListenerContainer-----------------------------------//
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        //创建时间监听容器
//        this.bean(new EnvEventListenersContainer<Object, IEnvironment>(this.app.getBean(IEnvEventListenersContainer.class)));
//        this.bean(new BeanEventListenerContainer<Object, IBeanDefinition>(this.app.getBean(IBeanEventListenerContainer.class)));
//        this.bean(new ObjectEventListenerContainer<Object>(this.app.getBean(IObjectEventListenerContainer.class)));
//        this.bean(new ClassEventListenerContainer<Object>(this.app.getBean(IClassEventListenerContainer.class)));
//        this.bean(new MavenEventListenerContainer<Object, Artifact>(this.app.getBean(IMavenEventListenerContainer.class)));
//        this.bean(new JarEventListenerContainer<Object>(this.app.getBean(IJarEventListenerContainer.class)));
//        super.init();
//    }
//}