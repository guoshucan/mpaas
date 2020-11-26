package ghost.framework.beans.annotation.event.module;
import ghost.framework.beans.annotation.container.BeanClassNameContainer;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.*;

/**
 * package: ghost.framework.beans.annotation.event.module
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释事件工厂注释，注释该类将自动安装入 {@see ghost.framework.context.bean.factory.method.IMethodAnnotationBeanFactoryContainer} 容器中
 * @Date: 2020/1/31:17:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@BeanClassNameContainer(value = "ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactoryContainer")//注入安装容器
public @interface MethodAnnotationEventFactory {
    /**
     * 注释的声明周期
     * 主要是在加载与卸载的处理机制做区分
     * 关系 {@see ghost.framework.context.loader.ILoader} 接口的卸载与加载
     * @return
     */
    AnnotationTag.AnnotationLifeCycle lifeCycle() default AnnotationTag.AnnotationLifeCycle.Unknown;
//    /**
//     * 事件工厂注释类型
//     * 此注释类型将添加进注释链容器中
//     * @return
//     */
//    Class<? extends Annotation> value();
    /**
     * 该类型注释是否为单实例
     * 默认为是
     *
     * @return
     */
    boolean single() default false;

    /**
     * 该注释标签
     * 默认为注入标签
     *
     * @return
     */
    AnnotationTag.AnnotationTags tag() default AnnotationTag.AnnotationTags.Invoke;
}