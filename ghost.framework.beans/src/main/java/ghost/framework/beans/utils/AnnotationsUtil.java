package ghost.framework.beans.utils;

import java.lang.annotation.Annotation;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块配置工具类。
 * @Date: 0:27 2018-06-21
 */
public final class AnnotationsUtil {
    /**
     * 判断对象是否有指定注释
     * @param c 要判断的对象
     * @param annotations 指定要判断是否存在的注释对象
     * @return
     */
    public static boolean existsAnnotation(Class<?> c, Class<? extends Annotation>[] annotations) {
        for (Annotation annotation : c.getDeclaredAnnotations()) {
            for (Class<? extends Annotation> ac : annotations) {
                if (annotation.annotationType().equals(ac)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断对象是否有指定注释
     * @param o 要判断的对象
     * @param annotations 指定要判断是否存在的注释对象
     * @return
     */
    public static boolean existsAnnotation(Object o, Class<? extends Annotation>[] annotations) {
        return existsAnnotation(o.getClass(), annotations);
    }
}
