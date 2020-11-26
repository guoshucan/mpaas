package ghost.framework.beans.annotation;

import ghost.framework.util.StringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:包图标接口
 * @Date: 23:21 2019-05-04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface Icon {
    /**
     * 图片路径。
     *
     * @return
     */
    String path();

    /**
     * 图标数据。
     *
     * @return
     */
     String base64() default "";
}
