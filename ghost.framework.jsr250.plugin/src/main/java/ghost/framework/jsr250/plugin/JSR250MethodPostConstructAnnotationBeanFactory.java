package ghost.framework.jsr250.plugin;

import ghost.framework.beans.annotation.event.module.MethodAnnotationEventFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.BeanMethodException;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactory;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/**
 * package: ghost.framework.jsr250.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定 {@link PostConstruct} JSR-250标准注解之 @PostConstruct注释事件工厂
 * @Date: 2020/2/21:13:45
 */
@MethodAnnotationEventFactory(lifeCycle =  AnnotationTag.AnnotationLifeCycle.Loader)
public class JSR250MethodPostConstructAnnotationBeanFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements MethodAnnotationBeanFactory<O, T, E> {
    public JSR250MethodPostConstructAnnotationBeanFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "JSR250MethodPostConstructAnnotationBeanFactory{" +
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
    private Class<? extends Annotation> annotation = PostConstruct.class;

    /**
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }


    @Override
    public void loader(E event) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().info("method:" + event.toString());
        }
        //获取注释
        PostConstruct postConstruct = this.getAnnotation(event);
        //判断是否无效处理引发错误
        try {
            event.getMethod().setAccessible(true);
            event.getMethod().invoke(event.getTarget(), event.getExecuteOwner().newInstanceParameters(event.getTarget(), event.getMethod()));
        } catch (Exception e) {
            if (this.getLog().isTraceEnabled()) {
                e.printStackTrace();
                this.getLog().debug(e.getMessage(), e);
            } else {
                this.getLog().error(e.getMessage(), e);
            }
            throw new BeanMethodException(e, event.getMethod());
        }
    }
}