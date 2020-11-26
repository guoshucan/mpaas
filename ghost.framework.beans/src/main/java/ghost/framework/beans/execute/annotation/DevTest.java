package ghost.framework.beans.execute.annotation;

import ghost.framework.beans.enums.SearchStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.execute.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:开发测试注释
 * @Date: 15:05 2020/1/29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DevTest {
    /**
     * 需要作为条件的类的Class对象数组
     */
    Class<?>[] value() default {};
    /**
     * 搜索容器层级
     */
    SearchStrategy search() default SearchStrategy.App;
}
