package ghost.framework.context.event.maven;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.maven.ExecuteOwnerBeanTargetHandle;
import ghost.framework.maven.FileArtifact;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.core.event.maven
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven插件事件目标处理类
 * @Date: 2020/2/14:8:12
 * @param <O> 拥有者
 * @param <T> 目标对象
 */
public class MavenPluginEventTargetHandle
        <O extends ICoreInterface, T extends Object>
        extends ExecuteOwnerBeanTargetHandle<O, T>
        implements IMavenPluginEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public MavenPluginEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 初始化事件不表处理头
     *
     * @param owner         设置事件目标对象拥有者
     * @param target        设置目标对象
     * @param alreadyLoaded 是否已经加载包
     */
    public MavenPluginEventTargetHandle(O owner, T target, boolean alreadyLoaded) {
        super(owner, target);
        this.alreadyLoaded = alreadyLoaded;
    }
    /**
     * 初始化事件不表处理头
     *
     * @param owner         设置事件目标对象拥有者
     * @param target        设置目标对象
     * @param plugins 插件
     */
    public MavenPluginEventTargetHandle(O owner, T target, Map<FileArtifact, List<FileArtifact>> plugins) {
        super(owner, target);
        this.plugins = plugins;
    }

    /**
     * 插件地图
     */
    private Map<FileArtifact, List<FileArtifact>> plugins = new LinkedHashMap<>();
    /**
     * 获取插件列表
     * @return 返回插件地图
     */
    @Override
    public Map<FileArtifact, List<FileArtifact>> getPlugins() {
        return plugins;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type=type;
    }
    private Class<?> type;
    /**
     * 设置插件地图
     * {@see MavenPluginEventTargetHandle::artifactMap}参数格式，Map<插件包信息, List<插件依赖包>>
     * @param plugins 插件地图
     */
    @Override
    public void setPlugins(Map<FileArtifact, List<FileArtifact>> plugins) {
        this.plugins = plugins;
    }
    /**
     * 设置是否已经加载依赖
     * @param alreadyLoaded 是否已经加载依赖
     */
    @Override
    public void setAlreadyLoaded(boolean alreadyLoaded) {
        this.alreadyLoaded = alreadyLoaded;
    }

    /**
     * 是否已经加载包
     */
    private boolean alreadyLoaded;
    /**
     * 获取是否已经加载依赖
     * 如果为true表示插件的相关依赖包已经加载
     * 如果为false表示插件相关依赖包未加载，需要先加载插件依赖包后再加载插件本身
     * @return
     */
    public boolean isAlreadyLoaded() {
        return alreadyLoaded;
    }
}