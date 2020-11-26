package ghost.framework.application.core.environment;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationEnvironment;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.core.environment.EventEnvironment;
import ghost.framework.core.event.environment.factory.IEnvironmentEventListenerFactoryContainer;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用env
 * @Date: 9:49 2019-06-08
 */
@Component
public final class ApplicationEnvironment extends EventEnvironment implements IApplicationEnvironment {
    /**
     * 应用内容
     */
    private IApplication app;

    /**
     * 获取env事件监听容器
     *
     * @return
     */
    @Override
    protected IEnvironmentEventListenerFactoryContainer getEventListenerFactoryContainer() {
        if (eventListenerFactoryContainer == null) {
            eventListenerFactoryContainer = this.app.getNullableBean(IEnvironmentEventListenerFactoryContainer.class);
        }
        return eventListenerFactoryContainer;
    }

    /**
     *
     * @return
     */
    @Override
    protected ICoreInterface getTarget() {
        return app;
    }
    /**
     *
     * @param app
     */
    public ApplicationEnvironment(@Autowired IApplication app) {
        super();
        this.app = app;
    }
}