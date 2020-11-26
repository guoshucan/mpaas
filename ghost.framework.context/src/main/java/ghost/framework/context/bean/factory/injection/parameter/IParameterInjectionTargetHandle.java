package ghost.framework.context.bean.factory.injection.parameter;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * package: ghost.framework.core.bean.factory.injection.parameter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注入参数事件目标处理接口
 * @Date: 2020/1/7:19:13
 * @param <O>
 * @param <T>
 * @param <P>
 * @param <V>
 */
public interface IParameterInjectionTargetHandle
        <O extends ICoreInterface, T extends Object, C extends Constructor, M extends Method, P extends Parameter, V extends Object>
        extends
        IParametersBeanTargetHandle<O, T, P>,
        IParameterBeanTargetHandle<O, T, P>,
        IValueBeanTargetHandle<O, T, V>,
        IValuesBeanTargetHandle<O, T, V>,
        IAnnotationBeanTargetHandle<O, T>,
        IMethodBeanTargetHandle<O, T, M>,
        IConstructorEventTargetHandle<O, T, C, P> {
}