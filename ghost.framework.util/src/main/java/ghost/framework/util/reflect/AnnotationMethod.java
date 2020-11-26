package ghost.framework.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.util.reflect
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释函数
 * @Date: 2020/5/29:19:12
 */
public class AnnotationMethod {
    private final Annotation annotation;
    private final Method method;

    public Method getMethod() {
        return method;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public AnnotationMethod(Method method, Annotation annotation) {
        this.method = method;
        this.annotation = annotation;
    }
}