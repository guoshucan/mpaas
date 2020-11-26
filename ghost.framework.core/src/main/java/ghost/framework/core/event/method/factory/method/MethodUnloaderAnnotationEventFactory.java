package ghost.framework.core.event.method.factory.method;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.BeanMethodException;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.IMethodLoaderAnnotationBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.method.factory.order
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/14:8:43
 */
public class MethodUnloaderAnnotationEventFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodLoaderAnnotationBeanFactory<O, T, E> {
    public MethodUnloaderAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodUnloaderAnnotationEventFactory{" +
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
     * 获取日志
     *
     * @return
     */
    @Override
    public Log getLog() {
        return log;
    }

    /**
     * 类型注释工厂注释
     */
    private Class<? extends Annotation> annotation = Unloader.class;

    /**
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 日志
     */
     private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //获取注释
        Unloader ul = this.getAnnotation(event);
        try {
            event.getMethod().invoke(event.getTarget(), event.getExecuteOwner().newInstanceParameters(event.getTarget(), event.getMethod()));
        } catch (Exception e) {
            throw new BeanMethodException(e);
        }
    }
}