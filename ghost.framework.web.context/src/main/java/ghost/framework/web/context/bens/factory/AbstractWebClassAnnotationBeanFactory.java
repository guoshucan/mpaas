package ghost.framework.web.context.bens.factory;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.*;
import ghost.framework.context.log.IGetLog;
import ghost.framework.context.module.IModule;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;

/**
 * package: ghost.framework.web.context.bens.factory
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
 * 主要实现
 * {@link AbstractWebClassAnnotationBeanFactory#getWebModule()}
 * {@link IWebClassAnnotationBeanFactory#getWebModule()}
 * @Date: 2020/6/3:22:56
 */
public abstract class AbstractWebClassAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
    extends AbstractClassAnnotationBeanFactory<O, T, E, V>
    implements IWebClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注入 ghost.framework.web.module 模块接口
     */
    @Autowired
    private IModule module;

    /**
     * 获取web模块接口
     * 本身也是作为获取 ghost.framework.web.module 模块本身对象接口
     *
     * @return 返回 ghost.framework.web.module 模块接口
     */
    @Override
    public IModule getWebModule() {
        return module;
    }
}