package ghost.framework.core.event.bean.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IItemBeanTargetHandle;
import ghost.framework.core.event.bean.factory.IBeanEventFactory;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import java.util.ArrayList;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定事件监听工厂容器类
 * @Date: 8:54 2019/12/27
 * @param <O> 发起发对象
 * @param <T> 绑定定义接口
 * @param <E> 绑定事件目标处理接口
 * @param <S> 绑定名称类型
 * @param <L> 继承 {@link IBeanEventFactory} 接口对象
 */
public class BeanEventFactoryContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IItemBeanTargetHandle<O, T, S>,
                S extends String,
                L extends IBeanEventFactory<O, T, E, S>
                >
        extends AbstractBeanFactoryContainer<L>
        implements IBeanEventFactoryContainer<O, T, E, S, L> {

    /**
     * 加载事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getClass().getName());
        //遍历事件监听容器列表
        for (L l : new ArrayList<L>(this)) {
            //执行对象绑定
            l.loader(event);
            //判断是否已经处理
            if (event.isHandle()) {
                return;
            }
        }
    }

    /**
     * 卸载事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
    }
}