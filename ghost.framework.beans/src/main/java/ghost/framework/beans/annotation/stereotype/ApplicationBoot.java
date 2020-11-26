package ghost.framework.beans.annotation.stereotype;
import ghost.framework.beans.execute.LoadingMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:运行类注释
 * @Date: 15:58 2020/1/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationBoot {
    /**
     * 模块加载模式
     * 默认为注释加载模式
     * @return
     */
    LoadingMode mode() default LoadingMode.annotation;
    /**
     * 基础类
     */
    Class<?>[] baseClass() default {};
    /**
     * 扫描基础包
     * @return
     */
    String[] basePackage() default {};
}