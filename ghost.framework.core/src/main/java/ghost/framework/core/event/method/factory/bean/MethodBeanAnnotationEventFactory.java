package ghost.framework.core.event.method.factory.bean;

import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.BeanMethodException;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.IMethodBeanAnnotationBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link Bean} 列表容器事件工厂类
 * @Date: 19:01 2020/1/14
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
public class MethodBeanAnnotationEventFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        implements IMethodBeanAnnotationBeanFactory<O, T, E> {
    public MethodBeanAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "MethodBeanAnnotationEventFactory{" +
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
    private Class<? extends Annotation> annotation = Bean.class;

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
        Bean bean = this.getAnnotation(event);
        //判断是否无效处理引发错误
        try {
            for (Class<?> c : bean.depend()) {
                event.getExecuteOwner().addBean(c);
            }
            event.getMethod().setAccessible(true);
            if (bean.value().equals("")) {
                event.getExecuteOwner().addBean(event.getMethod().invoke(event.getTarget(), event.getExecuteOwner().newInstanceParameters(event.getTarget(), event.getMethod())));
            } else {
                event.getExecuteOwner().addBean(bean.value(), event.getMethod().invoke(event.getTarget(), event.getExecuteOwner().newInstanceParameters(event.getTarget(), event.getMethod())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.log.error(e.getMessage(), e);
            throw new BeanMethodException(e, event.getMethod());
        }
    }
}