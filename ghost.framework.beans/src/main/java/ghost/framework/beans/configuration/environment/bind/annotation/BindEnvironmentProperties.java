package ghost.framework.beans.configuration.environment.bind.annotation;

import ghost.framework.util.StringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env绑定配置注释
 * @Date: 8:21 2019/12/23
 */
@Target({
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEnvironmentProperties {
    /**
     * 绑定名称
     * @return
     */
    String name() default "";
    /**
     * 依赖类型
     * @return
     */
    Class<?>[] type() default {};
    /**
     * 配置前缀
     * 没有前缀时从根目录开始
     * 正常必须有前缀
     *
     * @return
     */
    String prefix();

    /**
     * 排除类声明属性
     *
     * @return
     */
    String[] exclude() default {};

    /**
     * 排序
     *
     * @return
     */
    int order() default 0;

    /**
     * 绑定模式枚举
     * 默认为与env双向绑定
     *
     * @return
     */
    Mode mode() default Mode.ALL;

    /**
     * 绑定模式枚举
     */
    enum Mode {
        /**
         * 双向绑定
         * 接受被env更新与向env更新
         */
        ALL,
        /**
         * 只接受被env更新
         */
        IN,
        /**
         * 只向env更新
         */
        OUT
    }
}