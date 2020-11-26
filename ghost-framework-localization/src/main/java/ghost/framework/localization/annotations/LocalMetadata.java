package ghost.framework.localization.annotations;

import java.lang.annotation.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:本地化注释。
 * @Date: 7:06 2018/5/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PACKAGE})
@Inherited
public @interface LocalMetadata { }
