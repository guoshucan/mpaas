package ghost.framework.context.locale;

import ghost.framework.context.IGetDomain;

import java.util.Map;

/**
 * package: ghost.framework.context.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域域接口
 * {@link IGetDomain#getDomain()}
 * @Date: 2020/6/14:16:00
 */
public interface ILocaleDomain extends IGetDomain {
    /**
     * @return
     */
    Map<LocaleKey, Map<Object, Object>> getLocaleMap();
}