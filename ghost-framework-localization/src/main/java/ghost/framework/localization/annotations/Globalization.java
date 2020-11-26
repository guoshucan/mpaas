package ghost.framework.localization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:国家化注释。
 * @Date: 10:49 2018-11-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.PARAMETER,//注释参数，在函数入口注入
        ElementType.TYPE//注入类型，在类型初始化时将国际化对应类所在包的节点注入该类，在获取是使用使用该类初始化注入的区域资源，提高国际化资源访问速度
})
public @interface Globalization {
    /**
     * 指定国际化的键。
     * 不指定侧返回整个国际化的map对象。
     *
     * @return
     */
    String key() default "";

    /**
     * 是否以函数节点国际化。
     * 如果使用函数化侧在语言包下增加函数名称节点。
     *
     * @return
     */
    boolean method() default false;
}