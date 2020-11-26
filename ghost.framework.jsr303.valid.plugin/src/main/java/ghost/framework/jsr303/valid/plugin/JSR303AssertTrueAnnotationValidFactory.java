package ghost.framework.jsr303.valid.plugin;

import ghost.framework.beans.annotation.valid.AnnotationValidFactory;
import ghost.framework.context.valid.AnnotationValidBeanFactory;

import javax.validation.constraints.AssertTrue;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.jsr303.valid.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/29:13:40
 */
@AnnotationValidFactory
public class JSR303AssertTrueAnnotationValidFactory implements AnnotationValidBeanFactory {
    /**
     * 类型注释工厂注释
     */
    private final Class<? extends Annotation> annotation = AssertTrue.class;

    /**
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }
}
