package ghost.framework.localization.annotations;

import java.lang.annotation.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:自定将对应包类的区域化注入指定属性。
 * @Date: 21:33 2018-11-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE//注入类型，在类型初始化时将国际化对应类所在包的节点注入该类，在获取是使用使用该类初始化注入的区域资源，提高国际化资源访问速度
})
@Inherited
public @interface AutoGlobalization {
    /**
     * 例的名称。
     * 在自动加载类是对该类的此属性名称进行注入区域地图。
     * @return
     */
    String value() default "globalizationField";
}