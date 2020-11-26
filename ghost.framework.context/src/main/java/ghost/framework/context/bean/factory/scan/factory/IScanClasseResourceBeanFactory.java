package ghost.framework.context.bean.factory.scan.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.scan.IScanResourceBeanTargetHandle;

/**
 * package: ghost.framework.context.bean.factory.scan.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:扫描开发本地\\Classes路径目录事件工厂接口
 * @Date: 2020/2/23:13:44
 */
public interface IScanClasseResourceBeanFactory
        <
                O extends ICoreInterface,
                T,
                E extends IScanResourceBeanTargetHandle<O, T>
                >
        extends IScanResourceBeanFactory<O, T, E> {
}