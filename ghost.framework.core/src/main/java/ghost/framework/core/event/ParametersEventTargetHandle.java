package ghost.framework.core.event;

import ghost.framework.context.bean.factory.IParametersBeanTargetHandle;
import ghost.framework.context.maven.ExecuteOwnerBeanTargetHandle;

/**
 * 素组参数事件目标处理类
 * @param <O>
 * @param <T>
 * @param <P>
 */
public class ParametersEventTargetHandle<O, T, P> extends ExecuteOwnerBeanTargetHandle<O, T> implements IParametersBeanTargetHandle<O, T, P> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public ParametersEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param parameters 设置数组参数
     */
    public ParametersEventTargetHandle(O owner, T target, P[] parameters) {
        super(owner, target);
        this.parameters = parameters;
    }

    /**
     * 数组参数
     */
    private P[] parameters;

    /**
     * 获取数组参数
     * @return
     */
    @Override
    public P[] getParameters() {
        return parameters;
    }

    /**
     * 设置数组参数
     * @param parameters
     */
    @Override
    public void setParameters(P[] parameters) {
        this.parameters = parameters;
    }
}