package ghost.framework.beans.annotation.configuration.properties;

import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.configuration.properties
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置文件注释
 * @Date: 20:43 2019-05-27
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
@Order
public @interface ConfigurationPropertiess {
    ConfigurationProperties[] value();
}
