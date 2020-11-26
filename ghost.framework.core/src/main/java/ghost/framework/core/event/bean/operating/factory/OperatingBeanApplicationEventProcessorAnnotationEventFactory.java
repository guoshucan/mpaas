package ghost.framework.core.event.bean.operating.factory;

import ghost.framework.beans.annotation.event.BeanEventListenerProcessor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/**
 * package: ghost.framework.core.event.bean.operating.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/16:16:47
 */
public class OperatingBeanApplicationEventProcessorAnnotationEventFactory
        <O extends ICoreInterface, T extends IBeanDefinition, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IOperatingBeanApplicationEventProcessorAnnotationEventFactory<O, T, E> {
    /**
     * e
     * 日志
     */
     private Log log = LogFactory.getLog(this.getClass());
    public OperatingBeanApplicationEventProcessorAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }
    @Override
    public String toString() {
        return "OperatingBeanApplicationEventProcessorAnnotationEventFactory{" +
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
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = BeanEventListenerProcessor.class;

    /**
     * 获取绑定注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public Annotation getAnnotation(IAnnotationBeanTargetHandle event) {
        return null;
    }

    /**
     * 添加绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //这里不做任何处理
        //处理处理嵌套注释 BeanClassNameContainer的操作
    }
    /**
     * 删除绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
    }
}