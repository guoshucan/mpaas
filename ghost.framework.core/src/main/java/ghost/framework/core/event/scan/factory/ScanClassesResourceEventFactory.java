package ghost.framework.core.event.scan.factory;

import ghost.framework.beans.annotation.scan.ScanResourceEventFactory;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.scan.IScanResourceBeanTargetHandle;
import ghost.framework.context.bean.factory.scan.factory.IScanClasseResourceBeanFactory;

/**
 * package: ghost.framework.core.event.scan.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描开发本地\\Classes路径目录事件工厂类
 * @Date: 2020/2/23:13:43
 */
@ScanResourceEventFactory
public class ScanClassesResourceEventFactory
        <
                O extends ICoreInterface,
                T,
                E extends IScanResourceBeanTargetHandle<O, T>
                >
        implements IScanClasseResourceBeanFactory<O, T, E> {
    @Override
    public void loader(E event) {

    }
}
