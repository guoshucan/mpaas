package ghost.framework.beans.application.event;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:全局应用事件主题枚举
 * @Date: 2020/2/16:0:32
 */
public enum ApplicationEventTopic {
    /**
     * 应用启动完成
     */
    AppLaunchCompleted,
    /**
     * 模块初始化
     */
    ModuleLaunch,
    /**
     * 模块初始化完成
     */
    ModuleLaunchCompleted,
    /**
     * 加载模块启动线程
     */
    loadModuleStartThread,
    /**
     * 加载模块启动线程完成
     */
    loadModuleStartThreadCompleted,
    /**
     * 插件加载存在
     */
    PluginLoaderExist,
    /**
     * 插件加载完成
     */
    PluginLoaderCompleted,
    /**
     * 模块加载插件
     */
    ModuleLaunchPlugin;
}
