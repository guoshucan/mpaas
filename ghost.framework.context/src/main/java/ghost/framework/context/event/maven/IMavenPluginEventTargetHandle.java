package ghost.framework.context.event.maven;

import ghost.framework.maven.FileArtifact;

import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.context.event.maven
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven插件事件目标处理接口
 * @Date: 2020/2/14:8:12
 * @param <O> 拥有者
 * @param <T> 目标对象
 */
public interface IMavenPluginEventTargetHandle<O, T>
        extends IMavenEventTargetHandle<O, T> {
    /**
     * 获取是否已经加载依赖
     * 如果为true表示插件的相关依赖包已经加载
     * 如果为false表示插件相关依赖包未加载，需要先加载插件依赖包后再加载插件本身
     * @return
     */
    boolean isAlreadyLoaded();

    /**
     * 设置是否已经加载依赖
     * @param alreadyLoaded 是否已经加载依赖
     */
    void setAlreadyLoaded(boolean alreadyLoaded);

    /**
     * 设置插件地图
     * {@see MavenPluginEventTargetHandle::artifactMap}参数格式，Map<插件包信息, List<插件依赖包>>
     * @param plugins 插件地图
     */
    void setPlugins(Map<FileArtifact, List<FileArtifact>> plugins);

    /**
     * 获取插件列表
     * @return 返回插件地图
     */
    Map<FileArtifact, List<FileArtifact>> getPlugins();

    /**
     * 获取插件加载或卸载类型
     * @return
     */
    Class<?> getType();

    /**
     * 设置插件加载或卸载类型
     * @param type
     */
    void setType(Class<?> type);
}