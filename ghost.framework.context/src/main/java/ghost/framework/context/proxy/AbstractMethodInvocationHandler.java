package ghost.framework.context.proxy;

/**
 * package: ghost.framework.context.proxy
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数代理处理基础类
 * @Date: 2020/5/13:4:38
 */
public abstract class AbstractMethodInvocationHandler<I> implements IMethodInvocationHandler<I> {
    protected AbstractMethodInvocationHandler() {
    }

    protected AbstractMethodInvocationHandler(I target) {
        this.target = target;
    }

    @Override
    public I getTarget() {
        return target;
    }

    private I target;

    @Override
    public void setTarget(I target) {
        this.target = target;
    }
}