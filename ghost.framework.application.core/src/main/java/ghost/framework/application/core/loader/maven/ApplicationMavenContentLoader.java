//package ghost.framework.app.core.loader;
//
//import ghost.framework.app.context.IApplicationMavenContentLoader;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.bean.Bean;
//import ghost.framework.beans.annotation.conditional.ConditionalOnMissingBean;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.beans.annotation.invoke.Init;
//import ghost.framework.beans.execute.annotation.Main;
//import ghost.framework.context.bean.utils.BeanUtil;
//import ghost.framework.beans.utils.OrderAnnotationUtil;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//import ghost.framework.util.generic.ListUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
///**
// * package: ghost.framework.app.core
// *
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用maven内容加载器
// * @Date: 2019-11-17:12:13
// */
//public class ApplicationMavenContentLoader implements IApplicationMavenContentLoader {
//    private Logger log = LoggerFactory.getLogger(ApplicationMavenContentLoader.class);
//    @Autowired
//    private IApplication app;
//
//    public ApplicationMavenContentLoader() {
//        this.log.info("~" + this.getClass().getName());
//    }
//    private List<Class<?>> configurationList = new ArrayList<>();
//    private List<Class<?>> serviceList = new ArrayList<>();
//    private List<Class<?>> mainList = new ArrayList<>();
//    /**
//     * 模块类加载器
//     *
//     * @param c 加载类
//     */
//    public void loadClassLoader(Class<?> c) {
//        //配置类
//        if (c.isAnnotationPresent(Configuration.class)) {
//            configurationList.loader(c);
//        }
//        //服务类配置注释
//        if (c.isAnnotationPresent(Service.class)) {
//            serviceList.loader(c);
//        }
//        //启动类
//        if (c.isAnnotationPresent(ghost.framework.beans.execute.annotation.Main.class)) {
//            mainList.loader(c);
//        }
//    }
//    /**
//     * 绑定注入
//     * @param o
//     * @param m
//     * @param b
//     * @return
//     * @throws Exception
//     */
//    private Object beanInjection(Object o, Method m, Bean b) throws Exception {
//        //设置反射替换env配置值
////        this.app.setAnnotationReflectEnvValue(this.app.getEnv(), b);
//        //创建绑定函数返回创建对象
//        Object mio = m.invoke(o, this.app.newInstanceParameters(m));
//        //注入对象
//        this.app.injection(mio);
//        return mio;
//    }
//    /**
//     * 绑定注入
//     * @param o
//     * @param m
//     * @param b
//     * @return
//     * @throws Exception
//     */
//    private Object beanInjection(Object o, Method m, ConditionalOnMissingBean b) throws Exception {
//        //设置反射替换env配置值
////        this.app.setAnnotationReflectEnvValue(this.app.getEnv(), b);
//        //创建绑定函数返回创建对象
//        Object mio = m.invoke(o, this.app.newInstanceParameters(m));
//        //注入对象
//        this.app.injection(mio);
////        //绑定注入类型
////        if (b.injectionList().length > 0) {
////            //遍历注入类型列表
////            for (Class<?> c : b.injectionList()) {
////                //判断是否为模块注入
//////                if (b.module()) {
//////                    //模块注入
//////                    Object io = this.content.getBean(c);
//////                    if (io instanceof List) {
//////                        ((List) io).loader(mio);
//////                    }
//////                } else {
//////                    //应用注入
//////                    Object io = this.content.getBean(c);
//////                    if (io instanceof List) {
//////                        ((List) io).loader(mio);
//////                    }
//////                }
////            }
////        }
//        return mio;
//    }
//    /**
//     *
//     * @param o
//     * @param m
//     */
//    private void initBean(Object o, Method m) {
//        //获取绑定注释
//        Bean bean = m.getAnnotation(Bean.class);
//        //设置反射替换env配置值
////        this.app.setAnnotationReflectEnvValue(this.app.getEnv(), bean);
//        //判断注入模式
////        if (bean.module()) {
////            //判断是否为模块绑定
////            if (bean.value().equals("")) {
////                //验证不存在时绑定
////                if (m.getReturnType().equals(Void.TYPE)) {
////                    //没有返回函数绑定，只做调用
////                    m.invoke(o, this.content.newMethodParameter(m));
////                } else {
////                    //有返回函数绑定
////                    this.content.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////                }
////            } else {
////                //绑定模块
////                if (m.getReturnType().equals(Void.TYPE)) {
////                    //没有返回函数绑定，只做调用
////                    m.invoke(o, this.content.newMethodParameter( m));
////                } else {
////                    //获取绑定模块
////                    IModuleContent mc = this.content.getModule(bean.value());
////                    if (mc == null) {
////                        //获取不到要绑定的模块id错误
////                        throw new ModuleBeanException("Bean.value not error:" + bean.value());
////                    }
////                    //绑定
////                    mc.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////                }
////            }
////        } else {
////            //绑定应用
////            //验证不存在时绑定
////            if (m.getReturnType().equals(Void.TYPE)) {
////                //没有返回函数绑定，只做调用
////                m.invoke(o, this.content.newMethodParameter(m));
////            } else {
////                //有返回函数绑定
////                this.content.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////            }
////        }
//    }
//    /**
//     *
//     * @param o
//     * @param m
//     */
//    private void initConditionalOnMissingBean(Object o, Method m) {
//        //获取绑定注释
//        ConditionalOnMissingBean bean = m.getAnnotation(ConditionalOnMissingBean.class);
//        //设置反射替换env配置值
////        this.app.setAnnotationReflectEnvValue(this.app.getEnv(), bean);
////        //判断注入模式
////        if (bean.module()) {
////            //判断是否为模块绑定
////            if (bean.value().equals("")) {
////                //
////                if (m.getReturnType().equals(Void.TYPE)) {
////                    //没有返回函数绑定，只做调用
////                    m.invoke(o, this.content.newMethodParameter(m));
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
////                    m.invoke(o, this.content.newMethodParameter(m));
////                } else {
////                    //获取绑定模块
////                    IModuleContent mc = this.content.getModule(bean.value());
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
////                m.invoke(o, this.content.newMethodParameter(m));
////            } else {
////                //有返回函数绑定
////                this.content.bean(bean.scope(), bean.value(), this.beanInjection(o, m, bean));
////            }
////        }
//    }
//    /**
//     * 遍历绑定函数
//     * @param o
//     */
//    private void forBean(Object o) {
//        //遍历配置类函数列表
//        for (Method m : OrderAnnotationUtil.getAnnotationMethodListSort(o, Bean.class)) {
//            //判断是否有注释绑定
//            //排除静态函数、没返回对象函数、没注释绑定函数
//            if (BeanUtil.isBeanMethodAccessible(m)) {
//                this.initBean(o, m);
//                continue;
//            }
//        }
//        //遍历配置类函数列表
//        for (Method m : OrderAnnotationUtil.getAnnotationMethodListSort(o, ConditionalOnMissingBean.class)) {
//            //判断是否有注释绑定
//            if (BeanUtil.isConditionalOnMissingBeanMethodAccessible(m)) {
//                this.initConditionalOnMissingBean(o, m);
//                continue;
//            }
//        }
//    }
//    private void configurationProperties(Class<?> c, ConfigurationProperties[] propertiess)  {
//        this.configurationProperties(c, ListUtil.toList(propertiess));
//    }
//
//    private void configurationProperties(Class<?> c, List<ConfigurationProperties> propertiess)  {
//        //获取类配置文件注释列表
//        for (ConfigurationProperties properties : propertiess) {
//            //设置反射替换env配置值
////            this.app.setAnnotationReflectEnvValue(this.app.getEnv(), properties);
//            //判断是否为模块注释
////            if (properties.module()) {
////                //模块配置
////                if (this.app.isDev()) {
////                    //调试模式
////                    this.app.getEnv().merge(c, properties);
////                } else {
////                    //jar运行模式
//////                    this.app.getEnv().merge(properties.prefix(), this.app.getUrlPath(properties.path()));
////                }
////            } else {
////                //应用配置
////                if (this.app.isDev()) {
////                    //调试模式
////                    this.app.getEnv().merge(c, properties);
////                } else {
////                    //jar运行模式
//////                    this.app.getEnv().merge(properties.prefix(), this.app.getUrlPath(properties.path()));
////                }
////            }
//        }
//    }
//    /**
//     * 初始化配置绑定配置类的注入配置
//     * @param config
//     */
//    public void initSelectProperties(Configuration config)  throws Exception {
////        for (ClassProperties p : config.select()) {
////            //设置反射替换env配置值
//////            this.app.setAnnotationReflectEnvValue(this.app.getEnv(), p);
////            Object o;
//////            if (p.module()) {
//////                //获取现在配置的模块
//////                IModuleContent mc = this.content.getModule(p.value());
//////                if (mc == null) {
//////                    throw new ModuleInvalidException(p.value());
//////                }
//////                //初始化选择模块配置
//////                o = this.content.newModuleInstance(mc, p.depend());
//////                //绑定
//////                mc.bean(o);
//////                //注入属性
//////                EnvironmentReader reader = mc.getEnv().getReader(p.prefix());
//////                //遍历属性
//////                for (Field field : p.depend().getDeclaredFields()) {
//////                    field.setAccessible(true);
//////                    try {
//////                        if (reader.equalsIgnoreCase(field.getName())) {
//////                            field.set(o, reader.get(field.getName()));
//////                        }
//////                    }catch (Exception e){
//////                        e.printStackTrace();
//////                    }finally {
//////                        field.setAccessible(false);
//////                    }
//////                }
//////            } else {
////                //初始化选择应用配置
//////                o = this.app.newInstance(p.depend());
////                //绑定
//////                this.app.bean(o);
//////                //注入属性
//////                EnvironmentReader reader = this.app.getEnv().getReader(p.prefix());
//////                //遍历属性
//////                for (Field field : p.depend().getDeclaredFields()) {
//////                    field.setAccessible(true);
//////                    try {
//////                        if (reader.equalsIgnoreCase(field.getName())) {
//////                            field.set(o, reader.get(field.getName()));
//////                        }
//////                    }catch (Exception e){
//////                        e.printStackTrace();
//////                    }finally {
//////                        field.setAccessible(false);
//////                    }
//////                }
//////            }
////            //遍历绑定函数
//////            this.forBean(o);
////        }
//    }
//
//    /**
//     * 初始化扫面的类处理
//     */
//    public void init() throws Exception {
//        //初始化configuration类列表
//        OrderAnnotationUtil.classListSort(this.configurationList);
//        for (Class<?> c : this.configurationList) {
//            //创建configuration实例
//            Object o = this.app.newInstance(c);
//            //获取配置注释
//            Configuration configuration = c.getAnnotation(Configuration.class);
//            //设置反射替换env配置值
////            this.app.setAnnotationReflectEnvValue(this.app.getEnv(), configuration);
//            //处理SelectProperties绑定配置参数注释类的注释绑定
//            this.initSelectProperties(configuration);
//            //判断对象是否需要绑定
//            if(configuration.bean()){
//                //需要绑定进容器
//                this.app.bean(o);
//            }
//            //遍历绑定函数
//            this.forBean(o);
//        }
//        //初始化service类列表
//        OrderAnnotationUtil.classListSort(this.serviceList);
//        for (Class<?> c : this.serviceList) {
//            //创建service实例
//            Object o = this.app.newInstance(c);
//            //遍历绑定函数
//            this.forBean(o);
//        }
//        //初始化main类列表
//        OrderAnnotationUtil.classListSort(this.mainList);
//        for (Class<?> c : this.mainList) {
//            //初始化运行类
//            Object o = this.app.newInstance(c);
//            //遍历绑定函数
//            this.forBean(o);
//            //获取主类注释
//            ghost.framework.beans.execute.annotation.Main main = c.getAnnotation(Main.class);
//            //设置反射替换env配置值
////            this.app.setAnnotationReflectEnvValue(this.app.getEnv(), main);
//            //初始化main注释的配置
//            if (main.properties().length > 0) {
//                this.configurationProperties(c, main.properties());
//            }
//            //注入注释
////            this.app.registeredObject(o);
//            //注册绑定
//                //绑定道内容
//                this.app.bean(o);
//            //绑定类型
//            this.beanClass(c);
//            //获取初始化函数
//            Method im = ReflectUtil.getAnnotationVoidMethod(c, ghost.framework.beans.annotation.invoke.Init.class);
//            //判断是否有初始化函数
//            if (im != null) {
//                //获取init注释
//                ghost.framework.beans.annotation.invoke.Init init = im.getAnnotation(Init.class);
//                //设置反射替换env配置值
////                this.app.setAnnotationReflectEnvValue(this.app.getEnv(), init);
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
//                    im.invoke(o, this.app.newInstanceParameters(im));
//                }
//            }
//            //获取启动函数
//            Method sm = ReflectUtil.getAnnotationVoidMethod(c, ghost.framework.beans.annotation.invoke.Start.class);
//            //判断是否有启动函数
//            if (sm != null) {
//                //获取启动注释
//                ghost.framework.beans.annotation.invoke.Start Before = sm.getAnnotation(ghost.framework.beans.annotation.invoke.Start.class);
//                //设置反射替换env配置值
////                this.app.setAnnotationReflectEnvValue(this.app.getEnv(), Before);
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
//                                sm.invoke(o, app.newInstanceParameters(sm));
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
////                    t.setContextClassLoader((ClassLoader) this.app.getClassLoader());
//                    //启动线程
//                    t.Before();
//                } else {
//                    sm.invoke(o, this.app.newInstanceParameters(sm));
//                }
//            }
//        }
//    }
//    /**
//     * 添加类型绑定
//     *
//     * @param bean 绑定注释
//     * @param c    绑定类型
//     */
//    private void bean(Bean bean, Class<?> c) {
//        //判断是否为模块绑定
////        if (bean.value().equals("")) {
////            //验证不存在时绑定
////            this.content.bean(bean.scope(), bean.value(), c);
////        } else {
////            //绑定模块
////            //获取绑定模块
////            IModuleContent m = this.content.getModule(bean.value());
////            if (m == null) {
////                //获取不到要绑定的模块id错误
////                throw new ModuleException("Bean.value:" + bean.value());
////            }
////            //绑定
////            m.bean(bean.scope(), bean.value(), c);
////        }
//    }
//    /**
//     * 添加类型绑定
//     *
//     * @param bean 绑定注释
//     * @param c    绑定类型
//     */
//    private void addConditionalOnMissingBean(ConditionalOnMissingBean bean, Class<?> c)  {
//        //判断是否为模块绑定
////        if (bean.value().equals("")) {
////            //验证不存在时绑定
////            if (!this.content.beanContains(bean.scope(), bean.value(), c)) {
////                this.content.bean(bean.scope(), bean.value(), c);
////            }
////        }
//    }
//    /**
//     * 绑定类
//     * @param c
//     */
//    private void beanClass(Class<?> c)  {
//        //判断是否有注释绑定
//        if (c.isAnnotationPresent(Bean.class)) {
//            //获取绑定注释
//            this.bean(c.getAnnotation(Bean.class), c);
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
//}
