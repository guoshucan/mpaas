package ghost.framework.web.context.bens.annotation;

import ghost.framework.beans.annotation.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.context.bens.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理函数返回值解析器注释
 * @Date: 2020/3/5:15:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface HandlerMethodReturnValueResolver {
}
