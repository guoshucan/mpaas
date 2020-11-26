package ghost.framework.context.bean.factory.method;

import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link Bean} 列表容器事件工厂类
 * @Date: 19:01 2020/1/14
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
public interface IMethodBeanAnnotationBeanFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
        extends MethodAnnotationBeanFactory<O, T, E> {
}
