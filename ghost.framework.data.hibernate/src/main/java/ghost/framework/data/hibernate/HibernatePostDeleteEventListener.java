package ghost.framework.data.hibernate;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/22:9:21
 */
public class HibernatePostDeleteEventListener implements PostDeleteEventListener {
    private Logger log = LoggerFactory.getLogger(HibernatePostDeleteEventListener.class);

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        log.debug("onPostDelete:" + event.toString());
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        log.debug("requiresPostCommitHanding:" + persister.toString());
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        log.debug("requiresPostCommitHandling:" + persister.toString());
        return false;
    }
}
