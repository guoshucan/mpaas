package ghost.framework.core.event.scan.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.scan.IScanBeanTargetHandle;
import ghost.framework.context.bean.factory.scan.container.IScanBeanFactoryContainer;
import ghost.framework.context.bean.factory.scan.factory.IScanBeanFactory;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;

/**
 * package: ghost.framework.core.event.scan.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描事件工厂容器基础类
 * @Date: 2020/2/23:13:36
 */
public abstract class AbstractScanEventFactoryContainer
        <
                O extends ICoreInterface,
                T,
                E extends IScanBeanTargetHandle<O, T>,
                L extends IScanBeanFactory<O, T, E>
                >
        extends AbstractBeanFactoryContainer<L>
    implements IScanBeanFactoryContainer<O, T, E, L> {
}