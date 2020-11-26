package ghost.framework.core.event.bean.factory;

import ghost.framework.beans.annotation.conditional.ConditionalOnClass;
import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IItemBeanTargetHandle;
import ghost.framework.core.event.bean.container.IBeanEventFactoryContainer;
import ghost.framework.util.StringUtils;

/**
 * package: ghost.framework.core.event.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/12:11:08
 */
@ConditionalOnMissingClass(IBeanEventFactoryContainer.class)
//@Global
//@Local
@Configuration
@ConfigurationProperties
@ConditionalOnClass(IBeanEventFactoryContainer.class)
@Component
@BeanCollectionContainer(IBeanEventFactoryContainer.class)
public class BeanEventFactory
        <O extends ICoreInterface, T extends Object, E extends IItemBeanTargetHandle<O, T, S>, S extends String>
        implements IBeanEventFactory<O, T, E, S> {
    @Override
    public void loader(E event) {
        if (StringUtils.isEmpty(event.getName())) {
            event.getOwner().addBean(event.getTarget());
        } else {
            event.getOwner().addBean(event.getName(), event.getTarget());
        }
        event.setHandle(true);
    }
}