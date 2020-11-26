package ghost.framework.context.bean.factory.scan.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.context.bean.factory.scan.IScanBeanTargetHandle;
import ghost.framework.context.bean.factory.scan.factory.IScanBeanFactory;
import ghost.framework.context.loader.ILoader;

/**
 * package: ghost.framework.context.bean.factory.scan.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描事件工厂容器接口
 * @Date: 2020/2/23:13:29
 */
public interface IScanBeanFactoryContainer
        <
                O extends ICoreInterface,
                T,
                E extends IScanBeanTargetHandle<O, T>,
                L extends IScanBeanFactory<O, T, E>
                >
        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
}