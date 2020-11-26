package ghost.framework.context.bean.factory;

/**
 * 绑定参数目标处理接口
 * 实现功能
 * {@link IParameterBeanTargetHandle#getParameter()}
 * {@link IParameterBeanTargetHandle#setParameter(Object)} ()}
 * @param <O> 发起方对象
 * @param <T> 绑定目标
 * @param <P> 参数类型
 */
public interface IParameterBeanTargetHandle<O, T, P> extends IOwnerBeanTargetHandle<O, T> {
    /**
     * 获取参数
     * @return
     */
    P getParameter();

    /**
     * 设置参数
     * @param parameter
     */
    void setParameter(P parameter);
}