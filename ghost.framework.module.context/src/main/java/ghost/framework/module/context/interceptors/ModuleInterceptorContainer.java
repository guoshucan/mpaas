package ghost.framework.module.context.interceptors;

import ghost.framework.core.interceptors.IInterceptor;
import ghost.framework.core.interceptors.InterceptorContainer;
import ghost.framework.module.context.IModuleContent;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块拦截器容器基础类
 * @Date: 0:47 2019/12/1
 */
public abstract class ModuleInterceptorContainer<I extends IInterceptor> extends InterceptorContainer<I> {
    /**
     * 模块内容
     */
    private IModuleContent module;

    /**
     * 获取模块内容
     * @return
     */
    public IModuleContent getModule() {
        return module;
    }

    /**
     * 初始化模块拦截器容器基础类
     * @param module 模块内容
     */
    protected ModuleInterceptorContainer(IModuleContent module) {
        super();
        this.module = module;
    }
}