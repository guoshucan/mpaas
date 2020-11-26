package ghost.framework.beans.annotation.stereotype;

import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.annotation.stereotype
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置注释
 * @Date: 9:59 2019-05-18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order(2)
public @interface Configuration {
    /**
     * 配置名称
     * 如果bean参数为是时设置此名称为bean的名称，如果未设置名称时将使用注释所在类的名称作为bean名称
     * @return
     */
    String name() default "";
//    /**
//     * 从配置文件加载配置到当前env
//     * @return
//     */
//    ConfigurationProperties[] properties() default {};
//    /**
//     * 类型注释配置
//     * 直接在注释 {@link ClassProperties ::depend} 参数类型注入值
//     * 此注释只做初始化一次性注入配置文件参数
//     * 也可以认为为初始化加载配置参数模式
//     * @return
//     */
//    ConfigurationClassProperties[] classProperties() default {};
    /**
     * 是否使用代理模式创建注释类型实例
     * 默认控制器使用代理模式
     * 如果需要在aop实现就必须使用代理模式创建实例
     * @return
     */
    boolean proxy() default true;
    /**
     * 依赖类
     * @return
     */
    Class<?>[] depend() default {};
}