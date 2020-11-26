package ghost.framework.context.method;

import ghost.framework.context.asm.ParameterUtils;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数目标对象
 * @Date: 12:45 2019/11/24
 */
public abstract class MethodTarget {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodTarget that = (MethodTarget) o;
        return target.equals(that.target) &&
                method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, method);
    }

    @Override
    public String toString() {
        return "MethodTarget{" +
                "target=" + target.toString() +
                ", method=" + method.getName() +
                '}';
    }

    /**
     * 请求函数拥有者
     */
    protected Object target;

    /**
     * 获取请求函数拥有者
     *
     * @return
     */
    public Object getTarget() {
        return target;
    }

    /**
     * 请求函数
     */
    protected Method method;

    /**
     * 获取函数
     *
     * @return
     */
    public Method getMethod() {
        return method;
    }

    private final NameParameter[] parameters;

    /**
     * 获取数组参数
     *
     * @return
     */
    public NameParameter[] getParameters() {
        return parameters;
    }

    /***
     * 初始化请求函数对象
     * @param method         函数对象
     */
    public MethodTarget(Object target, Method method) {
        Assert.notNull(target, "MethodTarget is target null error");
        Assert.notNull(method, "MethodTarget is method null error");
        this.target = target;
        this.method = method;
        this.method.setAccessible(true);
        //创建带名称的数组参数
        this.parameters = ParameterUtils.getParameters(this.method);
    }
}