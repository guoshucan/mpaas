package ghost.framework.context.loader;

import ghost.framework.context.bean.factory.IBeanTargetHandle;

/**
 * package: ghost.framework.context.loader
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:加载接口，作为加载与卸载的基础接口
 * @Date: 2020/1/14:13:12
 * @param <O> 发起方对象
 * @param <T> 目标对象
 * @param <E> 处理事件对象
 */
public interface ILoader<O, T, E extends IBeanTargetHandle<O, T>> {

    /**
     * 加载模块
     *
     * @param event 事件对象
     */
    void loader(E event);

    /**
     * 卸载模块
     *
     * @param event 事件对象
     */
    default void unloader(E event) { }
}