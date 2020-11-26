package ghost.framework.context.bean.factory;

/**
 * 素组参数事件目标处理接口
 * @param <O>
 * @param <T>
 * @param <P>
 */
public interface IParametersBeanTargetHandle<O, T, P> extends IExecuteOwnerBeanTargetHandle<O, T> {
    /**
     * 获取数组参数
     * @return
     */
    P[] getParameters();

    /**
     * 设置数组参数
     * @param parameters
     */
    void setParameters(P[] parameters);
}