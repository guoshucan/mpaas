package ghost.framework.beans.annotation.bean.factory;

import ghost.framework.beans.annotation.container.BeanClassNameContainer;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型事件工厂注释，注释该类将自动安装入 {@see ghost.framework.context.bean.factory.IClassBeanFactoryContainer} 容器中
 * @Date: 14:41 2020/1/31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@BeanClassNameContainer("ghost.framework.context.bean.factory.IClassBeanFactoryContainer")
public @interface ClassBeanFactory {
    /**
     * 该类型注释是否为单实例
     * 默认为是
     *
     * @return
     */
    boolean single() default false;
    /**
     * 源类型注释
     * @return
     */
    AnnotationTag.AnnotationTags tag() default AnnotationTag.AnnotationTags.StereoType;
}