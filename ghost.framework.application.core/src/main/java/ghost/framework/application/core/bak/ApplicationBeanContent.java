//package ghost.framework.app.core;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import ghost.framework.app.context.IApplicationBean;
//import ghost.framework.core.application.interceptors.annotation.IApplicationAnnotationInterceptorContainer;
//import ghost.framework.core.application.interceptors.scan.IApplicationScanTypeInterceptorContainer;
//import ghost.framework.app.core.event.*;
//import ghost.framework.app.core.interceptors.ApplicationAnnotationInterceptorContainer;
//import ghost.framework.app.core.interceptors.ApplicationScanTypeInterceptorContainer;
//import ghost.framework.beans.*;
//import ghost.framework.beans.annotation.*;
//import ghost.framework.beans.configuration.annotation.ClassProperties;
//import ghost.framework.beans.annotation.module.annotation.ModuleTempDirectory;
//import ghost.framework.beans.utils.OrderAnnotationUtil;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.context.env.EnvironmentInvalidException;
//import ghost.framework.context.env.IEnvironment;
//import ghost.framework.core.event.bean.IBeanEventListenerContainer;
//import ghost.framework.core.event.obj.IObjectEventListenerContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.context.module.exception.*;
//import ghost.framework.module.context.ModuleDirectoryFile;
//import ghost.framework.module.util.PropertiesUtil;
//import ghost.framework.util.*;
//import net.sf.cglib.proxy.InterfaceMaker;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ConcurrentSkipListMap;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用绑定基础类
// * @Date: 22:43 2019/5/22
// */
//abstract class ApplicationBeanContent extends ApplicationEventContent implements IApplicationBean {
//    /**
//     * 初始化应用env基础类
//     *
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationBeanContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationBeanContent");
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationBeanContent.init->Before");
//        super.init();
//        //创建绑定地图
//        this.beanMap = new ConcurrentSkipListMap<>();
//        synchronized (this.beanMap) {
//            try {
//                //初始化添加绑定对象
//                this.bean(this);
//                //初始化json对象
//                this.bean(new ObjectMapper());
//                //创建绑定cglib代理创建接口类
//                this.bean(new InterfaceMaker());
//                //绑定应用类加载器
//                this.bean(this.getClassLoader());
//                //
//                this.bean(new ApplicationBeanEventListenerContainer());
//                this.initializeBeanEvent = true;
//                //初始化事件
//                this.bean(new ApplicationEnvEventListenerContainer());
//                //添加env
//                this.bean(this.env);
//                //初始化扫描类型拦截器容器
//                this.bean(new ApplicationScanTypeInterceptorContainer());
//                //初始化注释拦截器容器
//                this.bean(new ApplicationAnnotationInterceptorContainer());
//                //
//                this.bean(new ApplicationModuleBeanEventListenerContainer());
//                //
//                this.bean(new ApplicationObjectEventListenerContainer());
//                //
//                this.bean(new ApplicationModuleEnvEventListenerContainer());
//                //
//                this.bean(new ApplicationModuleEventListenerContainer());
//                //
//                this.bean(new ApplicationJarLoaderEventListenerContainer());
//                //
//                this.bean(new ApplicationModuleObjectEventListenerContainer());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        //加载确定类所在包的全体扫描
//        this.getLog().info("~ApplicationBeanContent.init->After");
//    }
//
//    /**
//     * 初始化绑定事件
//     */
//    private boolean initializeBeanEvent = false;
//
//    @Override
//    public void bean(IBeanDefinition definition) {
//
//    }
//
//    @Override
//    public Object getAnnotationBean(Class<? extends Annotation> a) {
//        return null;
//    }
//
//    @Override
//    public <R> R getAnnotationBean(Class<? extends Annotation> a, Class<R> r) {
//        return null;
//    }
//
//    /**
//     * 获取扫描类型拦截器容器接口
//     *
//     * @return
//     */
//    @Override
//    public IApplicationScanTypeInterceptorContainer getScanTypeInterceptorContainer() {
//        return this.getBean(IApplicationScanTypeInterceptorContainer.class);
//    }
//
//    /**
//     * 获取注释拦截器容器
//     *
//     * @return
//     */
//    @Override
//    public IApplicationAnnotationInterceptorContainer getAnnotationInterceptorContainer() {
//        return this.getBean(IApplicationAnnotationInterceptorContainer.class);
//    }
//
//    /**
//     * 添加绑定对象
//     *
//     * @param value 绑定key
//     * @param o   绑定对象
//     */
//    @Override
//    public void bean(String value, Object o) {
//        //创建绑定定义
//        this.loader((new IBeanDefinition(value, o)));
//    }
//
//    /**
//     * 添加绑定对象
//     *
//     * @param o 绑定对象
//     */
//    @Override
//    public void bean(Object o) throws Exception {
//        //对象注入
//        this.injection(o);
//        //创建绑定定义
//        this.loader(new IBeanDefinition(o));
//    }
//
//    /**
//     * 删除绑定对象
//     *
//     * @param o 绑定对象
//     */
//    @Override
//    public void removeBean(Object o) {
//        //创建定义key
//        IBeanDefinition d = null;
//        //声明基础删除绑定出现错误
//        Exception exception = null;
//        try {
//            synchronized (this.beanMap) {
//                d = this.beanMap.get(o.getClass().getName());
//                //卸载前事件
//                if (this.initializeBeanEvent) {
////                    this.getBeanEventListenerFactoryContainer().uninstallBeanEventBefore(d);
//                }
//                this.beanMap.remove(d);
//            }
//        } catch (Exception e) {
//            //删除绑定出现错误
//            exception = e;
//        } finally {
//            if (d == null) {
//                this.getLog().info("removeBean->" + o.toString());
//            } else {
//                this.getLog().info("removeBean->" + d.toString());
//            }
//        }
//        this.dClose(d);
//    }
//
//    /**
//     * 释放资源
//     *
//     * @param d
//     */
//    private void dClose(IBeanDefinition d) {
//        //卸载前事件
//        if (this.initializeBeanEvent) {
////            this.getBeanEventListenerFactoryContainer().uninstallBeanEventAfter(d);
//        }
//        //判断绑定定义对象是否需要释放资源
//        if (d != null && d.getObject() != null) {
//            //
//            if (d.getObject() instanceof AutoCloseable) {
//                try {
//                    ((AutoCloseable) d.getObject()).close();
//                } catch (Exception e) {
//                    ExceptionUtil.debugOrError(this.getLog(), e);
//                }
//            }
//        }
//    }
//
//    /**
//     * 添加绑定
//     *
//     * @param c 绑定类型
//     */
//    @Override
//    public void bean(Class<?> c) throws Exception {
//        //创建类实例
//        this.loader(new IBeanDefinition(c.getName(), this.newModuleInstance(c)));
//    }
//
//    /**
//     * 添加绑定
//     *
//     * @param value 绑定key
//     * @param c   绑定类型
//     */
//    @Override
//    public void bean(String value, Class<?> c) throws Exception {
//        //创建类实例
//        this.loader(new IBeanDefinition(value, this.newModuleInstance(c)));
//    }
//
//
//    /**
//     * 添加定义
//     *
//     * @param d 绑定定义
//     */
//    private void loader(IBeanDefinition d) {
//        //绑定前事件
////        if (this.initializeBeanEvent) {
////            this.getBeanEventListenerFactoryContainer().registeredBeanEventBefore(d);
////        }
//        synchronized (this.beanMap) {
//            this.beanMap.put(d.getName(), d);
//        }
//        //绑定后事件
////        if (this.initializeBeanEvent) {
////            this.getBeanEventListenerFactoryContainer().registeredBeanEventAfter(d);
////        }
//        //
//        this.getLog().info("loader Bean:" + d.toString());
//    }
//
//    /**
//     * 绑定定义列表
//     */
//    private ConcurrentSkipListMap<String, IBeanDefinition> beanMap;
//
//    /**
//     * 后去绑定定义列表
//     *
//     * @return
//     */
//    public ConcurrentSkipListMap<String, IBeanDefinition> getBeanMap() {
//        return beanMap;
//    }
//
////    /**
////     * 获取绑定key与域对象
////     *
////     * @param scope 绑定域
////     * @param value   绑定key
////     * @return
////     * @throws IllegalArgumentException
////     */
////    @Override
////    public Object getBeanScopeAndIdObject(String scope, String value) throws IllegalArgumentException {
////        if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(scope)) {
////            return this.getBeanScopeAndIdObject(value, scope);
////        }
////        if (!StringUtils.isEmpty(value) && StringUtils.isEmpty(scope)) {
////            return this.getBeanKeyObject(value);
////        }
////        throw new IllegalArgumentException(this.getClass().getName() + "->getBeanScopeAndIdObject->value:" + value + ",scope:" + scope);
////    }
//
////    /**
////     * 获取绑定类型对象
////     *
////     * @param className 绑定类型名称
////     * @return
////     * @throws IllegalArgumentException
////     */
////    @Override
////    public Object getBean(String className) throws IllegalArgumentException {
////        return BeanUtil.getBeanObject(this, this.beanMap, className);
////    }
//
//    /**
//     * 获取绑定类型对象
//     *
//     * @param c 绑定类型
//     * @return
//     */
//    @Override
//    public <T> T getBean(Class<T> c) {
//        //判断是否未接口
//        if (c.isInterface()) {
//            return (T) BeanUtil.getInterfaceBeanDefinition(this.beanMap, c).getObject();
//        }
//        return (T) this.getBean(c.getName());
//    }
//
//    @Override
//    public <T> T getBean(Autowired autowired, Class<?> c) throws IllegalArgumentException {
//        if (!autowired.value().equals("")) {
//            return this.getBean(autowired.value());
//        }
//        return (T) this.getBean(c);
//    }
//
//    /**
//     * 获取绑定注释对象
//     *
//     * @param autowired 绑定注释
//     * @param c         绑定的类型
//     * @return
//     */
//    @Override
//    public <T> T getBean(ModuleAutowired autowired, Class<?> c) {
//        //判断是否使用key或域域key绑定
//        if (autowired.value().equals("")) {
//            if (autowired.value().equals("")) {
//                return (T) this.getBean(c);
//            }
//            //遍历模块
//            synchronized (this.getModuleMap()) {
//                for (Map.Entry<String, IModule> e : this.getModuleMap().entrySet()) {
//                    //找到指定模块
//                    if (autowired.value().equals(e.getName())) {
//                        //绑定对象
//                        return (T) e.getOrderValue().getBean(c);
//                    }
//                }
//            }
//        } else {
//            if (autowired.value().equals("")) {
//                return this.getBean(autowired.value());
//            }
//            //遍历模块
////            synchronized (this.getModuleMap()) {
////                for (Map.Entry<String, IModule> e : this.getModuleMap().entrySet()) {
////                    //找到指定模块
////                    if (autowired.value().equals(e.getName())) {
////                        //绑定对象
////                        return e.getOrderValue().getBean(autowired.value());
////                    }
////                }
////            }
//        }
//        throw new IllegalArgumentException(this.getClass().getName() + "->getBeanAutowiredObject->value:" + autowired.value() + ",value:" + autowired.value());
//    }
//
//    /**
//     * 获取绑定定义对象
//     *
//     * @param value 绑定key，如果没有指定绑定key侧为绑定类的全称
//     * @return
//     */
//    @Override
//    public <T> T getBean(String value) {
//        return (T) this.getBeanDefinition(value).getObject();
//    }
////    /**
////     * 按照绑定定义键获取绑定对象
////     *
////     * @param value 绑定键
////     * @return
////     */
////    @Override
////    public IBeanDefinition getBeanDefinition(BeanDefinitionKey value) {
////        return BeanUtil.getBeanDefinition(this.beanMap, value, this.rootClassLoader);
////    }
////
////    /**
////     * 按照绑定定义键获取绑定对象
////     *
////     * @param value 绑定键
////     * @param <T> 邦迪对象类型
////     * @return
////     */
////    @Override
////    public <T> T getBean(BeanDefinitionKey value) {
////        return (T) this.getBeanDefinition(value);
////    }
//
//    /**
//     * 获取绑定定义
//     *
//     * @param value 绑定key
//     * @return
//     */
//    @Override
//    public IBeanDefinition getBeanDefinition(String value) {
//        return BeanUtil.getBeanDefinition(this.beanMap, value, (ClassLoader) this.getClassLoader());
//    }
//
//    /**
//     * 获取绑定定义
//     *
//     * @param c 绑定类型
//     * @return
//     * @throws IllegalArgumentException
//     */
//    @Override
//    public IBeanDefinition getBeanDefinition(Class<?> c) throws IllegalArgumentException {
//        if (c.isInterface()) {
//            return BeanUtil.getInterfaceBeanDefinition(this.beanMap, c);
//        }
//        return this.getBeanDefinition(c.getName());
//    }
//
//    /**
//     * 删除模块绑定
//     *
//     * @param content 模块内容
//     * @param value     绑定key
//     */
//    @Override
//    public void removeModuleBean(IModule content, String value) {
//        content.removeBean(value);
//    }
//
//    /**
//     * 删除模块绑定
//     *
//     * @param value 模块key
//     * @param value      绑定key
//     */
//    @Override
//    public void removeModuleBean(String value, String value) {
//        IModule c = this.getModule(value);
//        if (c != null) {
//            c.removeBean(value);
//        }
//    }
//
//    /**
//     * 删除模块绑定
//     *
//     * @param value 模块key
//     * @param c        绑定类型
//     */
//    @Override
//    public void removeModuleBean(String value, Class<?> c) {
//        IModule m = this.getModule(value);
//        if (m != null) {
//            m.removeBean(c.getName());
//        }
//    }
//
//    /**
//     * 删除模块绑定
//     *
//     * @param value 模块key
//     * @param o        绑定对象
//     */
//    @Override
//    public void removeModuleBean(String value, Object o) {
//        this.removeModuleBean(value, o.getClass());
//    }
//
//    /**
//     * 删除模块绑定
//     *
//     * @param c 绑定类型
//     */
//    @Override
//    public void removeBean(Class<?> c) {
//        this.removeBean(c.getName());
//    }
//
//    /**
//     * 删除绑定
//     *
//     * @param value 绑定key
//     */
//    @Override
//    public void removeBean(String value) {
//        IBeanEventListenerContainer<IApplication, IBeanDefinition> beanEventListenerContainer = this.getBean(IBeanEventListenerContainer.class);
//        IBeanDefinition d;
//        synchronized (this.beanMap) {
//            d = this.beanMap.get(value);
//            //卸载前事件
//            beanEventListenerContainer.removeBeanEventBefore(this, d);
//            this.beanMap.remove(value);
//        }
//        beanEventListenerContainer.removeBeanEventAfter(this, d);
//        this.dClose(d);
//    }
//
//    /**
//     * 验证绑定是否存在
//     *
//     * @param c 绑定类型
//     * @return
//     */
//    @Override
//    public boolean beanContains(Class<?> c) {
//        return this.beanContains(c.getName());
//    }
//
//    @Override
//    public boolean beanContains(Object obj) {
//        return false;
//    }
//
//    /**
//     * 验证绑定是否存在
//     *
//     * @param value 绑定key
//     * @return
//     */
//    @Override
//    public boolean beanContains(String value) {
//        synchronized (this.beanMap) {
//            return this.beanMap.containsKey(value);
//        }
//    }
//
//    /**
//     * 后去构建类型参数列表
//     *
//     * @param module 模块内容
//     * @param c      构建
//     * @return
//     */
////    @Override
//    public Object[] newModuleInstanceParameter(IModule module, Constructor c) throws Exception {
//        //判断是否有类型析构参数
//        if (c.getParameterCount() == 0) {
//            //没有析构参数
//            return null;
//        }
//        //析构嗯都参数对象数组
//        Object[] objects = new Object[c.getParameterCount()];
//        //析构的参数类型数组
//        Class[] paramTypes = c.getParameterTypes();
//        int i = 0;
//        for (Parameter p : c.getParameters()) {
//            //判断是否注释注入参数
//            //注入属性
//            if (p.isAnnotationPresent(Autowired.class)) {
//                //获取注入注释
//                Autowired auto = p.getAnnotation(Autowired.class);
//                //是否使用模块注入
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), auto);
//                //应用内容注入参数
//                if (auto.value().equals("")) {
//                    objects[i] = this.getBean(paramTypes[i]);
//                } else {
//                    objects[i] = this.getBean(auto.value());
//                }
//                i++;
//                continue;
//            }
//            //注入属性
//            if (p.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired auto = p.getAnnotation(ModuleAutowired.class);
//                //是否使用模块注入
//                if (!auto.value().equals("")) {
//                    //获取模块内容
//                    IModule mc = auto.value().equals(module.getName()) ? module : this.getModule(auto.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(auto.value());
//                    }
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(mc.getEnv(), auto);
//                    //模块注入参数
//                    if (auto.value().equals("")) {
//                        objects[i] = this.getBean(paramTypes[i]);
//                    } else {
//                        objects[i] = module.getBean(auto.value());
//                    }
//                } else {
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(this.getEnv(), auto);
//                    //应用内容注入参数
//                    if (auto.value().equals("")) {
//                        objects[i] = this.getBean(paramTypes[i]);
//                    } else {
//                        objects[i] = this.getBean(auto.value());
//                    }
//                }
//                i++;
//                continue;
//            }
//            //注入值
//            if (p.isAnnotationPresent(Value.class)) {
//                //基础参数类型
//                //配置参数注入
//                Value v = p.getAnnotation(Value.class);
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                //应用内容注入参数
//                objects[i] = ReflectUtil.objectConverter(this.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                i++;
//                continue;
//            }
//            //注入值
//            if (p.isAnnotationPresent(ModuleValue.class)) {
//                //基础参数类型
//                //配置参数注入
//                ModuleValue v = p.getAnnotation(ModuleValue.class);
//                //是否使用模块注入
//                if (!v.value().equals("")) {
//                    IModule mc = v.value().equals(module.getName()) ? module : this.getModule(v.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(v.value());
//                    }
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(mc.getEnv(), v);
//                    //模块注入参数
//                    objects[i] = ReflectUtil.objectConverter(module.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                } else {
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                    //应用内容注入参数
//                    objects[i] = ReflectUtil.objectConverter(this.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                }
//                i++;
//                continue;
//            }
//            //注入模块目录
//            if (p.isAnnotationPresent(ModuleTempDirectory.class)) {
//                objects[i] = this.injectionParameterModuleTempDirectory(module, p);
//                i++;
//                continue;
//            }
//            //应用内容注入参数
//            if (ReflectUtil.isBasicType(paramTypes[i])) {
////                    //判断参数是否注释可空
////                    if (p.isAnnotationPresent(IsNull.class) || p.isAnnotationPresent(IsNullOrDefaultValue.class)) {
////                        //基础类型可空与默认值
////                        objects[i] = ReflectUtil.primitiveDefaultValue(p.getParameterizedType());
////                    } else {
////                        //创建对象初始化参数错误
////                        throw new IllegalArgumentException();
////                    }
//                //基础类型可空与默认值
//                objects[i] = ReflectUtil.primitiveDefaultValue(paramTypes[i]);
//            } else {
//                //判断参数是否不理会
//                if (p.isAnnotationPresent(Null.class)) {
//                    objects[i] = null;
//                } else {
//                    objects[i] = this.getBean(paramTypes[i]);
//                }
//            }
//            i++;
//        }
//        return objects;
//    }
//
//    /**
//     * 参数注入模块目录注释
//     *
//     * @param module    模块内容
//     * @param parameter 注入参数
//     */
//    private Object injectionParameterModuleTempDirectory(IModule module, Parameter parameter) {
//        //获取注入注释
//        ModuleTempDirectory directory = parameter.getAnnotation(ModuleTempDirectory.class);
//        //注入值
//        try {
//            //设置反射替换env配置值
//            this.setAnnotationReflectEnvValue(module.getEnv(), directory);
//            //判断是否制定模块
//            if (directory.value().equals("")) {
//                //content对象本身模块注入
//                return this.injectionParameterModuleTempDirectory(module, parameter, directory);
//            } else {
//                IModule mc = this.getModule(directory.value());
//                //获取绑定无效错误处理
//                if (mc == null && directory.error()) {
//                    throw new ModuleNotException(directory.value());
//                }
//                return this.injectionParameterModuleTempDirectory(mc, parameter, directory);
//            }
//        } catch (IllegalArgumentException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        }
//        return null;
//    }
//
//    /**
//     * 参数注入模块目录注释
//     *
//     * @param module    模块内容
//     * @param parameter 注入参数
//     * @param directory 模块临时模块注释
//     * @return
//     */
//    private Object injectionParameterModuleTempDirectory(IModule module, Parameter parameter, ModuleTempDirectory directory) throws Exception {
//        //获取模块临时模块
//        ModuleDirectoryFile file = module.getAnnotationBean(TempDirectory.class, ModuleDirectoryFile.class);
//        //获取绑定无效错误处理
//        if (file == null && directory.error()) {
//            throw new ModuleBeanNotException(parameter.toString());
//        }
//        //注入String类型路径
//        if (parameter.getType().getTypeName().equals(String.class.getTypeName())) {
//            return file.getPath();
//        }
//        //注入File文件对象类型
//        if (parameter.getType().getTypeName().equals(File.class.getTypeName())) {
//            return file;
//        }
//        return null;
//    }
//
//    /**
//     * 创建实例
//     *
//     * @param module 模块内容，为创建该类的模块
//     * @param c      构建类
//     * @return
//     */
//    @Override
//    public Object newModuleInstance(IModule module, Class<?> c) throws Exception {
//        Object o = null;
//        //获取类构建函数列表
//        Constructor[] constructors = c.getConstructors();
//        //判断是否有函数构建函数
//        if (constructors == null || constructors.length == 0) {
//            //没有构建函数
//            o = c.newModuleInstance();
//        } else if (constructors.length == 1) {
//            //有一个构建函数实例类对象
//            if (constructors[0].getParameterCount() == 0) {
//                //没有构建函数
//                o = c.newModuleInstance();
//            } else {
//                o = constructors[0].newModuleInstance(this.newModuleInstanceParameter(module, constructors[0]));
//            }
//        } else {
//            //遍历构建函数列表
//            for (Constructor constructor : constructors) {
//                //判断是否注释了入口函数
//                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
//                    //指定入口函数构建对象
//                    o = constructor.newModuleInstance(this.newModuleInstanceParameter(module, constructor));
//                    break;
//                }
//            }
//        }
//        if (o == null) {
//            //该类没有实例化错误
//            throw new InstantiationException(c.getName());
//        }
//        //检查是否需要注入配置
//        if (c.isAnnotationPresent(ClassProperties.class)) {
//            this.injectionClassProperties(c, o, c.getAnnotation(ClassProperties.class));
//        }
//        //注入值
//        this.injection(module, o);
//        //注册创建对象
////        this.getModuleObjectEventListenerContainer().registrarObjectEvent(content, o);
//        //返回创建对象
//        return o;
//    }
//
//    /**
//     * 注入值
//     *
//     * @param o 要注入的对象
//     */
//    @Override
//    public void injection(Object o) throws Exception {
//        for (Method method : o.getClass().getDeclaredMethods()) {
//            if (Modifier.isStatic(method.getModifiers())) {
//                continue;
//            }
//            //判断是否注入
//            if (method.isAnnotationPresent(Autowired.class)) {
//                //获取注入注释
//                Autowired a = method.getAnnotation(Autowired.class);
//                //使用应用注入
//                //调用函数
//                method.invoke(o, this.newMethodParameter(method));
//                continue;
//            }
//            //判断是否注入
//            if (method.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired a = method.getAnnotation(ModuleAutowired.class);
//                //是否使用模块注入
//                if (!a.value().equals("")) {
//                    //使用模块注入
//                    IModule c = this.getModule(a.value());
//                    if (c == null) {
//                        //throw new ModuleInvalidException(a.value());
//                    }
//                    //调用函数
//                    method.invoke(o, this.newMethodParameter(c, method));
//                } else {
//                    //使用应用注入
//                    //调用函数
//                    method.invoke(o, this.newMethodParameter(method));
//                }
//                continue;
//            }
//        }
//        //遍历注册对象列表
//        for (Field field : o.getClass().getDeclaredFields()) {
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//            //判断是否注入
//            if (field.isAnnotationPresent(Autowired.class)) {
//                //使用应用注入
//                this.injectionAutowired(o, field);
//                continue;
//            }
//            //判断是否注入
//            if (field.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired a = field.getAnnotation(ModuleAutowired.class);
//                //是否使用模块注入
//                if (!a.value().equals("")) {
//                    //使用模块注入
//                    IModule c = this.getModule(a.value());
//                    if (c == null) {
//                        throw new ModuleInvalidException(a.value());
//                    }
//                    this.injectionModuleAutowired(c, o, field);
//                } else {
//                    //使用应用注入
//                    this.injectionAutowired(o, field);
//                }
//                continue;
//            }
//            //判断不为静态熟悉声明注入值
//            if (field.isAnnotationPresent(Value.class)) {
//                Value v = field.getAnnotation(Value.class);
//                this.injectionValue(v, o, field);
//                continue;
//            }
//            //判断不为静态熟悉声明注入值
//            if (field.isAnnotationPresent(ModuleValue.class)) {
//                ModuleValue v = field.getAnnotation(ModuleValue.class);
//                if (v.value().equals("")) {
//                    //使用模块注入
//                    IModule c = this.getModule(v.value());
//                    if (c == null) {
//                        throw new ModuleInvalidException(v.value());
//                    }
//                    this.injectionValue(c, o, field);
//                } else {
//                    this.injectionModuleValue(v, o, field);
//                }
//                continue;
//            }
//            //注入模块模块注释
//            if (field.isAnnotationPresent(ModuleTempDirectory.class)) {
//                ModuleTempDirectory d = field.getAnnotation(ModuleTempDirectory.class);
//                //使用模块注入
//                IModule c = this.getModule(d.value());
//                if (c == null) {
//                    throw new ModuleInvalidException(d.value());
//                }
//                this.injectionModuleTempDirectory(c, o, field);
//                continue;
//            }
//        }
//    }
//
//    public void injectionModuleValue(ModuleValue v, Object o, Field field) {
//        //判断注入参数是否有效
////        if (v.value().equals("")) {
////            ExceptionUtil.debugOrError(this.log, new Exception("模块{Id:" + content.getId() + "}类型{Class:" + o.getClass().getName() + "}注入参数值无效例名称{Field:" + field.getName() + "}"));
////        } else {
////            //注入值
////            try {
////                //设置反射替换env配置值
////                this.setAnnotationReflectEnvValue(this.getEnv(), v);
////                //注入全局配置值
////                ReflectUtil.setFieldValue(o, field, this.getEnv().get(v.prefix(), v.value()));
////            } catch (IllegalArgumentException e) {
////                ExceptionUtil.debugOrError(this.log, e);
////            } catch (IllegalAccessException e) {
////                ExceptionUtil.debugOrError(this.log, e);
////            } catch (Exception e) {
////                ExceptionUtil.debugOrError(this.log, e);
////            }
////        }
//    }
//
////    /**
////     * 注入函数
////     * @param content 模块内容
////     * @param o 函数拥有者对象
////     * @param method 函数对象
////     * @throws IllegalArgumentException
////     */
////    public  void method(IModule content, Object o, Method method) throws IllegalArgumentException {
////        //获取注入注释
////        Autowired a = method.getAnnotation(Autowired.class);
////        //注入值
////        try {
////            //获取函数参数列表
////            Parameter[] parameters = method.getParameters();
////            if (parameters == null || parameters.length == 0) {
////                throw new IllegalArgumentException("parameters is null error");
////            }
////            //判断注入是否指定模块
////            if (a.module()) {
////                //获取需要获取绑定注入的模块
////                IModule mc;
////                //判断指定注入模块是否为本注入引用模块内容
////                if (a.value().equals(content.getId())) {
////                    mc = content;
////                } else {
////                    mc = this.getModule(a.value());
////                }
////                //判断获取模块无效是否引发错误
////                if (mc == null) {
////                    //是否引发错误
////                    if (a.error()) {
////                        throw new ModuleInvalidException(a.value());
////                    }
////                }
////                //调用函数
////                method.invoke(o, this.newMethodParameter(mc, method));
////            } else {
////                //调用函数
////                method.invoke(o, this.newMethodParameter(method));
////            }
////        } catch (IllegalArgumentException e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        } catch (IllegalAccessException e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        } catch (Exception e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        }
////    }
//
//    /**
//     * 注入值
//     *
//     * @param module 模块内容
//     * @param o      要注入的对象
//     */
//    @Override
//    public void injection(IModule module, Object o) throws Exception {
//        //遍历注册对象列表
//        for (Field field : o.getClass().getDeclaredFields()) {
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//            //判断是否
//            if (field.isAnnotationPresent(Autowired.class)) {
//                //获取注入注释
//                Autowired a = field.getAnnotation(Autowired.class);
//                this.injectionAutowired(module, o, field);
//                continue;
//            }
//            //判断是否
//            if (field.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired a = field.getAnnotation(ModuleAutowired.class);
//                //是否使用模块注入
//                if (!a.value().equals("")) {
//                    //获取模块内容
//                    IModule mc = this.getModule(a.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(a.value());
//                    }
//                    //使用模块注入
//                    this.injectionModuleAutowired(mc, o, field);
//                } else {
//                    //使用应用注入
//                    this.injectionModuleAutowired(module, o, field);
//                }
//                continue;
//            }
//            //判断不为静态熟悉声明注入值
//            if (field.isAnnotationPresent(Value.class)) {
//                this.injectionValue(module, o, field);
//                continue;
//            }
//            //注入模块模块注释
//            if (field.isAnnotationPresent(ModuleTempDirectory.class)) {
//                this.injectionModuleTempDirectory(module, o, field);
//                continue;
//            }
//        }
//    }
//
//    /**
//     * 注入模块目录注释
//     *
//     * @param module    模块内容
//     * @param o         注入对象
//     * @param field     注入对象属性
//     * @param directory 注入对象注释
//     * @throws ModuleBeanNotException
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    private void injectionModuleTempDirectory(IModule module, Object o, Field field, ModuleTempDirectory directory) throws ModuleBeanNotException, IllegalArgumentException, IllegalAccessException {
//        //获取模块临时模块
//        ModuleDirectoryFile file = module.getAnnotationBean(TempDirectory.class, ModuleDirectoryFile.class);
//        //获取绑定无效错误处理
//        if (file == null && directory.error()) {
//            throw new ModuleBeanNotException(field.toString());
//        }
//        //注入String类型路径
//        if (field.getGenericType().getTypeName().equals(String.class.getTypeName())) {
//            ReflectUtil.setField(o, field, file.getPath());
//            return;
//        }
//        //注入File文件对象类型
//        if (field.getGenericType().getTypeName().equals(File.class.getTypeName())) {
//            ReflectUtil.setField(o, field, file);
//        }
//    }
//
//    /**
//     * 注入模块目录注释
//     *
//     * @param module 模块内容
//     * @param o      注入对象
//     * @param field  注入对象属性
//     */
//    private void injectionModuleTempDirectory(IModule module, Object o, Field field) {
//        //获取注入注释
//        ModuleTempDirectory directory = field.getAnnotation(ModuleTempDirectory.class);
//        //注入值
//        try {
//            //判断是否制定模块
//            if (directory.value().equals("")) {
//                //module
//                this.injectionModuleTempDirectory(module, o, field, directory);
//            } else {
//                IModule mc = this.getModule(directory.value());
//                //获取绑定无效错误处理
//                if (mc == null && directory.error()) {
//                    throw new ModuleNotException(directory.value());
//                }
//                this.injectionModuleTempDirectory(mc, o, field, directory);
//            }
//        } catch (IllegalArgumentException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (IllegalAccessException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        }
//    }
//
//    /**
//     * 绑定对象
//     *
//     * @param o
//     * @throws Exception
//     */
//    private void forBean(Object o) throws Exception {
//        //绑定函数列表
//        List<BeanMethod> list = new ArrayList<>();
//        //获取函数注释列表
//        for (Method m : o.getClass().getDeclaredMethods()) {
//            if (m.isAnnotationPresent(Bean.class)) {
//                list.loader(new BeanMethod(m));
//                continue;
//            }
//            if (m.isAnnotationPresent(ConditionalOnMissingBean.class)) {
//                list.loader(new BeanMethod(m));
//                continue;
//            }
//            if (m.isAnnotationPresent(ModuleBean.class)) {
//                list.loader(new BeanMethod(m));
//                continue;
//            }
//            if (m.isAnnotationPresent(ConditionalOnMissingModuleBean.class)) {
//                list.loader(new BeanMethod(m));
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
//    private void initBean(Object o, Method m) throws Exception {
//        //绑定应用
//        //获取绑定注释
//        Bean bean = m.getAnnotation(Bean.class);
//        //判断注入类型
//        if (bean.injectionList().length > 0) {
//            //遍历注入类型列表
//            for (Class<?> c : bean.injectionList()) {
////                this.bean(c);
//            }
//        }
//        //验证不存在时绑定
//        if (m.getReturnType().equals(Void.TYPE)) {
//            //没有返回函数绑定，只做调用
//            m.invoke(o, this.newMethodParameter(m));
//        } else {
//            //有返回函数绑定
////            this.bean(bean.value(), this.beanInjection(o, m, bean));
//        }
//    }
//
//    private Class<?> beanInjection(Object o, Method m, Bean bean) {
//        return null;
//    }
//
//    private void initConditionalOnMissingBean(Object o, Method m) throws ModuleInvalidException, ModuleBeanException,
//            InstantiationException, IllegalAccessException, IllegalArgumentException,
//            InvocationTargetException, IOException, NoSuchFieldException, EnvironmentInvalidException {
//        //获取绑定注释
//        ConditionalOnMissingBean bean = m.getAnnotation(ConditionalOnMissingBean.class);
//        //判断注入模式
//        //验证不存在时绑定
////        if (m.getReturnType().equals(Void.TYPE)) {
////            //没有返回函数绑定，只做调用
////            m.invoke(o, this.newMethodParameter(this.content, m));
////        } else {
////            //有返回函数绑定
////            this.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////        }
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
////            //绑定应用
////            //验证不存在时绑定
////            if (m.getReturnType().equals(Void.TYPE)) {
////                //没有返回函数绑定，只做调用
////                m.invoke(o, this.content.getApplicationContent().newMethodParameter(this.content, m));
////            } else {
////                //有返回函数绑定
////                this.content.getApplicationContent().bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////            }
////        }
//    }
//
//    private void initConditionalOnMissingModuleBean(Object o, Method m) {
//
//    }
//
//    private void initModuleBean(Object o, Method m) {
//
//    }
//
//    /**
//     * 应用注入对象
//     *
//     * @param o     注入值所属对象
//     * @param field 注入例对象
//     */
//    private void injectionAutowired(Object o, Field field) {
//        //获取注入注释
//        Autowired a = field.getAnnotation(Autowired.class);
//        //注入值
//        try {
//            Object obj = this.getBean(a, field.getType());
//            if (obj == null && a.error()) {
//                throw new BeanNotException(field);
//            }
//            //注入全局配置值
//            ReflectUtil.setField(o, field, obj);
//        } catch (IllegalArgumentException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (IllegalAccessException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        }
//    }
//
////    /**
////     * 模块注入对象
////     *
////     * @param content 模块内容
////     * @param o       注入值所属对象
////     * @param field   注入例对象
////     */
////    @Override
////    public void injectionModuleAutowired(IModule content, Object o, Field field) {
////        //获取注入注释
////        Autowired a = field.getAnnotation(Autowired.class);
////        //注入类型
////        try {
////            //判断是否未声明域与key
////            if (a.scope().equals("") && a.value().equals("")) {
////                //判断是否为模块env
////                if (field.getType().equals(ModuleEnvironment.class)) {
////                    ReflectUtil.setField(o, field, content.getEnv());
////                    return;
////                }
////                //判断是否为模块内容
////                if (field.getType().equals(IModule.class)) {
////                    ReflectUtil.setField(o, field, content);
////                    return;
////                }
////            }
////            //注入模块其它类型
////            ReflectUtil.setField(o, field, content.getBean(a, field.getType()));
////        } catch (IllegalArgumentException e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        } catch (IllegalAccessException e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        } catch (Exception e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        }
////    }
//
//    /**
//     * 注入对象
//     *
//     * @param module 模块内容
//     * @param o      注入值所属对象
//     * @param field  注入例对象
//     */
//    @Override
//    public void injectionModuleAutowired(IModule module, Object o, Field field) {
//        //获取注入注释
//        ModuleAutowired a = field.getAnnotation(ModuleAutowired.class);
//        //注入值
//        try {
//            //判断注入是否指定模块
//            if (!a.value().equals("")) {
////                //设置反射替换env配置值
////                this.setAnnotationReflectEnvValue(content.getEnv(), a);
////                //判断是否有指定注入的模块id
////                if (a.value().equals("")) {
////                    ReflectUtil.setField(o, field, content.getBean(a, field.getGenericType().getTypeName()));
////                } else {
//                //获取需要获取绑定注入的模块
//                IModule c;
//                //判断指定注入模块是否为本注入引用模块内容
//                if (a.value().equals(module.getName())) {
//                    c = module;
//                } else {
//                    c = this.getModule(a.value());
//                }
//                this.setAnnotationReflectEnvValue(module.getEnv(), a);
//                //判断获取模块无效是否引发错误
//                if (c == null) {
//                    //是否引发错误
//                    if (a.error()) {
//                        throw new ModuleInvalidException(a.value());
//                    }
//                } else {
//                    //注入模块配置值
//                    ReflectUtil.setField(o, field, c.getBean(a, field.getType()));
//                }
////                }
//            } else {
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), a);
//                ReflectUtil.setField(o, field, this.getBean(a, field.getType()));
//            }
//        } catch (IllegalArgumentException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (IllegalAccessException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        }
//    }
//
//    /**
//     * @param v
//     * @param o
//     * @param field
//     */
//    private void injectionValue(Value v, Object o, Field field) {
//        try {
//            //设置反射替换env配置值
//            this.setAnnotationReflectEnvValue(this.getEnv(), v);
//            //注入全局配置值
//            ReflectUtil.setFieldValue(o, field, this.getEnv().get(v.prefix(), v.value()));
//        } catch (IllegalArgumentException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (IllegalAccessException e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        }
//    }
//
//    /**
//     * 注入值
//     *
//     * @param module 模块内容
//     * @param o      注入值所属对象
//     * @param field  注入例对象
//     */
//    private void injectionValue(IModule module, Object o, Field field) {
//        //获取注入参数
//        Value v = field.getAnnotation(Value.class);
//        //判断注入参数是否有效
//        if (v.value().equals("")) {
//            ExceptionUtil.debugOrError(this.getLog(), new Exception("模块{Id:" + module.getApp() + "}类型{Class:" + o.getClass().getName() + "}注入参数值无效例名称{Field:" + field.getName() + "}"));
//        } else {
//            //注入值
//            try {
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                //注入全局配置值
//                ReflectUtil.setFieldValue(o, field, this.getEnv().get(v.prefix(), v.value()));
//            } catch (IllegalArgumentException e) {
//                ExceptionUtil.debugOrError(this.getLog(), e);
//            } catch (IllegalAccessException e) {
//                ExceptionUtil.debugOrError(this.getLog(), e);
//            } catch (Exception e) {
//                ExceptionUtil.debugOrError(this.getLog(), e);
//            }
//        }
//    }
//
//    /**
//     * 注入配置参数
//     *
//     * @param c
//     * @param o
//     * @param classProperties
//     * @throws IOException
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws NoSuchFieldException
//     */
//    private void injectionClassProperties(Class<?> c, Object o, ClassProperties classProperties) throws Exception {
//        //设置反射替换env配置值
//        this.setAnnotationReflectEnvValue(this.getEnv(), classProperties);
//        //声明注入配置
//        Properties p = null;
//        //判断是否为调试模式
//        if (AssemblyUtil.isClassDev(c)) {
//            //获取当前类所在的开发目录配置文件
//            p = PropertiesUtil.readProperties(c, classProperties.path());
//        } else {
//            //获取jar运行包的目录配置文件
//            p = PropertiesUtil.readProperties(c.getProtectionDomain().getCodeSource().getLocation().getPath(), classProperties.path());
//        }
//        //遍历注入值
//        for (Field f : c.getFields()) {
//            if (p.containsKey(f.getName())) {
//                f.setAccessible(true);
//                f.set(o, p.get(f.getName()));
//            }
//        }
//    }
//
//    /**
//     * 构建类
//     *
//     * @param constructor
//     * @return
//     */
//    @Override
//    public Object newModuleInstance(Constructor constructor) throws Exception {
//        //有一个构建函数实例类对象
//        if (constructor.getParameterCount() == 0) {
//            //没有参数构建类
//            return constructor.newModuleInstance();
//        } else {
//            //有参数构建类
//            return constructor.newModuleInstance(this.newModuleInstanceParameter(constructor));
//        }
//    }
//
//    /**
//     * 创建实例
//     *
//     * @param c 构建类
//     * @return
//     */
//    @Override
//    public Object newModuleInstance(Class<?> c) throws Exception {
//        Object o = null;
//        //获取类构建函数列表
//        Constructor[] constructors = c.getConstructors();
//        //判断是否有函数构建函数
//        if (constructors == null || constructors.length == 0) {
//            //没有构建函数
//            try {
//                o = c.newModuleInstance();
//            } catch (Exception e) {
//                throw new RuntimeException(e.getMessage(), e);
//            }
//        } else if (constructors.length == 1) {
//            //有一个构建函数实例类对象
//            try {
//                o = this.newModuleInstance(constructors[0]);
//            } catch (Exception e) {
//                throw new RuntimeException(e.getMessage(), e);
//            }
//        } else {
//            //遍历构建函数列表
//            for (Constructor constructor : constructors) {
//                //判断是否注释了入口函数
//                if (constructor.isAnnotationPresent(ghost.framework.beans.annotation.constructor.Constructor.class)) {
//                    try {
//                        //指定入口函数构建对象
//                        o = this.newModuleInstance(constructors[0]);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e.getMessage(), e);
//                    }
//                    break;
//                }
//            }
//        }
//        if (o == null) {
//            //该类没有实例化错误
//            throw new RuntimeException(new InstantiationException(c.getName()));
//        }
//        //检查是否需要注入配置
//        if (c.isAnnotationPresent(ClassProperties.class)) {
//            this.injectionClassProperties(c, o, c.getAnnotation(ClassProperties.class));
//        }
//        //注入值
//        this.injection(o);
//        //绑定
////        this.forBean(o);
//        //注册创建对象
//        this.getBean(IObjectEventListenerContainer.class).addEventAfter(this, o);
//        //返回创建对象
//        return o;
//    }
//
//    /**
//     * 后去构建类型参数列表
//     *
//     * @param c 构建
//     * @return
//     */
//    @Override
//    public Object newModuleInstanceParameter(Constructor c) throws Exception {
//        if (c.getParameterCount() == 0) {
//            return null;
//        }
//        //析构的参数对象素组
//        Object[] objects = new Object[c.getParameterCount()];
//        //析构的参数类型数组
//        Class[] paramTypes = c.getParameterTypes();
//        int i = 0;
//        for (Parameter p : c.getParameters()) {
//            //判断是否注释注入参数
//            if (p.isAnnotationPresent(Autowired.class)) {
//                //获取注入注释
//                Autowired auto = p.getAnnotation(Autowired.class);
//                //
//                if (auto.value().equals("")) {
//                    objects[i] = this.getBean(paramTypes[i]);
//                } else {
//                    objects[i] = this.getBean(auto.value());
//                }
//                i++;
//                continue;
//            }
//            //判断是否注释注入参数
//            if (p.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired auto = p.getAnnotation(ModuleAutowired.class);
//                if (!auto.value().equals("")) {
//                    //应用内容注入参数
//                    objects[i] = this.getModuleBean(auto, paramTypes[i]);
//                } else {
//                    //
//                    objects[i] = this.getBean(auto.value());
//                }
//                i++;
//                continue;
//            }
//            if (p.isAnnotationPresent(Value.class)) {
//                //基础参数类型
//                //配置参数注入
//                Value value = p.getAnnotation(Value.class);
//                this.setAnnotationReflectEnvValue(this.env, value);
//                //应用内容注入参数
//                objects[i] = ReflectUtil.objectConverter(this.getEnv().get(value.prefix(), value.value()), paramTypes[i]);
//                i++;
//                continue;
//            }
//            if (p.isAnnotationPresent(ModuleValue.class)) {
//                //基础参数类型
//                //配置参数注入
//                ModuleValue value = p.getAnnotation(ModuleValue.class);
//                if (!value.value().equals("")) {
//                    //获取需要获取绑定注入的模块
//                    IModule mc = null;
//                    //判断指定注入模块是否为本注入引用模块内容
//                    if (!value.value().equals("")) {
//                        mc = this.getModule(value.value());
//                    }
//                    //判断获取模块无效是否引发错误
//                    if (mc == null) {
//                        //是否引发错误
//                        if (value.error()) {
//                            throw new ModuleInvalidException(value.value());
//                        }
//                    } else {
//                        this.setAnnotationReflectEnvValue(mc.getEnv(), value);
//                        //应用内容注入参数
//                        objects[i] = ReflectUtil.objectConverter(this.getEnv().get(value.prefix(), value.value()), paramTypes[i]);
//                    }
//                } else {
//                    this.setAnnotationReflectEnvValue(this.env, value);
//                    //应用内容注入参数
//                    objects[i] = ReflectUtil.objectConverter(this.getEnv().get(value.prefix(), value.value()), paramTypes[i]);
//                }
//                i++;
//                continue;
//            }
//            //应用内容注入参数
//            if (ReflectUtil.isBasicType(paramTypes[i])) {
////                    //判断参数是否注释可空
////                    if (p.isAnnotationPresent(IsNull.class) || p.isAnnotationPresent(IsNullOrDefaultValue.class)) {
////                        //基础类型可空与默认值
////                        objects[i] = ReflectUtil.primitiveDefaultValue(p.getParameterizedType());
////                    } else {
////                        //创建对象初始化参数错误
////                        throw new IllegalArgumentException();
////                    }
//                //基础类型可空与默认值
//                objects[i] = ReflectUtil.primitiveDefaultValue(paramTypes[i]);
//                i++;
//                continue;
//            }
//            //判断参数是否不理会
//            if (p.isAnnotationPresent(Null.class)) {
//                objects[i] = null;
//            } else {
//                objects[i] = this.getBean(paramTypes[i]);
//            }
//            i++;
//        }
//        return objects;
//    }
//
//    /**
//     * 获取模块绑定对象
//     *
//     * @param auto 注入注释
//     * @param c    绑定类型
//     * @return
//     */
//    private Object getModuleBean(ModuleAutowired auto, Class<?> c) throws Exception {
//        //判断是否有效注释模块id
//        if (auto.value().equals("")) {
//            throw new ModuleBeanEmptyException(c.getName());
//        }
//        //获取模块内容
//        IModule content = this.getModule(auto.value());
//        if (content == null) {
//            throw new ModuleNotException(auto.value());
//        }
//        //获取模块绑定对象
//        if (auto.value().equals("")) {
//            return content.getBean(c);
//        }
//        return content.getBean(auto.value());
//    }
//
//    /**
//     * 创建函数参数
//     *
//     * @param m
//     * @return
//     */
//    @Override
//    public Object[] newMethodParameter(Method m) throws Exception {
//        if (m.getParameterCount() == 0) {
//            return null;
//        }
//        //函数的参数对象数组
//        Object[] objects = new Object[m.getParameterCount()];
//        //函数的参数类型数组
//        Class[] paramTypes = m.getParameterTypes();
//        int i = 0;
//        for (Parameter p : m.getParameters()) {
//            //判断Autowired注释
//            if (p.isAnnotationPresent(Autowired.class)) {
//                //获取注入注释
//                Autowired auto = p.getAnnotation(Autowired.class);
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), auto);
//                //应用对象注入
//                if (auto.value().equals("")) {
//                    objects[i] = this.getBean(paramTypes[i]);
//                } else {
//                    objects[i] = this.getBean(auto.value());
//                }
//                i++;
//                continue;
//            }
//            //判断Autowired注释
//            if (p.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired auto = p.getAnnotation(ModuleAutowired.class);
//                if (!auto.value().equals("")) {
//                    IModule mc = this.getModule(auto.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(auto.value());
//                    }
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(mc.getEnv(), auto);
//                    //模块对象注入
//                    if (auto.value().equals("")) {
//                        objects[i] = mc.getBean(paramTypes[i]);
//                    } else {
//                        objects[i] = mc.getBean(auto.value());
//                    }
//                } else {
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(this.getEnv(), auto);
//                    //应用对象注入
//                    if (auto.value().equals("")) {
//                        objects[i] = this.getBean(paramTypes[i]);
//                    } else {
//                        objects[i] = this.getBean(auto.value());
//                    }
//                }
//                i++;
//                continue;
//            }
//            //判断Value注释
//            if (p.isAnnotationPresent(Value.class)) {
//                //基础参数类型
//                //配置参数注入
//                Value v = p.getAnnotation(Value.class);
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                //应用参数注入
//                objects[i] = ReflectUtil.objectConverter(this.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                i++;
//                continue;
//            }
//            //判断Value注释
//            if (p.isAnnotationPresent(ModuleValue.class)) {
//                //基础参数类型
//                //配置参数注入
//                ModuleValue v = p.getAnnotation(ModuleValue.class);
//                if (!v.value().equals("")) {
//                    IModule mc = this.getModule(v.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(v.value());
//                    }
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(mc.getEnv(), v);
//                    //模块参数注入
//                    objects[i] = ReflectUtil.objectConverter(mc.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                } else {
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                    //应用参数注入
//                    objects[i] = ReflectUtil.objectConverter(this.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                }
//                i++;
//                continue;
//            }
//            //注入模块目录
//            if (p.isAnnotationPresent(ModuleTempDirectory.class)) {
//                ModuleTempDirectory d = p.getAnnotation(ModuleTempDirectory.class);
//                IModule mc = this.getModule(d.value());
//                if (mc == null) {
//                    throw new ModuleInvalidException(d.value());
//                }
//                objects[i] = this.injectionParameterModuleTempDirectory(mc, p);
//                i++;
//                continue;
//            }
//            //判断Null注释
//            if (p.isAnnotationPresent(Null.class)) {
//                objects[i] = null;
//                i++;
//                continue;
//            }
//            //基本参数类型
//            if (ReflectUtil.isBasicType(paramTypes[i])) {
//                i++;
//                continue;
//            }
//            //优先从模块注入类型帝乡
//            objects[i] = this.getBean(paramTypes[i]);
//            //判断模块注入类型对象是否有效，如果无效使用应用内容注入类型对象
//            if (objects[i] == null) {
//                objects[i] = this.getBean(paramTypes[i]);
//            }
//            i++;
//        }
//        return objects;
//    }
//
//    /**
//     * 创建函数参数
//     *
//     * @param module 模块内容
//     * @param m      函数
//     * @return
//     */
////    @Override
//    public Object[] newMethodParameter(IModule module, Method m) throws Exception {
//        if (m.getParameterCount() == 0) {
//            return null;
//        }
//        //函数的参数对象数组
//        Object[] objects = new Object[m.getParameterCount()];
//        //函数的参数类型数组
//        Class[] paramTypes = m.getParameterTypes();
//        int i = 0;
//        for (Parameter p : m.getParameters()) {
//            //判断Autowired注释
//            if (p.isAnnotationPresent(Autowired.class)) {
//                //获取注入注释
//                Autowired auto = p.getAnnotation(Autowired.class);
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), auto);
//                //应用对象注入
//                if (auto.value().equals("")) {
//                    objects[i] = this.getBean(paramTypes[i]);
//                } else {
//                    objects[i] = this.getBean(auto.value());
//                }
//                i++;
//                continue;
//            }
//            //判断Autowired注释
//            if (p.isAnnotationPresent(ModuleAutowired.class)) {
//                //获取注入注释
//                ModuleAutowired auto = p.getAnnotation(ModuleAutowired.class);
//                if (!auto.value().equals("")) {
//                    //
//                    IModule mc = auto.value().equals(module.getName()) ? module : this.getModule(auto.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(auto.value());
//                    }
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(mc.getEnv(), auto);
//                    //模块对象注入
//                    if (auto.value().equals("")) {
//                        objects[i] = module.getBean(paramTypes[i]);
//                    } else {
//                        objects[i] = module.getBean(auto.value());
//                    }
//                } else {
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(this.getEnv(), auto);
//                    //应用对象注入
//                    if (auto.value().equals("")) {
//                        objects[i] = this.getBean(paramTypes[i]);
//                    } else {
//                        objects[i] = this.getBean(auto.value());
//                    }
//                }
//                i++;
//                continue;
//            }
//            //判断Value注释
//            if (p.isAnnotationPresent(Value.class)) {
//                //基础参数类型
//                //配置参数注入
//                Value v = p.getAnnotation(Value.class);
//                //设置反射替换env配置值
//                this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                //应用参数注入
//                objects[i] = ReflectUtil.objectConverter(this.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                i++;
//                continue;
//            }
//            //判断Value注释
//            if (p.isAnnotationPresent(ModuleValue.class)) {
//                //基础参数类型
//                //配置参数注入
//                ModuleValue v = p.getAnnotation(ModuleValue.class);
//                if (!v.value().equals("")) {
//                    IModule mc = v.value().equals(module.getName()) ? module : this.getModule(v.value());
//                    if (mc == null) {
//                        throw new ModuleInvalidException(v.value());
//                    }
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(mc.getEnv(), v);
//                    //模块参数注入
//                    objects[i] = ReflectUtil.objectConverter(module.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                } else {
//                    //设置反射替换env配置值
//                    this.setAnnotationReflectEnvValue(this.getEnv(), v);
//                    //应用参数注入
//                    objects[i] = ReflectUtil.objectConverter(this.getEnv().get(v.prefix(), v.value()), paramTypes[i]);
//                }
//                i++;
//                continue;
//            }
//            //注入模块目录
//            if (p.isAnnotationPresent(ModuleTempDirectory.class)) {
//                objects[i] = this.injectionParameterModuleTempDirectory(module, p);
//                i++;
//                continue;
//            }
//            //判断Null注释
//            if (p.isAnnotationPresent(Null.class)) {
//                objects[i] = null;
//                i++;
//                continue;
//            }
//            //基本参数类型
//            if (ReflectUtil.isBasicType(paramTypes[i])) {
//                i++;
//                continue;
//            }
//            //优先从模块注入类型帝乡
//            objects[i] = module.getBean(paramTypes[i]);
//            //判断模块注入类型对象是否有效，如果无效使用应用内容注入类型对象
//            if (objects[i] == null) {
//                objects[i] = this.getBean(paramTypes[i]);
//            }
//            i++;
//        }
//        return objects;
//    }
//
//    protected abstract IModule getModule(String value);
//
//    /**
//     * 设置注释内容替换
//     *
//     * @param env
//     * @param a
//     */
//    @Override
//    public void setAnnotationReflectEnvValue(IEnvironment env, Annotation a) throws Exception {
//        //反射代理
//        InvocationHandler ih = Proxy.getInvocationHandler(a);
//        //获取注释的map参数属性
//        Field f = ih.getClass().getDeclaredField(AnnotationReflectUtil.MEMBER_VALUES);
//        //设置权限
//        f.setAccessible(true);
//        //获取属性的map对象注释参数
//        Map<String, Object> m = (Map) f.get(ih);
//        //遍历注释参数是否带${xxx.xx}的格式在env中搜索指定的参数值填充
//        for (String value : new ArrayList<>(m.keySet())) {
//            Object value = m.get(value);
//            //验证注释参数类型是否为字符串类型跟需要替换配置参数
//            if (value instanceof String) {
//                String str = value.toString();
//                if (StringUtils.isEmpty(str)) {
//                    continue;
//                }
//                try {
//                    m.put(value, env.replaceMiddle(str));
//                    System.out.println(m.get(value).toString());
//                } catch (EnvironmentInvalidException e) {
//                    //判断错误是否引发错误，主要看注释是否带错误参数
//                    if (m.containsKey(AnnotationReflectUtil.ERROR) &&
//                            m.get(AnnotationReflectUtil.ERROR) instanceof Boolean &&
//                            (boolean) m.get(AnnotationReflectUtil.ERROR)) {
//                        throw e;
//                    } else {
//                        throw e;
//                    }
//                }
////                if (StringUtil.isMiddle(str, "${", "}")) {
////                    m.put(value,
////                            //替换${xxx.xx}中间内容
////                            StringUtil.replaceMiddle(str, "${", "}",
////                                    //获取env的${xxx.xx}中间的配置内容
////                                    env.get(StringUtil.getMiddle(str, "${", "}"))));
////                }
//            }
//        }
//        //遍历下级注释
//        for (Map.Entry<String, Object> entry : m.entrySet()) {
//            //判断注释参数是否为注释
//            if (Annotation.class.isAssignableFrom(entry.getOrderValue().getClass())) {
//                //设置注释参数替换，同时继续向注释下级替换
//                this.setAnnotationReflectEnvValue(env, (Annotation) entry.getOrderValue());
//                continue;
//            }
//            //判断注释参数是否为数组对象
//            if (entry.getOrderValue().getClass().isArray()) {
//                //获取数组注释参数
//                Object[] objects = (Object[]) entry.getOrderValue();
//                //遍历数组注释参数
//                for (Object o : objects) {
//                    //判断注释参数是否为注释
//                    if (Annotation.class.isAssignableFrom(o.getClass())) {
//                        //设置注释参数替换，同时继续向注释下级替换
//                        this.setAnnotationReflectEnvValue(env, (Annotation) o);
//                        continue;
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void injectionAutowired(IModule module, Object o, Field field) {
//    }
//}