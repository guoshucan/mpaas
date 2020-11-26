package ghost.framework.data.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/22:9:19
 */
public class HibernateDeleteEventListener implements DeleteEventListener {
    private DefaultLoadEventListener defaultLoadEventListener;
    private Logger log = LoggerFactory.getLogger(HibernateDeleteEventListener.class);
    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        log.debug("onDelete:" + event.toString());
    }

    @Override
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        log.debug("onDelete:" + event.toString());
    }
}