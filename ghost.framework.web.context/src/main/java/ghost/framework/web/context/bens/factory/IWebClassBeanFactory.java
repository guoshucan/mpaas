package ghost.framework.web.context.bens.factory;

import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.*;
import ghost.framework.context.log.IGetLog;
import ghost.framework.context.module.IModule;
/**
 * package: ghost.framework.web.context.bens.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web类型绑定工厂基础接口
 * {@link IGetLog}
 * {@link IGetApplication#getApp()}
 * {@link IBeanFactory}
 * {@link IApplicationOwnerBeanFactory}
 * {@link IApplicationExecuteOwnerBeanFactory}
 * {@link IClassBeanFactory}
 * 主要实现
 * {@link IWebClassBeanFactory#getWebModule()}
 * @Date: 2020/6/3:22:27
 */
public interface IWebClassBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassBeanFactory<O, T, E, V> {
    /**
     * 获取web模块接口
     * 本身也是作为获取 ghost.framework.web.module 模块本身对象接口
     * @return 返回 ghost.framework.web.module 模块接口
     */
    IModule getWebModule();
}