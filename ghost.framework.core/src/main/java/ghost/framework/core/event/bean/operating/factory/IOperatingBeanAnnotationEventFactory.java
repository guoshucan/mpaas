package ghost.framework.core.event.bean.operating.factory;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.IAnnotationBeanFactory;
import ghost.framework.context.loader.ILoader;

import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定事件工厂接口
 * @Date: 0:12 2020/1/13
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
public interface IOperatingBeanAnnotationEventFactory
        <O, T extends IBeanDefinition, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends IAnnotationBeanFactory, ILoader<O, T, E> {
//    /**
//     * 获取注释对象
//     * 当在事件的Target目标处理对象获取不到注释类型原始对象时在注释链获取该注释原始对象
//     * 通过 {@link IAnnotationEventFactory ::getAnnotationClass()} 该注释类型获取注释源对象
//     * @param event 事件对象
//     * @param <R>
//     * @return 返回Target目标注释类型，如果Target目标注释类型不存在将在注释执行链获取原始注释对象
//     */
//    @Override
//    default <R extends Annotation> R getAnnotation(E event) {
//        //在目标对判断是否包含该注释，如果存在直接获取
//        if(event.getMethod().isAnnotationPresent(this.getAnnotationClass())){
//            return (R)event.getMethod().getAnnotation(this.getAnnotationClass());
//        }
//        //在目标没有当前类型注释，该注释存在域注释本身依赖注释
//        return (R)this.forEachAnnotation(event.getMethod().getDeclaredAnnotations());
//    }
}