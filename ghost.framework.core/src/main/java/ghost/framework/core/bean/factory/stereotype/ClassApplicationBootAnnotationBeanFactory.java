package ghost.framework.core.bean.factory.stereotype;

import ghost.framework.beans.annotation.stereotype.ApplicationBoot;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.execute.LoadingMode;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.stereotype.IClassApplicationBootAnnotationBeanFactory;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * package: ghost.framework.core.event.annotation.factory.applicationBoot
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link ApplicationBoot} 注释事件工厂类
 * 作为运行类注释处理接口
 * @Date: 11:44 2020/1/10
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
@Component
public class ClassApplicationBootAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassApplicationBootAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = ApplicationBoot.class;

    /**
     * 重新注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 绑定事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取类型注释执行链
        Map<Class<? extends Annotation>, ExecutionAnnotation> executionAnnotationChain = this.getApp().getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getTarget().getDeclaredAnnotations());
        //判断是否有效执行注释链列表
        if (executionAnnotationChain == null || executionAnnotationChain.isEmpty()) {
            return;
        }
        //设置事件注释执行链
        event.setExecutionAnnotationChain(executionAnnotationChain);

        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
        ApplicationBoot boot = this.getAnnotation(event);
        //绑定依赖类型
        for (Class<?> c : boot.baseClass()) {
//            this.getApp().addBean(c);
        }
        //不等于注释的扫描目录
        if (boot.mode() != LoadingMode.annotation) {

        }
    }
}