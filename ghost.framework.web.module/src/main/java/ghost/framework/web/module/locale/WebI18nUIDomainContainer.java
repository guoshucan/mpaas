package ghost.framework.web.module.locale;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.locale.ILocaleContainer;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.core.locale.AbstractLocaleDomainContainer;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.context.locale.IWebI18nUIContainer;
import ghost.framework.web.context.locale.IWebI18nUIDomainContainer;

/**
 * package: ghost.framework.core.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:作为web页面UI的国际化域容器接口
 *  * 添加删除后调用 {@link IWebI18nUIDomainContainer#merge()} 合并后将国际化信息合并到 {@link IWebI18nUIContainer} 中。
 * 作为下面包注释的国际化域容器
 * {@link PluginPackage}
 * {@link ModulePackage}
 * @Date: 2020/6/14:16:05
 */
@Component
public final class WebI18nUIDomainContainer<L extends ILocaleDomain> extends AbstractLocaleDomainContainer<L> implements IWebI18nUIDomainContainer<L> {
    @Constructor
    public WebI18nUIDomainContainer() {
        super();
    }

    public WebI18nUIDomainContainer(int initialCapacity) {
        super(initialCapacity);
    }

    @Autowired
    private IWebI18nUIContainer container;

    @Override
    public ILocaleContainer getLocaleContainer() {
        return container;
    }
}