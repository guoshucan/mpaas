package ghost.framework.module.reflect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:接口信息注释
 * @Date: 23:59 2019/4/17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface InterfaceInfo {
    /**
     * 接口id
     * @return
     */
    String id();

    /**
     * 接口名称
     * @return
     */
    String name();

    /**
     * 接口组名称
     * @return
     */
    String groupName() default "";
}