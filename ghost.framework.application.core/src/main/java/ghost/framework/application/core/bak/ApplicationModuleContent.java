//package ghost.framework.app.core;
//
//import ghost.framework.core.application.loader.maven.IApplicationMavenModuleDependencyLoader;
//import ghost.framework.app.core.loader.ApplicationMavenModuleDependencyLoader;
//import ghost.framework.beans.annotation.module.annotation.IModuleInfo;
//import ghost.framework.context.app.IApplicationModule;
//import ghost.framework.context.module.IModule;
//import ghost.framework.util.ExceptionUtil;
//import org.eclipse.aether.artifact.Artifact;
//
//import java.net.MalformedURLException;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用模块基础类
// * @Date: 1:37 2019-05-28
// */
//abstract class ApplicationModuleContent extends ApplicationTransactionContent implements IApplicationModule {
//    /**
//     * 初始化应用env基础类
//     * @param rootClass 引导类
//     * @throws MalformedURLException
//     */
//    protected ApplicationModuleContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationModuleContent");
//    }
//
//    @Override
//    public boolean containsModule(String kry) {
//        return false;
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//        super.close();
//    }
//
//    @Override
//    public void removeModule(IModule module) {
//
//    }
//
//    /**
//     *
//     */
//    private ModuleUncaughtExceptionHandler exceptionHandler;
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception{
//        this.getLog().info("~ApplicationModuleContent.init->Before");
//        //初始化基础类
//        super.init();
//        //初始化模块容器
//        this.syncRoot = new Object();
//        //初始化模块线程未处理错误处理
////        this.exceptionHandler = new ModuleUncaughtExceptionHandler(this);
//        //初始化模块地图
//        this.map = new ConcurrentHashMap<>();
//        //初始化模块加载器
//        try {
////            this.bean(new ApplicationModuleLoader());
//            //加载模块
//            IApplicationMavenModuleDependencyLoader moduleMavenDependencyLoader = this.getBean(ApplicationMavenModuleDependencyLoader.class);
//            moduleMavenDependencyLoader.loader(this.getRootClass());
//        }catch (Exception e){
//            if (this.getLog().isDebugEnabled()) {
//                e.printStackTrace();
//            } else {
//                this.getLog().error(e.getMessage());
//            }
//        }
//        this.getLog().info("~ApplicationModuleContent.init->After");
//    }
////
////    /**
////     * 初始化安装模块
////     */
////    private void initIstallModule() {
////        //配置加载仓库
//////        String urls = env.getString(IApplicationMaven.APPLICATION_PROPERTIES_MAVEN_REPOSITORY_URLS);
////        //获取初始化安装模块列表
////        String[] names = StringUtils.split(this.env.getString(IApplicationMaven.APPLICATION_PROPERTIES_MAVEN_INSTALL_MODULE_NAMES), "|");
////        //遍历模块列表
////        for (String artifact : names) {
////            try {
////                //加载初始化模块
////                this.bootLoader.loadJarLoader(this.getMavenLocalRepositoryFile(), this.getMavenRepositoryContainer(), artifact);
////            } catch (Exception e) {
////                ExceptionUtil.debugOrError(this.log, e);
////            }
////        }
////    }
//
//    /**
//     * 验证包信息的模块是否已经安装了
//     * @param info 模块包信息
//     * @return
//     */
//
//    public boolean containsModule(IModuleInfo info) {
//        synchronized (this.syncRoot) {
//            return false;//this.map.containsKey(info.value());
//        }
//    }
//
//    /**
//     * 验证模块版本是否存在
//     *
//     * @param artifact 版本信息
//     * @return
//     */
//    @Override
//    public boolean containsModule(Artifact artifact) {
//        //锁定同步
//        synchronized (this.map) {
//            //遍历模块版本
//            for (IModule content : this.map.values()) {
//                //比对模块版本
////                if (content.getVersion().equals(content)) {
////                    //找到相同版本模块
////                    return true;
////                }
//            }
//            //未找到相同版本模块
//            return false;
//        }
//    }
//
//    /**
//     * 获取模块
//     *
//     * @param value 模块id
//     * @return
//     */
//    @Override
//    public IModule getModule(String value) {
//        return this.map.get(value);
//    }
//
//    /**
//     * 获取模块数量
//     *
//     * @return
//     */
//    @Override
//    public int size() {
//        return this.map.size();
//    }
//
//    /**
//     * 同步对象
//     */
//    private Object syncRoot;
//
//
//    public Set<Map.Entry<String, IModule>> entrySet() {
//        return this.map.entrySet();
//    }
//
//    /**
//     * 模块地图
//     */
//    private ConcurrentHashMap<String, IModule> map;
//    /**
//     * 获取模块地图
//     * @return
//     */
//    @Override
//    public Map<String, IModule> getModuleMap() {
//        return map;
//    }
//    /**
//     * 添加模块
//     *
//     * @param module 模块对象
//     */
//    @Override
//    public void addModule(IModule module) throws Exception {
//        //锁定同步
//        synchronized (this.syncRoot) {
//            try {
//                //添加模块前事件
////                this.getModuleEventListenerContainer().registeredModuleBefore(module);
//                this.map.put(module.getName(), module);
//            } finally {
//                //添加模块后事件
////                this.getModuleEventListenerContainer().registeredModuleAfter(module);
//            }
//        }
//    }
//
//    /**
//     * 删除模块
//     *
//     * @param value 模块id
//     * @throws Exception
//     */
//    @Override
//    public void removeModule(String value) throws Exception {
//        //锁定同步
//        synchronized (this.syncRoot) {
//            //获取要删除的模块
//            IModule module = this.map.get(value);
//            //判断模块是否有对象
//            if (module != null) {
//
//                try {
//                    //删除模块前事件
////                    this.getModuleEventListenerContainer().uninstallModuleBefore(module);
//                    //删除模块
//                    this.map.remove(value);
//                } catch (Exception e) {
//                    throw e;
//                } finally {
//                    try {
//                        //删除模块后事件
////                        this.getModuleEventListenerContainer().uninstallModuleAfter(module);
//                    } catch (Exception e) {
//                        ExceptionUtil.debugOrError(this.getLog(), e);
//                    }
//                    //释放模块资源
//                    module.close();
//                }
//            }
//        }
//    }
//
//    /**
//     * 注册模块后事件
//     * 主要模块整个加载全部完成后引发的事件
//     *
//     * @param content 模块内容
//     */
////    @Override
//    public void registeredModuleAfter(IModule content) {
////        this.getModuleEventListenerContainer().registeredModuleAfter(content);
//    }
//
//    /**
//     * 主持模块前事件
//     * 主要模块刚创建实例对象并添加入模块容器后引发的事件
//     *
//     * @param content 模块内容
//     */
////    @Override
//    public void registeredModuleBefore(IModule content) {
////        this.getModuleEventListenerContainer().registeredModuleBefore(content);
//    }
//}
