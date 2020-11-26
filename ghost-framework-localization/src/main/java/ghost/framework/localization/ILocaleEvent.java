package ghost.framework.localization;

import java.util.Locale;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:本地化事件接口。
 * @Date: 23:34 2018-09-08
 */
public interface ILocaleEvent {
    /**
     * 本地化更改事件调用函数。
     * @param locale
     */
    void change(Locale locale);
}