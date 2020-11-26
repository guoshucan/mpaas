package ghost.framework.web.angular1x.nginx.plugin.hibernate;

import ghost.framework.beans.annotation.stereotype.Component;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

/**
 * package: ghost.framework.web.angular1x.nginx.plugin.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link ghost.framework.web.angular1x.ssh.plugin.entity.SshServerEntity} 删除拦截器
 * {@link ghost.framework.web.angular1x.ssh.plugin.entity.SshServerEntity}
 * @Date: 2020/8/22:7:24
 */
@Component
public class SshServerEntityEmptyInterceptor extends EmptyInterceptor {
    private static final long serialVersionUID = 2251668828360372364L;

    /**
     * 更新数据时调用，但数据还没有更新到数据库
     * @param entity
     * @param id
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @param types
     * @return
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    /**
     * 提交之后调用(commit之后)
     * @param entities
     */
    @Override
    public void postFlush(Iterator entities) {
        super.postFlush(entities);
    }

    /**
     * 保存，删除，更新 在提交之前调用 (通常在 postFlush 之前).
     * @param entities
     */
    @Override
    public void preFlush(Iterator entities) {
        super.preFlush(entities);
    }

    /**
     * 保存数据的时候调用，数据还没有保存到数据库.
     * @param entity
     * @param id
     * @param state
     * @param propertyNames
     * @param types
     * @return
     */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        super.onCollectionRemove(collection, key);
    }

    @Override
    public String onPrepareStatement(String sql) {
        return super.onPrepareStatement(sql);
    }

    /**
     * 删除时调用.
     * @param entity
     * @param id
     * @param state
     * @param propertyNames
     * @param types
     */
    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        super.onDelete(entity, id, state, propertyNames, types);
    }
}