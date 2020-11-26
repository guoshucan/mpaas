package ghost.framework.beans.annotation.locale;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:国际化注释，国际化为服务对外显示的区域化
 * i18n标准
 * 默认jar包区域文件扩展名为json
 * 比如格式zn-CN.json
 * @Date: 0:36 2019/5/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface I18n {
    /**
     * 国际化包资源目录路径
     *
     * @return
     */
    String value() default "i18n";

    /**
     * 包括语言列表
     * 与 {@link I18n#localeTitles()} 参数成对表示
     * @return
     */
    String[] localeNames() default "zh-CN";

    /**
     * 包括语言标题列表
     * 与 {@link I18n#localeNames()} 参数成对表示
     * @return
     */
    String[] localeTitles() default "简体中文";
}