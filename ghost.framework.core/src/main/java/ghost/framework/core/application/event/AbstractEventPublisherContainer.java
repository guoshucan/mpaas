package ghost.framework.core.application.event;

import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.context.application.event.ApplicationEventListener;
import ghost.framework.context.application.event.IEventPublisherContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
/**
 * package: ghost.framework.context.application.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用实践推送容器
 * 处理继承 {@link ApplicationEventListener} 接口的对象推送事件
 * @Date: 2020/2/20:10:44
 */
public abstract class AbstractEventPublisherContainer
        extends AbstractCollection<ApplicationEventListener>
        implements IEventPublisherContainer {
    @Override
    public void close() throws Exception {
        synchronized (list) {
            list.clear();
        }
    }

    /**
     * 日志
     */
     private Log log = LogFactory.getLog(this.getClass());
    /**
     *
     */
    private Collection<ApplicationEventListener> list = new ArrayList<>(5);

    @Override
    public boolean add(ApplicationEventListener eventListener) {
        synchronized (list) {
            return list.add(eventListener);
        }
    }
    @Override
    public void publishEvent(AbstractApplicationEvent event) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("publishEvent:" + event.toString());
        }
        synchronized (list) {
            list.forEach(a -> a.onApplicationEvent(event));
        }
    }

    @Override
    public Iterator<ApplicationEventListener> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }
}