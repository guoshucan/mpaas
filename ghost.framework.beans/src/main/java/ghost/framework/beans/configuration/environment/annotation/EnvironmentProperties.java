package ghost.framework.beans.configuration.environment.annotation;
import ghost.framework.beans.configuration.environment.EnvironmentCompatibilityMode;
import ghost.framework.util.StringUtil;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:从env导入参数配置实例化注释类注释
 * @Date: 11:56 2019/12/24
 */
@Target({
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnvironmentProperties {
    /**
     * 兼容模式枚举
     * 默认为 {@link EnvironmentCompatibilityMode::IgnoreInvalid}
     * @return
     */
    EnvironmentCompatibilityMode compatibilityMode() default EnvironmentCompatibilityMode.IgnoreInvalid;
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
     * 绑定名称
     * @return
     */
    String name() default "";
    /**
     * 加载排序
     * @return
     */
    int order() default 0;
}
