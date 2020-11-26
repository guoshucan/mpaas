package ghost.framework.core.event.maven;

import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.module.IModuleClassLoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 16:12 2020/1/11
 * @param <O> 发起发类型
 * @param <T> 事件目标类型
 * @param <M> 模块类加载器类型
 */
public class MavenLoaderEventTargetHandle<O, T, M extends IModuleClassLoader> extends MavenEventTargetHandle<O, T> implements IMavenLoaderEventTargetHandle<O, T, M> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public MavenLoaderEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 模块类加载器
     */
    private M classLoader;

    /**
     * 获取模块类加载器
     * @return 返回模块类加载器
     */
    @Override
    public M getClassLoader() {
        return classLoader;
    }

    /**
     * 设置模块类加载器
     * @param classLoader 模块类加载器
     */
    @Override
    public void setClassLoader(M classLoader) {
        this.classLoader = classLoader;
    }
}
