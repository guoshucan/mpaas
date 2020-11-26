package ghost.framework.core.application.event;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.application.event.AbstractApplicationEvent;

/**
 * package: ghost.framework.core.application.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/20:16:10
 */
@Component
public class ModuleEventPublisherContainer extends AbstractEventPublisherContainer implements IModuleEventPublisherContainer {
    @Override
    public void onApplicationEvent(AbstractApplicationEvent event) {
        this.publishEvent(event);
    }
}
