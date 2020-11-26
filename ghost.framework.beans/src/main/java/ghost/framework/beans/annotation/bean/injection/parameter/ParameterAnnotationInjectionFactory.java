package ghost.framework.beans.annotation.bean.injection.parameter;

import ghost.framework.beans.annotation.container.BeanClassNameContainer;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.*;

/**
 * package: ghost.framework.beans.annotation.bean.injection.parameter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理注入参数事件工厂类注释
 * @Date: 2020/2/21:16:20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@BeanClassNameContainer(value = "ghost.framework.context.bean.factory.injection.parameter.ParameterInjectionContainer")//注入安装容器
public @interface ParameterAnnotationInjectionFactory {
    /**
     * 该类型注释是否为单实例
     * 默认为是
     * @return
     */
    boolean single() default true;
    /**
     * 该注释标签
     * 默认为注入标签
     * @return
     */
    AnnotationTag.AnnotationTags tag() default AnnotationTag.AnnotationTags.Injection;
}
