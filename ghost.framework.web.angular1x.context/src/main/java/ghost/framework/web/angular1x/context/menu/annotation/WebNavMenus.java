package ghost.framework.web.angular1x.context.menu.annotation;

import java.lang.annotation.*;

/**
 * package: ghost.framework.web.angular1x.context.router.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web左边数组菜单注释
 * @Date: 2020/4/26:15:43
 */
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebNavMenus {
    /**
     * web左边数组菜单
     * @return
     */
    WebNavMenu[] value();
}