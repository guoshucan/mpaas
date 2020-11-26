package ghost.framework.jetty.ws.module;
import ghost.framework.beans.annotation.Autowired;
import ghost.framework.beans.execute.annotation.BeanAction;
import ghost.framework.beans.execute.annotation.Init;
import ghost.framework.beans.execute.annotation.Start;
import ghost.framework.core.application.IApplication;
import ghost.framework.core.module.IModule;
import ghost.framework.core.module.environment.IModuleEnvironment;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 4:25 2019/11/8
 */
public final class JettyWsApplication implements AutoCloseable{
    @Override
    public void close() throws Exception {

    }
    /**
     * 拥有者模块内容
     */
    @Autowired
    private IModule module;
    /**
     * 模块全局配置
     */
    @Autowired
    private IModuleEnvironment env;
    /**
     * 注入应用内容接口
     */
    @Autowired
    private IApplication app;
    /**
     * 启动
     */
    @Init
    public void start() {

    }
    /**
     * 初始化函数
     * 在初始化类后执行此注释函数后再执行@Start注释函数
     */
    private void init() {

    }
}
