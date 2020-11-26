package ghost.framework.web.context.bens.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.module.IModule;

/**
 * package: ghost.framework.web.context.bens.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * 主要实现
 * {@link IWebClassAnnotationBeanFactory#getWebModule()}
 * @Date: 2020/6/3:23:00
 */
public interface IWebClassAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IClassAnnotationBeanFactory<O, T, E, V>
{
    /**
     * 获取web模块接口
     * 本身也是作为获取 ghost.framework.web.module 模块本身对象接口
     * @return 返回 ghost.framework.web.module 模块接口
     */
    IModule getWebModule();
}