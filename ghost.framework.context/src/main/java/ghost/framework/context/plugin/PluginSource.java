package ghost.framework.context.plugin;
import ghost.framework.context.application.IApplication;
import ghost.framework.maven.FileArtifact;
import ghost.framework.util.Assert;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * package: ghost.framework.core.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件源类型
 * @Date: 2020/2/21:17:28
 */
public final class PluginSource implements IPluginSource {
    /**
     * 初始化插件源类型
     *
     * @param name         插件名称
     * @param packageClass 插件包类型
     * @param artifact     插件包信息
     * @param artifactList 插件包依赖包列表
     * @param properties   插件配置
     */
    public PluginSource(String name, Class<?> packageClass, FileArtifact artifact, List<FileArtifact> artifactList, Properties properties) {
        Assert.notNullOrEmpty(name, "PluginSource is name null error");
        Assert.notNull(packageClass, "PluginSource is packageClass null error");
        Assert.notNull(artifact, "PluginSource is artifact null error");
        Assert.notNull(artifactList, "PluginSource is artifactList null error");
//        Assert.notNull(properties, "PluginSource is properties null error");
        this.name = name;
        this.packageClass = packageClass;
        this.artifact = artifact;
        this.artifactList = artifactList;
        this.properties = properties;
    }

    /**
     * 插件配置
     */
    private Properties properties;

    /**
     * 获取插件配置
     *
     * @return
     */
    @Override
    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        return properties;
    }

    /**
     * 设置插件配置
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 获取插件包类型
     *
     * @return
     */
    @Override
    public Class<?> getPackageClass() {
        return packageClass;
    }

    /**
     * 包类型
     */
    private Class<?> packageClass;
    /**
     * 包名称
     * 如果未注释插件名称时将使用插件包注释所在包名称作为插件名称
     */
    private String name;

    /**
     * 获取插件模块
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 插件依赖包列表
     */
    private List<FileArtifact> artifactList;

    /**
     * 获取插件依赖包列表
     *
     * @return
     */
    @Override
    public List<FileArtifact> getArtifactList() {
        return artifactList;
    }

    /**
     * 插件包
     */
    private FileArtifact artifact;

    /**
     * 获取插件包信息
     *
     * @return
     */
    @Override
    public FileArtifact getArtifact() {
        return artifact;
    }

    /**
     * 获取是否为全局插件
     * 如果为全局插件将安装在 {@link IApplication} 应用容器，后任何模块加载都将同时加载此模块
     * 如果不为全局插件将使用在指定安装位置容器
     * 如果为全局模块只安装在 {@link IApplication} 应用容器中此参数才设置有效
     *
     * @return
     */
    @Override
    public boolean isGlobal() {
        return this.artifact.getProperties().containsKey("global") && Boolean.valueOf(this.artifact.getProperties().get("global")).booleanValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginSource that = (PluginSource) o;
        return isGlobal() == that.isGlobal() &&
                packageClass.equals(that.packageClass) &&
                name.equals(that.name) &&
                artifactList.equals(that.artifactList) &&
                properties.equals(that.properties) &&
                artifact.equals(that.artifact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageClass, name, artifactList, artifact, properties, isGlobal());
    }

    @Override
    public String toString() {
        return "PluginSource{" +
                "packageClass=" + packageClass.getName() +
                ", name='" + name + '\'' +
                ", artifactList=" + (artifactList == null ? "" : artifactList.size()) +
                ", properties=" + (properties == null ? "" : properties.toString()) +
                ", artifact=" + artifact.toString() +
                ", global=" + isGlobal() +
                '}';
    }
}