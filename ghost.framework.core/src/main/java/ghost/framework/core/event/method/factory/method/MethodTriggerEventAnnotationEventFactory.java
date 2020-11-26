package ghost.framework.core.event.method.factory.method;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.event.annotation.TriggerEvent;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.IMethodTriggerEventAnnotationBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.method.factory.lastOrder
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link TriggerEvent} 列表容器事件工厂类
 * @Date: 19:18 2020/2/2
 */
public class MethodTriggerEventAnnotationEventFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodTriggerEventAnnotationBeanFactory<O, T, E> {
    public MethodTriggerEventAnnotationEventFactory(@Autowired IApplication app) {
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
    private Class<? extends Annotation> annotation = TriggerEvent.class;
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

    }
}