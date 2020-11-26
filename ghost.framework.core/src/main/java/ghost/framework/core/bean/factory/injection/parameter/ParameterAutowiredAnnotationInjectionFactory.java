package ghost.framework.core.bean.factory.injection.parameter;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionFactory;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionTargetHandle;
import ghost.framework.context.exception.InjectionParameterException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * package: ghost.framework.core.bean.factory.injection.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/9:23:56
 */
public class ParameterAutowiredAnnotationInjectionFactory
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IParameterInjectionTargetHandle<O, T, Constructor, Method, Parameter, Object>
                >
        implements IParameterInjectionFactory<O, T, E> {
    public ParameterAutowiredAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
     private Log log = LogFactory.getLog(ParameterAutowiredAnnotationInjectionFactory.class);

    @Override
    public String toString() {
        return "ParameterAutowiredAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

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
    public void injector(E event) {
        Autowired autowired = this.getAnnotation(event);
        //声明绑定对象接口
        IBeanDefinition definition;
        //获取绑定对象
        if (autowired.value().equals("")) {
            definition = event.getExecuteOwner().getBeanDefinition(event.getParameter().getType());
        } else {
            definition = event.getExecuteOwner().getBeanDefinition(autowired.value());
        }
        //判断是否注释可空注释
        if (event.getParameter().isAnnotationPresent(Nullable.class)) {
            if (definition != null) {
                event.setValue(definition.getObject());
            }
            this.log.info("parameter nullable:" + event.getParameter().toString() + "(" + event.getValue() + ")");
            return;
        }
        //判断是否不可空注释
        if (event.getParameter().isAnnotationPresent(NotNull.class)) {
            if (definition == null || definition.getObject() == null) {
                throw new InjectionParameterException(event.getParameter());
            } else {
                event.setValue(definition.getObject());
                this.log.info("parameter notNull:" + event.getParameter().toString() + "(" + event.getValue() + ")");
                return;
            }
        }
        //
        if (definition != null) {
            event.setValue(definition.getObject());
        }
        this.log.info("parameter:" + event.getParameter().toString() + "(" + event.getValue() + ")");
    }
}