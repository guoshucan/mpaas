package ghost.framework.context.event.scan;

import ghost.framework.context.bean.factory.scan.IScanBeanTargetHandle;
import ghost.framework.context.maven.OwnerEventTargetHandle;

/**
 * package: ghost.framework.core.event.scan
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:13:50
 */
public class ScanEventTargetHandle<O, T> extends OwnerEventTargetHandle<O, T> implements IScanBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ScanEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}
