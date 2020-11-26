package ghost.framework.localization;

import java.util.Locale;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:48 2019-02-01
 */
public interface ILocaleContainer {
    Map<String, Object> getContent(Locale locale, Class<?> declaringClass, boolean method, String methodName);
}
