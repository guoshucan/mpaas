package ghost.framework.context.bean.factory;
import ghost.framework.util.Assert;
import java.util.Objects;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:事件目标处理类
 * @Date: 22:51 2019/12/24
 * @param <O> 事件目标对象类型
 * @param <T> 事件对象类型
 */
public class BeanTargetHandle<O, T> implements IBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public BeanTargetHandle(O owner, T target) {
        Assert.notNull(owner, "EventTargetHandle null owner error");
//        Assert.notNull(target, "EventTargetHandle null target error");
        this.owner = owner;
        this.target = target;
    }

    /**
     * 事件目标对象拥有者
     */
    private O owner;

    /**
     * 获取事件目标对象拥有者
     *
     * @return
     */
    @Override
    public O getOwner() {
        return owner;
    }

    @Override
    public void setHandle(boolean handle) {
        this.handle = handle;
    }
    private boolean handle;
    @Override
    public boolean isHandle() {
        return handle;
    }

    /**
     * 目标对象
     */
    private T target;

    /**
     * 获取目标对象
     *
     * @return
     */
    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{owner:" + this.owner.toString() + ",target:" + (this.target == null ? "" : this.target.toString()) + ",handle:" + this.isHandle() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BeanTargetHandle) {
            BeanTargetHandle targetHandle = (BeanTargetHandle) obj;
            return Objects.equals(this.owner, targetHandle.owner) && Objects.equals(this.target, targetHandle.target) && this.isHandle() == targetHandle.isHandle();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();
        if (this.owner != null) {
            i += 31;
            i += this.owner.hashCode();
        }
        if (this.target != null) {
            i += 31;
            i += this.target.hashCode();
        }
        i += 31;
        i += new Boolean(this.isHandle()).hashCode();
        return i;
    }
}