package ghost.framework.core.event.object.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.bean.factory.IBeanFactory;
import ghost.framework.context.bean.factory.IBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型工厂接口
 * @Date: 8:37 2019/12/26
 * @param <O>
 * @param <T>
 * @param <E>
 */
public interface IObjectEventFactory<O extends ICoreInterface, T extends Object, E extends IBeanTargetHandle<O, T>> extends IBeanFactory, ILoader<O, T, E> {

}