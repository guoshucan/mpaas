package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.utils.OrderAnnotationUtil;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.context.bean.factory.IAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.bean.factory.injection.IInjectionObjectBeanTargetHandle;
import ghost.framework.context.bean.factory.injection.field.*;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.exception.InjectionFieldException;
import ghost.framework.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * package: ghost.framework.core.bean.factory.injection.value.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:参数与声明注释注入器容器类型
 * @Date: 2020/1/7:16:37
 * @param <O> 发起方容器类型
 * @param <T> 目标对象类型
 * @param <IE> 默认注入入口
 * @param <IO> 目标对象类型
 * @param <IF> 例注入事件目标操作类型
 * @param <IM> 注入函数
 * @param <V> 注释注入器类型
 */
public class DefaultInjectorIContainer
        <
        O extends ICoreInterface,
        T extends Object,
        IE extends IBeanTargetHandle<O, T>,
        IO extends IInjectionObjectBeanTargetHandle<O, T>,
        IF extends IFieldInjectionTargetHandle<O, T, Field, Object>,
        IM extends IMethodInjectionTargetHandle<O, T, Method, Object>,
//        V extends FieldInjectionFactory<O, T, IF>
        V extends IAnnotationBeanFactory<O, T, IAnnotationBeanTargetHandle<O, T>>
        >
        extends AbstractBeanFactoryContainer<V>
        implements IInjectionContainer<O, T, IE, IO, IF, IM, V> {
    public DefaultInjectorIContainer(@Autowired IApplication app,
                                     @Application @Autowired @Nullable IInjectionContainer<O, T, IE, IO, IF, IM, V> parent) {
        this.app = app;
        this.parent = parent;
    }

    @Override
    public IInjectionContainer<O, T, IE, IO, IF, IM, V> getParent() {
        return parent;
    }

    private IInjectionContainer<O, T, IE, IO, IF, IM, V> parent;
    /**
     * 应用接口
     */
    private IApplication app;

    public IApplication getApp() {
        return app;
    }

    @Application
    @Autowired
    private ConverterContainer container;

    /**
     * 注入对象
     *
     * @param event 注入事件
     */
    @Override
    public void injector(IO event) {
        this.getLog().debug("injector(object>class:" + event.getTarget().getClass().getName() + ")");
        //遍历声明注入
        for (Field field : ReflectUtil.getAllField(event.getTarget().getClass())) {
            //执行声明注入
            this.injector((IF) new FieldInjectionTargetHandle(event.getOwner(), event.getTarget(), field));
        }
        //遍历声明注入
        for (Method method : OrderAnnotationUtil.methodListSort(ReflectUtil.getAllMethod(event.getTarget().getClass()))) {
            //执行声明注入
            this.injector((IM) new MethodInjectionTargetHandle(event.getOwner(), event.getTarget(), method));
        }
    }

    /**
     * 注入函数
     * @param event 注入事件
     */
    @Override
    public void injector(IM event) {
        //获取类型注释执行链
        event.setExecutionAnnotationChain(this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getMethod().getDeclaredAnnotations()));
        if (event.getExecutionAnnotationChain().isEmpty()) {
            return;
        }
        this.getLog().debug("injector(class:" + event.getMethod().getDeclaringClass().getName() + ">method:" +
                event.getMethod().getName() + ">class:" + event.getMethod().getReturnType().getSimpleName() + ")");
        //遍历执行注释链
        for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : event.getExecutionAnnotationChain().entrySet()) {
            //遍历比对执行事件工厂
            for (V v : new ArrayList<V>(this)) {
                //判断是否为函数注入
                if (v instanceof MethodInjectionFactory) {
                    MethodInjectionFactory fif = (MethodInjectionFactory) v;
                    //判断注释工厂
                    if (fif.getAnnotationClass().equals(entry.getKey())) {
                        //判断未加载注释处理
                        if (!fif.isLoader(event)) {
                            fif.positionOwner(event);//定位拥有者
                            fif.injector(event);//注入处理
                            fif.setExecute(event);//设置注释执行状态
                            break;
                        }
                    }
                }
            }
        }
        /*
        //判断是否有注入注释
        if (event.getMethod().isAnnotationPresent(Autowired.class)) {
            try {
                event.getMethod().invoke(event.getExecuteOwner().newInstanceTargetParameters(event.getValue(), null, event.getMethod(), event.getMethod().getParameters()));
            } catch (IllegalAccessException | IllegalArgumentException |
                    InvocationTargetException e) {
                throw new InjectionBeanException(e.getMessage(), e);
            }
        }*/
    }

    /**
     * 注入声明事件
     *
     * @param event 注入事件
     */
    @Override
    public void injector(IF event) {
        //向父级加载
        if (event.isParentPriority() && this.parent != null) {
            //向父级别处理未执行的注释
            this.parent.injector(event);
            if (event.isHandle()) {
                return;
            }
        }
        //获取类型注释执行链
        event.setExecutionAnnotationChain(this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getField().getDeclaredAnnotations()));
        if (event.getExecutionAnnotationChain().isEmpty()) {
            return;
        }
        this.getLog().debug("injector(class:" + event.getField().getDeclaringClass().getName() + ">field:" +
                event.getField().getName() + ">class:" + event.getField().getType().getSimpleName() + ")");
        //遍历执行注释链
        for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : event.getExecutionAnnotationChain().entrySet()) {
            //遍历比对执行事件工厂
            for (V v : new ArrayList<V>(this)) {
                //判断是否为声明注入
                if (v instanceof FieldInjectionFactory) {
                    FieldInjectionFactory fif = (FieldInjectionFactory)v;
                    //判断注释工厂
                    if (fif.getAnnotationClass().equals(entry.getKey())) {
                        //判断未加载注释处理
                        if (!fif.isLoader(event)) {
                            fif.positionOwner(event);//定位拥有者
                            fif.injector(event);//注入处理
                            fif.setExecute(event);//设置注释执行状态
                            break;
                        }
                    }
                }
            }
        }
        //获取基础类型转换器工厂
//        ConverterPrimitive primitiveFactory = this.app.getNullableBean(ConverterPrimitive.class);
        //判断是否已经处理
        if (event.isHandle()) {
            //调用注释声明
            try {
                //判断是否为基础类型需要转换类型
                if (event.getField().getType().isPrimitive()) {
                    //转换值
                    //获取注入完成值
                    ReflectUtil.setField(event.getTarget(), event.getField(), container.getTargetTypeConverter(event.getField().getType()).convert(event.getValue()));
                } else {
                    //获取注入完成值
                    ReflectUtil.setField(event.getTarget(), event.getField(), event.getValue());
                }
                return;
            } catch (Exception e) {
                throw new InjectionFieldException(event.getField(), e);
            }
        } else {
            if (!event.isParentPriority() && this.parent != null) {
                this.parent.injector(event);
            }
        }
    }

    /**
     * 注入事件
     *
     * @param event 注入事件
     */
    @Override
    public void injector(IE event) {
        //判断是否为对象入口
        if (event instanceof IInjectionObjectBeanTargetHandle) {
            this.injector((IO) event);
        }
        //判断是否为声明入口
        if (event instanceof IFieldInjectionTargetHandle) {
            this.injector((IF) event);
        }
        //判断是否为函数入口
        if (event instanceof IMethodInjectionTargetHandle) {
            this.injector((IM) event);
        }
    }
}