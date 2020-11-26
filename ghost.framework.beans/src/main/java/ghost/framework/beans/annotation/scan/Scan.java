package ghost.framework.beans.annotation.scan;

import java.lang.annotation.*;

/**
 * 注释扫描
 */
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Scans.class)
public @interface Scan {
    /**
     * 注释包
     * @return
     */
    String[] basePackages() default {};

    /**
     * 注释包的类型
     * @return
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 注释排除包
     * @return
     */
    String[] excludePackages() default {};

    /**
     * 注释排除包类型
     * @return
     */
    Class<?>[] excludePackageClasses() default {};
}