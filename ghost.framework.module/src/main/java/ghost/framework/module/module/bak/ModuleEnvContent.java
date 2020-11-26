//package ghost.framework.module.module.bak;
//
//import ghost.framework.beans.annotation.module.annotation.ModuleConfigurationProperties;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.module.context.IModuleEnv;
//import ghost.framework.core.module.environment.IModuleEnvironment;
//import ghost.framework.module.env.ModuleEnvironment;
//import ghost.framework.maven.AssemblyUtil;
//import ghost.framework.util.StringUtil;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块配置内容
// * @Date: 18:00 2019/6/4
// */
//abstract class ModuleEnvContent extends ModuleContent implements IModuleEnv {
//    /**
//     * 模块配置
//     */
//    private ModuleEnvironment env;
//
//    /**
//     * 初始化模块配置内容
//     *
//     */
//    protected ModuleEnvContent() {
//        super();
//        this.getLog().info("~ModuleEnvContent");
//    }
//
//    protected ModuleEnvContent(IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public synchronized void close() throws Exception {
//        super.close();
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
//        //初始化Env
//        this.env = new ModuleEnvironment();
//        this.bean(this.env);
//        //设置是否为开发模式，如果不为开发模式侧路径为jar包的路径
//        this.env.setBoolean("Dev", AssemblyUtil.isDomainDev(this.getRootClass().getProtectionDomain()));
//        //加载模块jar包内置配置
//        //1先加载模块包内部配置文件
////        if (this.getModuleAnnotation().properties().length > 0) {
////            //遍历模块配置文件
////            for (ConfigurationProperties properties : this.getModuleAnnotation().properties()) {
////                //判断是否注释模块配置
////                if (properties.module()) {
////                    //模块配置合并
////                    if (this.isDev()) {
////                        //开发模式加载资源文件
////                        this.env.merge(properties.getClass(), properties);
////                    } else {
////                        //jar包运行模块加载资源文件
////                        this.env.merge(properties.prefix(), this.getUrlPath(properties.path()));
////                    }
////                } else {
////                    //判断是否为开发模式
////                    if (this.isDev()) {
////                        //开发模式
////                        this.app.getEnv().merge(properties.getClass(), properties);
////                    } else {
////                        //jar运行模式
////                        this.app.getEnv().merge(properties.prefix(), this.getUrlPath(properties.path()));
////                    }
////                }
////            }
////        }
//        //加载Application运行程序模块配置
//        //2加载程序应用配置，如果基础配置参数已经存在侧被覆盖
//        if (this.app.getRootClass().isAnnotationPresent(ModuleConfigurationProperties.class)) {
//            //获取模块注释列表
//            //遍历模块注释
//            for (ModuleConfigurationProperties p : this.app.getRootClass().getAnnotations(ModuleConfigurationProperties.class)) {
//                //核对本模块注释
//                if (p.key().equals(this.getName())) {
//                    //找到本模块注释
//                    //判断应用是否为调试模式
//                    if (this.app.isDev()) {
//                        //调试模式
//                        this.app.getEnv().merge(this.app.getRootClass(), p);
//                    } else {
//                        //运行模式
//                        this.app.getEnv().merge(p.prefix(), this.getUrlPath(p.key()));
//                    }
//                }
//            }
//        } else {
//            //在运行程序模块包没有注释ModuleConfigurationProperties模块配置时默认查找运行程序跟目录资源是否存在模块id的资源配置文件，如果有侧自动加载为模块配置文件
//            //判断应用是否为调试模式
//            if (this.app.isDev()) {
//                //调试模式
//                this.app.getEnv().merge(this.getRootClass(), this.getName() + ".properties");
//            } else {
//                //运行模式
//                this.app.getEnv().merge("", this.getUrlPath(this.getName() + ".properties"));
//            }
//        }
//        //如果未设置字符编码侧设置默认UTF-8编码
//        if (!this.env.containsKey(StringUtil.CharacterEncoding)) {
//            this.env.setString(StringUtil.CharacterEncoding, StringUtil.UTF8);
//        }
//    }
//
//    /**
//     * 获取模块env
//     *
//     * @return
//     */
//    @Override
//    public IModuleEnvironment getEnv() {
//        return env;
//    }
//}