//package ghost.framework.module.module.bak;
//
//import ghost.framework.beans.BeanMethod;
//import ghost.framework.context.bean.utils.BeanUtil;
//import ghost.framework.beans.annotation.*;
//import ghost.framework.beans.configuration.ConfigurationUtil;
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.beans.configuration.annotation.SelectProperties;
//import ghost.framework.beans.execute.annotation.Main;
//import ghost.framework.beans.annotation.module.annotation.ConditionalOnMissingModuleBean;
//import ghost.framework.beans.annotation.module.Module;
//import ghost.framework.beans.annotation.module.annotation.ModuleBean;
//import ghost.framework.beans.utils.OrderAnnotationUtil;
//import ghost.framework.context.env.EnvironmentReader;
//import ghost.framework.context.module.IModule;
//import ghost.framework.context.module.exception.ModuleBeanException;
//import ghost.framework.context.module.exception.ModuleException;
//import ghost.framework.module.context.IModuleLoaderContent;
//import ghost.framework.util.NotImplementedException;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//import ghost.framework.util.generic.ListUtil;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.net.MalformedURLException;
//import java.security.ProtectionDomain;
//import java.util.ArrayList;
//import java.util.List;
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块加载内容
// * @Date: 0:29 2019-05-18
// */
//public final class ModuleLoaderContent implements IModuleLoaderContent {
//    /**
//     * 日志
//     */
//    private Log log = LogFactory.getLog(this.getClass());
//    /**
//     * 模块内容
//     */
//    private IModule module;
//    @Override
//    public IModule getModule() {
//        return module;
//    }
//
//    /**
//     * 初始化模块加载内容
//     *
//     * @param module 模块内容
//     */
//    public ModuleLoaderContent(IModule module) {
//        this.module = module;
//
//    }
//
//    /**
//     *
//     */
//    public Module loaderPackage;
//    /**
//     * 注释服务类列表
//     */
//    private List<Class<?>> serviceList = new ArrayList<>();
//
//    /**
//     * 获取注释服务类列表
//     *
//     * @return
//     */
//    @Override
//    public List<Class<?>> getServiceList() {
//        return serviceList;
//    }
//
//    /**
//     * 注释配置类列表
//     */
//    private List<Class<?>> configurationList = new ArrayList<>();
//
//    /**
//     * 获取配置加载类
//     *
//     * @return
//     */
//    @Override
//    public List<Class<?>> getConfigurationList() {
//        return configurationList;
//    }
//
//    /**
//     * 绑定函数列表
//     * 函数注释可以带排序
//     * 可以按照排序从小到大注释值进行加载
//     */
//    private List<Method> beanList = new ArrayList<>();
//
//    /**
//     * 启动类
//     */
//    private List<Class<?>> mainList = new ArrayList<>();
//
//    /**
//     * 获取运行类
//     *
//     * @return
//     */
//    @Override
//    public List<Class<?>> getMainList() {
//        return mainList;
//    }
//
//    /**
//     * 初始化加载模块
//     *
//     * @param domain 包域
//     * @throws Exception
//     */
//    public void init(ProtectionDomain domain) throws Exception {
////        //创建模块
////        IModule content = this.createModule(domain);
////        //添加模块
////        this.bootLoader.getApplicationContent().addModule(content);
//        //模块加载前事件
//        //this.bootLoader.getBoot().moduleLoaderBefore(module, this);
//        //初始化配置
////        for (Class<?> c : this.configurationPropertiesList) {
////            InputStream is = null;
////            Properties prop = new Properties();
////            try {
////                 ConfigurationProperties properties = c.getAnnotation(ConfigurationProperties.class);
////                //是否加载配置文件路径
////                try {
////                    is = new FileInputStream(domain.getCodeSource().getLocation().getPath() + File.separator + properties.path());
////                    prop.load(is);
////                } catch (IOException e) {
////                    ExceptionUtil.debugOrError(this.log, e);
////                }
////                //判断注释是否我模块注释
////                if (properties.module()) {
////                    //模块配置
////                    if (properties.prefix().equals(StringConstant.empty)) {
////                        // module.getEnv().merge(prop);
////                    } else {
////                        // module.getEnv().merge(properties.prefix(), prop);
////                    }
////                } else {
////                    //全局配置
////                    if (properties.prefix().equals(StringConstant.empty)) {
////                        this.bootLoader.getApplicationContent().getEnv().merge(prop);
////                    } else {
////                        this.bootLoader.getApplicationContent().getEnv().merge(properties.prefix(), prop);
////                    }
////                }
////            } catch (Exception e) {
////                if (is != null) is.close();
////            }
////        }
//        //初始化配置
////        for (Class<?> bean : this.beanList) {
////            beanNewInstance(bean);
////        }
////        //初始化配置
////        for (Class<?> config : this.configurationList) {
////            configNewInstance(config);
////        }
//        //初始化主类
//        for (Class<?> main : this.mainList) {
//
//        }
//
//        //模块加载后事件
//        //this.bootLoader.getBoot().moduleLoaderAfter(module);
//    }
//
//    /**
//     *
//     * @param o
//     * @param m
//     * @throws Exception
//     */
//    private void initBean(Object o, Method m) throws Exception {
//        //获取绑定注释
//        Bean bean = m.getAnnotation(Bean.class);
//        //绑定应用
//        //验证不存在时绑定
//        if (m.getReturnType().equals(Void.TYPE)) {
//            //没有返回函数绑定，只做调用
//            m.invoke(this.beanInjection(o, m, bean), this.module.getApp().newModuleMethodParameters(this.module, m));
//        } else {
//            //有返回函数绑定
//            this.module.getApp().bean(bean.value(), this.beanInjection(o, m, bean));
//        }
//    }
//
//    /**
//     * 绑定注入
//     *
//     * @param o
//     * @param m
//     * @param b
//     * @return
//     * @throws Exception
//     */
//    private Object beanInjection(Object o, Method m, Bean b) throws Exception {
//        //创建绑定函数返回创建对象
//        Object mio = m.invoke(o, this.module.getApp().newModuleMethodParameters(this.module, m));
//        //注入对象
//        this.module.getApp().injection(this.module, mio);
//        //绑定注入类型
//        if (b.injectionList().length > 0) {
//            //遍历注入类型列表
//            for (Class<?> c : b.injectionList()) {
//                //应用注入
//                Object io = this.module.getApp().getBean(c);
//                if (io instanceof List) {
//                    ((List) io).put(mio);
//                }
//            }
//        }
//        return mio;
//    }
//
//    /**
//     * 绑定注入
//     *
//     * @param o
//     * @param m
//     * @param b
//     * @return
//     * @throws Exception
//     */
//    private Object beanInjection(Object o, Method m, ConditionalOnMissingBean b) throws Exception {
//        //创建绑定函数返回创建对象
//        Object mio = m.invoke(o, this.module.getApp().newModuleMethodParameters(this.module, m));
//        //注入对象
//        this.module.getApp().injection(this.module, mio);
//        //绑定注入类型
//        if (b.injectionList().length > 0) {
//            //遍历注入类型列表
//            for (Class<?> c : b.injectionList()) {
//                //应用注入
//                Object io = this.module.getApp().getBean(c);
//                if (io instanceof List) {
//                    ((List) io).put(mio);
//                }
//            }
//        }
//        return mio;
//    }
//
//    /**
//     * @param o
//     * @param m
//     * @throws Exception
//     */
//    private void initConditionalOnMissingBean(Object o, Method m) throws Exception {
//        //获取绑定注释
//        ConditionalOnMissingBean bean = m.getAnnotation(ConditionalOnMissingBean.class);
////        //判断注入模式
////        if (bean.module()) {
////            //判断是否为模块绑定
////            if (bean.value().equals("")) {
////                //
////                if (m.getReturnType().equals(Void.TYPE)) {
////                    //没有返回函数绑定，只做调用
////                    m.invoke(o, this.content.getApplicationContent().newMethodParameter(this.content, m));
////                } else {
////                    //验证不存在时绑定
////                    if (!this.content.beanContains(bean.scope(), bean.value())) {
////                        //调用函数返回要绑定的对象
////                        this.content.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////                    }
////                }
////            } else {
////                //绑定模块
////                if (m.getReturnType().equals(Void.TYPE)) {
////                    //没有返回函数绑定，只做调用
////                    m.invoke(o, this.content.getApplicationContent().newMethodParameter(this.content, m));
////                } else {
////                    //获取绑定模块
////                    IModule mc = this.content.getApplicationContent().getModule(bean.value());
////                    if (mc == null) {
////                        //获取不到要绑定的模块id错误
////                        throw new ModuleBeanException("Bean.value:" + bean.value());
////                    }
////                    //验证不存在时绑定
////                    if (!mc.beanContains(bean.scope(), bean.value())) {
////                        mc.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////                    }
////                }
////            }
////        } else {
//        //绑定应用
//        //验证不存在时绑定
//        if (m.getReturnType().equals(Void.TYPE)) {
//            //没有返回函数绑定，只做调用
//            m.invoke(o, this.module.getApp().newModuleMethodParameters(this.module, m));
//        } else {
//            //有返回函数绑定
//            this.module.getApp().bean(bean.value(), this.beanInjection(o, m, bean));
//        }
////        }
//    }
//
//    /**
//     * 遍历绑定函数
//     *
//     * @param o
//     * @throws Exception
//     * @throws IOException
//     */
//    private void forBean(Object o) throws Exception {
//        //绑定函数列表
//        List<BeanMethod> list = new ArrayList<>();
//        //获取函数注释列表
//        for (Method m : o.getClass().getDeclaredMethods()) {
//            if (m.isAnnotationPresent(Bean.class)) {
//                list.put(new BeanMethod(m));
//                continue;
//            }
//            if (m.isAnnotationPresent(ConditionalOnMissingBean.class)) {
//                list.put(new BeanMethod(m));
//                continue;
//            }
//            if (m.isAnnotationPresent(ghost.framework.beans.annotation.module.annotation.ModuleBean.class)) {
//                list.put(new BeanMethod(m));
//                continue;
//            }
//            if (m.isAnnotationPresent(ghost.framework.beans.annotation.module.annotation.ConditionalOnMissingModuleBean.class)) {
//                list.put(new BeanMethod(m));
//                continue;
//            }
//        }
//        //排序
//        OrderAnnotationUtil.beanMethodListSort(list);
//        //遍历绑定函数
//        for (BeanMethod m : list) {
//            //应用绑定
//            if (BeanUtil.isBeanMethodAccessible(m.getMethod())) {
//                this.initBean(o, m.getMethod());
//                continue;
//            }
//            //应用判断绑定
//            if (BeanUtil.isConditionalOnMissingBeanMethodAccessible(m.getMethod())) {
//                this.initConditionalOnMissingBean(o, m.getMethod());
//                continue;
//            }
//            //模块绑定
//            if (BeanUtil.isModuleBeanMethodAccessible(m.getMethod())) {
//                this.initModuleBean(o, m.getMethod());
//                continue;
//            }
//            //模块判断绑定
//            if (BeanUtil.isModuleConditionalOnMissingBeanMethodAccessible(m.getMethod())) {
//                this.initConditionalOnMissingModuleBean(o, m.getMethod());
//                continue;
//            }
//        }
//    }
//
//    /**
//     * 模块绑定
//     * @param o
//     * @param m
//     */
//    private void initConditionalOnMissingModuleBean(Object o, Method m) throws Exception{
//        //获取绑定注释
//        ghost.framework.beans.annotation.module.annotation.ConditionalOnMissingModuleBean bean = m.getAnnotation(ConditionalOnMissingModuleBean.class);
//        //判断注入模式
//        if (bean.value().equals("")) {
//            //验证不存在时绑定
//            if (m.getReturnType().equals(Void.TYPE)) {
//                //没有返回函数绑定，只做调用
//                throw new ModuleBeanException(bean.toString());
//            } else {
//                //判断是否已经存在绑定了
//                if(this.module.beanContains(bean.value())){
//                    return;
//                }
//                //有返回函数绑定
//                this.module.bean(bean.value(), o);
//            }
//        } else {
//            //获取绑定模块
//            IModule mc = this.module.getApp().getModule(bean.value());
//            if (mc == null) {
//                //获取不到要绑定的模块id错误
//                throw new ModuleBeanException("Bean not error value:" + bean.value());
//            }
//            //绑定模块
//            if (m.getReturnType().equals(Void.TYPE)) {
//                //没有返回函数绑定，只做调用
//                m.invoke(o, this.module.getApp().newModuleMethodParameters(mc, m));
//            } else {
//                //绑定
//                mc.bean(bean.value(), o);
//            }
//        }
//    }
//
//    /**
//     * 模块绑定
//     * @param o
//     * @param m
//     * @throws Exception
//     */
//    private void initModuleBean(Object o, Method m) throws Exception {
//        //获取绑定注释
//        ghost.framework.beans.annotation.module.annotation.ModuleBean bean = m.getAnnotation(ghost.framework.beans.annotation.module.annotation.ModuleBean.class);
//        //判断注入模式
//        if (bean.value().equals("")) {
//            //验证不存在时绑定
//            if (m.getReturnType().equals(Void.TYPE)) {
//                //没有返回函数绑定，只做调用
//                m.invoke(o, this.module.getApp().newModuleMethodParameters(this.module, m));
//            } else {
//                //有返回函数绑定
//                this.module.bean(bean.value(), o);
//            }
//        } else {
//            //获取绑定模块
//            IModule mc = this.module.getApp().getModule(bean.value());
//            if (mc == null) {
//                //获取不到要绑定的模块id错误
//                throw new ModuleBeanException("Bean not error value:" + bean.value());
//            }
//            //绑定模块
//            if (m.getReturnType().equals(Void.TYPE)) {
//                //没有返回函数绑定，只做调用
//                m.invoke(o, this.module.getApp().newModuleMethodParameters(mc, m));
//            } else {
//                //绑定
//                mc.bean(bean.value(), o);
//            }
//        }
//    }
//
//    /**
//     * 初始化配置绑定配置类的注入配置
//     *
//     * @param config
//     */
//    public void initSelectProperties(Configuration config) throws Exception {
//        for (SelectProperties p : config.select()) {
//            Object o;
////            if (p.module()) {
////                //获取现在配置的模块
////                IModule mc = this.content.getId().equals(p.value()) ? this.content : this.content.getApplicationContent().getModule(p.value());
////                if (mc == null) {
////                    throw new ModuleInvalidException(p.value());
////                }
////                //初始化选择模块配置
////                o = this.content.getApplicationContent().newModuleInstance(mc, p.depend());
////                //绑定
////                this.content.bean(o);
////                //注入属性
////                EnvironmentReader reader = mc.getEnv().getReader(p.prefix());
////                //遍历属性
////                for (Field field : p.depend().getDeclaredFields()) {
////                    field.setAccessible(true);
////                    try {
////                        if (reader.equalsIgnoreCase(field.getName())) {
////                            field.set(o, reader.get(field.getName()));
////                        }
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    } finally {
////                        field.setAccessible(false);
////                    }
////                }
////            } else {
//                //初始化选择应用配置
//                o = this.module.getApp().newModuleInstance(p.depend());
//                //绑定
//                this.module.getApp().bean(o);
//                //注入属性
//                EnvironmentReader reader = this.module.getEnv().getReader(p.prefix());
//                //遍历属性
//                for (Field field : p.depend().getDeclaredFields()) {
//                    field.setAccessible(true);
//                    try {
//                        if (reader.equalsIgnoreCase(field.getName())) {
//                            field.set(o, reader.get(field.getName()));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        field.setAccessible(false);
//                    }
//                }
////            }
//            //遍历绑定函数
//            this.forBean(o);
//        }
//    }
//
//    /**
//     * 初始化模块扫描的类
//     *
//     * @throws Exception
//     */
//    @Override
//    public void init() throws Exception {
//        //初始化configuration类列表
//        OrderAnnotationUtil.classListSort(this.configurationList);
//        for (Class<?> c : this.configurationList) {
//            //处理SelectProperties绑定配置参数注释类的注释绑定
//            this.initSelectProperties(c.getAnnotation(Configuration.class));
//            //创建configuration实例
//            Object o = this.module.getApp().newModuleInstance(this.module, c);
//            //遍历绑定函数
//            this.forBean(o);
//        }
//        //初始化service类列表
//        OrderAnnotationUtil.classListSort(this.serviceList);
//        for (Class<?> c : this.serviceList) {
//            //创建service实例
//            Object o = this.module.getApp().newModuleInstance(this.module, c);
//            //遍历绑定函数
//            this.forBean(o);
//        }
//        //初始化main类列表
//        OrderAnnotationUtil.classListSort(this.mainList);
//        for (Class<?> c : this.mainList) {
//            //初始化运行类
//            Object o = this.module.newModuleInstance(c);
//            //遍历绑定函数
//            this.forBean(o);
//            //获取主类注释
//            ghost.framework.beans.execute.annotation.Main main = c.getAnnotation(Main.class);
//            //初始化main注释的配置
//            if (main.properties().length > 0) {
//                this.configurationProperties(c, main.properties());
//            }
//            //注入注释
////            this.module.registeredObject(o);
//            //注册绑定
//            this.module.bean(o);
//            //绑定类型
//            this.beanClass(c);
//            //获取初始化函数
//            Method im = ReflectUtil.getAnnotationVoidMethod(c, Init.class);
//            //判断是否有初始化函数
//            if (im != null) {
//                //获取init注释
//                Init init = im.getAnnotation(Init.class);
//                //初始化init注释的配置
//                if (init.properties().length > 0) {
//                    this.configurationProperties(c, init.properties());
//                }
//                //调用初始化函数
//                im.setAccessible(true);
//                //判断是否有参数引用函数
//                if (im.getParameterCount() == 0) {
//                    //没有参数调用函数
//                    im.invoke(o, null);
//                } else {
//                    //有参数调用函数
//                    im.invoke(o, this.module.getApp().newModuleMethodParameters(this.module, im));
//                }
//            }
//            //获取启动函数
//            Method sm = ReflectUtil.getAnnotationVoidMethod(c, ghost.framework.beans.annotation.invoke.Start.class);
//            //判断是否有启动函数
//            if (sm != null) {
//                //获取启动注释
//                ghost.framework.beans.annotation.invoke.Start Before = sm.getAnnotation(ghost.framework.beans.annotation.invoke.Start.class);
//                //调用启动函数
//                sm.setAccessible(true);
//                //验证是否使用运行线程
//                if (Before.thread()) {
//                    //使用线程运行启动函数
//                    Thread t = new Thread(new Runnable() {
//                        /**
//                         * 运行线程启动函数
//                         */
//                        @Override
//                        public void run() {
//                            //处理错误
//                            try {
//                                sm.invoke(o, module.getApp().newModuleMethodParameters(module, sm));
//                            } catch (Exception e) {
//                                if (log.isDebugEnabled()) {
//                                    e.printStackTrace();
//                                    log.debug(e.getMessage());
//                                } else {
//                                    log.error(e.getMessage());
//                                }
//                            }
//                        }
//                    });
//                    //设置线程名称
//                    if (!Before.threadName().equals("")) {
//                        t.setName(Before.threadName());
//                    }
//                    //设置线程类加载器
////                    t.setContextClassLoader(this.module.getClassLoader());
//                    //启动线程
//                    t.Before();
//                } else {
//                    sm.invoke(o, this.module.getApp().newModuleMethodParameters(this.module, sm));
//                }
//            }
//        }
//        //设置模块初始化完成
//        ReflectUtil.setField(this.module, "initialize", true);
//        //引发模块初始化完成
////        this.module.getApp().registeredModuleAfter(this.module);
//    }
//
//    /**
//     * 绑定类
//     *
//     * @param c
//     * @throws Exception
//     */
//    private void beanClass(Class<?> c) throws Exception {
//        //判断是否有注释绑定
//        if (c.isAnnotationPresent(ghost.framework.beans.annotation.module.annotation.ModuleBean.class)) {
//            //获取绑定注释
//            this.bean(c.getAnnotation(ghost.framework.beans.annotation.module.annotation.ModuleBean.class), c);
//        }
//        //判断是否有注释绑定
//        if (c.isAnnotationPresent(ConditionalOnMissingBean.class)) {
//            //获取绑定注释
//            this.addConditionalOnMissingBean(c.getAnnotation(ConditionalOnMissingBean.class), c);
//        }
////        //判断是否有注释绑定
////        if (c.isAnnotationPresent(ConditionalOnMissingClass.class)) {
////            //获取绑定注释
////            for (Class<?> a : OrderUtil.getClassOrderList(c.getAnnotation(ConditionalOnMissingClass.class).value())) {
////                this.beanClass(a);
////            }
////        }
//    }
//
//    /**
//     * 添加类型绑定
//     *
//     * @param bean 绑定注释
//     * @param c    绑定类型
//     * @throws Exception
//     */
//    private void addConditionalOnMissingBean(ConditionalOnMissingBean bean, Class<?> c) throws Exception {
//        //判断是否为模块绑定
//        //if (bean.value().equals("")) {
//            //验证不存在时绑定
//            if (!this.module.beanContains(bean.value())) {
//                this.module.bean(bean.value(), c);
//            }
////        } else {
////            //绑定模块
////            //获取绑定模块
////            IModule m = this.content.getApplicationContent().getModule(bean.value());
////            if (m == null) {
////                //获取不到要绑定的模块id错误
////                throw new ModuleException("Bean.value:" + bean.value());
////            }
////            //绑定
////            if (!m.beanContains(bean.scope(), bean.value(), c)) {
////                m.bean(bean.scope(), bean.value(), c);
////            }
////        }
//    }
//    /**
//     * 添加类型绑定
//     *
//     * @param bean 绑定注释
//     * @param c    绑定类型
//     * @throws Exception
//     */
//    private void bean(ModuleBean bean, Class<?> c) throws Exception {
//        //判断是否为模块绑定
//        if (bean.value().equals("")) {
//            //验证不存在时绑定
//            this.module.bean(bean.value(), c);
//        } else {
//            //绑定模块
//            //获取绑定模块
//            IModule m = this.module.getApp().getModule(bean.value());
//            if (m == null) {
//                //获取不到要绑定的模块id错误
//                throw new ModuleException("Bean.value:" + bean.value());
//            }
//            //绑定
//            m.bean(bean.value(), c);
//        }
//    }
//
//    /**
//     * 初始化类配置文件注释
//     *
//     * @param c
//     * @throws MalformedURLException
//     */
//    private void initConfigurationProperties(Class<?> c) throws MalformedURLException, NotImplementedException {
//        this.configurationProperties(c, ConfigurationUtil.getClassOrderList(c));
//    }
//
//    private void configurationProperties(Class<?> c, ConfigurationProperties[] propertiess) throws MalformedURLException, NotImplementedException {
//        this.configurationProperties(c, ListUtil.toList(propertiess));
//    }
//
//    private void configurationProperties(Class<?> c, List<ConfigurationProperties> propertiess) throws MalformedURLException, NotImplementedException {
//        //获取类配置文件注释列表
//        for (ConfigurationProperties properties : propertiess) {
//            //判断是否为模块注释
////            if (properties.module()) {
////                //模块配置
////                if (this.module.isDev()) {
////                    //调试模式
////                    this.module.getEnv().merge(c, properties);
////                } else {
////                    //jar运行模式
//////                    this.module.getEnv().merge(properties.prefix(), this.module.getUrlPath(properties.path()));
////                }
////            } else {
//                //应用配置
//                if (this.module.getApp().isDev()) {
//                    //调试模式
//                    this.module.getApp().getEnv().merge(c, properties);
//                } else {
//                    //jar运行模式
////                    this.module.getApp().getEnv().merge(properties.prefix(), this.module.getUrlPath(properties.path()));
//                }
//            }
////        }
//    }
//}