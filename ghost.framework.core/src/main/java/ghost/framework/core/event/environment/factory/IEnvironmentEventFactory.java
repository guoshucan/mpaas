package ghost.framework.core.event.environment.factory;

import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.bean.factory.IBeanFactory;
import ghost.framework.core.event.environment.IEnvironmentEventTargetHandle;

import java.util.Properties;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型工厂接口
 * @Date: 8:37 2019/12/26
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> env事件目标处理类型
 */
public interface IEnvironmentEventFactory<O, T extends IEnvironment, E extends IEnvironmentEventTargetHandle<O, T>> extends IBeanFactory {
    /**
     * 清除事件
     */
    default void envClear(E event) {
    }

    /**
     * 删除事件
     *
     * @param key 键
     */
    default void envRemove(E event, String key) {
    }

    /**
     * 更改前事件
     *
     * @param key
     * @param v
     */
    default void envChangeBefore(E event, String key, Object v) {
    }

    /**
     * 更改后事件
     *
     * @param key
     * @param v
     */
    default void envChangeAfter(E event, String key, Object v) {
    }

    /**
     * 合并后事件
     *
     * @param prefix     合并前缀
     * @param properties
     */
    default void envMergeAfter(E event, String prefix, Properties properties) {
    }

    /**
     * 合并前事件
     *
     * @param prefix     合并前缀
     * @param properties
     */
    default void envMergeBefore(E event, String prefix, Properties properties) {
    }

    /**
     * 添加键值
     *
     * @param key
     * @param v
     */
    default void envAdd(E event, String key, Object v) {
    }

    default void envSet(E event, String key, Object v) {
    }

    default void envMergeAfter(E event, Properties properties) {
    }

    default void envMergeBefore(E event, Properties properties) {
    }

    default void envMergeAfter(E event, IEnvironment properties) {
    }

    default void envMergeBefore(E event, IEnvironment properties) {
    }
}