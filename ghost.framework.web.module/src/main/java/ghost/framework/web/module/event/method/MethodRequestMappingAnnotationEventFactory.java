package ghost.framework.web.module.event.method;

import ghost.framework.beans.annotation.event.module.MethodAnnotationEventFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.request.HttpRequestMethodPath;
import ghost.framework.web.module.http.request.HttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.web.module.event.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/19:12:12
 */
@MethodAnnotationEventFactory(tag = AnnotationTag.AnnotationTags.Container)
public class MethodRequestMappingAnnotationEventFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodRequestMappingAnnotationEventFactory<O, T, E> {
    public MethodRequestMappingAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodRequestMappingAnnotationEventFactory{" +
                "annotation=" + annotation.toString() +
                '}';
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
     * 类型注释工厂注释
     */
    private Class<? extends Annotation> annotation = RequestMapping.class;

    /**
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }
    @Autowired
    private IModule module;

    @Override
    public void loader(E event) {
        //获取注释
        RequestMapping mapping = this.getAnnotation(event);
        //判断是否无效处理引发错误
        //获取 HttpRequestMethod 容器接口
        IHttpRequestMethodContainer methodContainer = this.module.getBean(IHttpRequestMethodContainer.class);
        //创建请求函数
        HttpRequestMethod requestMethod = new HttpRequestMethod(
                event.getTarget(),
                event.getTarget().getClass().getAnnotation(RestController.class),
                event.getMethod(),
                mapping);
        synchronized (methodContainer.getSortedMap()) {
            //将请求函数添加入请求函数容器
            methodContainer.put(new HttpRequestMethodPath(requestMethod.getPath(), requestMethod), requestMethod);
        }
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug("add HttpRequestMethodContainer RequestMapping>method:" + event.getMethod().getName() + ">Path:" + requestMethod.getPath());
        }
    }
}