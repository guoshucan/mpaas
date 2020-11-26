package ghost.framework.context.bean.factory.scan.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IApplicationOwnerBeanFactory;
import ghost.framework.context.bean.factory.scan.IScanBeanTargetHandle;

/**
 * package: ghost.framework.context.bean.factory.scan.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描事件工厂接口
 * @Date: 2020/2/23:13:31
 */
public interface IScanBeanFactory
        <
                O extends ICoreInterface,
                T,
                E extends IScanBeanTargetHandle<O, T>
                >
        extends IApplicationOwnerBeanFactory<O, T, E>
{
}
