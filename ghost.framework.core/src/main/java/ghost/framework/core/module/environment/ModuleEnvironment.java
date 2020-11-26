package ghost.framework.core.module.environment;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.environment.Environment;
import ghost.framework.core.environment.EventEnvironment;
import ghost.framework.core.event.environment.factory.IEnvironmentEventListenerFactoryContainer;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.environment.IModuleEnvironment;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块env类
 * @Date: 9:50 2019-06-08
 */
@Component
public final class ModuleEnvironment extends EventEnvironment implements IModuleEnvironment {
    /**
     * 模块内容
     */
    private IModule module;

    /**
     * 获取env事件监听容器
     *
     * @return
     */
    @Override
    protected IEnvironmentEventListenerFactoryContainer getEventListenerFactoryContainer() {
        if (eventListenerFactoryContainer == null) {
            eventListenerFactoryContainer = this.module.getNullableBean(IEnvironmentEventListenerFactoryContainer.class);
        }
        return eventListenerFactoryContainer;
    }

    @Override
    protected ICoreInterface getTarget() {
        return module;
    }

    /**
     * 初始化模块nev
     */
    @Constructor
    public ModuleEnvironment(@Module @Autowired IModule module) {
        super();
        this.module = module;
    }

    /**
     * 初始化模块nev
     *
     * @param env 合并env
     */
    public ModuleEnvironment(Environment env) {
        super();
        this.merge(env);
    }
}