package ghost.framework.core.application.event;

import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.context.application.event.ApplicationEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.core.application.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/20:15:23
 */
@Component
@BeanCollectionContainer(IApplicationEventPublisherContainer.class)
public class ApplicationEventListenerTest implements ApplicationEventListener {
     private Log log = LogFactory.getLog(ApplicationEventListenerTest.class);
    /**
     * 注入应用接口
     */
    @Autowired
    private IApplication app;

    @Override
    public void onApplicationEvent(AbstractApplicationEvent event) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(app.getName() + ">onApplicationEvent:" + event.toString());
        }
    }
}