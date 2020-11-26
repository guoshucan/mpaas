package ghost.framework.web.mvc.context.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.mvc.context.bind.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:作为 {@link Controller} 注释控制器的请求函数返回包装注释，函数使用此注释将使用RESTful风格返回
 * @Date: 2020/6/2:21:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ResponseBody {
}