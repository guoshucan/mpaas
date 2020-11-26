package ghost.framework.module.core.interceptors;
import ghost.framework.context.module.IModule;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 10:54 2019-05-18
 */
public interface ModuleLoaderContentAnnotationInterceptors {
    /**
     * 获取加载内容注释拦截器列表
     *
     * @return
     */
    ConcurrentSkipListMap<String, ModuleLoaderContentAnnotationInterceptor> getLoaderContentAnnotationInterceptors();
    /**
     * 注册注释拦截器
     *
     * @param annotationInterceptor 注释拦截器
     */
    void registrarAnnotationInterceptor(ModuleLoaderContentAnnotationInterceptor annotationInterceptor);

    /**
     * 卸载注释拦截器
     *
     * @param annotationInterceptor 注释拦截器
     */
    void unloaderAnnotationInterceptor(ModuleLoaderContentAnnotationInterceptor annotationInterceptor);

    /**
     * 注册注释拦截器
     * @param module 拦截器拥有者
     * @param annotation 注释类型
     */
    void registrarAnnotationInterceptor(IModule module, Class<? extends Annotation> annotation);

    /**
     * 卸载注释拦截器
     * @param module 拦截器拥有者
     * @param annotation 注释类型
     */
    void unloaderAnnotationInterceptor(IModule module, Class<? extends Annotation> annotation);
    /**
     * 注册注释拦截器
     *
     * @param annotation 注释类型
     */
    void registrarAnnotationInterceptor(Class<? extends Annotation> annotation);

    /**
     * 卸载注释拦截器
     *
     * @param annotation 注释类型
     */
    void unloaderAnnotationInterceptor(Class<? extends Annotation> annotation);
}
