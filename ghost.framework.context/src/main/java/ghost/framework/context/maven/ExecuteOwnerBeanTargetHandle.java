package ghost.framework.context.maven;

import ghost.framework.context.bean.factory.IExecuteOwnerBeanTargetHandle;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:执行拥有者事件目标处理类
 * @Date: 2020/2/22:12:11
 */
public class ExecuteOwnerBeanTargetHandle<O, T> extends OwnerEventTargetHandle<O, T> implements IExecuteOwnerBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ExecuteOwnerBeanTargetHandle(O owner, T target) {
        super(owner, target);
    }

    @Override
    public O getExecuteOwner() {
        return executeOwner;
    }

    private O executeOwner;

    @Override
    public void setExecuteOwner(O executeOwner) {
        this.executeOwner = executeOwner;
    }
}