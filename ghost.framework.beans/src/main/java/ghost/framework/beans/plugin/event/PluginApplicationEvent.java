package ghost.framework.beans.plugin.event;
import ghost.framework.beans.application.event.GenericApplicationEvent;
import ghost.framework.maven.FileArtifact;

import java.util.Objects;

/**
 * package: ghost.framework.beans.plugin.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件应用事件
 * @Date: 2020/2/21:19:20
 */
public class PluginApplicationEvent extends GenericApplicationEvent {
    private static final long serialVersionUID = 3865414198838567808L;

    /**
     * 初始化应用事件基础类
     *
     * @param source   消息源对象
     * @param topic    指定事件主题
     * @param name 插件名称
     * @param artifact 插件包
     */
    public PluginApplicationEvent(Object source, String topic, String name, FileArtifact artifact) {
        super(source, topic);
        this.name = name;
        this.artifact = artifact;
    }

    /**
     * 插件名称
     */
    private String name;

    /**
     * 获取插件名称
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 插件包
     */
    private FileArtifact artifact;

    /**
     * 获取插件包
     *
     * @return
     */
    public FileArtifact getArtifact() {
        return artifact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PluginApplicationEvent that = (PluginApplicationEvent) o;
        return name.equals(that.name) &&
                artifact.equals(that.artifact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, artifact);
    }

    @Override
    public String toString() {
        return "PluginApplicationEvent{" +
                "name='" + name + '\'' +
                ", artifact=" + artifact.toString() +
                '}';
    }
}