package ghost.framework.module.context;

import ghost.framework.core.module.IModule;

import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 10:24 2019-05-18
 */
public interface IModuleLoaderContent {
    /**
     * 获取加载模块内容
     * @return
     */
    IModule getModule();
    /**
     * 获取配置加载类
     *
     * @return
     */
    List<Class<?>> getConfigurationList();

    /**
     * 获取运行类
     *
     * @return
     */
    List<Class<?>> getMainList();

    /**
     * 初始化模块扫描的类
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 获取服务注释类列表
     * @return
     */
    List<Class<?>> getServiceList();
}
