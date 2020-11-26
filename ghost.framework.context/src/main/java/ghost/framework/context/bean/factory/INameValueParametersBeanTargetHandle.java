package ghost.framework.context.bean.factory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:50 2020/1/14
 */
public interface INameValueParametersBeanTargetHandle<O, T, S, V, P>
        extends INameValueBeanTargetHandle<O, T, S, V>,
        INameBeanTargetHandle<O, T, S>,
        IValueBeanTargetHandle<O, T, V>,
        IParametersBeanTargetHandle<O, T, P> {
}