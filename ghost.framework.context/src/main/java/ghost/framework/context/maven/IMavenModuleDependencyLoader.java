package ghost.framework.context.maven;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.module.IModuleClassLoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven模块依赖加载器接口
 * @Date: 12:46 2019/12/8
 */
public interface IMavenModuleDependencyLoader<O extends ICoreInterface, T, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
        extends ILoader<O, T, E> {
}
