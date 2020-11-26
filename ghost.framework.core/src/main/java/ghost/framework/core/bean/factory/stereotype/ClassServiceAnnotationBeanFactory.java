package ghost.framework.core.bean.factory.stereotype;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.stereotype.IClassServiceAnnotationBeanFactory;
import ghost.framework.context.exception.InjectionAnnotationBeanException;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/**
 * package: ghost.framework.core.event.annotation.factory.entrance
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link Service} 注释事件工厂类
 * @Date: 11:44 2020/1/10
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
@Component
public class ClassServiceAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassServiceAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = Service.class;

    /**
     * 重新注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassServiceAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 绑定事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
        Service service = this.getAnnotation(event);
        //绑定依赖类型
        for (Class<?> c : service.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        //构建类型实例
        this.newInstance(event);
        //判断服务注释是否指定名称
        if (service.name().equals("")) {
            //未指定绑定服务名称
            //使用注释函数名称绑定
            try {
                //判断函数名称绑定注释
                Method method = ReflectUtil.findMethod(event.getTarget(), Service.Name.class);
                //判断是否注释函数名称
                if (method != null) {
                    //获取函数注释名称
                    Service.Name name = method.getAnnotation(Service.Name.class);
                    //调用函数获取绑定名称
                    event.setName(name.prefix() + method.invoke(event.getValue(), event.getExecuteOwner().newInstanceParameters(event.getValue(), method)).toString());
                }
            } catch (Exception e) {
                throw new InjectionAnnotationBeanException(e);
            }
        } else {
            //指定绑定的服务名称
            event.setName(service.name());
        }
        //绑定入容器中
        if (StringUtils.isEmpty(event.getName())) {
            event.getExecuteOwner().addBean(event.getValue());
        } else {
            event.getExecuteOwner().addBean(event.getName(), event.getValue());
        }
    }

    /**
     * 删除绑定事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        if (!event.getTarget().isAnnotationPresent(this.annotation)) {
            return;
        }
        this.getLog().info("unloader:" + event.toString());
        //获取注入注释对象
        Service service = (Service) event.getTarget().getAnnotation(this.annotation);
        //获取核心接口
        this.positionOwner(event);
        //构建类型
        event.setValue((V) event.getExecuteOwner().getBean((Class<?>) event.getTarget()));
        //锁定对象
        synchronized (event.getValue()) {
            //判断服务注释是否指定名称
            if (service.name().equals("")) {
                //未指定绑定服务名称
                //判断是否注释名称
                if (event.getTarget().isAnnotationPresent(Service.Name.class)) {
                    Service.Name name = event.getTarget().getAnnotation(Service.Name.class);
                    //使用注释函数名称绑定
                    try {
                        //判断函数名称绑定注释
                        Method method = ReflectUtil.findMethod(event.getTarget(), Service.Name.class);
                        //调用函数获取绑定名称
                        event.setName(name.prefix() + method.invoke(event.getValue(), event.getExecuteOwner().newInstanceParameters(event.getValue(), method)).toString());
                    } catch (Exception e) {
                        throw new InjectionAnnotationBeanException(e);
                    }
                }
            } else {
                //指定绑定的服务名称
                event.setName(service.name());
            }
            //删除依赖类型
            for (Class<?> c : service.depend()) {
                event.getExecuteOwner().removeBean(c);
            }
            //删除本身
            if (event.getName().equals("")) {
                event.getExecuteOwner().removeBean(event.getTarget());
            } else {
                event.getExecuteOwner().removeBean(event.getName());
            }
        }
        //已经处理
        event.setHandle(true);
    }
}