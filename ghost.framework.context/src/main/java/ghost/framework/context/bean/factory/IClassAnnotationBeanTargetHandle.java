package ghost.framework.context.bean.factory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释事件目标处理接口
 * @Date: 13:10 2020/1/10
 * @param <O> 发起方类型
 * @param <T> 处理目标类型
 * @param <V> 类型构建后的对象
 * @param <S> 类型创建绑定名称，可以为空
 * @param <P> 类型构建参数类型
 */
public interface IClassAnnotationBeanTargetHandle
        <O, T, V, S, P>
        extends IAnnotationBeanTargetHandle<O, T>,
        IItemBeanTargetHandle<O, T, S>,
        INameBeanTargetHandle<O, T, S>,
        IValueBeanTargetHandle<O, T, V>,
        IParametersBeanTargetHandle<O, T, P>,
        INameValueParametersBeanTargetHandle<O, T, S, V, P> {
}