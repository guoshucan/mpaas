package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.log.IGetLog;

/**
 * package: ghost.framework.core.bean
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释绑定工厂基础类
 * {@link IGetLog}
 * {@link IGetApplication#getApp()}
 * {@link IBeanFactory}
 * {@link IApplicationOwnerBeanFactory}
 * {@link IApplicationExecuteOwnerBeanFactory}
 * {@link IClassAnnotationBeanFactory}
 * @Date: 2020/6/3:22:56
 */
public abstract class AbstractClassAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
    implements IClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 处理错误
     * @param e
     */
    protected void exception(Exception e) {
        if (this.getLog().isDebugEnabled()) {
            e.printStackTrace();
            this.getLog().debug(e.getMessage(), e);
        } else {
            this.getLog().error(e.getMessage(), e);
        }
    }
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