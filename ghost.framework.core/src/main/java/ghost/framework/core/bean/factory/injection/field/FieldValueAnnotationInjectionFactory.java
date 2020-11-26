package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.field.FieldInjectionFactory;
import ghost.framework.context.bean.factory.injection.field.IFieldInjectionTargetHandle;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.exception.InjectionFieldNullException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/9:22:45
 */
public class FieldValueAnnotationInjectionFactory
        <
                O extends ICoreInterface,
                T extends Object,
                IF extends IFieldInjectionTargetHandle<O, T, Field, Object>
                >
        implements FieldInjectionFactory<O, T, IF> {
    public FieldValueAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "FieldValueAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    private IApplication app;
    private Log log = LogFactory.getLog(FieldValueAnnotationInjectionFactory.class);

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public IApplication getApp() {
        return app;
    }

    private Class<? extends Annotation> annotation = Value.class;

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

//    /**
//     * 获取拥有者的env接口
//     * 此函数必须在调用positionOwner函数定位好拥有者之后
//     *
//     * @param event 事件对象
//     * @return
//     */
//    private IEnvironment getEnvironment(IF event) {
//        //判断env拥有者类型
//        if (event.getExecuteOwner() instanceof IApplication) {
//            //获取应用env
//            return event.getExecuteOwner().getBean(IApplicationEnvironment.class);
//        }
//        //获取模块env
//        return event.getExecuteOwner().getBean(IModuleEnvironment.class);
//    }

    @Override
    public void injector(IF event) {
        this.getLog().info("field:" + event.toString());
        //获取注入值注释对象
        Value value = this.getAnnotation(event);
        //获取应用env
        IEnvironment environment = this.getEnvironment(event);
        //
//        if (event.getField().isAnnotationPresent(Nullable.class)) {
//            event.setValue(environment.getNullable(value.prefix(), value.value()));
//            return;
//        }
        //
//        if (event.getField().isAnnotationPresent(NotNull.class)) {
//            event.setValue(environment.getNullable(value.prefix(), value.value()));
//            if (event.getValue() == null) {
//                throw new InjectorFieldNullException(event.getField());
//            }
//            return;
//        }
        String o = environment.getNullable(value.prefix(), value.value());
        if (value.required()) {
            if (o == null) {
                throw new InjectionFieldNullException(event.getField());
            } else {
                event.setValue(this.app.getBean(ConverterContainer.class).convert(event.getField().getType(), o));
            }
        } else {
            if (o != null) {
                event.setValue(this.app.getBean(ConverterContainer.class).convert(event.getField().getType(), o));
            }
        }
    }
}