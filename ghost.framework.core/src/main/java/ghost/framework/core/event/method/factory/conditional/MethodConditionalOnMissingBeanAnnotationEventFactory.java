package ghost.framework.core.event.method.factory.conditional;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingBean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.conditional.IMethodConditionalOnMissingBeanAnnotationEventFactory;
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
public class MethodConditionalOnMissingBeanAnnotationEventFactory<O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodConditionalOnMissingBeanAnnotationEventFactory<O, T, E> {
    public MethodConditionalOnMissingBeanAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodConditionalOnMissingBeanAnnotationEventFactory{" +
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
    private Class<? extends Annotation> annotation = ConditionalOnMissingBean.class;

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
        ConditionalOnMissingBean onMissingBean = this.getAnnotation(event);
        for (Class<?> c : onMissingBean.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        Class<?>[] cs = onMissingBean.value();
        String[] name = onMissingBean.name();
        for (int i = 0; i <= name.length; i++) {
            if (!event.getExecuteOwner().containsBean(name[i])) {
                event.getExecuteOwner().addBean(name[i], cs[i]);
            }
        }
    }
}