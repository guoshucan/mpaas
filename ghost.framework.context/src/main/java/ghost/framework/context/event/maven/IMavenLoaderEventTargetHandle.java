package ghost.framework.context.event.maven;

import ghost.framework.context.module.IModuleClassLoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用maven加载接口
 * @Date: 16:12 2020/1/11
 * @param <O> 发起发类型
 * @param <T> 事件目标类型
 * @param <M> 模块类加载器类型
 */
public interface IMavenLoaderEventTargetHandle<O, T, M extends IModuleClassLoader> extends IMavenEventTargetHandle<O, T> {
    /**
     * 获取模块类加载器
     * @return 返回模块类加载器
     */
    M getClassLoader();

    /**
     * 设置模块类加载器
     * @param classLoader 模块类加载器
     */
    void setClassLoader(M classLoader);
}