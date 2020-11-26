package ghost.framework.core.bean.factory.injection.parameter;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.context.bean.factory.injection.parameter.ParameterInjectionContainer;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionFactory;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionTargetHandle;
import ghost.framework.context.converter.ConverterContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Map;
/**
 * package: ghost.framework.core.bean.factory.injection.parameter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认参数注入容器
 * @Date: 2020/2/9:23:23
 */
public class DefaultParameterInjectionContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IParameterInjectionTargetHandle<O, T, Constructor, Method, Parameter, Object>,
                V extends IParameterInjectionFactory<O, T, E>
                >
        extends AbstractBeanFactoryContainer<V>
        implements ParameterInjectionContainer<O, T, E, V> {
    public DefaultParameterInjectionContainer(@Autowired IApplication app, @Application @Autowired @Nullable ParameterInjectionContainer<O, T, E, V> parent) {
        this.app = app;
        this.parent = parent;
    }
    private IApplication app;
    private ParameterInjectionContainer<O, T, E, V> parent;
    @Override
    public ParameterInjectionContainer<O, T, E, V> getParent() {
        return parent;
    }

    @Override
    public void injector(E event) {
        //判断是否为一个参数注入
        if (event.getParameter() != null) {
            //向父级加载
            if (event.isParentPriority() && this.parent != null) {
                //向父级别处理未执行的注释
                this.parent.injector(event);
                if (event.isHandle()) {
                    return;
                }
            }
            //获取执行注释链
            event.setExecutionAnnotationChain(this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getParameter().getAnnotations()));
            if (event.getExecutionAnnotationChain().isEmpty()) {
                event.setValue(event.getOwner().getNullableBean(event.getParameter().getType()));
                event.setHandle(true);
                return;
            }
            //遍历注释执行链
            for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : event.getExecutionAnnotationChain().entrySet()) {
                //一个参数注入
                for (V v : new ArrayList<>(this)) {
                    //判断注释工厂
                    if (v.getAnnotationClass().equals(entry.getKey())) {
                        //判断未加载注释处理
                        if (!v.isLoader(event)) {
                            v.positionOwner(event);//定位拥有者
                            v.injector(event);//注入处理
                            v.setExecute(event);//设置注释执行状态
                            break;
                        }
                    }
                }
            }
            //如果注释执行完成退出
            if (event.isHandle()) {
                return;
            }
            if (!event.isParentPriority() && this.parent != null) {
                this.parent.injector(event);
            }
            return;
        } else {
            //获取基础类型转换器工厂
            ConverterContainer container = this.app.getNullableBean(ConverterContainer.class);
            //多个参数注入
            //创建返回数组参数
            Object[] objects = new Object[event.getParameters().length];
            int i = 0;
            //遍历执行全部参数
            for (Parameter p : event.getParameters()) {
                //设置注入参数
                event.setParameter(p);
                //向父级加载
                this.injector(event);
                //判断是否为基础类型需要转换类型
                if (container != null && p.getType().isPrimitive()) {
                    //转换值
                    //获取注入完成值
                    objects[i] = container.getTargetTypeConverter(p.getType()).convert(event.getValue());
                } else {
                    //获取注入完成值
                    objects[i] = event.getValue();
                }
                i++;
            }
            //完成全部注释参数
            event.setValues(objects);
        }
    }
}