package ghost.framework.core.bean.factory.injection.parameter;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionTargetHandle;
import ghost.framework.core.event.ValueEventTargetHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
/**
 * package: ghost.framework.core.bean.factory.injection.parameter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注入参数事件目标处理类
 * @Date: 2020/1/7:19:13
 * @param <O>
 * @param <T>
 * @param <P>
 * @param <V>
 */
public class ParameterInjectionTargetHandle
        <O extends ICoreInterface, T extends Object, C extends Constructor, M extends Method, P extends Parameter, V extends Object>
        extends ValueEventTargetHandle<O, T, V>
        implements IParameterInjectionTargetHandle<O, T, C, M, P, V> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner       设置事件目标对象拥有者
     * @param target      设置目标对象
     * @param constructor 参数所属构建对象
     */
    public ParameterInjectionTargetHandle(O owner, T target, C constructor) {
        super(owner, target);
        this.setConstructor(constructor);
    }

    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param method 参数所属函数
     */
    public ParameterInjectionTargetHandle(O owner, T target, M method) {
        super(owner, target);
        this.setMethod(method);
    }

    /**
     * 初始化事件不表处理头
     *
     * @param owner     设置事件目标对象拥有者
     * @param target    设置目标对象
     * @param parameter 注入参数
     */
    public ParameterInjectionTargetHandle(O owner, T target, P parameter) {
        super(owner, target);
        this.parameter = parameter;
    }

    @Override
    public void setValues(V[] values) {
        this.values = values;
    }

    private V[] values;

    @Override
    public V[] getValues() {
        return values;
    }

    /**
     * 获取父及优先注释
     *
     * @return
     */
    @Override
    public ParentPriority getParentPriority() {
        if (this.parameter.isAnnotationPresent(ParentPriority.class)) {
            return this.parameter.getAnnotation(ParentPriority.class);
        }
        return null;
    }

    private P parameter;

    @Override
    public C getConstructor() {
        return constructor;
    }

    @Override
    public void setConstructor(C constructor) {
        this.constructor = constructor;
        this.setParameters((P[]) this.constructor.getParameters());
    }

    private C constructor;

    @Override
    public void setMethod(M method) {
        this.method = method;
        this.setParameters((P[]) this.method.getParameters());
    }

    private M method;

    @Override
    public M getMethod() {
        return method;
    }

    @Override
    public P getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(P parameter) {
        this.parameter = parameter;
    }

    @Override
    public P[] getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(P[] parameters) {
        this.parameters = parameters;
    }

    private P[] parameters;
}