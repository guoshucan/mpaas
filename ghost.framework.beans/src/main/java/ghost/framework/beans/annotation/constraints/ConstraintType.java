package ghost.framework.beans.annotation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.constraints
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:约束类型注释
 * @Date: 2020/6/20:19:48
 */
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintType {
    /**
     * 类型列表
     * @return
     */
    Class[] value();
}
