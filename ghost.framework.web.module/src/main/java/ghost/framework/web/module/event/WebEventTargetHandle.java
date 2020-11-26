package ghost.framework.web.module.event;

import ghost.framework.context.maven.OwnerEventTargetHandle;
import ghost.framework.web.context.event.IWebEventTargetHandle;

/**
 * package: ghost.framework.web.module.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/27:20:05
 */
public class WebEventTargetHandle<O, T> extends OwnerEventTargetHandle<O, T> implements IWebEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public WebEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}
