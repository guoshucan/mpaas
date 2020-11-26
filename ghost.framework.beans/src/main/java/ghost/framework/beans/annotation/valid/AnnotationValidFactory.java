package ghost.framework.beans.annotation.valid;

import ghost.framework.beans.annotation.container.BeanClassNameContainer;
import ghost.framework.beans.annotation.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.valid
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/29:14:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.TYPE
        })
@Component
@BeanClassNameContainer("ghost.framework.context.valid.IAnnotationValidFactoryContainer")
public @interface AnnotationValidFactory {
}