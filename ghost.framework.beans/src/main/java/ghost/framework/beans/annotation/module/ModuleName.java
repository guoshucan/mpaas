package ghost.framework.beans.annotation.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.module
 * 模块id注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.PACKAGE})
public @interface ModuleName {
    /**
     * 绑定时指定模块id
     * 该模块id主要是指定该注入参数使用该模块的绑定定义对象注入
     * @return
     */
    String name();
    /**
     * 如果无效是否引发错误
     * 如果为否侧不做任何处理，忽略绑定无效问题
     * @return
     */
    boolean error() default false;
}