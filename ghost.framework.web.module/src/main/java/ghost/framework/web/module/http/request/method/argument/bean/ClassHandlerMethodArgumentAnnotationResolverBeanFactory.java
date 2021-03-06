package ghost.framework.web.module.http.request.method.argument.bean;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolver;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolverContainer;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.module.http.request.method.argument.bean
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/29:9:38
 */
@ClassAnnotationBeanFactory(single = true, tag = AnnotationTag.AnnotationTags.Container)
public class ClassHandlerMethodArgumentAnnotationResolverBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends IRequestMethodArgumentResolver
                >
        implements IClassHandlerMethodArgumentAnnotationResolverBeanFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassHandlerMethodArgumentAnnotationResolverBeanFactory(@Autowired IApplication app) {
        this.app = app;
    }

    /**
     * 应用接口
     */
    private IApplication app;

    /**
     * 获取应用接口
     *
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = HandlerMethodArgumentResolver.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 注入模块
     */
    @Autowired
    private IModule module;

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //加入处理函数参数解析器容器
        this.module.getBean(IRequestMethodArgumentResolverContainer.class).add(event.getValue());
    }
}