package ghost.framework.core.locale;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.locale.II18nContainer;
import ghost.framework.context.locale.II18nDomainContainer;
import ghost.framework.context.locale.ILocaleContainer;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;

/**
 * package: ghost.framework.core.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * 作为下面包注释的国际化域容器
 * {@link PluginPackage}
 * {@link ModulePackage}
 * @Date: 2020/6/14:16:05
 */
@Component
public final class I18nDomainContainer<L extends ILocaleDomain> extends AbstractLocaleDomainContainer<L> implements II18nDomainContainer<L> {
    @Constructor
    public I18nDomainContainer() {
        super();
    }

    public I18nDomainContainer(int initialCapacity) {
        super(initialCapacity);
    }

    @Autowired
    private II18nContainer container;

    @Override
    public ILocaleContainer getLocaleContainer() {
        return container;
    }
}