package ghost.framework.context.base;

import ghost.framework.beans.maven.IGetArtifacts;
import ghost.framework.context.IGetDev;
import ghost.framework.context.IGetName;
import ghost.framework.context.application.event.ApplicationEventPublisher;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.context.base.proxy.ICglibInstance;
import ghost.framework.context.base.proxy.IJdkInstance;
import ghost.framework.context.base.proxy.IJavassistInstance;
import ghost.framework.context.bean.IBean;
import ghost.framework.context.plugin.IGetPluginContainer;
import ghost.framework.context.proxy.IProxy;

/**
 * package: ghost.framework.core.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:核心接口
 * @Date: 2020/1/8:21:57
 */
public interface ICoreInterface
        extends IBean, IInstance, IObjectInjection, IGetClassLoader, IProxy, IGetHome, IGetName,
        IAnnotationMethod, IGetDev, IGetArtifacts, IGetEnv, IGetPluginContainer, ICglibInstance, IJdkInstance, IJavassistInstance,
        ApplicationEventPublisher, IInstanceInjection {
}