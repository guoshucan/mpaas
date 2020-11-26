package ghost.framework.web.socket.plugin.bean.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionFactory;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionTargetHandle;

import javax.websocket.server.PathParam;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * package: ghost.framework.web.socket.plugin.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:参数 {@link PathParam} 注释注入工厂接口
 * @Date: 2020/5/2:16:49
 */
public interface IParameterPathParamAnnotationInjectionFactory
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IParameterInjectionTargetHandle<O, T, Constructor, Method, Parameter, Object>
                >
        extends IParameterInjectionFactory<O, T, E> {
}
