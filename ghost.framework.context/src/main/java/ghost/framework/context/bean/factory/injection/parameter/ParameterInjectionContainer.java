package ghost.framework.context.bean.factory.injection.parameter;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
/**
 * package: ghost.framework.core.bean.factory.injection.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:参数注入容器接口
 * @Date: 2020/2/9:23:23
 */
public interface ParameterInjectionContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IParameterInjectionTargetHandle<O, T, Constructor, Method, Parameter, Object>,
                V
                > extends IBeanFactoryContainer<V> {
    ParameterInjectionContainer<O, T, E, V> getParent();

    /**
     * 声明注入事件
     *
     * @param event 注入事件
     */
    void injector(E event);
}