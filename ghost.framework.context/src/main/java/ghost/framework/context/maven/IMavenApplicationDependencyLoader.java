package ghost.framework.context.maven;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.module.IModuleClassLoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven应用依赖加载接口
 * @Date: 10:10 2019/12/8
 * @param <O>
 * @param <T>
 * @param <M>
 * @param <E>
 */
public interface IMavenApplicationDependencyLoader<O extends ICoreInterface, T, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
        extends ILoader<O, T, E> {
}