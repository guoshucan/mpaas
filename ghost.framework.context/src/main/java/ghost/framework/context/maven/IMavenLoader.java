package ghost.framework.context.maven;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.module.IModuleClassLoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用maven加载接口
 * @Date: 8:10 2019/12/17
 * @param <O> 发起发类型
 * @param <T> 事件目标类型
 * @param <M> 模块类加载器类型
 * @param <E> maven加载事件目标处理类型
 */
public interface IMavenLoader<O extends ICoreInterface, T, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
        extends ILoader<O, T, E> {
}