package ghost.framework.context.plugin;

/**
 * package: ghost.framework.context.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取插件容器接口
 * @Date: 2020/2/22:17:43
 */
@FunctionalInterface
public interface IGetPluginContainer {
    /**
     * 获取插件容器接口
     * @return
     */
    IPluginContainer getPluginContainer();
}
