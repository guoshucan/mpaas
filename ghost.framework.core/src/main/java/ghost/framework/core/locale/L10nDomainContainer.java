package ghost.framework.core.locale;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.locale.IL10nContainer;
import ghost.framework.context.locale.IL10nDomainContainer;
import ghost.framework.context.locale.ILocaleContainer;
import ghost.framework.context.locale.ILocaleDomain;

/**
 * package: ghost.framework.core.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/14:16:05
 */
@Component
public final class L10nDomainContainer<L extends ILocaleDomain> extends AbstractLocaleDomainContainer<L> implements IL10nDomainContainer<L> {
    @Constructor
    public L10nDomainContainer() {
        super();
    }

    public L10nDomainContainer(int initialCapacity) {
        super(initialCapacity);
    }
    @Autowired
    private IL10nContainer container;

    @Override
    public ILocaleContainer getLocaleContainer() {
        return container;
    }
}