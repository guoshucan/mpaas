package ghost.framework.beans.annotation.scan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描包注释，指定包被扫描
 * @Date: 2:04 2019/12/18
 */
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScanPackage {
    /**
     * 指定扫描类型名称
     * @return
     */
    String[] designatedClassName() default {};

    /**
     * 指定扫描类型
     * @return
     */
    Class<?>[] designatedClass() default {};
}