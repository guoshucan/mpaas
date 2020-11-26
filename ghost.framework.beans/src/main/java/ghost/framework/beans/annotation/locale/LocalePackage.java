package ghost.framework.beans.annotation.locale;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.locale.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域包注释，也是等于语言包注释
 * @Date: 2020/6/9:11:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE})
public @interface LocalePackage {
    /**
     * 语言包名称，如果未设置时将按照注释所在package-info位置的包名称作为区域包名称
     *
     * @return
     */
    String value() default "";

    /**
     * 区域包唯一id
     *
     * @return
     */
    String uuid() default "";

    /**
     * 依赖包名称
     * 表示此区域包只能被指定依赖包加载
     * @return
     */
    String[] dependentPackage() default {};

    /**
     * 依赖包uuid
     * 表示此区域包只能被指定依赖uuid包加载
     * @return
     */
    String[] dependentPackageUuid() default {};
}