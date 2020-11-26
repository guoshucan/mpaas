package ghost.framework.beans.resource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.context.bens.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web资源注释
 * @Date: 2020/3/13:18:16
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
    /**
     * 资源路径
     * 表示在包 resources 目录下的文件夹路径
     *
     * @return
     */
    String[] value();
}