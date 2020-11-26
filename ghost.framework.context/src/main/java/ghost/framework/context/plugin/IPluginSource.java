package ghost.framework.context.plugin;

import ghost.framework.context.IGetDomain;
import ghost.framework.context.IGetName;
import ghost.framework.context.application.IApplication;
import ghost.framework.maven.FileArtifact;

import java.util.List;
import java.util.Properties;

/**
 * package: ghost.framework.context.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件源接口
 * @Date: 2020/2/21:17:29
 */
public interface IPluginSource extends IGetName, IGetDomain {
    /**
     * 获取插件配置
     *
     * @return
     */
    Properties getProperties();

    /**
     * 插件所属域
     * 一般情况属于下面两个接口对象的域
     * {@link IApplication}
     * {@link ghost.framework.context.module.IModule}
     * @return
     */
    @Override
    default Object getDomain() {
        return null;
    }

    /**
     * 设置插件配置
     *
     * @param properties
     */
    void setProperties(Properties properties);

    /**
     * 获取是否为全局插件
     * 如果为全局插件将安装在 {@link IApplication} 应用容器，后任何模块加载都将同时加载此模块
     * 如果不为全局插件将使用在指定安装位置容器
     * 如果为全局模块只安装在 {@link IApplication} 应用容器中此参数才设置有效
     *
     * @return
     */
    boolean isGlobal();

    /**
     * 获取插件包类型
     *
     * @return
     */
    Class<?> getPackageClass();

    /**
     * 获取插件模块
     *
     * @return
     */
    @Override
    String getName();

    /**
     * 获取包信息
     *
     * @return
     */
    FileArtifact getArtifact();

    /**
     * 获取插件依赖包列表
     *
     * @return
     */
    List<FileArtifact> getArtifactList();
}