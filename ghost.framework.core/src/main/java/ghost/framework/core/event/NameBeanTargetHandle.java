package ghost.framework.core.event;

import ghost.framework.context.bean.factory.INameBeanTargetHandle;
import ghost.framework.context.maven.ExecuteOwnerBeanTargetHandle;


/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象事件目标处理
 * @Date: 16:28 2019/12/26
 * @param <O> 发起方类型
 * @param <T> 目标处理类型
 * @param <S> 事件目标处理名称类型
 */
public class NameBeanTargetHandle<O, T, S> extends ExecuteOwnerBeanTargetHandle<O, T> implements INameBeanTargetHandle<O, T, S> {
    /**
     * 初始化对象事件目标处理
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public NameBeanTargetHandle(O owner, T target) {
        super(owner, target);
    }
    /**
     * 初始化对象事件目标处理
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param name 设置绑定名称
     */
    public NameBeanTargetHandle(O owner, T target, S name) {
        super(owner, target);
        this.name = name;

    }

    private S name;

    /**
     * 设置绑定名称
     * @param name
     */
    @Override
    public void setName(S name) {
        this.name = name;
    }

    /**
     * 获取绑定名称
     * @return
     */
    @Override
    public S getName() {
        return name;
    }
}