package ghost.framework.core.event.bean.factory;

import ghost.framework.context.bean.factory.IBeanFactory;
import ghost.framework.context.bean.factory.IItemBeanTargetHandle;
import ghost.framework.context.loader.ILoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释绑定工厂基础类接口
 * @Date: 21:42 2019/12/21
 * @param <O> 发起发对象
 * @param <T> 绑定定义接口
 * @param <E> 绑定事件目标处理接口
 * @param <S> 绑定名称类型
 */
public interface IBeanEventFactory<O, T, E extends IItemBeanTargetHandle<O, T, S>, S> extends IBeanFactory, ILoader<O, T, E> { }