package ghost.framework.beans.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块服务注释，主动加载时对该注释的类进行实例化绑定到容器内，按照注释的模块id注释或模块类绑定
 * @Date: 23:25 2019-10-06
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    /**
     * 依赖类配置
     *
     * @return
     */
    Class<?>[] depend() default {};

    /**
     * 服务名称
     *
     * @return
     */
    String name() default "";
    /**
     * 是否使用代理模式创建注释类型实例
     * 默认控制器使用代理模式
     * 如果需要在aop实现就必须使用代理模式创建实例
     * @return
     */
    boolean proxy() default true;
    /**
     * 注释服务绑定的函数名称
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Name {
        /**
         * 配置前缀
         *
         * @return
         */
        String prefix() default "";
    }
}