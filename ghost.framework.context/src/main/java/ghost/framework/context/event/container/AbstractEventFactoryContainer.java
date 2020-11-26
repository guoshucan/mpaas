package ghost.framework.context.event.container;
import ghost.framework.context.application.IApplication;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * package: ghost.framework.core.event.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认事件监听容器
 * @Date: 19:48 2020/2/2
 */
public abstract class AbstractEventFactoryContainer extends AbstractCollection<IExecuteEvent> implements IEventFactoryContainer {
    /**
     * 初始化默认事件监听容器
     *
     * @param app 应用接口
     */
    public AbstractEventFactoryContainer(IApplication app) {
        this.app = app;
    }

    /**
     * 应用接口
     */
    protected IApplication app;

    protected List<IExecuteEvent> eventListeners = new ArrayList<>();

    @Override
    public Iterator<IExecuteEvent> iterator() {
        return eventListeners.iterator();
    }

    @Override
    public int size() {
        return eventListeners.size();
    }

    /**
     * 执行事件
     *
     * @param event 事件对象
     */
    @Override
    public abstract void execute(IEvent event);
}