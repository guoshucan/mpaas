package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.field.IMethodInjectionTargetHandle;
import ghost.framework.context.bean.factory.injection.field.MethodInjectionFactory;
import ghost.framework.context.exception.InjectionBeanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link Autowired} 注入工厂类
 * @Date: 2020/7/22:22:37
 */
public class MethodAutowiredAnnotationInjectionFactory   <
        O extends ICoreInterface,
        T extends Object,
        IM extends IMethodInjectionTargetHandle<O, T, Method, Object>
        >
        implements MethodInjectionFactory<O, T, IM> {
    public MethodAutowiredAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodAutowiredAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    private IApplication app;
    private Log log = LogFactory.getLog(MethodAutowiredAnnotationInjectionFactory.class);

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public IApplication getApp() {
        return app;
    }

    private Class<? extends Annotation> annotation = Autowired.class;

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public void injector(IM event) {
        this.getLog().info("method:" + event.toString());
        //
//        Autowired autowired = this.getAnnotation(event);
        Object v = event.getValue() == null ? event.getTarget() : event.getValue();
        try {
            event.getMethod().setAccessible(true);
            event.getMethod().invoke(v, event.getExecuteOwner().newInstanceTargetParameters(v, null, event.getMethod(), event.getMethod().getParameters()));
        } catch (IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e) {
            throw new InjectionBeanException(e.getMessage(), e);
        } finally {
            event.getMethod().setAccessible(false);
        }
        log.info("method:" + event.getMethod().toString());
    }
}