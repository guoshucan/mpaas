package ghost.framework.web.session.core;

import ghost.framework.web.context.servlet.filter.ISessionFilter;

/**
 * package: ghost.framework.web.session.core
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:会话仓库过滤器接口
 * @Date: 2020/2/27:17:32
 */
public interface ISessionRepositoryFilter<S extends Session> extends ISessionFilter {
    /**
     * 设置会话存储仓库
     * @param sessionRepository
     */
    void setSessionRepository(SessionRepository<S> sessionRepository);
}