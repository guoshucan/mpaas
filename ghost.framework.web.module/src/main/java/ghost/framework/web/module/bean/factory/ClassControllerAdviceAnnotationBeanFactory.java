package ghost.framework.web.module.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.exception.ExceptionHandler;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.util.ReflectUtil;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IClassControllerAdviceAnnotationBeanFactory;
import ghost.framework.web.context.bind.annotation.RestControllerAdvice;
import ghost.framework.web.context.controller.ExceptionHandlerMethod;
import ghost.framework.web.context.controller.IControllerExceptionHandlerContainer;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * package: ghost.framework.web.module.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link RestControllerAdvice} 注释事件工厂
 * @Date: 2020/2/27:23:56
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassControllerAdviceAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
    extends AbstractWebClassAnnotationBeanFactory<O, T, E, V>
        implements IClassControllerAdviceAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = RestControllerAdvice.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }


    private Logger logger = Logger.getLogger(ClassControllerAdviceAnnotationBeanFactory.class);

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取注入注释对象
//        ControllerAdvice controller = this.getAnnotation(event);
        //获取容器接口
        IControllerExceptionHandlerContainer exceptionHandlerContainer = this.getWebModule().getBean(IControllerExceptionHandlerContainer.class);
        //获取指定注释的全部注释函数
        List<Method> list = ReflectUtil.getAllAnnotationMethods(event.getTarget(), ExceptionHandler.class);
        //遍历请求函数
        for (Method method : list) {
            //获取函数错误注释
            ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
            for (Class<? extends Throwable> throwable : exceptionHandler.value()) {
                synchronized (exceptionHandlerContainer.getRoot()) {
                    if (exceptionHandlerContainer.containsKey(throwable)) {
                        logger.warn("exception handler exist:" + throwable.getName());
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("exception handler put:" + exceptionHandlerContainer.put(throwable, new ExceptionHandlerMethod(event.getValue(), method, throwable)).toString());
                        } else {
                            exceptionHandlerContainer.put(throwable, new ExceptionHandlerMethod(event.getValue(), method, throwable));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}