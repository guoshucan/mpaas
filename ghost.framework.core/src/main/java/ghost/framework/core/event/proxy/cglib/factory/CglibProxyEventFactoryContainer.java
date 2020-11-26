package ghost.framework.core.event.proxy.cglib.factory;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
/**
 * package: ghost.framework.core.event.proxy.cglib.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019/12/31:21:05
 */
@Component
public class CglibProxyEventFactoryContainer<O, T, C extends Class<?>, L extends ICglibProxyEventFactory<O, T, C, R>, E extends IBeanTargetHandle<O, T>, R>
        extends AbstractBeanFactoryContainer<L>
        implements ICglibProxyEventFactoryContainer<O, T, C, L, E, R> {
    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public CglibProxyEventFactoryContainer(
            @Application @Autowired @Nullable ICglibProxyEventFactoryContainer<O, T, C, L, E, R> parent) {
        this.parent = parent;
    }
    private ICglibProxyEventFactoryContainer<O, T, C, L, E, R> parent;
    @Override
    public ICglibProxyEventFactoryContainer<O, T, C, L, E, R> getParent() {
        return parent;
    }
}