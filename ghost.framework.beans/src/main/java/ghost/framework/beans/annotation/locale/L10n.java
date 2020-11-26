package ghost.framework.beans.annotation.locale;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:本地化注释，本地化为线程或打印本地输出的区域化
 * l10n标准
 * 默认jar包区域文件扩展名为json
 * 比如格式zn-CN.json
 * @Date: 0:36 2019/5/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface L10n {
    /**
     * 本地化包资源目录路径
     *
     * @return
     */
    String value() default "l10n";

    /**
     * 包括语言列表
     *
     * @return
     */
    String[] localeNames() default "zh-CN";

    /**
     * 包括语言标题列表
     *
     * @return
     */
    String[] localeTitles() default "简体中文";

    /**
     * 本地化默认语言
     *
     * @return
     */
    String[] defaultLanguage() default "";
}