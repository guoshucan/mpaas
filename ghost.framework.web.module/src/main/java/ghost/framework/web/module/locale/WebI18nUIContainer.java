package ghost.framework.web.module.locale;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.core.locale.LocaleContainer;
import ghost.framework.web.context.locale.IWebI18nUIContainer;

/**
 * package: ghost.framework.web.module.locale
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域国际化容器
 * i18n
 * {@link LocaleContainer}
 * {@link IWebI18nUIContainer}
 * @Date: 19:15 2020/1/20
 */
@Component
public final class WebI18nUIContainer extends LocaleContainer implements IWebI18nUIContainer {
    /**
     * 注释构建入口
     * @param domain
     */
    @Constructor
    public WebI18nUIContainer(ICoreInterface domain) {
        this.domain = domain;
    }
    /**
     * 构建入口
     * @param domain
     */
    public WebI18nUIContainer(Object domain) {
        this.domain = domain;
    }

    /**
     * 获取所属域
     * @return
     */
    @Override
    public Object getDomain() {
        return domain;
    }

    private final Object domain;
}