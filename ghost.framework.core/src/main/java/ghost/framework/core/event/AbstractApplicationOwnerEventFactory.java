package ghost.framework.core.event;

import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IApplicationOwnerBeanFactory;
import ghost.framework.context.bean.factory.IOwnerBeanTargetHandle;
import org.apache.commons.logging.Log;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用事件工厂基础类
 * @Date: 16:25 2020/1/16
 */
public abstract class AbstractApplicationOwnerEventFactory
        <O extends ICoreInterface, T, E extends IOwnerBeanTargetHandle<O, T>>
        implements IApplicationOwnerBeanFactory<O, T, E> {
    /**
     * 获取日志
     * @return
     */
    @Override
    public Log getLog() {
        return this.app.getLog();
    }

    /**
     * 应用接口
     */
    private IApplication app;

    /**
     * 获取应用接口
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 初始化应用注释注入基础类
     * @param app 设置应用接口
     */
    protected AbstractApplicationOwnerEventFactory(IApplication app){
        this.app = app;
    }
}