package ghost.framework.beans.annotation.configuration;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.resolver.ResolverCompatibleMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.configuration.annotation
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置类型注释，为配置注入类型的注释，在类初始化注入声明前执行配置类型注入与绑定
 * @Date: 2019-11-17:18:27
 */
@Target({
        ElementType.TYPE,
        ElementType.PACKAGE,
        ElementType.ANNOTATION_TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Order(-1)
public @interface ConfigurationClassProperties {
    /**
     * 配置参数绑定类型
     *
     * @return
     */
    Class<?> value();

    /**
     * 配置前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 绑定名称
     * 作为Bean容器名称
     * 如果未设置名称侧使用类型作为Bean容器名称
     *
     * @return
     */
    String name() default "";

    /**
     * 配置文件路径，必须制定配置文件
     * 格式为 application.properties
     * 默认为空，按照当前拥有者的env注入
     * 如果指定了配置文件将使用当前配置文件注入
     *
     * @return
     */
    String path() default "";

    /**
     * 兼容模式
     * 默认为 {@link ResolverCompatibleMode#IgnoreNullAndEmpty}
     * @return
     */
    ResolverCompatibleMode compatibleMode() default ResolverCompatibleMode.IgnoreNullAndEmpty;

    /**
     * 加载排序
     *
     * @return
     */
    int order() default 0;
}