package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.log.IGetLog;
/**
 * package: ghost.framework.web.module.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型绑定工厂基础类
 * {@link IGetLog}
 * {@link IGetApplication#getApp()}
 * {@link IBeanFactory}
 * {@link IApplicationOwnerBeanFactory}
 * {@link IApplicationExecuteOwnerBeanFactory}
 * {@link IClassBeanFactory}
 * @Date: 2020/6/3:22:29
 */
public abstract class AbstractClassBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        implements IClassBeanFactory<O, T, E, V> {

    /**
     * 应用接口
     */
    @Autowired
    private IApplication app;

    /**
     * 获取应用接口
     *
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }
}