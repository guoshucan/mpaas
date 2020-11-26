package ghost.framework.core.event.method.factory.conditional;

import ghost.framework.beans.annotation.conditional.ConditionalOnBean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.conditional.IMethodConditionalOnBeanAnnotationEventFactory;
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
 * @Date: 2020/1/30:19:04
 */
public class MethodConditionalOnBeanAnnotationEventFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodConditionalOnBeanAnnotationEventFactory<O, T, E> {
    public MethodConditionalOnBeanAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodConditionalOnBeanAnnotationEventFactory{" +
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
    private Class<? extends Annotation> annotation = ConditionalOnBean.class;

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
        ConditionalOnBean onBean = this.getAnnotation(event);
        for (Class<?> c : onBean.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        Class<?>[] cs = onBean.value();
        String[] name = onBean.name();
        for (int i = 0; i <= name.length; i++) {
            if (event.getExecuteOwner().containsBean(name[i])) {
                event.getExecuteOwner().addBean(name[i], cs[i]);
            }
        }
    }
}