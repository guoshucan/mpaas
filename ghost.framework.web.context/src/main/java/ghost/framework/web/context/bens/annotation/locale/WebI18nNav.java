package ghost.framework.web.context.bens.annotation.locale;

import java.lang.annotation.*;

/**
 * package: ghost.framework.web.context.bens.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web容器i18n左边菜单国际化注释
 * @Date: 2020/6/13:23:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE})
public @interface WebI18nNav {
    /**
     * 虚拟路径
     * 如果使用url虚拟路径时必须设置 {@link WebI18nUI#value()} 本地包路径
     * 格式比如 virtualPath = /virtual/path/i18n
     * 在Url地址的访问就是http://127.0.0.1/virtual/path/i18n/zh_CN.json
     * 如果指定虚拟路径根目录
     * 格式比如 virtualPath = /
     * @return
     */
    String virtualPath() default "";
    /**
     * 国际化包资源目录路径
     *
     * @return
     */
    String value() default "i18n/nav";

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
}