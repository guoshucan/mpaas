package ghost.framework.core.event.maven;

import ghost.framework.context.event.maven.IMavenEventTargetHandle;
import ghost.framework.context.maven.ExecuteOwnerBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 16:14 2020/1/11
 * @param <O> 拥有者
 * @param <T> 目标对象
 */
public class MavenEventTargetHandle<O, T> extends ExecuteOwnerBeanTargetHandle<O, T> implements IMavenEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public MavenEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}