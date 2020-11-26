package ghost.framework.core.event.object.container;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.core.event.object.factory.IObjectEventFactory;
import java.util.ArrayList;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象事件监听工厂容器
 * @Date: 8:37 2019/12/27
 * @param <O>
 * @param <T>
 * @param <L>
 * @param <E>
 */
public class ObjectEventFactoryContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IBeanTargetHandle<O, T>,
                L extends IObjectEventFactory<O, T, E>
                >
        extends AbstractBeanFactoryContainer<L>
        implements IObjectEventFactoryContainer<O, T, E, L> {
    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public ObjectEventFactoryContainer(@Application @Autowired @Nullable IObjectEventFactoryContainer<O, T, E, L> parent) {
        this.parent = parent;
    }

    /**
     * 父级类事件监听容器
     */
    private IObjectEventFactoryContainer<O, T, E, L> parent;

    @Override
    public IObjectEventFactoryContainer<O, T, E, L> getParent() {
        return parent;
    }

    @Override
    public void loader(E event) {
        this.getLog().info("loader");
        for (L l : new ArrayList<>(this)) {
            l.loader(event);
            //判断事件是否已经处理
            if (event.isHandle()) {
                return;
            }
        }
        if (this.parent != null) {
            this.parent.loader(event);
        }
    }

    @Override
    public void unloader(E event) {
        this.getLog().info("loader");
        for (L l : new ArrayList<>(this)) {
            l.unloader(event);
            //判断事件是否已经处理
            if (event.isHandle()) {
                return;
            }
        }
        if (this.parent != null) {
            this.parent.unloader(event);
        }
    }
}