package ghost.framework.beans.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.stereotype
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:组件注释
 * @Date: 2020/1/10:15:31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * 依赖类
     * @return
     */
    Class<?>[] depend() default {};
    /**
     * 是否使用代理模式创建注释类型实例
     * 默认控制器使用代理模式
     * 如果需要在aop实现就必须使用代理模式创建实例
     * @return
     */
    boolean proxy() default true;
}