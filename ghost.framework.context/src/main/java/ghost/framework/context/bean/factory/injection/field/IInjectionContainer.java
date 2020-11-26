package ghost.framework.context.bean.factory.injection.field;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.context.bean.factory.injection.IInjectionObjectBeanTargetHandle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.context.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:参数与声明注释注入器容器接口
 * @Date: 2020/1/7:16:37
 * @param <O> 发起方容器类型
 * @param <T> 目标对象类型
 * @param <IE> 默认注入入口
 * @param <IO> 目标对象类型
 * @param <IF> 例注入事件目标操作类型
 * @param <IM> 注入函数
 * @param <V> 注释注入器类型
 */
public interface IInjectionContainer
        <
                O extends ICoreInterface,
                T extends Object,
                IE extends IBeanTargetHandle<O, T>,
                IO extends IInjectionObjectBeanTargetHandle<O, T>,
                IF extends IFieldInjectionTargetHandle<O, T, Field, Object>,
                IM extends IMethodInjectionTargetHandle<O, T, Method, Object>,
                V
                > extends IBeanFactoryContainer<V> {
    IInjectionContainer<O, T, IE, IO, IF, IM, V> getParent();
    /**
     * 对象注入
     * @param event 注入事件
     */
    void injector(IO event);
    /**
     * 声明注入事件
     *
     * @param event 注入事件
     */
    void injector(IF event);
    /**
     * 函数注入事件
     *
     * @param event 注入事件
     */
    void injector(IM event);
    /**
     * 声明注入事件
     *
     * @param event 注入事件
     */
    void injector(IE event);
}