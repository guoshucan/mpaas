package ghost.framework.core.event.environment.factory;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.core.event.environment.IEnvironmentEventTargetHandle;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;

import java.util.Properties;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 8:51 2019/12/27
 */
@Service
public class EnvironmentEventListenerFactoryContainer<O extends ICoreInterface, T extends IEnvironment, L extends IEnvironmentEventFactory<O, T, E>, E extends IEnvironmentEventTargetHandle<O, T>>
        extends AbstractBeanFactoryContainer<L>
        implements IEnvironmentEventListenerFactoryContainer<O, T, L, E> {
    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public EnvironmentEventListenerFactoryContainer(@Application @Module @Autowired @Nullable IEnvironmentEventListenerFactoryContainer<O, T, L, E> parent) {
        this.parent = parent;
    }

    private IEnvironmentEventListenerFactoryContainer<O, T, L, E> parent;

    @Override
    public void envClear(E event) {

    }

    @Override
    public void envRemove(E event, String key) {

    }

    @Override
    public void envChangeBefore(E event, String key, Object v) {

    }

    @Override
    public void envChangeAfter(E event, String key, Object v) {

    }

    @Override
    public void envMergeAfter(E event, String prefix, Properties properties) {

    }

    @Override
    public void envMergeBefore(E event, String prefix, Properties properties) {

    }

    @Override
    public void envAdd(E event, String key, Object v) {

    }

    @Override
    public void envSet(E event, String key, Object v) {

    }

    @Override
    public void envMergeAfter(E event, Properties properties) {

    }

    @Override
    public void envMergeBefore(E event, Properties properties) {

    }

    @Override
    public void envMergeAfter(E event, IEnvironment properties) {

    }

    @Override
    public void envMergeBefore(E event, IEnvironment properties) {

    }
//
//    @Override
//    public void envAdd(T target, E env, String key, Object v) {
//
//    }
//
//    @Override
//    public void envSet(T target, E env, String key, Object v) {
//
//    }
//
//    @Override
//    public void envMergeAfter(T target, E env, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(T target, E env, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeAfter(T target, E env, IEnvironment properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(T target, E env, IEnvironment properties) {
//
//    }
//    @Override
//    public void envAdd(O target, T env, String key, Object v) {
//
//    }
//
//    @Override
//    public void envSet(O target, T env, String key, Object v) {
//
//    }
//
//    @Override
//    public void envMergeAfter(O module, T env, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(O module, T env, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeAfter(O module, T env, IEnvironment properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(O module, T env, IEnvironment properties) {
//
//    }
}