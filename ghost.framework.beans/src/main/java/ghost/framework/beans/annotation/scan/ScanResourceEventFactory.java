package ghost.framework.beans.annotation.scan;

import ghost.framework.beans.annotation.container.BeanClassNameContainer;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.scan
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:14:20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@BeanClassNameContainer(value = "ghost.framework.context.bean.factory.scan.container.IScanResourceBeanFactoryContainer")//注入安装容器
public @interface ScanResourceEventFactory {
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
    AnnotationTag.AnnotationTags tag() default AnnotationTag.AnnotationTags.StereoType;
}
