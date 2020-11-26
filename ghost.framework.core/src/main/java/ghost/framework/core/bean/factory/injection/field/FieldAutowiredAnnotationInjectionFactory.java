package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.injection.field.FieldInjectionFactory;
import ghost.framework.context.bean.factory.injection.field.IFieldInjectionTargetHandle;
import ghost.framework.context.exception.InjectionFieldException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link Autowired} 注入工厂类
 * @Date: 2020/2/9:22:45
 */
public class FieldAutowiredAnnotationInjectionFactory
        <
                O extends ICoreInterface,
                T extends Object,
                IF extends IFieldInjectionTargetHandle<O, T, Field, Object>
                >
        implements FieldInjectionFactory<O, T, IF> {
    public FieldAutowiredAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "FieldAutowiredAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    private IApplication app;
    private Log log = LogFactory.getLog(FieldAutowiredAnnotationInjectionFactory.class);

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
    public void injector(IF event) {
        this.getLog().info("field:" + event.toString());
        //
        Autowired autowired = this.getAnnotation(event);
        //声明绑定对象接口
        IBeanDefinition definition;
        //获取绑定对象
        if (autowired.value().equals("")) {
            definition = event.getExecuteOwner().getBeanDefinition(event.getField().getType());
        } else {
            definition = event.getExecuteOwner().getBeanDefinition(autowired.value());
        }
        if (autowired.required()) {
            if (definition == null || definition.getObject() == null) {
                throw new InjectionFieldException(event.getField());
            } else {
                event.setValue(definition.getObject());
            }
        } else {
            //
            if (definition != null) {
                event.setValue(definition.getObject());
            }
        }
        log.info("field:" + event.getField().toString() + "(" + event.getValue() + ")");
    }
}