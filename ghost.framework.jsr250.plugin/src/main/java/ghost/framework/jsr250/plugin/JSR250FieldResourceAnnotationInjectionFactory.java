package ghost.framework.jsr250.plugin;

import ghost.framework.beans.annotation.bean.injection.field.FieldAnnotationInjectionFactory;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.injection.field.FieldInjectionFactory;
import ghost.framework.context.bean.factory.injection.field.IFieldInjectionTargetHandle;
import ghost.framework.context.exception.InjectionFieldException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
/**
 * package: ghost.framework.jsr250.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注入 {@link Resource} JSR-250标准注解之 @Resource注释事件工厂
 * @Date: 2020/2/21:15:25
 */
@FieldAnnotationInjectionFactory
public class JSR250FieldResourceAnnotationInjectionFactory
        <
                O extends ICoreInterface,
                T extends Object,
                IF extends IFieldInjectionTargetHandle<O, T, Field, Object>
                >
        implements FieldInjectionFactory<O, T, IF> {
    public JSR250FieldResourceAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "JSR250FieldResourceAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    private IApplication app;
     private Log log = LogFactory.getLog(JSR250FieldResourceAnnotationInjectionFactory.class);

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public IApplication getApp() {
        return app;
    }

    private Class<? extends Annotation> annotation = Resource.class;

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public void injector(IF event) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("field:" + event.toString());
        }
        //
        Resource resource = this.getAnnotation(event);
        //声明绑定对象接口
        IBeanDefinition definition;
        //获取绑定对象
        if (resource.type().equals(java.lang.Object.class)) {
            if (resource.name().equals("")) {
                definition = event.getExecuteOwner().getBeanDefinition(event.getField().getType());
            } else {
                definition = event.getExecuteOwner().getBeanDefinition(resource.name());
            }
        } else {
            //处理注入资源指定类型
            definition = event.getExecuteOwner().getBeanDefinition(resource.type());
            if (resource.name().equals("")) {
                definition = event.getExecuteOwner().getBeanDefinition(event.getField().getType());
            } else {
                definition = event.getExecuteOwner().getBeanDefinition(resource.name());
            }
        }
        //判断是否注释可空注释
        if (event.getField().isAnnotationPresent(Nullable.class)) {
            if (definition != null) {
                event.setValue(definition.getObject());
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("field nullable:" + event.getField().toString() + "(" + event.getValue() + ")");
            }
            return;
        }
        //判断是否不可空注释
        if (event.getField().isAnnotationPresent(NotNull.class)) {
            if (definition == null || definition.getObject() == null) {
                throw new InjectionFieldException(event.getField());
            } else {
                event.setValue(definition.getObject());
                if (this.log.isDebugEnabled()) {
                    this.log.debug("field notNull:" + event.getField().toString() + "(" + event.getValue() + ")");
                }
                return;
            }
        }
        //
        if (definition != null) {
            event.setValue(definition.getObject());
        }
        if (this.log.isDebugEnabled()) {
            log.debug("field:" + event.getField().toString() + "(" + event.getValue() + ")");
        }
    }
}