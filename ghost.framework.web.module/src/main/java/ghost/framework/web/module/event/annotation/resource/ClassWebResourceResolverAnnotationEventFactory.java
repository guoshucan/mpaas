package ghost.framework.web.module.event.annotation.resource;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.bens.annotation.WebResourceResolver;
import ghost.framework.web.context.resource.IWebResourceResolver;
import ghost.framework.web.context.resource.IWebResourceResolverContainer;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.module.event.annotation.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/15:10:15
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassWebResourceResolverAnnotationEventFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends IWebResourceResolver
                >
        implements IClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassWebResourceResolverAnnotationEventFactory(@Autowired IApplication app) {
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
    private final Class<? extends Annotation> annotation = WebResourceResolver.class;

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
     * 注入web模块接口
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
        //获取注入注释对象
//        WebResourceResolver resolver = this.getAnnotation(event);
        //获取web资源解析器容器接口
        this.module.getBean(IWebResourceResolverContainer.class).add(event.getValue());
    }

    @Override
    public void unloader(E event) {

    }
}