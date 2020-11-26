package ghost.framework.web.session.core;

import java.util.Collection;

/**
 * package: ghost.framework.web.session.core
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:会话仓库容器接口
 * @Date: 2020/2/27:17:17
 */
public interface ISessionRepositoryContainer<S extends Session, R> extends Collection<R> {
    /**
     * 获取默认存储仓库
     * 按照存储列表第一个位置获取，并设置第一个位置为默认会话操作仓库
     *
     * @return
     */
    SessionRepository<S> getDefault();
}