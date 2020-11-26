package ghost.framework.context.bean.factory.scan.container;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.scan.IScanResourceBeanTargetHandle;
import ghost.framework.context.bean.factory.scan.factory.IScanResourceBeanFactory;

/**
 * package: ghost.framework.context.bean.factory.scan.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描资源事件工厂容器接口
 * @Date: 2020/2/23:13:29
 */
public interface IScanResourceBeanFactoryContainer
        <
                O extends ICoreInterface,
                T,
                E extends IScanResourceBeanTargetHandle<O, T>,
                L extends IScanResourceBeanFactory<O, T, E>
                >
        extends IScanBeanFactoryContainer<O, T, E, L> {
}