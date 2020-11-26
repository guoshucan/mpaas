package ghost.framework.module.core.interceptors;
import ghost.framework.context.module.IModule;

import java.lang.annotation.Annotation;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块加载内容注释拦截器
 * @Date: 11:01 2019-05-18
 */
public final class ModuleLoaderContentAnnotationInterceptor {
    /**
     * 重写哈希值
     * @return
     */
    @Override
    public int hashCode() {
        return super.hashCode() + this.annotation.hashCode() + this.module.hashCode();
    }

    /**
     * 重写比对
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModuleLoaderContentAnnotationInterceptor) {
            ModuleLoaderContentAnnotationInterceptor interceptor = (ModuleLoaderContentAnnotationInterceptor) obj;
            if (this.module.equals(interceptor.module) && this.annotation.equals(interceptor.annotation)) {
                return true;
            } else {
                return false;
            }
        }
        return super.equals(obj);
    }
    /**
     * 初始化模块加载内容注释拦截器
     * @param module 注释所属模块
     * @param annotation 注释类型
     */
    public ModuleLoaderContentAnnotationInterceptor(IModule module, Class<? extends Annotation> annotation) {
        this.module = module;
        this.annotation = annotation;
    }
    /**
     * 初始化模块加载内容注释拦截器
     * @param annotation 注释类型
     */
    public ModuleLoaderContentAnnotationInterceptor(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
    /**
     * 拥有者
     */
    private IModule module;

    /**
     * 获取拥有者
     * @return
     */
    public IModule getModule() {
        return module;
    }

    /**
     * 注释类型
     */
    private Class<? extends Annotation> annotation;

    /**
     * 获取注释类型
     * @return
     */
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
