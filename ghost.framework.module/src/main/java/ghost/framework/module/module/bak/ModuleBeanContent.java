//package ghost.framework.module.module.bak;
//
//import ghost.framework.beans.IBeanDefinition;
//import ghost.framework.context.bean.utils.BeanUtil;
//import ghost.framework.beans.enums.CallMode;
//import ghost.framework.context.bean.IBeanDefinition;
//import ghost.framework.beans.annotation.*;
//import ghost.framework.beans.execute.annotation.Main;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.core.event.bean.IBeanEventListenerContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.core.module.IModuleBean;
//import ghost.framework.core.module.thread.MainThread;
//import ghost.framework.util.ExceptionUtil;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentSkipListMap;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块绑定内容
// * @Date: 23:00 2019/5/22
// */
//abstract class ModuleBeanContent extends ModuleEventContent implements IModuleBean {
//
//    /**
//     * 初始化模块绑定内容
//     */
//    protected ModuleBeanContent() {
//        super();
//        this.getLog().info("~ModuleBeanContent");
//    }
//    private List<MainThread> mainThreadList = new ArrayList<>();
//
//    public List<MainThread> getMainThreadList() {
//        return mainThreadList;
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public synchronized void close() throws Exception {
//        //释放绑定资源
//        if (this.beanMap != null) {
//            synchronized (this.beanMap) {
//                for (IBeanDefinition d : this.beanMap.values()) {
//                    this.dClose(d);
//                }
//                this.beanMap.clear();
//            }
//        }
//        super.close();
//    }
//
//    /**
//     * 创建实例
//     *
//     * @param c 构建类
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public Object newModuleInstance(Class<?> c) throws Exception{
//        return this.app.newModuleInstance(this, c);
//    }
//
//    @Override
//    public Object newModuleInstance(Constructor constructor) {
//        return null;
//    }
//
//    public Object newModuleInstanceParameter(Constructor constructor)  {
//        return null;
//    }
//
//    @Override
//    public void injection(Object o) {
//
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        super.init();
//        //初始化绑定列表
//        this.beanMap = new ConcurrentSkipListMap<>();
//        //绑定应用对象
//        this.bean(this.getApp());
//        //绑定模块自身Env
//        this.bean(this.getEnv());
//        //绑定模块自身
//        this.bean(this);
//        //绑定模块自身临时目录
//        this.bean(this.getTempDirectory());
//    }
//
//    /**
//     * 指定注释获取绑定对象
//     *
//     * @param a
//     * @return
//     */
//    @Override
//    public Object getAnnotationBean(Class<? extends Annotation> a) {
//        return BeanUtil.getAnnotationBean(this.beanMap, a);
//    }
//
//    /**
//     * 指定注释获取绑定对象
//     *
//     * @param a   注释类型
//     * @param r   返回类型
//     * @param <R>
//     * @return
//     */
//    @Override
//    public <R> R getAnnotationBean(Class<? extends Annotation> a, Class<R> r) {
//        return (R) BeanUtil.getAnnotationBean(this.beanMap, a);
//    }
//
//    /**
//     * 绑定列表
//     */
//    private ConcurrentSkipListMap<String, IBeanDefinition> beanMap;
//
//    /**
//     * 获取绑定定义
//     *
//     * @param value 绑定key
//     * @return
//     */
//    @Override
//    public IBeanDefinition getBeanDefinition(String value)  {
//        return BeanUtil.getBeanDefinition(this.beanMap, value, this.classLoader);
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
//    public IBeanDefinition getBeanDefinition(Class<?> c)  {
//        if(c.isInterface()){
//            return BeanUtil.getInterfaceBeanDefinition(this.beanMap, c);
//        }
//        return BeanUtil.getBeanDefinition(this.beanMap, c.getName(), this.classLoader);
//    }
//
//    /**
//     * 绑定回调接口处理
//     *
//     * @param definition 绑定定义对象
//     * @throws Exception
//     */
//    private void beanCall(IBeanDefinition definition)  {
//        //获取绑定回调注释函数
//        Method m = ReflectUtil.getAnnotationVoidMethod(definition.getObject().getClass(), ghost.framework.beans.execute.annotation.Call.class);
//        //判断是否注释了回调
//        if (m == null) {
//            //没有注释回调退出
//            return;
//        }
//        //获取注释回调
//        ghost.framework.beans.execute.annotation.Call call = m.getAnnotation(ghost.framework.beans.execute.annotation.Call.class);
//        //判断是否有注释绑定回调，而且只有完成回调时才回调｛CallMode.complete｝
//        if (call.depend().equals(Bean.class) && call.action().equals(CallMode.complete)) {
//            m.setAccessible(true);
//            //m.invoke(definition.getObject(), this.app.newMethodParameter(this, m));
//        }
//    }
//
//    /**
//     * 添加绑定对象
//     *
//     * @param o 绑定对象
//     * @throws RuntimeException
//     */
//    @Override
//    public void bean(Object o) {
//        this.put(new IBeanDefinition(o.getClass().getName(), o));
//    }
//    @Override
//    public void bean(String value, Object o)  {
//        this.put(new IBeanDefinition(o.getClass().getName(), o));
//    }
//    @Override
//    public void bean(Class<?> o) {
//        this.put(new IBeanDefinition(o.getClass().getName(), o));
//    }
//    /**
//     * 添加定义
//     *
//     * @param definition 绑定定义
//     * @throws RuntimeException
//     */
//    private void put(IBeanDefinition definition) {
//        //绑定前事件
//        IBeanEventListenerContainer<IModule, IBeanDefinition> beanEventListenerContainer = this.getBean(IBeanEventListenerContainer.class);
//        beanEventListenerContainer.addBeanEventBefore(this, definition);
//        //注入绑定对象
//        this.addBeanAutowired(definition);
//        //添加绑定定义
//        synchronized (this.beanMap) {
//            if (this.beanMap.containsKey(definition.getName())) {
//                //throw new ModuleBeanExistException("IBeanDefinition is exist error value:" + definition.getName());
//            }
//            this.beanMap.put(definition.getName(), definition);
//        }
//        //绑定完成接口处理
//        this.beanCall(definition);
//        //绑定后事件
//        beanEventListenerContainer.addBeanEventAfter(this, definition);
//        //
//        this.getLog().info("put Bean:" + definition.toString());
//    }
//
//    /**
//     * 绑定注入对象
//     *
//     * @param definition 模块绑定定义
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     * @throws IllegalArgumentException
//     * @throws InvocationTargetException
//     * @throws IOException
//     */
//    private void addBeanAutowired(IBeanDefinition definition)  {
//        //遍历对象属性列表
//        for (Field field : definition.getClass().getDeclaredFields()) {
//            //验证是否注释绑定
//            if (field.isAnnotationPresent(Autowired.class)) {
//                //设置权限
//                field.setAccessible(true);
//                //获取绑定注释
//                Autowired a = field.getAnnotation(Autowired.class);
//                Object o = this.getBean(a, field.getDeclaringClass());
//                if (o == null) {
//                    if (this.getLog().isDebugEnabled()) {
//                        this.getLog().debug("模块内容没有绑定定义的注入对象错误！");
//                    } else {
//                        this.getLog().error("模块内容没有绑定定义的注入对象错误！");
//                    }
//                    return;
//                } else {
//                    //field.set(definition.getObject(), o);
//                }
//            }
//        }
//    }
//    /**
//     * 删除绑定对象
//     *
//     * @param o 绑定对象
//     */
//    @Override
//    public void removeBean(Object o) {
//        IBeanEventListenerContainer<IModule, IBeanDefinition> beanEventListenerContainer = this.getBean(IBeanEventListenerContainer.class);
//        IBeanDefinition d = null;
//        synchronized (this.beanMap) {
//            d = this.beanMap.get(o.getClass().getName());
//            beanEventListenerContainer.removeBeanEventBefore(this, d);
//            //卸载前事件
//            this.beanMap.remove(d);
//        }
//        beanEventListenerContainer.removeBeanEventAfter(this, d);
//        this.dClose(d);
//    }
////    /**
////     * 获取绑定注释对象
////     *
////     * @param a         绑定注释
////     * @param className 绑定类型名称
////     * @return
////     * @throws ModuleBeanNotException
////     * @throws InstantiationException
////     * @throws IllegalAccessException
////     * @throws IllegalArgumentException
////     * @throws InvocationTargetException
////     */
////    @Override
////    public  <T> T  getBean(ModuleAutowired a, String className) throws ModuleBeanNotException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
////        T o = null;
////        //判断注入模式
////        if (!a.value().equals("")) {
////            //获取模块id
////            IModuleContent mc = this.applicationContent.getModule(a.value());
////            if (mc == null) {
////                //模块无效错误
////                throw new IllegalArgumentException(a.value());
////            }
////            //从注入的属性获取绑定的对象
////            if (a.value().equals("")) {
////                o = mc.getBean(className);
////                //判断是否向上层委派注入
////                if (o == null && a.delegation()) {
////                    o = this.getApplicationContent().getBean(className);
////                } else {
////                    return o;
////                }
////            }
////        }
////        //从注入的属性获取绑定的对象
////        if (a.value().equals("")) {
////            o = this.getBean(className);
////            //判断是否向上层委派注入
////            if (o == null && a.delegation()) {
////                o = this.getApplicationContent().getBean(className);
////            } else {
////                return o;
////            }
////        }
////        //从应用注入绑定对象
////        //从注入的属性获取绑定的对象
//////        o = this.getApplicationContent().getBean(a, className);
////        //获取无效是否引发错误
////        if (a.error()) {
////            throw new ModuleBeanNotException(a, className);
////        }
////        return o;
////    }
//
//    /**
//     * 删除绑定
//     *
//     * @param value 绑定key
//     */
//    @Override
//    public void removeBean(String value) {
//        IBeanEventListenerContainer<IModule, IBeanDefinition> beanEventListenerContainer = this.getBean(IBeanEventListenerContainer.class);
//        IBeanDefinition d;
//        synchronized (this.beanMap) {
//            d = this.beanMap.get(value);
//            beanEventListenerContainer.removeBeanEventBefore(this, d);
//            //卸载前事件
//            this.beanMap.remove(value);
//        }
//        beanEventListenerContainer.removeBeanEventAfter(this, d);
//        this.dClose(d);
//    }
//
//    /**
//     * 释放资源
//     *
//     * @param d
//     */
//    private void dClose(IBeanDefinition d) {
//        //判断绑定定义对象是否需要释放资源
//        if (d != null && d.getObject() != null) {
//            //判断是否为主类
//            if (d.getObject().getClass().isAnnotationPresent(Main.class)) {
//                try {
//                    //遍历是否注释停止函数
//                    for (Method m : d.getObject().getClass().getMethods()) {
//                        if (m.isAnnotationPresent(ghost.framework.beans.annotation.invoke.Stop.class)) {
//                            //执行停止
//                            m.invoke(d.getObject(), null);
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    ExceptionUtil.debugOrError(this.getLog(), e);
//                }
//            }
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
//     * 添加绑定定义
//     *
//     * @param definition 绑定定义
//     * @throws Exception
//     */
//    @Override
//    public void bean(IBeanDefinition definition)  {
//        this.put(definition);
//    }
//
//    /**
//     * 添加绑定
//     *
//     * @param value 绑定域
//     * @param c   绑定类型
//     * @throws Exception
//     */
//    @Override
//    public void bean(String value, Class<?> c) throws Exception {
//        //创建类实例
//        this.put(new IBeanDefinition(StringUtil.inEmptyToNull(value), this.app.newModuleInstance(this, c)));
//    }
//
//    /**
//     * 获取绑定类型对象
//     *
//     * @param value 绑定类型名称
//     * @return
//     * @throws IllegalArgumentException
//     */
//    @Override
//    public <T> T getBean(String value)  {
//        return null;//BeanUtil.getBeanObject(this, this.beanMap, value);
//    }
//    /**
//     * 应用注入
//     *
//     * @param a
//     * @param c
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public <T> T  getBean(Autowired a, Class<?> c)  {
//        return this.app.getBean(a, c);
//    }
//    /**
//     * 获取绑定注释对象
//     *
//     * @param a 绑定注释
//     * @param c 绑定的类型
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public  <T> T  getBean(ModuleAutowired a, Class<?> c)  {
//        T o = null;
//        //判断是否使用key或域域key绑定
//        if (a.value().equals("")) {
//            //没有key与域绑定使用类型绑定
//            if (!a.value().equals("")) {
//                //获取模块
//                IModule mc = this.app.getModule(a.value());
//                if (mc == null) {
//                    //模块无效错误
//                    throw new IllegalArgumentException(a.value());
//                }
//                //绑定模块注入
//                o = (T)mc.getBean(c);
//                //判断是否向上层委派注入
//                if (o == null && a.delegation()) {
//                    //绑定应用注入
//                    o = (T)this.app.getBean(c);
//                }
//            } else {
//                //绑定应用注入
//                o = (T)this.app.getBean(c);
//            }
//        } else {
//            //绑定模块注入
//            o = this.getBean(a.value());
//            //判断是否向上层委派注入
//            if (o == null && a.delegation()) {
//                //绑定应用注入
//                o = this.app.getBean(a.value());
//            }
//        }
//        return o;
//    }
//    /**
//     * 删除模块绑定
//     *
//     * @param c 绑定类型
//     */
//    @Override
//    public void removeBean(Class<?> c)  {
//        this.removeBean(c.getName());
//    }
//    /**
//     * 验证绑定是否存在
//     *
//     * @param c 绑定类型
//     * @return
//     */
//    @Override
//    public boolean beanContains(Class<?> c)  {
//        synchronized (this.beanMap) {
//            return this.beanMap.containsKey(c.getName());
//        }
//    }
//
//    /**
//     * 验证绑定是否存在
//     *
//     * @param value 绑定域
//     * @return
//     */
//    @Override
//    public boolean beanContains(String value)  {
//        synchronized (this.beanMap) {
//            return this.beanMap.containsKey(value);
//        }
//    }
//
//    /**
//     * 验证绑定是否存在
//     * @return
//     */
//    @Override
//    public boolean beanContains(Object o)  {
//        return this.beanContains(o.getClass());
//    }
//
//    @Override
//    public <T> T getBean(Class<T> aClass)  {
//        return null;
//    }
//}