package ghost.framework.context.locale;

import java.util.Locale;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:语言化接口
 * @Date: 22:43 2019/11/27
 */
public interface ILocale {
    /**
     * 获取语言化
     * @return
     */
    Locale getLocale();

    /**
     * 设置语言化
     * @param lan
     */
    void setLocale(String lan);
}