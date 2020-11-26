package ghost.framework.module.context;

import ghost.framework.core.locale.ILocaleContainer;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块语言内容接口
 * @Date: 18:38 2019/6/4
 */
public interface IModuleLanguageContent {
    /**
     * 获取国际化
     *
     * @return
     */
    ILocaleContainer getGlobalContainer();

    /**
     * 获取本地化
     *
     * @return
     */
    ILocaleContainer getLocalContainer();
}
