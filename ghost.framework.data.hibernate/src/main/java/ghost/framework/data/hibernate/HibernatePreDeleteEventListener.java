package ghost.framework.data.hibernate;

import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/22:9:20
 */
public class HibernatePreDeleteEventListener implements PreDeleteEventListener {
    private Logger log = LoggerFactory.getLogger(HibernatePreDeleteEventListener.class);

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        log.debug("onPreDelete:" + event.toString());
        return false;
    }
}