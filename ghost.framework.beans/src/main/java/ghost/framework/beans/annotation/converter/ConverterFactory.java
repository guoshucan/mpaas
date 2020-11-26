package ghost.framework.beans.annotation.converter;

import ghost.framework.beans.annotation.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.converter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:转换器工厂注释
 * @Date: 2020/3/1:10:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ConverterFactory {
    /**
     * 转换工厂是否可以使用默认值
     * 是否在转换无效时使用默认值
     * @return
     */
    boolean defaultValue() default false;
}