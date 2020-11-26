package ghost.framework.context.base;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.base
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释函数接口
 * @Date: 11:21 2020/1/18
 */
public interface IAnnotationMethod {
    /**
     * 查询指定注释的函数并调用返回值
     * @param object 调用注释函数对象
     * @param annotation 注释类型
     * @param <T>
     * @return
     */
    <T> T findAnnotationMethodInvoke(Object object, Class<? extends Annotation> annotation);
}