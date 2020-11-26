package ghost.framework.app;

import ghost.framework.maven.Booter;
import ghost.framework.maven.URLArtifact;
import ghost.framework.module.assembly.AssemblyUtil;
import ghost.framework.module.configuration.ConfigurationUtil;
import ghost.framework.module.configuration.annotations.ConfigurationProperties;
import ghost.framework.module.core.ApplicationHome;
import ghost.framework.module.core.ApplicationUtil;
import ghost.framework.module.core.IApplicationContent;
import ghost.framework.module.core.IApplicationMaven;
import ghost.framework.module.core.env.Environment;
import ghost.framework.util.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用平台运行基础类
 * @Date: 20:41 2019-05-30
 */
@ConfigurationProperties(order = Integer.MIN_VALUE)
public abstract class AppBase implements AutoCloseable {
    /**
     * 日志
     */
    private static Logger log;
    /**
     * 应用内容
     */
    private static IApplicationContent applicationContent;
    /**
     * 获取是否为开发模式
     *
     * @return
     */
    public static boolean isDev() {
        return env.getBoolean(IApplicationContent.dev);
    }
    /**
     * 获取是否为windows
     *
     * @return
     */
    public boolean isWindows() {
        return env.getBoolean(IApplicationContent.os);
    }

    /**
     * 获取是否为linux
     *
     * @return
     */
    public static boolean isLinux() {
        return env.getBoolean(IApplicationContent.os);
    }
    private static Environment env;
    /**
     * 运行
     *
     * @param rootClass 引导类
     */
    public static IApplicationContent run(Class<?> rootClass) {
        try {
            //日志
            log = Logger.getLogger(rootClass);
            //配置
            env = new Environment();
            //运行home
            ApplicationHome rootHome = new ApplicationHome(rootClass);
            //设置是否为开发模式
            env.setBoolean(IApplicationContent.dev, AssemblyUtil.isPathDev(rootHome.getSource().getPath()));
            //获取基类基础配置
            ConfigurationProperties[] propertiess = rootClass.getAnnotationsByType(ConfigurationProperties.class);
            List<ConfigurationProperties> list = ConfigurationUtil.getOrderList(propertiess);
            for (ConfigurationProperties p : list) {
                //判断是否在开发模式
                if (isDev()) {
                    //开发模式读取配置文件
                    env.merge(rootHome.getSource().getPath() + File.separator + p.path());
                } else {
                    //运行模式读取配置文件
                    env.merge(rootClass, p.path());
                }
            }
            File m2Dir = null;
            //创建m2目录
            if (isLinux()) {
                //linux的m2本都仓库目录
                if (env.containsKey(IApplicationMaven.LINUX_MAVEN_LOCAL_REPOSITORY_PATH)) {
                    env.setString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, env.getString(IApplicationMaven.LINUX_MAVEN_LOCAL_REPOSITORY_PATH));
                    env.remove(IApplicationMaven.LINUX_MAVEN_LOCAL_REPOSITORY_PATH);
                }
            } else {
                //win的m2本都仓库目录
                if (env.containsKey(IApplicationMaven.WIN_MAVEN_LOCAL_REPOSITORY_PATH)) {
                    env.setString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, env.getString(IApplicationMaven.WIN_MAVEN_LOCAL_REPOSITORY_PATH));
                    env.remove(IApplicationMaven.WIN_MAVEN_LOCAL_REPOSITORY_PATH);
                }
            }
            //判断是否有maven目录配置
            if (!env.containsKey(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH)) {
                //设置临时maven目录参数
                env.setString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, System.getProperty("java.io.tmpdir") + File.separator + "m2" + File.separator + "repository");
            }
            //创建maven本地仓库目录
            m2Dir = new File(env.getString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH));
            //判断是否目录存在
            if (!m2Dir.exists()) {
                //目录不存在创建
                m2Dir.mkdirs();
            }
            //初始化模块
            initInstallModule(rootClass, rootHome, env, m2Dir);
        } catch (Exception e) {
            System.out.println(ExceptionUtil.outStackTrace(e));
            ExceptionUtil.debugOrError(log, e);
        }
        return applicationContent;
    }

    /**
     * 从中央仓库加载启动时注释加载的初始化包
     * @param rootClass 引导类
     * @param rootHome root home
     * @param env 配置
     * @param m2Dir 本地maven仓库目录
     */
    private static void initInstallModule(Class<?> rootClass, ApplicationHome rootHome, Environment env, File m2Dir) {
        try {
            //配置加载仓库
            String urls = env.getString(IApplicationMaven.APPLICATION_PROPERTIES_MAVEN_REPOSITORY_URLS);
            //获取加载版本
            String name = env.getString(IApplicationMaven.APPLICATION_PROPERTIES_MAVEN_INSTALL_APP_NAMES);//先安装模块
            //验证是否有需要初始化加载的包
            if (!StringUtils.isEmpty(urls) && !StringUtils.isEmpty(name)) {
                if (log.isDebugEnabled()) {
                    log.debug("Start Initialization Load Foundation Pack");
                } else {
                    log.info("Start Initialization Load Foundation Pack");
                }
                //下载安装app运行包引用包列表
                List<URLArtifact> artifacts = Booter.getDependencyNodeDownloadURLArtifactList(m2Dir, urls, name, rootClass.getClassLoader());
                //下载运行包
                URLArtifact rootArtifact = Booter.download(m2Dir, urls, name);
                List<File> rootFiles = new ArrayList<>();
                for (URLArtifact a : artifacts) {
                    rootFiles.add(a.getUrl());
                }
                //初始化运行包应用内容
                applicationContent = ApplicationUtil.run(env, rootClass, rootArtifact.getUrl(), rootFiles);
                //
                if (log.isDebugEnabled()) {
                    log.debug("Start Initialization Load Foundation Pack Complete");
                } else {
                    log.info("Start Initialization Load Foundation Pack Complete");
                }
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtil.outStackTrace(e));
            ExceptionUtil.debugOrError(log, e);
        }
    }

    /**
     * 释放资源
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if (applicationContent != null) {
            applicationContent.close();
        }
    }
}
