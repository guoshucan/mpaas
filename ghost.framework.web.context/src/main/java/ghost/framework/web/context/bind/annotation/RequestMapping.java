package ghost.framework.web.context.bind.annotation;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.web.context.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求路径注释
 * @Date: 10:06 2019-10-06
 * @RequestMapping注解的六个属性以及作用
 * RequestMapping是一个用来处理请求地址映射的注解，可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。
 *
 * RequestMapping注解有六个属性，下面分成三类进行说明。
 *
 * value， method
 * value： 指定请求的实际地址，指定的地址可以是具体地址、可以RestFul动态获取、也可以使用正则设置；
 *
 * method： 指定请求的method类型， 分为GET、POST、PUT、DELETE等；
 *
 * consumes，produces
 * consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
 *
 * Controller: 指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
 *
 * params，headers
 * params： 指定request中必须包含某些参数值是，才让该方法处理。
 *
 * headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求。
 *
 * key：
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Order(3)
public @interface RequestMapping {
    /**
     * 请求路径
     * 不指定路径时函数名称作为请求路径
     * @return
     */
    String value() default "";
    /**
     * 请求模式
     * 默认为get模式
     * @return
     */
    RequestMethod[] method() default {};

    /**
     * 请求参数
     * 指定request中必须包含某些参数值是，才让该方法处理。
     * @return
     */
    String[] params() default {};

    /**
     * 请求头参数
     * 指定request中必须包含某些指定的header值，才能让该方法处理请求。
     * @return
     */
    String[] headers() default {};

    /**
     *consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
     * @return
     */
    String[] consumes() default {};

    /**
     *produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
     * 默认可以对应 {@link MediaType} 指定类型
     * @return
     */
    String[] produces() default {};
}
