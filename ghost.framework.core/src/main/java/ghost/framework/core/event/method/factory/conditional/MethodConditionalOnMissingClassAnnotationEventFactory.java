package ghost.framework.core.event.method.factory.conditional;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.conditional.IMethodConditionalOnMissingClassAnnotationEventFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.method.factory.lastOrder
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/30:19:06
 */
public class MethodConditionalOnMissingClassAnnotationEventFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodConditionalOnMissingClassAnnotationEventFactory<O, T, E> {
    public MethodConditionalOnMissingClassAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodConditionalOnMissingClassAnnotationEventFactory{" +
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
    private Class<? extends Annotation> annotation = ConditionalOnMissingClass.class;

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
        ConditionalOnMissingClass onMissingClass = this.getAnnotation(event);
        //判断是否无效处理引发错误
        for (Class<?> c : onMissingClass.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        for (Class<?> c : onMissingClass.value()) {
            if (!event.getExecuteOwner().containsBean(c)) {
                event.getExecuteOwner().addBean(c);
            }
        }
    }
}