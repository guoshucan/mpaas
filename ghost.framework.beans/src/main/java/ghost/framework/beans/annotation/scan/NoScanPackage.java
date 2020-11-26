package ghost.framework.beans.annotation.scan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:不扫描包注释，就是改包不被自动加载扫描
 * @Date: 2:04 2019/12/18
 */
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoScanPackage {
    /**
     * 执行类型
     *
     * @return
     */
    String[] executeClassName() default {};

    /**
     * 执行类型
     *
     * @return
     */
    Class<?>[] executeClass() default {};
}