package ghost.framework.core.event.scan.container;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.scan.IScanResourceBeanTargetHandle;
import ghost.framework.context.bean.factory.scan.container.IScanResourceBeanFactoryContainer;
import ghost.framework.context.bean.factory.scan.factory.IScanResourceBeanFactory;

/**
 * package: ghost.framework.core.event.scan.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描资源事件工厂容器
 * @Date: 2020/2/23:13:36
 */
@Component
public class ScanResourceEventFactoryContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IScanResourceBeanTargetHandle<O, T>,
                L extends IScanResourceBeanFactory<O, T, E>
                >
        extends AbstractScanEventFactoryContainer<O, T, E, L>
        implements IScanResourceBeanFactoryContainer<O, T, E, L> {
    public ScanResourceEventFactoryContainer(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;

    /**
     * 加载事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        throw new UnsupportedOperationException();
    }

    /**
     * 卸载事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        throw new UnsupportedOperationException();
    }
}