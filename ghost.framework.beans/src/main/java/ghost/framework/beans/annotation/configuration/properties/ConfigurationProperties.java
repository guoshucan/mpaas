package ghost.framework.beans.annotation.configuration.properties;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.parser.IPropertiesParser;

import java.lang.annotation.*;
import java.util.Properties;

/**
 * package: ghost.framework.beans.annotation.configuration.properties
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置文件注释，主要讲配置文件内容添加到对应的env中
 * @Date: 15:08 2019/5/24
 */
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.TYPE_USE,
        ElementType.ANNOTATION_TYPE,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConfigurationPropertiess.class)
@Order(1)
public @interface ConfigurationProperties {
    /**
     * 配置前缀
     * 没有前缀时从根目录开始
     *
     * @return
     */
    String prefix() default "";

    /**
     * 配置文件路径，必须制定配置文件
     *
     * @return
     */
    String path() default "application.properties";

    /**
     * 配置文件加载位置
     * 在多个ConfigurationProperties注释时按照位置大小顺序加载，从小到大的加载顺序加载注释的配置文件
     *
     * @return
     */
    int order() default 0;
    /**
     * 配置 {@link Properties} 类型解析器接口
     *
     * @return
     */
    Class<? extends IPropertiesParser>[] propertiesParser() default {};
}